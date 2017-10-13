package elukit.client.particle;

import elukit.client.Main;
import elukit.common.util.ARShapes;
import elukit.common.util.ArrayRenderer;
import elukit.common.util.MathUtil;
import elukit.common.util.NoiseGenUtil;

public class Particle {
	public float u = 0f, v = 0f;
	public float px = 0f, py = 0f, pz = 0f;
	public float x = 0f, y = 0f, z = 0f;
	public float vx = 0f, vy = 0f, vz = 0f;
	public float life = 0;
	public float maxlife = 0;
	public boolean additive = false;
	public float r = 1f, g = 1f, b = 1f, a = 1f;
	public float scale = 1f;
	public Particle(float x, float y, float z, float u, float v, float life){
		this.px = x;
		this.py = y;
		this.pz = z;
		this.x = x;
		this.y = y;
		this.z = z;
		this.u = u;
		this.v = v;
		this.life = life;
		this.maxlife = life;
	}
	
	public Particle setScale(float scale){
		this.scale = scale;
		return this;
	}
	
	public Particle setColor(float r, float g, float b, float a){
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		return this;
	}
	
	public Particle setAdditive(boolean additive){
		this.additive = additive;
		return this;
	}
	
	public Particle setMotion(float vx, float vy, float vz){
		this.vx = vx;
		this.vy = vy;
		this.vz = vz;
		return this;
	}
	
	public void update(){
		px = x;
		py = y;
		pz = z;
		x += vx*Main.deltaTime;
		y += vy*Main.deltaTime;
		z += vz*Main.deltaTime;
		life -= Main.deltaTime;
	}
	
	public boolean isAdditive(){
		return additive;
	}
	
	public float getLifeCoeff(){
		return (life-Main.partialTicks)/maxlife;
	}
	
	public float getIX(){
		return NoiseGenUtil.lerp(px, x, Main.partialTicks);
	}
	
	public float getIY(){
		return NoiseGenUtil.lerp(py, y, Main.partialTicks);
	}
	
	public float getIZ(){
		return NoiseGenUtil.lerp(pz, z, Main.partialTicks);
	}
	
	public void render(ArrayRenderer buf){
		if (MathUtil.distSq(x, y, z, Main.player.x, Main.player.y, Main.player.z) < Main.drawDist*Main.drawDist){
			ARShapes.renderCenteredBillboard(buf, getIX(), getIY(), getIZ(), scale*getLifeCoeff(), scale*getLifeCoeff(), r, g, b, a*getLifeCoeff(), u, v, u+32f/1024f, v+32f/1024f);
		}
	}
}
