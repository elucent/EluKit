package elukit.common.element;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import elukit.client.Main;
import elukit.client.lighting.Light;
import elukit.client.particle.Particle;
import elukit.common.collider.ColliderBox;
import elukit.common.collider.ICollider;
import elukit.common.level.Chunk;
import elukit.common.struct.Vec3i;
import elukit.common.struct.Vec4f;
import elukit.common.util.ARShapes;
import elukit.common.util.ArrayRenderer;
import elukit.common.util.NoiseGenUtil;
import elukit.common.util.Primitives;

public class ElementPlinth extends Element implements ILitElement, ITickableElement {
	float u = 0, v = 0;
	public ElementPlinth(int id, float u, float v){
		super(id);
		this.u = u;
		this.v = v;
	}
	
	@Override
	public void bake(Chunk level, Vec3i pos, ArrayRenderer buf){
		ARShapes.addSegment(buf, 
				pos.x+0.25f, pos.y+0.625f, pos.z+0.25f, 
				pos.x+0.75f, pos.y+0.625f, pos.z+0.25f, 
				pos.x+0.75f, pos.y+0.625f, pos.z+0.75f, 
				pos.x+0.25f, pos.y+0.625f, pos.z+0.75f, 
				pos.x, pos.y, pos.z, 
				pos.x+1, pos.y, pos.z, 
				pos.x+1, pos.y, pos.z+1, 
				pos.x, pos.y, pos.z+1, 
				1f, 1f, 1f, 1f, 
				new Vec4f[]{
						new Vec4f(32f,0f,16f,10f),
						new Vec4f(32f,0f,16f,10f),
						new Vec4f(48f,0f,16f,16f),
						new Vec4f(64f,0f,8f,8f),
						new Vec4f(32f,0f,16f,10f),
						new Vec4f(32f,0f,16f,10f)
				});
	}

	@Override
	public boolean isSolid(){
		return false;
	}

	@Override
	public void render(float x, float y, float z){
		//Primitives.renderBox(x, y, z, x+1f, y+1f, z+1f, u, v, u+16f, v+16f);
	}

	@Override
	public void addCollider(Chunk level, Vec3i pos, List<ICollider> colliders){
		colliders.add(new ColliderBox(pos.x,pos.y,pos.z,1f,0.625f,1f));
	}

	@Override
	public void addLights(Chunk level, Vec3i pos, List<Light> lights) {
		int x = level.getAbsoluteX()+pos.x;
		int y = pos.y;
		int z = level.getAbsoluteZ()+pos.z;
		Random rand = NoiseGenUtil.getRandom(Main.seed, x, y, z);
		Color c = Color.getHSBColor(rand.nextFloat(), 0.8f, 1.0f);
		lights.add(new Light(x+0.5f,y+1.0f,z+0.5f,c.getRed()/255f,c.getGreen()/255f,c.getBlue()/255f,1.0f,6.0f));
	}

	@Override
	public void update(Chunk level, Vec3i pos) {
		int x = level.getAbsoluteX()+pos.x;
		int y = pos.y;
		int z = level.getAbsoluteZ()+pos.z;
		Random rand = NoiseGenUtil.getRandom(Main.seed, x, y, z);
		Color c = Color.getHSBColor(rand.nextFloat(), 0.8f, 1.0f);
		level.world.particleSystem.addParticle(new Particle(x+0.5f,y+1.0f,z+0.5f,0f,0f,0.6f)
				.setAdditive(true)
				.setColor(c.getRed()/255f,c.getGreen()/255f,c.getBlue()/255f,0.45f)
				.setMotion(0.5f*(NoiseGenUtil.random.nextFloat()-0.5f), 2.75f*(NoiseGenUtil.random.nextFloat()), 0.5f*(NoiseGenUtil.random.nextFloat()-0.5f)));
	}
}
