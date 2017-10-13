package elukit.common.entity;

import java.util.List;

import elukit.common.collider.ICollider;
import elukit.common.level.Chunk;
import elukit.common.level.World;
import elukit.common.struct.Vec3i;

public interface ICollidableEntity {
	public void addCollider(World world, List<ICollider> colliders);
}
