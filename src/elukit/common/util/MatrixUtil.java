package elukit.common.util;

import elukit.common.struct.Mat4f;

public class MatrixUtil {
	public static Mat4f translate(float x, float y, float z){
		return new Mat4f(new float[]{
				1, 0, 0, x,
				0, 1, 0, y,
				0, 0, 1, z,
				0, 0, 0, 1
		});
	}
	
	public static Mat4f scale(float x, float y, float z){
		return new Mat4f(new float[]{
				x, 0, 0, 0,
				0, y, 0, 0,
				0, 0, z, 0,
				0, 0, 0, 1
		});
	}
	
	public static Mat4f rotateX(float a){
		float r = (float)Math.toRadians(a);
		float c = MathUtil.cos(r);
		float s = MathUtil.sin(r);
		return new Mat4f(new float[]{
				1, 0, 0, 0,
				0, c, -s, 0,
				0, s, c, 0,
				0, 0, 0, 1
		});
	}
	
	public static Mat4f rotateY(float a){
		float r = (float)Math.toRadians(a);
		float c = MathUtil.cos(r);
		float s = MathUtil.sin(r);
		return new Mat4f(new float[]{
				c, 0, s, 0,
				0, 1, 0, 0,
				-s, 0, c, 0,
				0, 0, 0, 1
		});
	}
	
	public static Mat4f rotateZ(float a){
		float r = (float)Math.toRadians(a);
		float c = MathUtil.cos(r);
		float s = MathUtil.sin(r);
		return new Mat4f(new float[]{
				c, -s, 0, 0,
				s, c, 0, 0,
				0, 0, 1, 0,
				0, 0, 0, 1
		});
	}

	public static Mat4f identity() {
		return new Mat4f(new float[]{
				1, 0, 0, 0,
				0, 1, 0, 0,
				0, 0, 1, 0,
				0, 0, 0, 1
		});
	}
}
