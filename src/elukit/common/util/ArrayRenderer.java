package elukit.common.util;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL15.*;

import static org.lwjgl.opengl.GL11.*;

public class ArrayRenderer {
	List<Float> vertexData = new ArrayList<>(), colorData = new ArrayList<>(), uvData = new ArrayList<>(), normalData = new ArrayList<>();
	float[] vertexArray, colorArray, uvArray, normalArray;
	int drawMode = GL_TRIANGLES;
	int vertexCount = 0;
	public static ArrayRenderer instance = new ArrayRenderer();
	public boolean vboEnabled = false;
	boolean baked = false;
	
	public boolean getBaked(){
		return baked;
	}
	
	int vertexBuf = 0, colorBuf = 0, uvBuf = 0, normalBuf = 0;
	
	public static enum DATATYPE {
		TYPE_VERT, TYPE_COLOR, TYPE_UV, TYPE_NORMAL
	}
	
	public void start(int mode, boolean vbo){
		drawMode = mode;
		vboEnabled = vbo;
	}
	
	public ArrayRenderer vert(float x, float y, float z){
		Primitives.addVertex(vertexData, x, y, z);
		return this;
	}
	
	public ArrayRenderer tex(float u, float v){
		Primitives.addUV(uvData, u, v);
		return this;
	}
	
	public ArrayRenderer col(float r, float g, float b, float a){
		Primitives.addColor(colorData, r, g, b, a);
		return this;
	}
	
	public ArrayRenderer norm(float x, float y, float z){
		Primitives.addNormal(normalData, x, y, z);
		return this;
	}
	
	public void bake(){
		baked = true;
		if (vboEnabled){
			vertexBuf = glGenBuffers();
			colorBuf = glGenBuffers();
			uvBuf = glGenBuffers();
			normalBuf = glGenBuffers();
		}
		
		vertexArray = new float[vertexData.size()];
		for (int i = 0; i < vertexData.size(); i ++){
			vertexArray[i] = vertexData.get(i);
		}
		
		colorArray = new float[colorData.size()];
		for (int i = 0; i < colorData.size(); i ++){
			colorArray[i] = colorData.get(i);
		}
		
		uvArray = new float[uvData.size()];
		for (int i = 0; i < uvData.size(); i ++){
			uvArray[i] = uvData.get(i);
		}
		
		normalArray = new float[normalData.size()];
		for (int i = 0; i < normalData.size(); i ++){
			normalArray[i] = normalData.get(i);
		}
		
		vertexCount = vertexData.size()/3;
		
		glBindBuffer(GL_ARRAY_BUFFER, vertexBuf);
		glBufferData(GL_ARRAY_BUFFER, vertexArray, GL_DYNAMIC_DRAW);
		
		glBindBuffer(GL_ARRAY_BUFFER, colorBuf);
		glBufferData(GL_ARRAY_BUFFER, colorArray, GL_DYNAMIC_DRAW);
		
		glBindBuffer(GL_ARRAY_BUFFER, uvBuf);
		glBufferData(GL_ARRAY_BUFFER, uvArray, GL_DYNAMIC_DRAW);
		
		glBindBuffer(GL_ARRAY_BUFFER, normalBuf);
		glBufferData(GL_ARRAY_BUFFER, normalArray, GL_DYNAMIC_DRAW);
		
		normalData.clear();
		uvData.clear();
		colorData.clear();
		vertexData.clear();
	}
	
	public void reset(){
		baked = false;
		if (vboEnabled){
			vboEnabled = false;
			glDeleteBuffers(vertexBuf);
			glDeleteBuffers(colorBuf);
			glDeleteBuffers(uvBuf);
			glDeleteBuffers(normalBuf);
		}
		
		vertexArray = new float[0];
		colorArray = new float[0];
		uvArray = new float[0];
		normalArray = new float[0];
	}
	
	public void render(){
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		glEnableClientState(GL_NORMAL_ARRAY);
		
		if (vboEnabled){
			glBindBuffer(GL_ARRAY_BUFFER, vertexBuf);
			glVertexPointer(3, GL_FLOAT, 0, 0);

			glBindBuffer(GL_ARRAY_BUFFER, colorBuf);
			glColorPointer(4, GL_FLOAT, 0, 0);

			glBindBuffer(GL_ARRAY_BUFFER, uvBuf);
			glTexCoordPointer(2, GL_FLOAT, 0, 0);

			glBindBuffer(GL_ARRAY_BUFFER, normalBuf);
			glNormalPointer(GL_FLOAT, 0, 0);
		}
		else {
			glVertexPointer(3, GL_FLOAT, 0, vertexArray);
			glColorPointer(4, GL_FLOAT, 0, colorArray);
			glTexCoordPointer(2, GL_FLOAT, 0, uvArray);
			glNormalPointer(GL_FLOAT, 0, normalArray);
		}
		
		glDrawArrays(drawMode, 0, vertexCount);
		
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		glDisableClientState(GL_NORMAL_ARRAY);
	}
}
