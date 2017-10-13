package elukit.common.element;

import java.util.List;
import java.util.Random;

import elukit.client.Main;
import elukit.common.collider.ColliderBox;
import elukit.common.collider.ICollider;
import elukit.common.level.Chunk;
import elukit.common.struct.Vec3i;
import elukit.common.util.ARShapes;
import elukit.common.util.ArrayRenderer;
import elukit.common.util.MathUtil;
import elukit.common.util.NoiseGenUtil;
import elukit.common.util.Primitives;

public class ElementBillboard extends Element implements IUnbakedElement {
	float u = 0, v = 0;
	public ElementBillboard(int id, float u, float v){
		super(id);
		this.u = u;
		this.v = v;
	}
	
	@Override
	public void bake(Chunk level, Vec3i pos, ArrayRenderer buf){
		//
	}

	@Override
	public void render(float x, float y, float z){
		//
	}

	@Override
	public void addCollider(Chunk level, Vec3i pos, List<ICollider> colliders){
		//
	}

	@Override
	public boolean isSolid(){
		return false;
	}

	@Override
	public void render(Chunk level, Vec3i pos, ArrayRenderer buf) {
		if (MathUtil.distSq(level.getAbsoluteX()+pos.x, pos.y, level.getAbsoluteZ()+pos.z, Main.player.x, Main.player.y, Main.player.z) < Main.drawDist*Main.drawDist){
			Random r = NoiseGenUtil.getRandom(Main.seed, pos.x, pos.y, pos.z);
			for (int i = 0; i < 1; i ++){
				ARShapes.renderYBillboard(buf, level.getAbsoluteX()+pos.x+r.nextFloat(), pos.y, level.getAbsoluteZ()+pos.z+r.nextFloat(), 0.5f+0.5f*r.nextFloat(), 0.5f+0.5f*r.nextFloat(), 1f, 1f, 1f, 1f, u, v, u+16f, v+16f);
			}
		}
	}
}
