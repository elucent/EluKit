package elukit.client;

import java.util.Stack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import elukit.client.shader.Shader;
import elukit.client.shader.ShaderManager;
import elukit.client.texture.Texture;
import elukit.common.struct.Mat4f;
import elukit.common.util.MatrixUtil;

public class StateManager {
	static Texture tex;
	static Shader shader;
	static Mat4f transform = MatrixUtil.identity();
	static Stack<Mat4f> matStack = new Stack<>();
	
	public static void useShader(Shader shaderIn){
		if (shaderIn == null){
			GL20.glUseProgram(0);
		}
		else {
			GL20.glUseProgram(shaderIn.getId());
		}
		shader = shaderIn;
	}
	
	public static void bindTexture(Texture texture){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
		tex = texture;
		if (shader != null){
			shader.uniform1f("texDim", tex.getWidth());
		}
	}
	
	public static Texture getTex(){
		return tex;
	}
	
	public static Shader getShader(){
		return shader;
	}
	
	public static void identity(){
		transform = MatrixUtil.identity();
	}
	
	public static void pushMatrix(){
		matStack.push(transform.copy());
	}
	
	public static void popMatrix(){
		transform = matStack.pop();
		updateTransform();
	}
	
	public static void rotateX(float angle){
		transform = MatrixUtil.rotateX(angle).mult(transform);
		updateTransform();
	}
	
	public static void rotateY(float angle){
		transform = MatrixUtil.rotateY(angle).mult(transform);
		updateTransform();
	}
	
	public static void rotateZ(float angle){
		transform = MatrixUtil.rotateZ(angle).mult(transform);
		updateTransform();
	}
	
	public static void translate(float dx, float dy, float dz){
		transform = MatrixUtil.translate(dx, dy, dz).mult(transform);
		updateTransform();
	}
	
	public static void updateTransform(){
		shader.uniformMat4f("transform", transform);
	}
}
