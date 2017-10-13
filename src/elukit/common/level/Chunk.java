package elukit.common.level;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import elukit.client.Main;
import elukit.client.lighting.ILightProvider;
import elukit.client.lighting.Light;
import elukit.client.shader.ShaderManager;
import elukit.client.texture.TextureManager;
import elukit.common.collider.ICollider;
import elukit.common.element.Element;
import elukit.common.element.ElementRegistry;
import elukit.common.element.ILitElement;
import elukit.common.element.ITickableElement;
import elukit.common.element.IUnbakedElement;
import elukit.common.entity.Entity;
import elukit.common.struct.Vec3f;
import elukit.common.struct.Vec3i;
import elukit.common.util.ArrayRenderer;
import elukit.common.util.DataHelper;
import elukit.common.util.MathUtil;
import elukit.common.util.NoiseGenUtil;
import elukit.common.util.Primitives;
import elukit.common.util.RenderUtil;

import static org.lwjgl.opengl.GL11.*;

public class Chunk {
	public static int DIM = 16;
	public int worldX = 0, worldZ = 0;
	public int width = 0, height = 0;
	public World world = null;
	ArrayRenderer render = new ArrayRenderer();
	
	public Map<Vec3i, Element> elements = new HashMap<Vec3i, Element>();
	
	public Map<Vec3i, ILitElement> litElements = new HashMap<Vec3i, ILitElement>();
	public Map<Vec3i, IUnbakedElement> unbakedElements = new HashMap<Vec3i, IUnbakedElement>();
	public Map<Vec3i, ITickableElement> tickableElements = new HashMap<Vec3i, ITickableElement>();
	
	public Chunk(World w, int x, int y){
		this.world = w;
		this.worldX = x;
		this.worldZ = y;
	}
	
	public void addColliders(List<ICollider> list, int x, int y, int z){
		Vec3i pos = new Vec3i(x-getAbsoluteX(),y,z-getAbsoluteZ());
		List<ICollider> temp = new ArrayList<>();
		if (elements.containsKey(pos)){
			elements.get(pos).addCollider(this, pos, temp);
		}
		for (ICollider c : temp){
			list.add(c.translate(getAbsoluteX(), 0, getAbsoluteZ()));
		}
	}
	
	public void setElement(Vec3i pos, Element e){
		if (e == null){
			if (elements.containsKey(pos)){
				elements.remove(pos);
				if (unbakedElements.containsKey(pos)){
					unbakedElements.remove(pos);
				}
				if (litElements.containsKey(pos)){
					litElements.remove(pos);
				}
				if (tickableElements.containsKey(pos)){
					tickableElements.remove(pos);
				}
			}
		}
		else {
			if (elements.containsKey(pos)){
				elements.replace(pos, e);
			}
			else {
				elements.put(pos, e);
			}
			if (e instanceof ITickableElement){
				if (tickableElements.containsKey(pos)){
					tickableElements.replace(pos, (ITickableElement)e);
				}
				else {
					tickableElements.put(pos, (ITickableElement)e);
				}
			}
			if (e instanceof IUnbakedElement){
				if (unbakedElements.containsKey(pos)){
					unbakedElements.replace(pos, (IUnbakedElement)e);
				}
				else {
					unbakedElements.put(pos, (IUnbakedElement)e);
				}
			}
			if (e instanceof ILitElement){
				if (litElements.containsKey(pos)){
					litElements.replace(pos, (ILitElement)e);
				}
				else {
					litElements.put(pos, (ILitElement)e);
				}
			}
		}
	}
	
	public void save(){
		try {
			String path = "assets/maps/"+worldX+"_"+worldZ+".lv";
			OutputStream out = new FileOutputStream(path);
			byte w1 = (byte)(width >> 8);
			byte w2 = (byte)(width);
			byte h1 = (byte)(height >> 8);
			byte h2 = (byte)(height);
			out.write(w1);
			out.write(w2);
			out.write(h1);
			out.write(h2);
			for (Entry<Vec3i, Element> t : elements.entrySet()){
				byte x1 = (byte)(t.getKey().x >> 8);
				byte x2 = (byte)(t.getKey().x);
				byte y1 = (byte)(t.getKey().y >> 8);
				byte y2 = (byte)(t.getKey().y);
				byte z1 = (byte)(t.getKey().z >> 8);
				byte z2 = (byte)(t.getKey().z);
				byte id1 = (byte)(t.getValue().id >> 8);
				byte id2 = (byte)(t.getValue().id);
				out.write(x1);
				out.write(x2);
				out.write(y1);
				out.write(y2);
				out.write(z1);
				out.write(z2+128);
				out.write(id1);
				out.write(id2);
			}
			out.write((byte)255);
			out.write((byte)255);
			out.write((byte)255);
			out.write((byte)255);
			out.write((byte)255);
			out.write((byte)255);
			out.write((byte)255);
			out.write((byte)255);
			/*Collider c = null;
			for (int i = 0; i < level.colliders.values().size(); i ++){
				c = ((Collider)(level.colliders.values().toArray())[i]);
				byte x = (byte)((int)c.x/8);
				byte y = (byte)((int)c.y/8);
				byte id1 = (byte)(c.id >> 8);
				byte id2 = (byte)(c.id);
				out.write(x);
				out.write(y);
				out.write(id1);
				out.write(id2);
				System.out.println(id1+","+id2);
				out.write((byte)255);
				out.write((byte)255);
				out.write((byte)255);
				out.write((byte)255);
			}*/
			out.close();
		}
		catch (IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public String getPath(){
		return "assets/maps/"+worldX+"_"+worldZ+".lv";
	}
	
	public boolean exists(){
		String path = getPath();
		File f = new File(path);
		return f.exists() && !f.isDirectory();
	}
	
	public boolean contains(int worldX, int worldY){
		return (worldX - this.worldX) < this.width/Chunk.DIM && (worldY-this.worldZ) < this.height/Chunk.DIM;
	}
	
	public byte[] serialize(){
		List<byte[]> bytes = new ArrayList<>();
		for (Entry<Vec3i, Element> e : elements.entrySet()){
			bytes.add(DataHelper.getBytes(e.getKey().x, 2));
			bytes.add(DataHelper.getBytes(e.getKey().y, 2));
			bytes.add(DataHelper.getBytes(e.getKey().z, 2));
			bytes.add(DataHelper.getBytes(e.getValue().id, 2));
		}
		int count = 0;
		for (byte[] a : bytes){
			count += a.length;
		}
		byte[] data = new byte[count];
		int counter = 0;
		for (int i = 0; i < bytes.size(); i ++){
			byte[] t = bytes.get(i);
			System.arraycopy(t, 0, data, counter, t.length);
			counter += t.length;
		}
		return data;
	}
	
	public void deserialize(byte[] data){
		elements.clear();
		for (int i = 0; i+7 < data.length; i += 8){
			int x = DataHelper.getInt(data[i+0],data[i+1]);
			int y = DataHelper.getInt(data[i+2],data[i+3]);
			int z = DataHelper.getInt(data[i+4],data[i+5]);
			int id = DataHelper.getInt(data[i+6],data[i+7]);
			setElement(new Vec3i(x,y,z),ElementRegistry.getTile(id));
		}
	}
	
	public void load(){
		String path = getPath();
		elements.clear();
		if (exists()){
			try {
				InputStream in = new FileInputStream(path);
				boolean loadingTiles = true;
				byte w1 = (byte)in.read();
				byte w2 = (byte)in.read();
				byte h1 = (byte)in.read();
				byte h2 = (byte)in.read();
				width = (int)(w1 << 8) + w2;
				height = (int)(h1 << 8) + h2;
				while (in.available() > 0){
					byte byte1 = (byte)in.read();
					byte byte2 = (byte)in.read();
					byte byte3 = (byte)in.read();
					byte byte4 = (byte)in.read();
					int byte5 = in.read();
					int byte6 = in.read();
					int byte7 = in.read();
					int byte8 = in.read();
					int x = (int)(byte1 << 8) + byte2;
					int y = (int)(byte3 << 8) + byte4;
					int z = ((int)(byte6))-128;
					int id = (int)(byte7 << 8) + byte8;
					if (byte1 == (byte)255 && byte2 == (byte)255 && byte3 == (byte)255 && byte4 == (byte)255){
						loadingTiles = false;
					}
					else {
						if (loadingTiles){
							Element t = ElementRegistry.getTile(id);
							Vec3i pos = new Vec3i(x,y,z);
							setElement(pos,t);
							t.onLoad(this, pos);
						}
						/*else {
							if (Collider.createColliderFromId(id) != null){
								newLevel.setCollider(Collider.createColliderFromId(id).translate(x, y));
							}
						}*/
					}
				}
			    in.close();
			} catch (IOException e) {
			    e.printStackTrace();
			    System.exit(-1);
			}
		}
		else {
			generate();
		}
	}
	
	public void generate(){
		Random rand = NoiseGenUtil.getRandom(Main.seed, worldX, worldZ);
		for (int i = 0; i < 16; i ++){
			for (int j = 0; j < 16; j ++){
				setElement(new Vec3i(i,0,j), ElementRegistry.getTile(0));
				if (i == 0 || i == 15 || j == 0 || j == 15){
					for (int k = 0; k < 4; k ++){
						if (k >= 2 || i != 8 && i != 7 && j != 7 && j != 8)
						setElement(new Vec3i(i,k+1,j), ElementRegistry.getTile(0));
					}
				}
				else {
					if (rand.nextBoolean()){
						setElement(new Vec3i(i,1,j), ElementRegistry.getTile(1));
					}
					if (rand.nextInt(60) == 0){
						setElement(new Vec3i(i,1,j), ElementRegistry.getTile(2));
					}
				}
			}
		}
	}
	
	public void bake(){
		render.start(GL11.GL_QUADS, true);
		for (Entry<Vec3i, Element> entry : this.elements.entrySet()){
			Element e = entry.getValue();
			Vec3i loc = entry.getKey();
			(e).bake(this, loc, render);
		}
		render.bake();
	}
	
	public void uploadLights(List<Light> lights){
		for (Entry<Vec3i, ILitElement> e : litElements.entrySet()){
			e.getValue().addLights(this, e.getKey(), lights);
		}
	}
	
	public void renderLevel(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,TextureManager.textures.get("tile"));
		
		GL11.glColor4f(1f, 1f, 1f, 1f);
		render.render();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	public void renderUnbaked(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,TextureManager.textures.get("tile"));
		
		ArrayRenderer buf = ArrayRenderer.instance;
		buf.start(GL11.GL_QUADS, false);
		
		GL11.glColor4f(1f, 1f, 1f, 1f);
		for (Entry<Vec3i, IUnbakedElement> e : unbakedElements.entrySet()){
			e.getValue().render(this, e.getKey(), buf);
		}
		
		buf.bake();
		buf.render();
		buf.reset();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	public int getAbsoluteX(){
		return worldX*Chunk.DIM;
	}
	
	public int getAbsoluteZ(){
		return worldZ*Chunk.DIM;
	}
}
