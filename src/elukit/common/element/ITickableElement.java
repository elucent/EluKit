package elukit.common.element;

import elukit.common.level.Chunk;
import elukit.common.struct.Vec3i;

public interface ITickableElement {
	public void update(Chunk level, Vec3i pos);
}
