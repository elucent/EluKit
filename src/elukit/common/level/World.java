package elukit.common.level;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_NORMAL_ARRAY;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glColorPointer;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glNormalPointer;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glVertexPointer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import elukit.client.Main;
import elukit.client.lighting.ILightProvider;
import elukit.client.lighting.Light;
import elukit.client.particle.ParticleSystem;
import elukit.client.shader.ShaderManager;
import elukit.client.texture.TextureManager;
import elukit.common.collider.ICollider;
import elukit.common.element.ILitElement;
import elukit.common.element.ITickableElement;
import elukit.common.entity.Entity;
import elukit.common.struct.Vec2i;
import elukit.common.struct.Vec3f;
import elukit.common.struct.Vec3i;
import elukit.common.util.Primitives;
import elukit.common.util.RenderUtil;
import elukit.common.util.ThreadUtil;

public class World {
	public Map<Vec2i, Chunk> chunks = new HashMap<>();
	public Vec3f light = (new Vec3f(-1.2f,-1.8f,1.5f)).normalize();
	
	public Map<Integer, Entity> entities = new HashMap<Integer, Entity>();
	public Set<Integer> entitiesToDelete = new HashSet<Integer>();
	
	public ParticleSystem particleSystem = new ParticleSystem();
	
	int entityid = 0;
	
	public void tickDeletions(){
		for (int i : entitiesToDelete){
			entities.remove(i);
		}
		particleSystem.tickDeletions();
		entitiesToDelete.clear();
	}
	
	public void tick(){
		Main.gameRenderer.pause();
		tickDeletions();
		Main.gameRenderer.resume();
		for (Entity e : entities.values()){
			e.update();
		}
		for (Chunk c : chunks.values()){
			for (Entry<Vec3i, ITickableElement> e : c.tickableElements.entrySet()){
				e.getValue().update(c, e.getKey());
			}
		}
		particleSystem.tick();
	}
	
	public void addEntity(Entity entity){
		entity.id = entityid ++;
		entity.setLevel(this);
		entities.put(entity.id, entity);
	}
	
	public void removeEntity(Entity entity){
		entitiesToDelete.add(entity.id);
	}
	
	public void uploadLights(List<Light> lights){
		for (Chunk c : chunks.values()){
			c.uploadLights(lights);
		}
		for (Entity e : entities.values()){
			if (e instanceof ILightProvider){
				((ILightProvider)e).addLights(lights);
			}
		}
	}
	
	public void genChunk(int x, int y){
		Chunk c = new Chunk(this,x,y);
		c.load();
		chunks.put(new Vec2i(x,y), c);
	}
	
	public void addColliders(List<ICollider> list, int x, int y, int z){
		Vec2i pos = new Vec2i(x/Chunk.DIM,z/Chunk.DIM);
		for (int i = -1; i < 2; i ++){
			for (int j = -1; j < 2; j ++){
				if (chunks.containsKey(pos.add(i, j))){
					chunks.get(pos.add(i, j)).addColliders(list, x, y, z);
				}
			}
		}
	}
	
	public void deleteChunk(Vec2i v){
		if (chunks.containsKey(v)){
			chunks.get(v).render.reset();
			chunks.remove(v);
		}
	}
	
	public void clearChunks(){
		Vec2i[] a = chunks.keySet().toArray(new Vec2i[chunks.keySet().size()]);
		for (Vec2i v : a){
			deleteChunk(v);
		}
	}
	
	public void setChunk(Chunk c){
		Vec2i pos = new Vec2i(c.worldX,c.worldZ);
		if (!chunks.containsKey(pos)){
			chunks.put(pos, c);
		}
		else {
			chunks.replace(pos, c);
		}
	}
	
	public void renderLevel(){
		int enableLighting = GL20.glGetUniformLocation(ShaderManager.defaultProgram, "enableLighting");
		GL20.glUniform1i(enableLighting, 1);
		int enableVertexOffset = GL20.glGetUniformLocation(ShaderManager.defaultProgram, "enableVertexOffset");
		GL20.glUniform1i(enableVertexOffset, 1);
		int light = GL20.glGetUniformLocation(ShaderManager.defaultProgram, "light");
		GL20.glUniform3f(light, this.light.x, this.light.y, this.light.z);

		GL20.glUseProgram(ShaderManager.defaultProgram);
		int translation = GL20.glGetUniformLocation(ShaderManager.defaultProgram, "translation");
		float time = 0f;
		for (Entry<Vec2i, Chunk> e : chunks.entrySet()){
			if (!e.getValue().render.getBaked() && time < 0.0025f){
				long l = System.nanoTime();
				e.getValue().bake();
				time += (System.nanoTime()-l)/1000000000f;
			}
			GL20.glUniform3f(translation, e.getKey().x*Chunk.DIM, 0, e.getKey().y*Chunk.DIM);
			e.getValue().renderLevel();
		}
		
		GL20.glUniform3f(translation, 0, 0, 0);
		GL20.glUniform1i(enableVertexOffset, 0);
		for (Entry<Vec2i, Chunk> e : chunks.entrySet()){
			e.getValue().renderUnbaked();
		}
	}
	
	public void renderEntities(){
		int enableLighting = GL20.glGetUniformLocation(ShaderManager.defaultProgram, "enableLighting");
		GL20.glUniform1i(enableLighting, 1);
		int enableVertexOffset = GL20.glGetUniformLocation(ShaderManager.defaultProgram, "enableVertexOffset");
		GL20.glUniform1i(enableVertexOffset, 0);
		int light = GL20.glGetUniformLocation(ShaderManager.defaultProgram, "light");
		GL20.glUniform3f(light, this.light.x, this.light.y, this.light.z);
		for (Entity e : entities.values()){
			e.render();
		}
		
		particleSystem.render();
	}
}
