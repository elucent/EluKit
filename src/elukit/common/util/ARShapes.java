package elukit.common.util;

import java.util.List;

import elukit.client.Main;
import elukit.common.struct.Vec3f;
import elukit.common.struct.Vec4f;

public class ARShapes {
	public static void addTri(ArrayRenderer buf,
			float x1, float y1, float z1, 
			float x2, float y2, float z2, 
			float x3, float y3, float z3, 
			float nx1, float ny1, float nz1,  
			float r, float g, float b, float a,
			float u1, float v1, float u2, float v2, float u3, float v3){
		buf.vert(x1,y1,z1);
		buf.norm(nx1,ny1,nz1);
		buf.col(r,g,b,a);
		buf.tex(u1,v1);
		buf.vert(x2,y2,z2);
		buf.norm(nx1,ny1,nz1);
		buf.col(r,g,b,a);
		buf.tex(u2,v2);
		buf.vert(x3,y3,z3);
		buf.norm(nx1,ny1,nz1);
		buf.col(r,g,b,a);
		buf.tex(u3,v3);
		buf.vert(x3,y3,z3);
		buf.norm(nx1,ny1,nz1);
		buf.col(r,g,b,a);
		buf.tex(u3,v3);
	}
	
	public static void addCyl(ArrayRenderer buf,
		float x1, float y1, float z1, float x2, float y2, float z2,
		float r, float g, float b, float a, 
		float minU, float minV, float maxU, float maxV, int steps){
		float c = 0;
		float s = 0;
		float c2 = 0;
		float s2 = 0;
		float cx = (x1 + x2)/2f;
		float cz = (z1 + z2)/2f;
		float xrad = Math.abs(x2 - x1)/2f;
		float zrad = Math.abs(z2 - z1)/2f;
		float incr = ((float)Math.PI*2f)/steps;
		float nc = (c + c2)/2f;
		float ns = (s + s2)/2f;
		for (float i = 0; i < steps; i ++){
			float ang = (float)Math.PI*2.0f*((float)i/(float)steps);
			float ang2 = (float)Math.PI*2.0f*((float)(i+1)/(float)steps);
			c = (float)Math.cos(ang);
			s = (float)Math.sin(ang);
			c2 = (float)Math.cos(ang2);
			s2 = (float)Math.sin(ang2);
			nc = (c + c2)/2f;
			ns = (s + s2)/2f;
			buf.vert( cx + xrad*s, y1, cz + zrad*c);
			buf.norm( nc, 0, -ns);
			buf.col( r, g, b, a);
			buf.tex( minU, minV);
			buf.vert( cx + xrad*s2, y1, cz + zrad*c2);
			buf.norm( nc, 0, -ns);
			buf.col( r, g, b, a);
			buf.tex( maxU, minV);
			buf.vert( cx + xrad*s2, y2, cz + zrad*c2);
			buf.norm( nc, 0, -ns);
			buf.col( r, g, b, a);
			buf.tex( maxU, maxV);
			buf.vert( cx + xrad*s, y2, cz + zrad*c);
			buf.norm( nc, 0, -ns);
			buf.col( r, g, b, a);
			buf.tex( minU, maxV);
		}
	}
	
	public static void addXWall(ArrayRenderer buf,
			float x1, float y1, float z1, float x2, float y2, float z2,
			float nx, float ny, float nz, float r, float g, float b, float a,
			float minU, float minV, float maxU, float maxV){
		buf.vert(x1,y1,z1);
		buf.norm(nx,ny,nz);
		buf.col(r,g,b,a);
		buf.tex(minU,minV);
		buf.vert(x1,y1,z2);
		buf.norm(nx,ny,nz);
		buf.col(r,g,b,a);
		buf.tex(maxU,minV);
		buf.vert(x1,y2,z2);
		buf.norm(nx,ny,nz);
		buf.col(r,g,b,a);
		buf.tex(maxU,maxV);
		buf.vert(x1,y2,z1);
		buf.norm(nx,ny,nz);
		buf.col(r,g,b,a);
		buf.tex(minU,maxV);
	}
	
	public static void addZWall(ArrayRenderer buf,
			float x1, float y1, float z1, float x2, float y2, float z2,
			float nx, float ny, float nz, float r, float g, float b, float a,
			float minU, float minV, float maxU, float maxV){
		buf.vert(x1,y1,z1);
		buf.norm(nx,ny,nz);
		buf.col(r,g,b,a);
		buf.tex(minU,minV);
		buf.vert(x2,y1,z1);
		buf.norm(nx,ny,nz);
		buf.col(r,g,b,a);
		buf.tex(maxU,minV);
		buf.vert(x2,y2,z1);
		buf.norm(nx,ny,nz);
		buf.col(r,g,b,a);
		buf.tex(maxU,maxV);
		buf.vert(x1,y2,z1);
		buf.norm(nx,ny,nz);
		buf.col(r,g,b,a);
		buf.tex(minU,maxV);
	}
	
	public static void addFloor(ArrayRenderer buf,
			float x1, float y1, float z1, float x2, float y2, float z2,
			float nx, float ny, float nz, float r, float g, float b, float a,
			float minU, float minV, float maxU, float maxV){
		buf.vert(x1,y1,z1);
		buf.norm(nx,ny,nz);
		buf.col(r,g,b,a);
		buf.tex(minU,minV);
		buf.vert(x2,y1,z1);
		buf.norm(nx,ny,nz);
		buf.col(r,g,b,a);
		buf.tex(maxU,minV);
		buf.vert(x2,y1,z2);
		buf.norm(nx,ny,nz);
		buf.col(r,g,b,a);
		buf.tex(maxU,maxV);
		buf.vert(x1,y1,z2);
		buf.norm(nx,ny,nz);
		buf.col(r,g,b,a);
		buf.tex(minU,maxV);
	}
	
	public static void addQuad(ArrayRenderer buf,
			float x1, float y1, float z1, 
			float x2, float y2, float z2, 
			float x3, float y3, float z3, 
			float x4, float y4, float z4, 
			float nx1, float ny1, float nz1,  
			float r, float g, float b, float a,
			float minU, float minV, float maxU, float maxV){
		buf.vert(x1,y1,z1);
		buf.norm(nx1,ny1,nz1);
		buf.col(r,g,b,a);
		buf.tex(minU,minV);
		buf.vert(x2,y2,z2);
		buf.norm(nx1,ny1,nz1);
		buf.col(r,g,b,a);
		buf.tex(maxU,minV);
		buf.vert(x3,y3,z3);
		buf.norm(nx1,ny1,nz1);
		buf.col(r,g,b,a);
		buf.tex(maxU,maxV);
		buf.vert(x4,y4,z4);
		buf.norm(nx1,ny1,nz1);
		buf.col(r,g,b,a);
		buf.tex(minU,maxV);
	}
	
	public static void addQuad(ArrayRenderer buf,
			float x1, float y1, float z1, 
			float x2, float y2, float z2, 
			float x3, float y3, float z3, 
			float x4, float y4, float z4, 
			float r, float g, float b, float a,
			float minU, float minV, float maxU, float maxV){
		Vec3f normal = MathUtil.getNormalFromQuad(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4).scale(-1.0f);
		buf.vert(x1,y1,z1);
		buf.norm(normal.x,normal.y,normal.z);
		buf.col(r,g,b,a);
		buf.tex(minU,minV);
		buf.vert(x2,y2,z2);
		buf.norm(normal.x,normal.y,normal.z);
		buf.col(r,g,b,a);
		buf.tex(maxU,minV);
		buf.vert(x3,y3,z3);
		buf.norm(normal.x,normal.y,normal.z);
		buf.col(r,g,b,a);
		buf.tex(maxU,maxV);
		buf.vert(x4,y4,z4);
		buf.norm(normal.x,normal.y,normal.z);
		buf.col(r,g,b,a);
		buf.tex(minU,maxV);
	}
	
	public static void addQuad(ArrayRenderer buf,
			float x1, float y1, float z1, 
			float x2, float y2, float z2, 
			float x3, float y3, float z3, 
			float x4, float y4, float z4, 
			float nx1, float ny1, float nz1, 
			float nx2, float ny2, float nz2, 
			float nx3, float ny3, float nz3, 
			float nx4, float ny4, float nz4, 
			float r, float g, float b, float a,
			float minU, float minV, float maxU, float maxV){
		buf.vert(x1,y1,z1);
		buf.norm(nx1,ny1,nz1);
		buf.col(r,g,b,a);
		buf.tex(minU,minV);
		buf.vert(x2,y2,z2);
		buf.norm(nx2,ny2,nz2);
		buf.col(r,g,b,a);
		buf.tex(maxU,minV);
		buf.vert(x3,y3,z3);
		buf.norm(nx3,ny3,nz3);
		buf.col(r,g,b,a);
		buf.tex(maxU,maxV);
		buf.vert(x4,y4,z4);
		buf.norm(nx4,ny4,nz4);
		buf.col(r,g,b,a);
		buf.tex(minU,maxV);
	}
	
	public static void addBox(ArrayRenderer buf, float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b, float a, boolean negX, boolean posX, boolean negY, boolean posY, boolean negZ, boolean posZ,
			float minU, float minV, float maxU, float maxV){
		int faceCount = (negZ ? 1 : 0)
				+ (posZ ? 1 : 0)
				+ (negY ? 1 : 0)
				+ (posY ? 1 : 0)
				+ (negX ? 1 : 0)
				+ (posX ? 1 : 0);
		for (int i = 0; i < 4*faceCount; i ++){
			buf.col(r,g,b,a);
		}
		for (int i = 0; i < faceCount; i ++){
			buf.tex(minU,minV);
			buf.tex(maxU,minV);
			buf.tex(maxU,maxV);
			buf.tex(minU,maxV);
		}
		
		if (negZ){
			buf.norm(0,0,-1);
			buf.norm(0,0,-1);
			buf.norm(0,0,-1);
			buf.norm(0,0,-1);
			buf.vert(x1,y1,z1); 
			buf.vert(x2,y1,z1);
			buf.vert(x2,y2,z1);
			buf.vert(x1,y2,z1);
		}
		
		if (negX){
			buf.norm(-1,0,0);
			buf.norm(-1,0,0);
			buf.norm(-1,0,0);
			buf.norm(-1,0,0);
			buf.vert(x1,y1,z2);
			buf.vert(x1,y1,z1);
			buf.vert(x1,y2,z1);
			buf.vert(x1,y2,z2);
		}
	
		if (negY){
			buf.norm(0,-1,0);
			buf.norm(0,-1,0);
			buf.norm(0,-1,0);
			buf.norm(0,-1,0);
			buf.vert(x2,y1,z2); 
			buf.vert(x1,y1,z2);
			buf.vert(x1,y1,z1);
			buf.vert(x2,y1,z1);
		}
		
		if (posZ){
			buf.norm(0,0,1);
			buf.norm(0,0,1);
			buf.norm(0,0,1);
			buf.norm(0,0,1);
			buf.vert(x2,y1,z2); 
			buf.vert(x1,y1,z2); 
			buf.vert(x1,y2,z2); 
			buf.vert(x2,y2,z2); 
		}
		
		if (posX){
			buf.norm(1,0,0);
			buf.norm(1,0,0);
			buf.norm(1,0,0);
			buf.norm(1,0,0);
			buf.vert(x2,y1,z1); 
			buf.vert(x2,y1,z2); 
			buf.vert(x2,y2,z2); 
			buf.vert(x2,y2,z1); 
		}
	
		if (posY){
			buf.norm(0,1,0);
			buf.norm(0,1,0);
			buf.norm(0,1,0);
			buf.norm(0,1,0);
			buf.vert(x2,y2,z2); 
			buf.vert(x1,y2,z2); 
			buf.vert(x1,y2,z1); 
			buf.vert(x2,y2,z1); 
		}
	}
	
	public static void addBox(ArrayRenderer buf, float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b, float a, boolean negX, boolean posX, boolean negY, boolean posY, boolean negZ, boolean posZ,
			Vec4f[] textures){
		int faceCount = (negZ ? 1 : 0)
				+ (posZ ? 1 : 0)
				+ (negY ? 1 : 0)
				+ (posY ? 1 : 0)
				+ (negX ? 1 : 0)
				+ (posX ? 1 : 0);
		for (int i = 0; i < 4*faceCount; i ++){
			buf.col(r,g,b,a);
		}
		for (int i = 0; i < faceCount; i ++){
			buf.tex(textures[i].x,textures[i].y);
			buf.tex(textures[i].z,textures[i].y);
			buf.tex(textures[i].z,textures[i].w);
			buf.tex(textures[i].x,textures[i].w);
		}
	
		if (negY){
			buf.norm(0,-1,0);
			buf.norm(0,-1,0);
			buf.norm(0,-1,0);
			buf.norm(0,-1,0);
			buf.vert(x2,y1,z2); 
			buf.vert(x1,y1,z2);
			buf.vert(x1,y1,z1);
			buf.vert(x2,y1,z1);
		}
	
		if (posY){
			buf.norm(0,1,0);
			buf.norm(0,1,0);
			buf.norm(0,1,0);
			buf.norm(0,1,0);
			buf.vert(x2,y2,z2); 
			buf.vert(x1,y2,z2); 
			buf.vert(x1,y2,z1); 
			buf.vert(x2,y2,z1); 
		}
		
		if (negZ){
			buf.norm(0,0,-1);
			buf.norm(0,0,-1);
			buf.norm(0,0,-1);
			buf.norm(0,0,-1);
			buf.vert(x1,y1,z1); 
			buf.vert(x2,y1,z1);
			buf.vert(x2,y2,z1);
			buf.vert(x1,y2,z1);
		}
		
		if (posZ){
			buf.norm(0,0,1);
			buf.norm(0,0,1);
			buf.norm(0,0,1);
			buf.norm(0,0,1);
			buf.vert(x2,y1,z2); 
			buf.vert(x1,y1,z2); 
			buf.vert(x1,y2,z2); 
			buf.vert(x2,y2,z2); 
		}
		
		if (negX){
			buf.norm(-1,0,0);
			buf.norm(-1,0,0);
			buf.norm(-1,0,0);
			buf.norm(-1,0,0);
			buf.vert(x1,y1,z2);
			buf.vert(x1,y1,z1);
			buf.vert(x1,y2,z1);
			buf.vert(x1,y2,z2);
		}
		
		if (posX){
			buf.norm(1,0,0);
			buf.norm(1,0,0);
			buf.norm(1,0,0);
			buf.norm(1,0,0);
			buf.vert(x2,y1,z1); 
			buf.vert(x2,y1,z2); 
			buf.vert(x2,y2,z2); 
			buf.vert(x2,y2,z1); 
		}
	}
	
	public static enum EnumFace {
		WEST, EAST, DOWN, UP, NORTH, SOUTH
	}
	
	public static void texForPos(ArrayRenderer buf, float x, float y, float z, Vec4f tex, EnumFace face){
		float u = tex.x;
		float v = tex.y;
		float tw = tex.z-tex.x;
		float th = tex.w-tex.y;
		float sx = MathUtil.fract(x);
		float sy = MathUtil.fract(y);
		float sz = MathUtil.fract(z);
		switch(face){
		case DOWN:
			buf.tex(u+(float)(sx*tw),v+(float)(sz*th));
			break;
		case EAST:
			buf.tex(u+(float)(tw-sz*tw),v+(float)(th-sy*th));
			break;
		case NORTH:
			buf.tex(u+(float)(sx*tw),v+(float)(th-sy*th));
			break;
		case SOUTH:
			buf.tex(u+(float)(tw-sx*tw),v+(float)(th-sy*th));
			break;
		case UP:
			buf.tex(u+(float)(tw-sx*tw),v+(float)(sz*th));
			break;
		case WEST:
			buf.tex(u+(float)(sz*tw),v+(float)(th-sy*th));
			break;
		default:
			break;	
		}
	}
	
	public static void addSegmentAutotex(ArrayRenderer buf, 
									float x1, float y1, float z1,
									float x2, float y2, float z2,
									float x3, float y3, float z3,
									float x4, float y4, float z4,
									float x5, float y5, float z5,
									float x6, float y6, float z6,
									float x7, float y7, float z7,
									float x8, float y8, float z8, 
									float r, float g, float b, float a,
									Vec4f[] textures){
		//1 -> 2 -> 3 -> 4
		//nxnz -> pxnz -> pxpz -> nxpz
		
		//texorder: up, down, north, south, east, west
		int faceCount = 6;
		for (int i = 0; i < 4*faceCount; i ++){
			buf.col(r,g,b,a);
		}
		
		Vec3f down = Primitives.getNormal(x1,y1,z1,x2,y2,z2,x3,y3,z3).scale(-1f);
		Vec3f up = Primitives.getNormal(x5,y5,z5,x6,y6,z6,x7,y7,z7);
		Vec3f west = Primitives.getNormal(x1,y1,z1,x8,y8,z8,x5,y5,z5);
		Vec3f east = Primitives.getNormal(x3,y3,z3,x6,y6,z6,x7,y7,z7);
		Vec3f south = Primitives.getNormal(x4,y4,z4,x7,y7,z7,x8,y8,z8);
		Vec3f north = Primitives.getNormal(x1,y1,z1,x5,y5,z5,x6,y6,z6);
		
		buf.norm(up.x,up.y,up.z);
		buf.vert(x1,y1,z1);
		texForPos(buf,x1,y1,z1,textures[0],EnumFace.UP);
		buf.norm(up.x,up.y,up.z);
		buf.vert(x2,y2,z2);
		texForPos(buf,x2,y2,z2,textures[0],EnumFace.UP);
		buf.norm(up.x,up.y,up.z);
		buf.vert(x3,y3,z3);
		texForPos(buf,x3,y3,z3,textures[0],EnumFace.UP);
		buf.norm(up.x,up.y,up.z);
		buf.vert(x4,y4,z4);
		texForPos(buf,x4,y4,z4,textures[0],EnumFace.UP);
		
		buf.norm(down.x,down.y,down.z);
		buf.vert(x5,y5,z5);
		texForPos(buf,x5,y5,z5,textures[1],EnumFace.DOWN);
		buf.norm(down.x,down.y,down.z);
		buf.vert(x6,y6,z6);
		texForPos(buf,x6,y6,z6,textures[1],EnumFace.DOWN);
		buf.norm(down.x,down.y,down.z);
		buf.vert(x7,y7,z7);
		texForPos(buf,x7,y7,z7,textures[1],EnumFace.DOWN);
		buf.norm(down.x,down.y,down.z);
		buf.vert(x8,y8,z8);
		texForPos(buf,x8,y8,z8,textures[1],EnumFace.DOWN);
		
		buf.norm(west.x,west.y,west.z);
		buf.vert(x1,y1,z1);
		texForPos(buf,x1,y1,z1,textures[5],EnumFace.WEST);
		buf.norm(west.x,west.y,west.z);
		buf.vert(x4,y4,z4);
		texForPos(buf,x4,y4,z4,textures[5],EnumFace.WEST);
		buf.norm(west.x,west.y,west.z);
		buf.vert(x8,y8,z8);
		texForPos(buf,x8,y8,z8,textures[5],EnumFace.WEST);
		buf.norm(west.x,west.y,west.z);
		buf.vert(x5,y5,z5);
		texForPos(buf,x5,y5,z5,textures[5],EnumFace.WEST);
		
		buf.norm(east.x,east.y,east.z);
		buf.vert(x2,y2,z2);
		texForPos(buf,x2,y2,z2,textures[4],EnumFace.EAST);
		buf.norm(east.x,east.y,east.z);
		buf.vert(x3,y3,z3);
		texForPos(buf,x3,y3,z3,textures[4],EnumFace.EAST);
		buf.norm(east.x,east.y,east.z);
		buf.vert(x7,y7,z7);
		texForPos(buf,x7,y7,z7,textures[4],EnumFace.EAST);
		buf.norm(east.x,east.y,east.z);
		buf.vert(x6,y6,z6);
		texForPos(buf,x6,y6,z6,textures[4],EnumFace.EAST);
		
		buf.norm(north.x,north.y,north.z);
		buf.vert(x3,y3,z3);
		texForPos(buf,x3,y3,z3,textures[2],EnumFace.NORTH);
		buf.norm(north.x,north.y,north.z);
		buf.vert(x4,y4,z4);
		texForPos(buf,x4,y4,z4,textures[2],EnumFace.NORTH);
		buf.norm(north.x,north.y,north.z);
		buf.vert(x8,y8,z8);
		texForPos(buf,x8,y8,z8,textures[2],EnumFace.NORTH);
		buf.norm(north.x,north.y,north.z);
		buf.vert(x7,y7,z7);
		texForPos(buf,x7,y7,z7,textures[2],EnumFace.NORTH);
		
		buf.norm(south.x,south.y,south.z);
		buf.vert(x1,y1,z1);
		texForPos(buf,x1,y1,z1,textures[3],EnumFace.SOUTH);
		buf.norm(south.x,south.y,south.z);
		buf.vert(x2,y2,z2);
		texForPos(buf,x2,y2,z2,textures[3],EnumFace.SOUTH);
		buf.norm(south.x,south.y,south.z);
		buf.vert(x6,y6,z6);
		texForPos(buf,x6,y6,z6,textures[3],EnumFace.SOUTH);
		buf.norm(south.x,south.y,south.z);
		buf.vert(x5,y5,z5);
		texForPos(buf,x5,y5,z5,textures[3],EnumFace.SOUTH);
	}
	
	public static void addSegment(ArrayRenderer buf, 
									float x1, float y1, float z1,
									float x2, float y2, float z2,
									float x3, float y3, float z3,
									float x4, float y4, float z4,
									float x5, float y5, float z5,
									float x6, float y6, float z6,
									float x7, float y7, float z7,
									float x8, float y8, float z8, 
									float r, float g, float b, float a,
									Vec4f[] textures){
		//1 -> 2 -> 3 -> 4
		//nxnz -> pxnz -> pxpz -> nxpz
		
		//texorder: up, down, north, south, east, west
		int faceCount = 6;
		for (int i = 0; i < 4*faceCount; i ++){
			buf.col(r,g,b,a);
		}
		for (int i = 0; i < faceCount; i ++){
			buf.tex(textures[i].x,textures[i].y);
			buf.tex(textures[i].z,textures[i].y);
			buf.tex(textures[i].z,textures[i].w);
			buf.tex(textures[i].x,textures[i].w);
		}
		
		Vec3f down = Primitives.getNormal(x1,y1,z1,x2,y2,z2,x3,y3,z3).scale(-1f);
		Vec3f up = Primitives.getNormal(x5,y5,z5,x6,y6,z6,x7,y7,z7);
		Vec3f west = Primitives.getNormal(x1,y1,z1,x8,y8,z8,x5,y5,z5);
		Vec3f east = Primitives.getNormal(x3,y3,z3,x6,y6,z6,x7,y7,z7);
		Vec3f south = Primitives.getNormal(x4,y4,z4,x7,y7,z7,x8,y8,z8);
		Vec3f north = Primitives.getNormal(x1,y1,z1,x5,y5,z5,x6,y6,z6);
		
		buf.norm(up.x,up.y,up.z);
		buf.vert(x1,y1,z1);
		buf.norm(up.x,up.y,up.z);
		buf.vert(x2,y2,z2);
		buf.norm(up.x,up.y,up.z);
		buf.vert(x3,y3,z3);
		buf.norm(up.x,up.y,up.z);
		buf.vert(x4,y4,z4);
		
		buf.norm(down.x,down.y,down.z);
		buf.vert(x5,y5,z5);
		buf.norm(down.x,down.y,down.z);
		buf.vert(x6,y6,z6);
		buf.norm(down.x,down.y,down.z);
		buf.vert(x7,y7,z7);
		buf.norm(down.x,down.y,down.z);
		buf.vert(x8,y8,z8);
		
		buf.norm(west.x,west.y,west.z);
		buf.vert(x1,y1,z1);
		buf.norm(west.x,west.y,west.z);
		buf.vert(x4,y4,z4);
		buf.norm(west.x,west.y,west.z);
		buf.vert(x8,y8,z8);
		buf.norm(west.x,west.y,west.z);
		buf.vert(x5,y5,z5);
		
		buf.norm(east.x,east.y,east.z);
		buf.vert(x2,y2,z2);
		buf.norm(east.x,east.y,east.z);
		buf.vert(x3,y3,z3);
		buf.norm(east.x,east.y,east.z);
		buf.vert(x7,y7,z7);
		buf.norm(east.x,east.y,east.z);
		buf.vert(x6,y6,z6);
		
		buf.norm(north.x,north.y,north.z);
		buf.vert(x3,y3,z3);
		buf.norm(north.x,north.y,north.z);
		buf.vert(x4,y4,z4);
		buf.norm(north.x,north.y,north.z);
		buf.vert(x8,y8,z8);
		buf.norm(north.x,north.y,north.z);
		buf.vert(x7,y7,z7);
		
		buf.norm(south.x,south.y,south.z);
		buf.vert(x1,y1,z1);
		buf.norm(south.x,south.y,south.z);
		buf.vert(x2,y2,z2);
		buf.norm(south.x,south.y,south.z);
		buf.vert(x6,y6,z6);
		buf.norm(south.x,south.y,south.z);
		buf.vert(x5,y5,z5);
	}
	
	public static void renderCenteredBillboard(ArrayRenderer buf,
			float x, float y, float z,
			float width, float height,
			float r, float g, float b, float a,
			float u1, float v1, float u2, float v2){
		float dx = x-Main.player.x;
		float dy = (y+height*0.5f)-(Main.player.y+Main.player.eyeHeight);
		float dz = z-Main.player.z;
		float dist = (float)Math.sqrt(dx*dx+dz*dz);
		
		double yaw = Math.atan2(dx, dz);
		double pitch = Math.atan2(dy, dist);
		
		float tX1 = width*(float)Math.cos(yaw);
		float tY1 = 0;
		float tZ1 = -width*(float)Math.sin(yaw);
		
		float tX2 = width*(float)Math.sin(yaw)*-(float)Math.sin(pitch);
		float tY2 = width*(float)Math.cos(pitch);
		float tZ2 = width*(float)Math.cos(yaw)*-(float)Math.sin(pitch);
		
		buf.vert(x-tX1*0.5f*width+tX2*height*0.5f, y-tY1*0.5f*width+tY2*height*0.5f, z-tZ1*0.5f*width+tZ2*height*0.5f).col(r, g, b, a).tex(u1, v1).norm(Main.world.light.x, Main.world.light.y, Main.world.light.z);
		buf.vert(x+tX1*0.5f*width+tX2*height*0.5f, y+tY1*0.5f*width+tY2*height*0.5f, z+tZ1*0.5f*width+tZ2*height*0.5f).col(r, g, b, a).tex(u2, v1).norm(Main.world.light.x, Main.world.light.y, Main.world.light.z);
		buf.vert(x+tX1*0.5f*width-tX2*height*0.5f, y+tY1*0.5f*width-tY2*height*0.5f, z+tZ1*0.5f*width-tZ2*height*0.5f).col(r, g, b, a).tex(u2, v2).norm(Main.world.light.x, Main.world.light.y, Main.world.light.z);
		buf.vert(x-tX1*0.5f*width-tX2*height*0.5f, y-tY1*0.5f*width-tY2*height*0.5f, z-tZ1*0.5f*width-tZ2*height*0.5f).col(r, g, b, a).tex(u1, v2).norm(Main.world.light.x, Main.world.light.y, Main.world.light.z);
	}
	
	public static void renderBillboard(ArrayRenderer buf,
			float x, float y, float z,
			float width, float height,
			float r, float g, float b, float a,
			float u1, float v1, float u2, float v2){
		float dx = x-Main.player.x;
		float dy = (y+height*0.5f)-(Main.player.y+Main.player.eyeHeight);
		float dz = z-Main.player.z;
		float dist = (float)Math.sqrt(dx*dx+dz*dz);
		
		double yaw = Math.atan2(dx, dz);
		double pitch = Math.atan2(dy, dist);
		
		float tX1 = width*(float)Math.cos(yaw);
		float tY1 = 0;
		float tZ1 = -width*(float)Math.sin(yaw);
		
		float tX2 = width*(float)Math.sin(yaw)*-(float)Math.sin(pitch);
		float tY2 = width*(float)Math.cos(pitch);
		float tZ2 = width*(float)Math.cos(yaw)*-(float)Math.sin(pitch);
		
		buf.vert(x-tX1*0.5f*width+tX2*height, y-tY1*0.5f*width+tY2*height, z-tZ1*0.5f*width+tZ2*height).col(r, g, b, a).tex(u1, v1).norm(Main.world.light.x, Main.world.light.y, Main.world.light.z);
		buf.vert(x+tX1*0.5f*width+tX2*height, y+tY1*0.5f*width+tY2*height, z+tZ1*0.5f*width+tZ2*height).col(r, g, b, a).tex(u2, v1).norm(Main.world.light.x, Main.world.light.y, Main.world.light.z);
		buf.vert(x+tX1*0.5f*width, y+tY1*0.5f*width, z+tZ1*0.5f*width).col(r, g, b, a).tex(u2, v2).norm(Main.world.light.x, Main.world.light.y, Main.world.light.z);
		buf.vert(x-tX1*0.5f*width, y-tY1*0.5f*width, z-tZ1*0.5f*width).col(r, g, b, a).tex(u1, v2).norm(Main.world.light.x, Main.world.light.y, Main.world.light.z);
	}
	
	public static void renderYBillboard(ArrayRenderer buf,
			float x, float y, float z,
			float width, float height,
			float r, float g, float b, float a,
			float u1, float v1, float u2, float v2){
		float dx = x-Main.player.x;
		float dz = z-Main.player.z;
		
		double ang = Math.atan2(dx, dz);
		float cos = (float)Math.cos(ang+Math.PI/2.0);
		float sin = (float)Math.sin(ang+Math.PI/2.0);
		
		buf.vert(x-sin*width*0.5f, y+height, z-cos*width*0.5f).col(r, g, b, a).tex(u1, v1).norm(Main.world.light.x, Main.world.light.y, Main.world.light.z);
		buf.vert(x+sin*width*0.5f, y+height, z+cos*width*0.5f).col(r, g, b, a).tex(u2, v1).norm(Main.world.light.x, Main.world.light.y, Main.world.light.z);
		buf.vert(x+sin*width*0.5f, y, z+cos*width*0.5f).col(r, g, b, a).tex(u2, v2).norm(Main.world.light.x, Main.world.light.y, Main.world.light.z);
		buf.vert(x-sin*width*0.5f, y, z-cos*width*0.5f).col(r, g, b, a).tex(u1, v2).norm(Main.world.light.x, Main.world.light.y, Main.world.light.z);
	}
}
