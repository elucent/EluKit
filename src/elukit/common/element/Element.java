package elukit.common.element;

import java.util.List;

import elukit.common.collider.ColliderBox;
import elukit.common.collider.ICollider;
import elukit.common.level.Chunk;
import elukit.common.struct.Vec3i;
import elukit.common.util.ArrayRenderer;
import elukit.common.util.Primitives;

public class Element {
	public int id = 0;
	
	public Element(int id){
		this.id = id;
	}
	
	public void bake(Chunk level, Vec3i pos, ArrayRenderer buf){
	}
	
	public void render(float x, float y, float z){
	}
	
	public void onLoad(Chunk level, Vec3i pos){
		
	}
	
	public boolean isSolid(){
		return true;
	}
	
	public void addCollider(Chunk level, Vec3i pos, List<ICollider> colliders){
	}
}
