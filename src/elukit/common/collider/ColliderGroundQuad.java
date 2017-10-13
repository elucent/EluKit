package elukit.common.collider;

import elukit.client.Main;
import elukit.common.util.NoiseGenUtil;

public class ColliderGroundQuad implements ICollider {
	public float x1 = 0, y1 = 0, z1 = 0, x2 = 0, y2 = 0, z2 = 0, x3 = 0, y3 = 0, z3 = 0, x4 = 0, y4 = 0, z4 = 0;
	public float x = 0, y = 0, z = 0, w = 0, h = 0, l = 0;
	public boolean inverted = false;
	public ColliderGroundQuad(float x1, float y1, float z1,
							float x2, float y2, float z2,
							float x3, float y3, float z3,
							float x4, float y4, float z4,
							boolean inverted){
		//1 -> 2 -> 3 -> 4
		//nxnz -> pxnz -> pxpz -> nxpz
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;
		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;
		this.x3 = x3;
		this.y3 = y3;
		this.z3 = z3;
		this.x4 = x4;
		this.y4 = y4;
		this.z4 = z4;
		this.x = Math.min(Math.min(x1, x2), Math.min(x3, x4));
		this.y = Math.min(Math.min(y1, y2), Math.min(y3, y4));
		this.z = Math.min(Math.min(z1, z2), Math.min(z3, z4));
		this.w = Math.abs(Math.max(Math.max(x1, x2),Math.max(x3, x4))-Math.min(Math.min(x1, x2), Math.min(x3, x4)));
		this.h = Math.abs(Math.max(Math.max(y1, y2),Math.max(y3, y4))-Math.min(Math.min(y1, y2), Math.min(y3, y4)));
		this.l = Math.abs(Math.max(Math.max(z1, z2),Math.max(z3, z4))-Math.min(Math.min(z1, z2), Math.min(z3, z4)));
		this.inverted = inverted;
	}
	
	@Override
	public float getBottom(float x, float y, float z) {
		if (inverted){
			return NoiseGenUtil.bilinear(y1, y2, y3, y4, (x-this.x)/this.w, (z-this.z)/this.l);
		}
		else {
			return this.y + this.h;
		}
	}
	
	@Override
	public float getTop(float x, float y, float z) {
		if (inverted){
			return this.y;
		}
		else {
			return NoiseGenUtil.bilinear(y1, y2, y3, y4, (x-this.x)/this.w, (z-this.z)/this.l);
		}
	}
	
	@Override
	public float getWest(float x, float y, float z) {
		return this.x;
	}
	
	@Override
	public float getEast(float x, float y, float z) {
		if (!inverted && y1 < y2 || inverted && y1 > y2){
			return NoiseGenUtil.bilinear(x1, x2, x3, x4, (y-this.y)/this.h, (z-this.z)/this.l);
		}
		else {
			return this.x + this.w;
		}
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
		if (x >= this.x && (y <= this.y+this.h+0.1f && !inverted || y >= this.y && inverted) && z >= this.z && x <= this.x + this.w && z <= this.z + this.l){
			float height = NoiseGenUtil.bilinear(y1, y2, y3, y4, (x-this.x)/this.w, (z-this.z)/this.l);
			if (!inverted){
				return y >= height;
			}
			else {
				return y <= height;
			}
		}
		return false;
	}

	@Override
	public ICollider translate(float x, float y, float z) {
		this.x1 += x;
		this.y1 += y;
		this.z1 += z;
		this.x2 += x;
		this.y2 += y;
		this.z2 += z;
		this.x3 += x;
		this.y3 += y;
		this.z3 += z;
		this.x4 += x;
		this.y4 += y;
		this.z4 += z;
		return this;
	}
}
