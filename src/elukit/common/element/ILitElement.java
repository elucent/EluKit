package elukit.common.element;

import java.util.List;

import elukit.client.lighting.Light;
import elukit.common.level.Chunk;
import elukit.common.struct.Vec3i;

public interface ILitElement {
	public void addLights(Chunk level, Vec3i pos, List<Light> lights);
}
