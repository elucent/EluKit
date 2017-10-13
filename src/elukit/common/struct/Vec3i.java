package elukit.common.struct;

public class Vec3i {
	public int x, y, z;
	public Vec3i(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
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
	
	public Vec3i add(int x, int y, int z){
		return new Vec3i(this.x+x,this.y+y,this.z+z);
	}
	
	public Vec3i north(){
		return new Vec3i(this.x,this.y,this.z-1);
	}
	
	public Vec3i south(){
		return new Vec3i(this.x,this.y,this.z+1);
	}
	
	public Vec3i down(){
		return new Vec3i(this.x,this.y+1,this.z);
	}
	
	public Vec3i up(){
		return new Vec3i(this.x,this.y-1,this.z);
	}
	
	public Vec3i left(){
		return new Vec3i(this.x-1,this.y,this.z);
	}
	
	public Vec3i right(){
		return new Vec3i(this.x+1,this.y,this.z);
	}
}
