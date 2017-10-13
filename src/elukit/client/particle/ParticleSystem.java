package elukit.client.particle;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import elukit.client.texture.TextureManager;
import elukit.common.util.ArrayRenderer;

public class ParticleSystem {
	List<Particle> particles = new ArrayList<>();
	
	public void addParticle(Particle particle){
		particles.add(particle);
	}
	
	public void tick(){
		for (int i = 0; i < particles.size(); i ++){
			particles.get(i).update();
		}
	}
	
	public void render(){
		ArrayRenderer buf = ArrayRenderer.instance;
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, TextureManager.get("particle"));
		
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		buf.start(GL11.GL_QUADS, false);
		
		for (int i = 0; i < particles.size(); i ++){
			if (i < particles.size() && particles.get(i) != null && !particles.get(i).isAdditive()){
				particles.get(i).render(buf);
			}
		}
		
		buf.bake();
		buf.render();
		buf.reset();
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		buf.start(GL11.GL_QUADS, false);
		
		for (int i = 0; i < particles.size(); i ++){
			if (i < particles.size() && particles.get(i) != null && particles.get(i).isAdditive()){
				particles.get(i).render(buf);
			}
		}
		
		buf.bake();
		buf.render();
		buf.reset();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthMask(true);
	}

	public void tickDeletions() {
		for (int i = 0; i < particles.size(); i ++){
			if (particles.get(i).life <= 0){
				particles.remove(i);
				i = Math.max(0, i-1);
			}
		}
	}
}
