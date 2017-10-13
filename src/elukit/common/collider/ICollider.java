package elukit.common.collider;

public interface ICollider {
	public float getBottom(float x, float y, float z);
	public float getTop(float x, float y, float z);
	public float getWest(float x, float y, float z);
	public float getEast(float x, float y, float z);
	public float getNorth(float x, float y, float z);
	public float getSouth(float x, float y, float z);
	public boolean isInside(float x, float y, float z);
	public ICollider translate(float x, float y, float z);
}
