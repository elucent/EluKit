package elukit.client.shader;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL20;

import elukit.common.struct.Mat4f;

public class Shader {
	int shaderId = 0;
	Map<String, Integer> locations = new HashMap<>();
	
	public Shader(int program){
		this.shaderId = program;
	}
	
	public int getId(){
		return shaderId;
	}
	
	public int getLocation(String loc){
		if (locations.containsKey(loc)){
			return locations.get(loc);
		}
		else {
			int l = GL20.glGetUniformLocation(shaderId, loc);
			locations.put(loc, l);
			return l;
		}
	}
	
	public void uniform1i(String loc, int i){
		int l = getLocation(loc);
		GL20.glUniform1i(l, i);
	}
	
	public void uniform2i(String loc, int i1, int i2){
		int l = getLocation(loc);
		GL20.glUniform2i(l, i1, i2);
	}
	
	public void uniform3i(String loc, int i1, int i2, int i3){
		int l = getLocation(loc);
		GL20.glUniform3i(l, i1, i2, i3);
	}
	
	public void uniform4i(String loc, int i1, int i2, int i3, int i4){
		int l = getLocation(loc);
		GL20.glUniform4i(l, i1, i2, i3, i4);
	}
	
	public void uniform1f(String loc, float f){
		int i = getLocation(loc);
		GL20.glUniform1f(i, f);
	}
	
	public void uniform2f(String loc, float f1, float f2){
		int i = getLocation(loc);
		GL20.glUniform2f(i, f1, f2);
	}
	
	public void uniform3f(String loc, float f1, float f2, float f3){
		int i = getLocation(loc);
		GL20.glUniform3f(i, f1, f2, f3);
	}
	
	public void uniform4f(String loc, float f1, float f2, float f3, float f4){
		int i = getLocation(loc);
		GL20.glUniform4f(i, f1, f2, f3, f4);
	}
	
	public void uniformMat4f(String loc, Mat4f mat){
		int i = getLocation(loc);
		GL20.glUniformMatrix4fv(i, true, mat.getUniform());
	}
}
