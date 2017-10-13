package elukit.common.util;

import elukit.common.struct.Vec3f;

public class MathUtil {
	public static final float RT2 = (float)Math.sqrt(2.0f);
	
	public static float lookX(float yaw, float pitch){
		return (float)(Math.sin(Math.toRadians(-yaw))*Math.cos(Math.toRadians(pitch)));
	}
	public static float lookY(float yaw, float pitch){
		return (float)(Math.sin(Math.toRadians(pitch)));
	}
	public static float lookZ(float yaw, float pitch){
		return (float)(Math.cos(Math.toRadians(-yaw))*Math.cos(Math.toRadians(pitch)));
	}
	public static float clamp(float f, float b, float u){
		return Math.max(b, Math.min(u, f));
	}
	public static double fastSqrt(double d){
		return Double.longBitsToDouble( ( ( Double.doubleToLongBits( d )-(1l<<52) )>>1 ) + ( 1l<<61 ) );
	}
	public static float fract(float n){
		return n - (int)Math.floor(n);
	}
	public static Vec3f getNormalFromQuad(float x1, float y1, float z1,
										  float x2, float y2, float z2,
										  float x3, float y3, float z3,
										  float x4, float y4, float z4){
		Vec3f a = new Vec3f(x2-x1,y2-y1,z2-z1);
		Vec3f b = new Vec3f(x4-x1,y4-y1,z4-z1);
		return (new Vec3f(a.y*b.z-a.z*b.y,a.z*b.x-a.x*b.z,a.x*b.y-a.y*b.x)).normalize();
	}
	public static float direction(float x1, float y1, float x2, float y2){
		return (float)Math.atan2(y2-y1, x2-x1);
	}
	
	public static float distSq(float x1, float y1, float z1, float x2, float y2, float z2){
		return (float)(Math.pow((x1-x2),2)+Math.pow((y1-y2),2)+Math.pow((z1-z2),2));
	}
	
	public static float dot(float x1, float y1, float z1, float x2, float y2, float z2){
		return x1*x2 + y1*y2 + z1*z2;
	}
}
