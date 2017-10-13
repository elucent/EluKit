package elukit.common.element;

import elukit.common.level.Chunk;
import elukit.common.struct.Vec3i;
import elukit.common.util.ArrayRenderer;

public interface IUnbakedElement {
	public void render(Chunk level, Vec3i pos, ArrayRenderer buf);
}
