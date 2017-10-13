package elukit.common.util;

import java.util.Random;

public class NoiseGenUtil {
	public static Random random = new Random();
	public static Random genRandom = new Random();
	public static float getNoise(long seed, int x, int y){
		genRandom.setSeed(simple_hash(new int[]{(int)seed,(int)(seed << 32),(int)Math.signum(y)*512+512,(int)Math.signum(x)*512+512,x,y},6));
		return genRandom.nextFloat();
	}
	
	public static float getNoise(long seed, int... args){
		genRandom.setSeed(simple_hash(args,args.length));
		return genRandom.nextFloat();
	}
	
	public static Random getRandom(long seed, int... args){
		Random r = new Random();
		r.setSeed(simple_hash(args,args.length));
		return r;
	}
	
	public static long getSeed(int seed, int x, int y){
		return simple_hash(new int[]{seed,(int)Math.signum(y)*512+512,(int)Math.signum(x)*512+512,x,y},5);
	}
	
	static int simple_hash(int[] is, int count){
		int i;
		int hash = 80238287;

		for (i = 0; i < count; i++){
			hash = (hash << 4) ^ (hash >> 28) ^ (is[i] * 5449 % 130651);
		}

		return hash % 75327403;
	}
	
	public static float fastSin(float x){
	    if (x < -3.14159265){
	        x += 6.28318531;
	    }
	    else {
	        if (x >  3.14159265){
	            x -= 6.28318531;
	        }
	    }

	    if (x < 0){
	        return (float) (1.27323954 * x + .405284735 * x * x);
	    }
	    else {
	        return (float) (1.27323954 * x - 0.405284735 * x * x);
	    }
	}
	
	public static float fastCos(float x){
	    if (x < -3.14159265){
	        x += 6.28318531;
	    }
	    else {
	        if (x >  3.14159265){
	            x -= 6.28318531;
	        }
	    }
	    x += 1.57079632;
	    if (x >  3.14159265){
	        x -= 6.28318531;
	    }

	    if (x < 0){
	        return (float) (1.27323954 * x + 0.405284735 * x * x);
	    }
	    else {
	        return (float) (1.27323954 * x - 0.405284735 * x * x);
	    }
	}

	public static float lerp(float s, float e, float t){
	    return(s*(1.0f-t)+(e)*t);
	}

	public static float cos_lerp(float s, float e, float t){
	    float t2 = (1.0f-fastCos(t*3.14159265358979323f))/2.0f;
	    return(s*(1.0f-t2)+(e)*t2);
	}
	
	public static float bilinear(float ul, float ur, float dr, float dl, float t1, float t2){
		return lerp(lerp(ul,ur,t1),lerp(dl,dr,t1),t2);
	}
	
	public static float trilinear(float nxnynz, float pxnynz, float pxnypz, float nxnypz, 
			float nxpynz, float pxpynz, float pxpypz, float nxpypz,
			float t1, float t2, float t3){
		return lerp(
				lerp(lerp(nxnynz,pxnynz,t1),lerp(nxnypz,pxnypz,t1),t2)
				,lerp(lerp(nxpynz,pxpynz,t1),lerp(nxpypz,pxpypz,t1),t2)
				,t3);
	}
	
	public static float bicosine(float ul, float ur, float dr, float dl, float t1, float t2){
		return cos_lerp(cos_lerp(ul,ur,t1),cos_lerp(dl,dr,t1),t2);
	}
	
	public static float tricosine(float nxnynz, float pxnynz, float pxnypz, float nxnypz, 
			float nxpynz, float pxpynz, float pxpypz, float nxpypz,
			float t1, float t2, float t3){
		return cos_lerp(
				cos_lerp(cos_lerp(nxnynz,pxnynz,t1),cos_lerp(nxnypz,pxnypz,t1),t2)
				,cos_lerp(cos_lerp(nxpynz,pxpynz,t1),cos_lerp(nxpypz,pxpypz,t1),t2)
				,t3);
	}
	
	public static float getOctave(long seed, int x, int y, int dimen){
		return bicosine(getNoise(seed, (int)Math.floor((float)x/(float)dimen)*dimen,(int)Math.floor((float)y/(float)dimen)*dimen)
				,getNoise(seed, (int)Math.floor((float)x/(float)dimen)*dimen+dimen,(int)Math.floor((float)y/(float)dimen)*dimen)
				,getNoise(seed, (int)Math.floor((float)x/(float)dimen)*dimen+dimen,(int)Math.floor((float)y/(float)dimen)*dimen+dimen)
				,getNoise(seed, (int)Math.floor((float)x/(float)dimen)*dimen,(int)Math.floor((float)y/(float)dimen)*dimen+dimen),
				Math.abs(((float)(((x)-Math.floor(((float)x/(float)dimen))*dimen)))/((float)dimen)),
				Math.abs(((float)(((y)-Math.floor(((float)y/(float)dimen))*dimen)))/((float)dimen)));
	}
	
	public static float getOctave(long seed, int x, int y, int z, int dimen){
		return tricosine(getNoise(seed, (int)Math.floor((float)x/(float)dimen)*dimen,(int)Math.floor((float)y/(float)dimen)*dimen,(int)Math.floor((float)z/(float)dimen)*dimen)
				,getNoise(seed, (int)Math.floor((float)x/(float)dimen)*dimen+dimen,(int)Math.floor((float)y/(float)dimen)*dimen,(int)Math.floor((float)z/(float)dimen)*dimen)
				,getNoise(seed, (int)Math.floor((float)x/(float)dimen)*dimen+dimen,(int)Math.floor((float)y/(float)dimen)*dimen+dimen,(int)Math.floor((float)z/(float)dimen)*dimen)
				,getNoise(seed, (int)Math.floor((float)x/(float)dimen)*dimen,(int)Math.floor((float)y/(float)dimen)*dimen+dimen,(int)Math.floor((float)z/(float)dimen)*dimen)
				,getNoise(seed, (int)Math.floor((float)x/(float)dimen)*dimen,(int)Math.floor((float)y/(float)dimen)*dimen,(int)Math.floor((float)z/(float)dimen)*dimen+dimen)
				,getNoise(seed, (int)Math.floor((float)x/(float)dimen)*dimen+dimen,(int)Math.floor((float)y/(float)dimen)*dimen,(int)Math.floor((float)z/(float)dimen)*dimen+dimen)
				,getNoise(seed, (int)Math.floor((float)x/(float)dimen)*dimen+dimen,(int)Math.floor((float)y/(float)dimen)*dimen+dimen,(int)Math.floor((float)z/(float)dimen)*dimen+dimen)
				,getNoise(seed, (int)Math.floor((float)x/(float)dimen)*dimen,(int)Math.floor((float)y/(float)dimen)*dimen+dimen,(int)Math.floor((float)z/(float)dimen)*dimen+dimen),
				Math.abs(((float)(((x)-Math.floor(((float)x/(float)dimen))*dimen)))/((float)dimen)),
				Math.abs(((float)(((y)-Math.floor(((float)y/(float)dimen))*dimen)))/((float)dimen)),
				Math.abs(((float)(((z)-Math.floor(((float)z/(float)dimen))*dimen)))/((float)dimen)));
	}
}