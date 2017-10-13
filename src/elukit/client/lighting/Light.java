package elukit.client.lighting;

import java.util.Comparator;

import elukit.client.Main;
import elukit.common.util.MathUtil;

public class Light {
	public float x = 0, y = 0, z = 0, r = 0, g = 0, b = 0, a = 0, rad = 0;
	
	public Light(float x, float y, float z, float r, float g, float b, float a, float rad){
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		this.rad = rad;
	}
	
	public static DistComparator distComparator = new DistComparator();
	
	public static class DistComparator implements Comparator<Light> {
		@Override
		public int compare(Light arg0, Light arg1) {
			double dist1 = MathUtil.distSq(arg0.x,arg0.y,arg0.z,
					Main.player.x,Main.player.y,Main.player.z);
			double dist2 = MathUtil.distSq(arg1.x,arg1.y,arg1.z,
					Main.player.x,Main.player.y,Main.player.z);
			return Double.compare(dist1, dist2);
		}
	}
}
