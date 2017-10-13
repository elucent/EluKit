package elukit.common.struct;

public class Vec3f {
	public float x, y, z;
	public Vec3f(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float length(){
		return (float) Math.sqrt(x*x+y*y+z*z);
	}
	
	public Vec3f normalize(){
		float length = length();
		return new Vec3f(x/length,y/length,z/length);
	}
	
	public Vec3f scale(float scale){
		return new Vec3f(x*scale,y*scale,z*scale);
	}
	
	@Override
	public boolean equals(Object v){
		if (v instanceof Vec3i){
			Vec3i v3 = (Vec3i)v;
			return v3.x == x && v3.y == y && v3.z == z;
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return (""+x+"_"+y+"_"+z).hashCode();
	}
	
	public Vec3f cross(Vec3f v){
		return new Vec3f(
				this.y*v.z-this.z*v.y,
				this.z*v.x-this.x*v.z,
				this.x*v.y-this.y*v.x
				);
	}
}
