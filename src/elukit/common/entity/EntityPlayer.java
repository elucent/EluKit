package elukit.common.entity;

import static org.lwjgl.opengl.GL11.*;

import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import elukit.client.Main;
import elukit.client.lighting.ILightProvider;
import elukit.client.lighting.Light;
import elukit.client.particle.Particle;
import elukit.client.shader.ShaderManager;
import elukit.client.texture.TextureManager;
import elukit.common.element.Element;
import elukit.common.element.ElementRegistry;
import elukit.common.level.Chunk;
import elukit.common.network.NetworkHelper;
import elukit.common.network.packet.CPacketChunkRequest;
import elukit.common.struct.Vec2i;
import elukit.common.struct.Vec3i;
import elukit.common.struct.Vec4f;
import elukit.common.util.ARShapes;
import elukit.common.util.ArrayRenderer;
import elukit.common.util.MathUtil;
import elukit.common.util.NoiseGenUtil;
import elukit.common.util.Primitives;

public class EntityPlayer extends Entity implements ILightProvider {
	float fallCoeff = 0;
	float fallTime = 0;
	float xMoveCoeff = 0;
	float zMoveCoeff = 0;
	public float eyeHeight = 1f;
	float forwardMotion = 0;
	float sideMotion = 0;
	
	public float cameraPitch = 30, cameraYaw = 0;
	
	public EntityPlayer(boolean isClient){
		width = 0.5f;
		height = 1.25f;
		z = 0.5f;
	}
	
	@Override
	public void update(){
		super.update();
		
		double[] mouseX = new double[1];
		double[] mouseY = new double[1];
		GLFW.glfwGetCursorPos(Main.window, mouseX, mouseY);
		float centerX = Main.WIDTH/2;
		float centerY = Main.HEIGHT/2;
		
		float dx = (float)mouseX[0] - centerX;
		float dy = (float)mouseY[0] - centerY;
		cameraYaw += dx*0.65f;
		cameraPitch += dy*0.65f;
		cameraPitch = MathUtil.clamp(cameraPitch, -90f, 90f);
		
		GLFW.glfwSetCursorPos(Main.window, (centerX), (centerY));
		
		if (Main.heldKeys.contains(GLFW_KEY_D)){
			xMoveCoeff = Math.min(1.0f, xMoveCoeff+Main.deltaTime*10f);
			sideMotion -= Main.deltaTime*(40f);
			sideMotion = Math.max(-4f,sideMotion);
			xFaceCoeff = Math.max(-1.0f, xFaceCoeff-Main.deltaTime*5f);
		}
		else {
			if (sideMotion < 0){
				sideMotion *= Math.max(0f,1.0f-Main.deltaTime*30f);
			}
			xMoveCoeff *= Math.max(0f,1.0f-Main.deltaTime*10f);
			if (xFaceCoeff > 0){
				xFaceCoeff *= Math.max(0f,1.0f-Main.deltaTime*10f);
			}
		}
		if (Main.heldKeys.contains(GLFW_KEY_A)){
			xMoveCoeff = Math.min(1.0f, xMoveCoeff+Main.deltaTime*10f);
			sideMotion += Main.deltaTime*(40f);
			sideMotion = Math.min(4f,sideMotion);
			xFaceCoeff = Math.min(1.0f, xFaceCoeff+Main.deltaTime*5f);
		}
		else {
			if (sideMotion > 0){
				sideMotion *= Math.max(0f,1.0f-Main.deltaTime*30f);
			}
			xMoveCoeff *= Math.max(0f,1.0f-Main.deltaTime*10f);
			if (xFaceCoeff < 0){
				xFaceCoeff *= Math.max(0f,1.0f-Main.deltaTime*10f);
			}
		}
		if (Main.heldKeys.contains(GLFW_KEY_W)){
			zMoveCoeff = Math.min(1.0f, zMoveCoeff+Main.deltaTime*10f);
			forwardMotion -= Main.deltaTime*(40f);
			forwardMotion = Math.max(-4f,forwardMotion);
			zFaceCoeff = Math.max(-1.0f, zFaceCoeff-Main.deltaTime*5f);
		}
		else {
			if (forwardMotion < 0){
				forwardMotion *= Math.max(0f,1.0f-Main.deltaTime*30f);
			}
			zMoveCoeff *= Math.max(0f,1.0f-Main.deltaTime*10f);
			if (zFaceCoeff > 0){
				zFaceCoeff *= Math.max(0f,1.0f-Main.deltaTime*10f);
			}
		}
		if (Main.heldKeys.contains(GLFW_KEY_S)){
			zMoveCoeff = Math.min(1.0f, zMoveCoeff+Main.deltaTime*10f);
			forwardMotion += Main.deltaTime*(40f);
			forwardMotion = Math.min(4f,forwardMotion);
			zFaceCoeff = Math.min(1.0f, zFaceCoeff+Main.deltaTime*5f);
		}
		else {
			if (forwardMotion > 0){
				forwardMotion *= Math.max(0f,1.0f-Main.deltaTime*30f);
			}
			zMoveCoeff *= Math.max(0f,1.0f-Main.deltaTime*10f);
			if (zFaceCoeff < 0){
				zFaceCoeff *= Math.max(0f,1.0f-Main.deltaTime*10f);
			}
		}
		motionY -= Main.deltaTime*20f;
		if (Main.tappedKeys.contains(GLFW_KEY_SPACE)/* && grounded*/){
			motionY = 8f;
		}
		if (!Main.heldKeys.contains(GLFW_KEY_SPACE) && motionY > -2.5f){
			motionY -= Main.deltaTime*(60f * ((grounded && !collidedDown) ? 2.5f : 0.5f));
		}
		if (!grounded && !collidedDown){
			fallTime = Math.min(1.0f, fallTime+Main.deltaTime*10f);
			fallCoeff = Math.max(-1.0f, Math.min(1.0f, fallCoeff+Math.signum(motionY)*Main.deltaTime*8f));
		}
		else {
			fallTime *= Math.max(0f,1.0f-Main.deltaTime*10f);
			fallCoeff *= Math.max(0f,1.0f-Main.deltaTime*8f);
		}
		
		motionX = MathUtil.lookX(cameraYaw, 0)*forwardMotion + MathUtil.lookX(cameraYaw+90,0)*sideMotion;
		motionZ = MathUtil.lookZ(cameraYaw, 0)*forwardMotion + MathUtil.lookZ(cameraYaw+90,0)*sideMotion;
		
		/*for (int r = 0; r < 6; r ++){
			for (int xx = -r; xx < r+1; xx ++){
				for (int zz = -r; zz < r+1; zz ++){
					Vec2i pos = new Vec2i((int)Math.floor(x/Chunk.DIM),(int)Math.floor(z/Chunk.DIM)).add(xx, zz);
					if (r < 3){
						if (!world.chunks.containsKey(pos)){
							if (Main.connectedMP){
								NetworkHelper.sendPacket(Main.socket, Main.address.getAddress(), Main.address.getPort(), new CPacketChunkRequest(pos.x,pos.y));
							}
						}
					}
					else if (Math.abs(xx) > 4 || Math.abs(zz) > 4){
						world.deleteChunk(pos);
					}
				}
			}
		}*/
		/*if (Main.ticks % 1 == 0){
			world.particleSystem.addParticle(new Particle(x+0.75f*MathUtil.lookX(cameraYaw-150f, -0.5f*cameraPitch),y+0.5f+0.375f*MathUtil.lookY(cameraYaw-150f, -0.5f*cameraPitch),z+0.75f*MathUtil.lookZ(cameraYaw-150f, -0.5f*cameraPitch),0f,0f,50)
					.setColor(1f, 0.25f, 0.0625f, 1f)
					.setAdditive(true)
					.setScale(0.75f)
					.setMotion(0.5f*(NoiseGenUtil.random.nextFloat()-0.5f), 1.75f*(NoiseGenUtil.random.nextFloat()), 0.5f*(NoiseGenUtil.random.nextFloat()-0.5f)));
			world.particleSystem.addParticle(new Particle(x+0.75f*MathUtil.lookX(cameraYaw-150f, -0.5f*cameraPitch),y+0.5f+0.375f*MathUtil.lookY(cameraYaw-150f, -0.5f*cameraPitch),z+0.75f*MathUtil.lookZ(cameraYaw-150f, -0.5f*cameraPitch),0f,0f,50)
					.setColor(1f, 0.25f, 0.0625f, 0.125f)
					.setAdditive(true)
					.setScale(1.25f)
					.setMotion(0.5f*(NoiseGenUtil.random.nextFloat()-0.5f), 1.75f*(NoiseGenUtil.random.nextFloat()), 0.5f*(NoiseGenUtil.random.nextFloat()-0.5f)));
		}*/
	}
	
	@Override
	public void render(){
		float moveCoeff = Math.max(xMoveCoeff, zMoveCoeff);
		float walkCycleSin = (float)Math.sin(Math.PI*3.0*this.timeExisted)*moveCoeff;
		float walkCycleDouble = (float)Math.sin(Math.PI*6.0*this.timeExisted)*moveCoeff;
		float walkCycleCos = (float)Math.cos(Math.PI*3.0*this.timeExisted)*moveCoeff;
		if (fallTime > 0){
			walkCycleSin = NoiseGenUtil.lerp(walkCycleSin,fallCoeff,fallTime);
			walkCycleDouble = NoiseGenUtil.lerp(walkCycleDouble,0,fallTime);
			walkCycleCos = NoiseGenUtil.lerp(walkCycleCos,(float)Math.cos(Math.asin(fallCoeff)),fallTime);
		}
		
		GL20.glUseProgram(0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,0);
		
		ArrayRenderer buf = ArrayRenderer.instance;
		buf.start(GL11.GL_QUADS, false);
		
		//ARShapes.addBox(buf, x-0.5f, y, z-0.5f, x+0.5f, y+1, z+0.5f, 1, 1, 1, 1, true, true, true, true, true, true, 0f, 0f, 0f, 0f);
		
		buf.bake();
		buf.render();
		buf.reset();
		
		GL20.glUseProgram(ShaderManager.defaultProgram);
	}

	@Override
	public void addLights(List<Light> lights) {
		lights.add(new Light(x,y+0.75f,z,1f,0.25f,0.0625f,1.5f,8f));
		lights.add(new Light(x,y+0.75f,z,1f,0.5f,0.125f,4.0f,4f));
	}
}