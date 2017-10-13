package elukit.common.collider;

public class ColliderBox implements ICollider {
	public float x = 0, y = 0, z = 0, w = 0, h = 0, l = 0;
	public ColliderBox(float x, float y, float z, float w, float h, float l){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		this.h = h;
		this.l = l;
	}
	
	@Override
	public float getBottom(float x, float y, float z) {
		return this.y;
	}
	
	@Override
	public float getTop(float x, float y, float z) {
		return this.y + this.h;
	}
	
	@Override
	public float getWest(float x, float y, float z) {
		return this.x;
	}
	
	@Override
	public float getEast(float x, float y, float z) {
		return this.x + this.w;
	}
	
	@Override
	public float getNorth(float x, float y, float z) {
		return this.z;
	}
	
	@Override
	public float getSouth(float x, float y, float z) {
		return this.z + this.l;
	}
	
	@Override
	public boolean isInside(float x, float y, float z) {
		return x >= this.x && y >= this.y && z >= this.z && x < this.x + this.w && y < this.y + this.h && z < this.z + this.l;
	}

	@Override
	public ICollider translate(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
}
