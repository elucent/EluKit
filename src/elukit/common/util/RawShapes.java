package elukit.common.util;

import java.util.List;

import elukit.common.struct.Vec3f;
import elukit.common.struct.Vec4f;

public class RawShapes {

	
	public static void addTri(List<Float> vertexData, List<Float> colorData, List<Float> uvData, List<Float> normalData,
			float x1, float y1, float z1, 
			float x2, float y2, float z2, 
			float x3, float y3, float z3, 
			float nx1, float ny1, float nz1,  
			float r, float g, float b, float a,
			float u1, float v1, float u2, float v2, float u3, float v3){
		Primitives.addVertex(vertexData,x1,y1,z1);
		Primitives.addNormal(normalData,nx1,ny1,nz1);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,u1,v1);
		Primitives.addVertex(vertexData,x2,y2,z2);
		Primitives.addNormal(normalData,nx1,ny1,nz1);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,u2,v2);
		Primitives.addVertex(vertexData,x3,y3,z3);
		Primitives.addNormal(normalData,nx1,ny1,nz1);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,u3,v3);
		Primitives.addVertex(vertexData,x3,y3,z3);
		Primitives.addNormal(normalData,nx1,ny1,nz1);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,u3,v3);
	}
	
	public static void addCyl(List<Float> vertexData, List<Float> colorData, List<Float> uvData, List<Float> normalData,
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
			Primitives.addVertex(vertexData, cx + xrad*s, y1, cz + zrad*c);
			Primitives.addNormal(normalData, nc, 0, -ns);
			Primitives.addColor(colorData, r, g, b, a);
			Primitives.addUV(uvData, minU, minV);
			Primitives.addVertex(vertexData, cx + xrad*s2, y1, cz + zrad*c2);
			Primitives.addNormal(normalData, nc, 0, -ns);
			Primitives.addColor(colorData, r, g, b, a);
			Primitives.addUV(uvData, maxU, minV);
			Primitives.addVertex(vertexData, cx + xrad*s2, y2, cz + zrad*c2);
			Primitives.addNormal(normalData, nc, 0, -ns);
			Primitives.addColor(colorData, r, g, b, a);
			Primitives.addUV(uvData, maxU, maxV);
			Primitives.addVertex(vertexData, cx + xrad*s, y2, cz + zrad*c);
			Primitives.addNormal(normalData, nc, 0, -ns);
			Primitives.addColor(colorData, r, g, b, a);
			Primitives.addUV(uvData, minU, maxV);
		}
	}
	
	public static void addXWall(List<Float> vertexData, List<Float> colorData, List<Float> uvData, List<Float> normalData,
			float x1, float y1, float z1, float x2, float y2, float z2,
			float nx, float ny, float nz, float r, float g, float b, float a,
			float minU, float minV, float maxU, float maxV){
		Primitives.addVertex(vertexData,x1,y1,z1);
		Primitives.addNormal(normalData,nx,ny,nz);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,minU,minV);
		Primitives.addVertex(vertexData,x1,y1,z2);
		Primitives.addNormal(normalData,nx,ny,nz);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,maxU,minV);
		Primitives.addVertex(vertexData,x1,y2,z2);
		Primitives.addNormal(normalData,nx,ny,nz);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,maxU,maxV);
		Primitives.addVertex(vertexData,x1,y2,z1);
		Primitives.addNormal(normalData,nx,ny,nz);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,minU,maxV);
	}
	
	public static void addZWall(List<Float> vertexData, List<Float> colorData, List<Float> uvData, List<Float> normalData,
			float x1, float y1, float z1, float x2, float y2, float z2,
			float nx, float ny, float nz, float r, float g, float b, float a,
			float minU, float minV, float maxU, float maxV){
		Primitives.addVertex(vertexData,x1,y1,z1);
		Primitives.addNormal(normalData,nx,ny,nz);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,minU,minV);
		Primitives.addVertex(vertexData,x2,y1,z1);
		Primitives.addNormal(normalData,nx,ny,nz);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,maxU,minV);
		Primitives.addVertex(vertexData,x2,y2,z1);
		Primitives.addNormal(normalData,nx,ny,nz);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,maxU,maxV);
		Primitives.addVertex(vertexData,x1,y2,z1);
		Primitives.addNormal(normalData,nx,ny,nz);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,minU,maxV);
	}
	
	public static void addFloor(List<Float> vertexData, List<Float> colorData, List<Float> uvData, List<Float> normalData,
			float x1, float y1, float z1, float x2, float y2, float z2,
			float nx, float ny, float nz, float r, float g, float b, float a,
			float minU, float minV, float maxU, float maxV){
		Primitives.addVertex(vertexData,x1,y1,z1);
		Primitives.addNormal(normalData,nx,ny,nz);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,minU,minV);
		Primitives.addVertex(vertexData,x2,y1,z1);
		Primitives.addNormal(normalData,nx,ny,nz);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,maxU,minV);
		Primitives.addVertex(vertexData,x2,y1,z2);
		Primitives.addNormal(normalData,nx,ny,nz);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,maxU,maxV);
		Primitives.addVertex(vertexData,x1,y1,z2);
		Primitives.addNormal(normalData,nx,ny,nz);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,minU,maxV);
	}
	
	public static void addQuad(List<Float> vertexData, List<Float> colorData, List<Float> uvData, List<Float> normalData,
			float x1, float y1, float z1, 
			float x2, float y2, float z2, 
			float x3, float y3, float z3, 
			float x4, float y4, float z4, 
			float nx1, float ny1, float nz1,  
			float r, float g, float b, float a,
			float minU, float minV, float maxU, float maxV){
		Primitives.addVertex(vertexData,x1,y1,z1);
		Primitives.addNormal(normalData,nx1,ny1,nz1);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,minU,minV);
		Primitives.addVertex(vertexData,x2,y2,z2);
		Primitives.addNormal(normalData,nx1,ny1,nz1);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,maxU,minV);
		Primitives.addVertex(vertexData,x3,y3,z3);
		Primitives.addNormal(normalData,nx1,ny1,nz1);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,maxU,maxV);
		Primitives.addVertex(vertexData,x4,y4,z4);
		Primitives.addNormal(normalData,nx1,ny1,nz1);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,minU,maxV);
	}
	
	public static void addQuad(List<Float> vertexData, List<Float> colorData, List<Float> uvData, List<Float> normalData,
			float x1, float y1, float z1, 
			float x2, float y2, float z2, 
			float x3, float y3, float z3, 
			float x4, float y4, float z4, 
			float r, float g, float b, float a,
			float minU, float minV, float maxU, float maxV){
		Vec3f normal = MathUtil.getNormalFromQuad(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4).scale(-1.0f);
		Primitives.addVertex(vertexData,x1,y1,z1);
		Primitives.addNormal(normalData,normal.x,normal.y,normal.z);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,minU,minV);
		Primitives.addVertex(vertexData,x2,y2,z2);
		Primitives.addNormal(normalData,normal.x,normal.y,normal.z);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,maxU,minV);
		Primitives.addVertex(vertexData,x3,y3,z3);
		Primitives.addNormal(normalData,normal.x,normal.y,normal.z);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,maxU,maxV);
		Primitives.addVertex(vertexData,x4,y4,z4);
		Primitives.addNormal(normalData,normal.x,normal.y,normal.z);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,minU,maxV);
	}
	
	public static void addQuad(List<Float> vertexData, List<Float> colorData, List<Float> uvData, List<Float> normalData,
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
		Primitives.addVertex(vertexData,x1,y1,z1);
		Primitives.addNormal(normalData,nx1,ny1,nz1);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,minU,minV);
		Primitives.addVertex(vertexData,x2,y2,z2);
		Primitives.addNormal(normalData,nx2,ny2,nz2);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,maxU,minV);
		Primitives.addVertex(vertexData,x3,y3,z3);
		Primitives.addNormal(normalData,nx3,ny3,nz3);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,maxU,maxV);
		Primitives.addVertex(vertexData,x4,y4,z4);
		Primitives.addNormal(normalData,nx4,ny4,nz4);
		Primitives.addColor(colorData,r,g,b,a);
		Primitives.addUV(uvData,minU,maxV);
	}
	
	public static void addBox(List<Float> vertexData, List<Float> colorData, List<Float> uvData, List<Float> normalData, float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b, float a, boolean negX, boolean posX, boolean negY, boolean posY, boolean negZ, boolean posZ,
			float minU, float minV, float maxU, float maxV){
		int faceCount = (negZ ? 1 : 0)
				+ (posZ ? 1 : 0)
				+ (negY ? 1 : 0)
				+ (posY ? 1 : 0)
				+ (negX ? 1 : 0)
				+ (posX ? 1 : 0);
		for (int i = 0; i < 4*faceCount; i ++){
			Primitives.addColor(colorData,r,g,b,a);
		}
		for (int i = 0; i < faceCount; i ++){
			Primitives.addUV(uvData,minU,minV);
			Primitives.addUV(uvData,maxU,minV);
			Primitives.addUV(uvData,maxU,maxV);
			Primitives.addUV(uvData,minU,maxV);
		}
		
		if (negZ){
			Primitives.addNormal(normalData,0,0,-1);
			Primitives.addNormal(normalData,0,0,-1);
			Primitives.addNormal(normalData,0,0,-1);
			Primitives.addNormal(normalData,0,0,-1);
			Primitives.addVertex(vertexData,x1,y1,z1); 
			Primitives.addVertex(vertexData,x2,y1,z1);
			Primitives.addVertex(vertexData,x2,y2,z1);
			Primitives.addVertex(vertexData,x1,y2,z1);
		}
		
		if (negX){
			Primitives.addNormal(normalData,-1,0,0);
			Primitives.addNormal(normalData,-1,0,0);
			Primitives.addNormal(normalData,-1,0,0);
			Primitives.addNormal(normalData,-1,0,0);
			Primitives.addVertex(vertexData,x1,y1,z2);
			Primitives.addVertex(vertexData,x1,y1,z1);
			Primitives.addVertex(vertexData,x1,y2,z1);
			Primitives.addVertex(vertexData,x1,y2,z2);
		}
	
		if (negY){
			Primitives.addNormal(normalData,0,-1,0);
			Primitives.addNormal(normalData,0,-1,0);
			Primitives.addNormal(normalData,0,-1,0);
			Primitives.addNormal(normalData,0,-1,0);
			Primitives.addVertex(vertexData,x2,y1,z2); 
			Primitives.addVertex(vertexData,x1,y1,z2);
			Primitives.addVertex(vertexData,x1,y1,z1);
			Primitives.addVertex(vertexData,x2,y1,z1);
		}
		
		if (posZ){
			Primitives.addNormal(normalData,0,0,1);
			Primitives.addNormal(normalData,0,0,1);
			Primitives.addNormal(normalData,0,0,1);
			Primitives.addNormal(normalData,0,0,1);
			Primitives.addVertex(vertexData,x2,y1,z2); 
			Primitives.addVertex(vertexData,x1,y1,z2); 
			Primitives.addVertex(vertexData,x1,y2,z2); 
			Primitives.addVertex(vertexData,x2,y2,z2); 
		}
		
		if (posX){
			Primitives.addNormal(normalData,1,0,0);
			Primitives.addNormal(normalData,1,0,0);
			Primitives.addNormal(normalData,1,0,0);
			Primitives.addNormal(normalData,1,0,0);
			Primitives.addVertex(vertexData,x2,y1,z1); 
			Primitives.addVertex(vertexData,x2,y1,z2); 
			Primitives.addVertex(vertexData,x2,y2,z2); 
			Primitives.addVertex(vertexData,x2,y2,z1); 
		}
	
		if (posY){
			Primitives.addNormal(normalData,0,1,0);
			Primitives.addNormal(normalData,0,1,0);
			Primitives.addNormal(normalData,0,1,0);
			Primitives.addNormal(normalData,0,1,0);
			Primitives.addVertex(vertexData,x2,y2,z2); 
			Primitives.addVertex(vertexData,x1,y2,z2); 
			Primitives.addVertex(vertexData,x1,y2,z1); 
			Primitives.addVertex(vertexData,x2,y2,z1); 
		}
	}
	
	public static void addBox(List<Float> vertexData, List<Float> colorData, List<Float> uvData, List<Float> normalData, float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b, float a, boolean negX, boolean posX, boolean negY, boolean posY, boolean negZ, boolean posZ,
			Vec4f[] textures){
		int faceCount = (negZ ? 1 : 0)
				+ (posZ ? 1 : 0)
				+ (negY ? 1 : 0)
				+ (posY ? 1 : 0)
				+ (negX ? 1 : 0)
				+ (posX ? 1 : 0);
		for (int i = 0; i < 4*faceCount; i ++){
			Primitives.addColor(colorData,r,g,b,a);
		}
		for (int i = 0; i < faceCount; i ++){
			Primitives.addUV(uvData,textures[i].x,textures[i].y);
			Primitives.addUV(uvData,textures[i].z,textures[i].y);
			Primitives.addUV(uvData,textures[i].z,textures[i].w);
			Primitives.addUV(uvData,textures[i].x,textures[i].w);
		}
	
		if (negY){
			Primitives.addNormal(normalData,0,-1,0);
			Primitives.addNormal(normalData,0,-1,0);
			Primitives.addNormal(normalData,0,-1,0);
			Primitives.addNormal(normalData,0,-1,0);
			Primitives.addVertex(vertexData,x2,y1,z2); 
			Primitives.addVertex(vertexData,x1,y1,z2);
			Primitives.addVertex(vertexData,x1,y1,z1);
			Primitives.addVertex(vertexData,x2,y1,z1);
		}
	
		if (posY){
			Primitives.addNormal(normalData,0,1,0);
			Primitives.addNormal(normalData,0,1,0);
			Primitives.addNormal(normalData,0,1,0);
			Primitives.addNormal(normalData,0,1,0);
			Primitives.addVertex(vertexData,x2,y2,z2); 
			Primitives.addVertex(vertexData,x1,y2,z2); 
			Primitives.addVertex(vertexData,x1,y2,z1); 
			Primitives.addVertex(vertexData,x2,y2,z1); 
		}
		
		if (negZ){
			Primitives.addNormal(normalData,0,0,-1);
			Primitives.addNormal(normalData,0,0,-1);
			Primitives.addNormal(normalData,0,0,-1);
			Primitives.addNormal(normalData,0,0,-1);
			Primitives.addVertex(vertexData,x1,y1,z1); 
			Primitives.addVertex(vertexData,x2,y1,z1);
			Primitives.addVertex(vertexData,x2,y2,z1);
			Primitives.addVertex(vertexData,x1,y2,z1);
		}
		
		if (posZ){
			Primitives.addNormal(normalData,0,0,1);
			Primitives.addNormal(normalData,0,0,1);
			Primitives.addNormal(normalData,0,0,1);
			Primitives.addNormal(normalData,0,0,1);
			Primitives.addVertex(vertexData,x2,y1,z2); 
			Primitives.addVertex(vertexData,x1,y1,z2); 
			Primitives.addVertex(vertexData,x1,y2,z2); 
			Primitives.addVertex(vertexData,x2,y2,z2); 
		}
		
		if (negX){
			Primitives.addNormal(normalData,-1,0,0);
			Primitives.addNormal(normalData,-1,0,0);
			Primitives.addNormal(normalData,-1,0,0);
			Primitives.addNormal(normalData,-1,0,0);
			Primitives.addVertex(vertexData,x1,y1,z2);
			Primitives.addVertex(vertexData,x1,y1,z1);
			Primitives.addVertex(vertexData,x1,y2,z1);
			Primitives.addVertex(vertexData,x1,y2,z2);
		}
		
		if (posX){
			Primitives.addNormal(normalData,1,0,0);
			Primitives.addNormal(normalData,1,0,0);
			Primitives.addNormal(normalData,1,0,0);
			Primitives.addNormal(normalData,1,0,0);
			Primitives.addVertex(vertexData,x2,y1,z1); 
			Primitives.addVertex(vertexData,x2,y1,z2); 
			Primitives.addVertex(vertexData,x2,y2,z2); 
			Primitives.addVertex(vertexData,x2,y2,z1); 
		}
	}
	
	public static void addSegment(List<Float> vertexData, List<Float> colorData, List<Float> uvData, List<Float> normalData,
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
			Primitives.addColor(colorData,r,g,b,a);
		}
		for (int i = 0; i < faceCount; i ++){
			Primitives.addUV(uvData,textures[i].x,textures[i].y);
			Primitives.addUV(uvData,textures[i].z,textures[i].y);
			Primitives.addUV(uvData,textures[i].z,textures[i].w);
			Primitives.addUV(uvData,textures[i].x,textures[i].w);
		}
		
		Vec3f down = Primitives.getNormal(x1,y1,z1,x2,y2,z2,x3,y3,z3).scale(-1f);
		Vec3f up = Primitives.getNormal(x5,y5,z5,x6,y6,z6,x7,y7,z7);
		Vec3f west = Primitives.getNormal(x1,y1,z1,x8,y8,z8,x5,y5,z5);
		Vec3f east = Primitives.getNormal(x3,y3,z3,x6,y6,z6,x7,y7,z7);
		Vec3f south = Primitives.getNormal(x4,y4,z4,x7,y7,z7,x8,y8,z8);
		Vec3f north = Primitives.getNormal(x1,y1,z1,x5,y5,z5,x6,y6,z6);
		
		Primitives.addNormal(normalData,up.x,up.y,up.z);
		Primitives.addVertex(vertexData,x1,y1,z1);
		Primitives.addNormal(normalData,up.x,up.y,up.z);
		Primitives.addVertex(vertexData,x2,y2,z2);
		Primitives.addNormal(normalData,up.x,up.y,up.z);
		Primitives.addVertex(vertexData,x3,y3,z3);
		Primitives.addNormal(normalData,up.x,up.y,up.z);
		Primitives.addVertex(vertexData,x4,y4,z4);
		
		Primitives.addNormal(normalData,down.x,down.y,down.z);
		Primitives.addVertex(vertexData,x5,y5,z5);
		Primitives.addNormal(normalData,down.x,down.y,down.z);
		Primitives.addVertex(vertexData,x6,y6,z6);
		Primitives.addNormal(normalData,down.x,down.y,down.z);
		Primitives.addVertex(vertexData,x7,y7,z7);
		Primitives.addNormal(normalData,down.x,down.y,down.z);
		Primitives.addVertex(vertexData,x8,y8,z8);
		
		Primitives.addNormal(normalData,west.x,west.y,west.z);
		Primitives.addVertex(vertexData,x1,y1,z1);
		Primitives.addNormal(normalData,west.x,west.y,west.z);
		Primitives.addVertex(vertexData,x4,y4,z4);
		Primitives.addNormal(normalData,west.x,west.y,west.z);
		Primitives.addVertex(vertexData,x8,y8,z8);
		Primitives.addNormal(normalData,west.x,west.y,west.z);
		Primitives.addVertex(vertexData,x5,y5,z5);
		
		Primitives.addNormal(normalData,east.x,east.y,east.z);
		Primitives.addVertex(vertexData,x2,y2,z2);
		Primitives.addNormal(normalData,east.x,east.y,east.z);
		Primitives.addVertex(vertexData,x3,y3,z3);
		Primitives.addNormal(normalData,east.x,east.y,east.z);
		Primitives.addVertex(vertexData,x7,y7,z7);
		Primitives.addNormal(normalData,east.x,east.y,east.z);
		Primitives.addVertex(vertexData,x6,y6,z6);
		
		Primitives.addNormal(normalData,north.x,north.y,north.z);
		Primitives.addVertex(vertexData,x3,y3,z3);
		Primitives.addNormal(normalData,north.x,north.y,north.z);
		Primitives.addVertex(vertexData,x4,y4,z4);
		Primitives.addNormal(normalData,north.x,north.y,north.z);
		Primitives.addVertex(vertexData,x8,y8,z8);
		Primitives.addNormal(normalData,north.x,north.y,north.z);
		Primitives.addVertex(vertexData,x7,y7,z7);
		
		Primitives.addNormal(normalData,south.x,south.y,south.z);
		Primitives.addVertex(vertexData,x1,y1,z1);
		Primitives.addNormal(normalData,south.x,south.y,south.z);
		Primitives.addVertex(vertexData,x2,y2,z2);
		Primitives.addNormal(normalData,south.x,south.y,south.z);
		Primitives.addVertex(vertexData,x6,y6,z6);
		Primitives.addNormal(normalData,south.x,south.y,south.z);
		Primitives.addVertex(vertexData,x5,y5,z5);
	}
}
