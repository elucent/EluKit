package elukit.common.element;

import java.util.List;

import elukit.common.collider.ColliderBox;
import elukit.common.collider.ICollider;
import elukit.common.level.Chunk;
import elukit.common.struct.Vec3i;
import elukit.common.util.ARShapes;
import elukit.common.util.ArrayRenderer;
import elukit.common.util.Primitives;

public class ElementSimpleCube extends Element {
	float u = 0, v = 0;
	public ElementSimpleCube(int id, float u, float v){
		super(id);
		this.u = u;
		this.v = v;
	}
	
	@Override
	public void bake(Chunk level, Vec3i pos, ArrayRenderer buf){
		ARShapes.addBox(buf,
					pos.x, pos.y+1f, pos.z, 
					pos.x+1f, pos.y, pos.z+1f, 
					1f, 1f, 1f, 1f, 
					!level.elements.containsKey(pos.left()) || level.elements.containsKey(pos.left()) && !level.elements.get(pos.left()).isSolid(), 
					!level.elements.containsKey(pos.right()) || level.elements.containsKey(pos.right()) && !level.elements.get(pos.right()).isSolid(),
					!level.elements.containsKey(pos.down()) || level.elements.containsKey(pos.down()) && !level.elements.get(pos.down()).isSolid(), 
					!level.elements.containsKey(pos.up()) || level.elements.containsKey(pos.up()) && !level.elements.get(pos.up()).isSolid(), 
					!level.elements.containsKey(pos.north()) || level.elements.containsKey(pos.north()) && !level.elements.get(pos.north()).isSolid(), 
					!level.elements.containsKey(pos.south()) || level.elements.containsKey(pos.south()) && !level.elements.get(pos.south()).isSolid(), 
					u, v, 
					u+16f, v+16f);
	}

	@Override
	public void render(float x, float y, float z){
		//Primitives.renderBox(x, y, z, x+1f, y+1f, z+1f, u, v, u+16f/1024f, v+16f/1024f);
	}

	@Override
	public void addCollider(Chunk level, Vec3i pos, List<ICollider> colliders){
		colliders.add(new ColliderBox(pos.x,pos.y,pos.z,1f,1f,1f));
	}
}
