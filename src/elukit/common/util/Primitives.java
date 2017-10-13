package elukit.common.util;

import static org.lwjgl.opengl.GL11.*;

import java.util.List;

import elukit.common.struct.Vec3f;
import elukit.common.struct.Vec4f;

public class Primitives {
	public static void addVertex(List<Float> data, float x, float y, float z){
		data.add(x);
		data.add(y);
		data.add(z);
	}
	
	public static void addNormal(List<Float> data, float x, float y, float z){
		data.add(x);
		data.add(y);
		data.add(z);
	}
	
	public static void addColor(List<Float> data, float r, float g, float b, float a){
		data.add(r);
		data.add(g);
		data.add(b);
		data.add(a);
	}
	
	public static void addUV(List<Float> data, float u, float v){
		data.add(u);
		data.add(v);
	}
	
	public static Vec3f getNormal(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3){
		return (new Vec3f(x2-x1,y2-y1,z2-z1)).cross(new Vec3f(x3-x1,y3-y1,z3-z1)).normalize();
	}
}
