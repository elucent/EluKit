package elukit.common.struct;

public class Vec2i {
	public int x, y;
	public Vec2i(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object v){
		if (v instanceof Vec2i){
			Vec2i v2 = (Vec2i)v;
			return v2.x == x && v2.y == y;
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return (""+x+"_"+y).hashCode();
	}
	
	public Vec2i add(int x, int y){
		return new Vec2i(this.x+x,this.y+y);
	}
}
