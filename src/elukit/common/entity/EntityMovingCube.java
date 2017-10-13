package elukit.common.entity;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import elukit.client.StateManager;
import elukit.client.shader.ShaderManager;
import elukit.client.texture.TextureManager;
import elukit.common.collider.ColliderMovingBox;
import elukit.common.collider.ICollider;
import elukit.common.level.World;
import elukit.common.struct.Vec3f;
import elukit.common.struct.Vec3i;
import elukit.common.struct.Vec4f;
import elukit.common.util.ARShapes;
import elukit.common.util.ArrayRenderer;
import elukit.common.util.MathUtil;

public class EntityMovingCube extends Entity implements ICollidableEntity {
	ColliderMovingBox collider = new ColliderMovingBox(0,0,0,3,1,3);
	float period = 0;
	Vec3i p1, p2;
	
	public EntityMovingCube(Vec3i pos1, Vec3i pos2, float period){
		this.p1 = pos1;
		this.p2 = pos2;
		this.period = period;
		this.width = 3f;
		this.height = 1f;
		noClip = true;
	}
	
	@Override
	public void addCollider(World world, List<ICollider> colliders) {
		colliders.add(collider);
	}
	
	@Override
	public void update(){
		super.update();
		float t = MathUtil.fract(timeExisted/period);
		Vec3f v1 = new Vec3f((p2.x-p1.x)/(period/2f),(p2.y-p1.y)/(period/2f),(p2.z-p1.z)/(period/2f));
		Vec3f v2 = v1.scale(-1f);
		if (t < 0.5){
			if (motionX != v1.x || motionY != v1.y || motionZ != v1.z){
				motionX = v1.x;
				motionY = v1.y;
				motionZ = v1.z;
				x = p1.x+0.5f;
				y = p1.y;
				z = p1.z+0.5f;
			}
		}
		else {
			if (motionX != v2.x || motionY != v2.y || motionZ != v2.z){
				motionX = v2.x;
				motionY = v2.y;
				motionZ = v2.z;
				x = p2.x+0.5f;
				y = p2.y;
				z = p2.z+0.5f;
			}
		}
		
		collider.motionX = motionX;
		collider.motionY = motionY;
		collider.motionZ = motionZ;
		collider.x = x-width/2f;
		collider.y = y;
		collider.z = z-width/2f;
	}

	@Override
	public void render(){
		StateManager.bindTexture(TextureManager.texEntity);
		
		StateManager.useShader(ShaderManager.defaultShader);
		
		StateManager.pushMatrix();
		StateManager.identity();
		/*StateManager.rotateX((float)Math.sin(Math.toRadians(timeExisted*420f)) * 5f);
		StateManager.rotateY(timeExisted*120f);*/
		StateManager.translate(getIX(), getIY()+0.5f, getIZ());
		
		ArrayRenderer buf = ArrayRenderer.instance;
		buf.start(GL11.GL_QUADS, false);
		
		ARShapes.addTexturedBox(buf, -1.5f, -0.5f, -1.5f, 3f, 1f, 3f, 1, 1, 1, 1, 
				new Vec4f[]{
						new Vec4f(48f,0f,48f,16f),
						new Vec4f(48f,0f,48f,16f),
						new Vec4f(0f,0f,48f,48f),
						new Vec4f(0f,0f,48f,48f),
						new Vec4f(48f,0f,48f,16f),
						new Vec4f(48f,0f,48f,16f)
						});
		
		
		buf.bake();
		buf.render();
		buf.reset();
		
		StateManager.popMatrix();
	}
}
