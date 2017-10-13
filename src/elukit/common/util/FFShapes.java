package elukit.common.util;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;

import elukit.common.struct.Vec3f;
import elukit.common.struct.Vec4f;

public class FFShapes {
	public static void renderBox(float x1, float y1, float z1, float x2, float y2, float z2){
		glBegin(GL_QUADS);
		renderBoxBatched(x1, y1, z1, x2, y2, z2);
		glEnd();
	}
	
	public static void renderBox(float x1, float y1, float z1, float x2, float y2, float z2, float minU, float minV, float maxU, float maxV){
		glBegin(GL_QUADS);
		renderBoxBatched(x1, y1, z1, x2, y2, z2, minU, minV, maxU, maxV);
		glEnd();
	}
	
	public static void renderBox(float x1, float y1, float z1, float x2, float y2, float z2, Vec4f[] textures){
		glBegin(GL_QUADS);
		renderBoxBatched(x1, y1, z1, x2, y2, z2, textures);
		glEnd();
	}
	
	public static void renderSegment(float x1, float y1, float z1,
			float x2, float y2, float z2,
			float x3, float y3, float z3,
			float x4, float y4, float z4,
			float x5, float y5, float z5,
			float x6, float y6, float z6,
			float x7, float y7, float z7,
			float x8, float y8, float z8, 
			Vec4f[] textures){
		glBegin(GL_QUADS);
		renderSegmentBatched(
				x1, y1, z1, 
				x2, y2, z2, 
				x3, y3, z3, 
				x4, y4, z4, 
				x5, y5, z5, 
				x6, y6, z6, 
				x7, y7, z7, 
				x8, y8, z8,
				textures);
		glEnd();
	}
	
	public static void renderSegmentBatched(float x1, float y1, float z1,
									float x2, float y2, float z2,
									float x3, float y3, float z3,
									float x4, float y4, float z4,
									float x5, float y5, float z5,
									float x6, float y6, float z6,
									float x7, float y7, float z7,
									float x8, float y8, float z8, 
									Vec4f[] textures){
		//1 -> 2 -> 3 -> 4
		//nxnz -> pxnz -> pxpz -> nxpz
		
		//texorder: up, down, north, south, east, west
		Vec3f down = Primitives.getNormal(x1,y1,z1,x2,y2,z2,x3,y3,z3).scale(-1f);
		Vec3f up = Primitives.getNormal(x5,y5,z5,x6,y6,z6,x7,y7,z7);
		Vec3f west = Primitives.getNormal(x1,y1,z1,x8,y8,z8,x5,y5,z5);
		Vec3f east = Primitives.getNormal(x3,y3,z3,x6,y6,z6,x7,y7,z7);
		Vec3f south = Primitives.getNormal(x4,y4,z4,x7,y7,z7,x8,y8,z8);
		Vec3f north = Primitives.getNormal(x1,y1,z1,x5,y5,z5,x6,y6,z6);
		
		glNormal3f(up.x,up.y,up.z);
		glTexCoord2f(textures[0].x,textures[0].y); glVertex3f(x1,y1,z1);
		glTexCoord2f(textures[0].z,textures[0].y); glVertex3f(x2,y2,z2);
		glTexCoord2f(textures[0].z,textures[0].w); glVertex3f(x3,y3,z3);
		glTexCoord2f(textures[0].x,textures[0].w); glVertex3f(x4,y4,z4);
		
		glNormal3f(down.x,down.y,down.z);
		glTexCoord2f(textures[1].x,textures[1].y); glVertex3f(x5,y5,z5);
		glTexCoord2f(textures[1].z,textures[1].y); glVertex3f(x6,y6,z6);
		glTexCoord2f(textures[1].z,textures[1].w); glVertex3f(x7,y7,z7);
		glTexCoord2f(textures[1].x,textures[1].w); glVertex3f(x8,y8,z8);
		
		glNormal3f(west.x,west.y,west.z);
		glTexCoord2f(textures[2].x,textures[2].y); glVertex3f(x1,y1,z1);
		glTexCoord2f(textures[2].z,textures[2].y); glVertex3f(x4,y4,z4);
		glTexCoord2f(textures[2].z,textures[2].w); glVertex3f(x8,y8,z8);
		glTexCoord2f(textures[2].x,textures[2].w); glVertex3f(x5,y5,z5);
		
		glNormal3f(east.x,east.y,east.z);
		glTexCoord2f(textures[3].x,textures[3].y); glVertex3f(x2,y2,z2);
		glTexCoord2f(textures[3].z,textures[3].y); glVertex3f(x3,y3,z3);
		glTexCoord2f(textures[3].z,textures[3].w); glVertex3f(x7,y7,z7);
		glTexCoord2f(textures[3].x,textures[3].w); glVertex3f(x6,y6,z6);
		
		glNormal3f(north.x,north.y,north.z);
		glTexCoord2f(textures[4].x,textures[4].y); glVertex3f(x3,y3,z3);
		glTexCoord2f(textures[4].z,textures[4].y); glVertex3f(x4,y4,z4);
		glTexCoord2f(textures[4].z,textures[4].w); glVertex3f(x8,y8,z8);
		glTexCoord2f(textures[4].x,textures[4].w); glVertex3f(x7,y7,z7);
		
		glNormal3f(south.x,south.y,south.z);
		glTexCoord2f(textures[5].x,textures[5].y); glVertex3f(x1,y1,z1);
		glTexCoord2f(textures[5].z,textures[5].y); glVertex3f(x2,y2,z2);
		glTexCoord2f(textures[5].z,textures[5].w); glVertex3f(x6,y6,z6);
		glTexCoord2f(textures[5].x,textures[5].w); glVertex3f(x5,y5,z5);
	}
	
	public static void renderBoxBatched(float x1, float y1, float z1, float x2, float y2, float z2, Vec4f[] textures){
		glNormal3f(0,0,-1);
		glTexCoord2f(textures[0].x, textures[0].y); glVertex3f(x1,y1,z1); 
		glTexCoord2f(textures[0].z, textures[0].y); glVertex3f(x2,y1,z1);
		glTexCoord2f(textures[0].z, textures[0].w); glVertex3f(x2,y2,z1);
		glTexCoord2f(textures[0].x, textures[0].w); glVertex3f(x1,y2,z1);
		
		glNormal3f(-1,0,0);
		glTexCoord2f(textures[1].x, textures[1].y); glVertex3f(x1,y1,z2);
		glTexCoord2f(textures[1].z, textures[1].y); glVertex3f(x1,y1,z1);
		glTexCoord2f(textures[1].z, textures[1].w); glVertex3f(x1,y2,z1);
		glTexCoord2f(textures[1].x, textures[1].w); glVertex3f(x1,y2,z2);
	
		glNormal3f(0,-1,0);
		glTexCoord2f(textures[2].x, textures[2].y); glVertex3f(x2,y1,z2); 
		glTexCoord2f(textures[2].z, textures[2].y); glVertex3f(x1,y1,z2);
		glTexCoord2f(textures[2].z, textures[2].w); glVertex3f(x1,y1,z1);
		glTexCoord2f(textures[2].x, textures[2].w); glVertex3f(x2,y1,z1);
		
		glNormal3f(0,0,1);
		glTexCoord2f(textures[3].x, textures[3].y); glVertex3f(x2,y1,z2); 
		glTexCoord2f(textures[3].z, textures[3].y); glVertex3f(x1,y1,z2); 
		glTexCoord2f(textures[3].z, textures[3].w); glVertex3f(x1,y2,z2); 
		glTexCoord2f(textures[3].x, textures[3].w); glVertex3f(x2,y2,z2); 
		
		glNormal3f(1,0,0);
		glTexCoord2f(textures[4].x, textures[4].y); glVertex3f(x2,y1,z1); 
		glTexCoord2f(textures[4].z, textures[4].y); glVertex3f(x2,y1,z2); 
		glTexCoord2f(textures[4].z, textures[4].w); glVertex3f(x2,y2,z2); 
		glTexCoord2f(textures[4].x, textures[4].w); glVertex3f(x2,y2,z1); 
	
		glNormal3f(0,1,0);
		glTexCoord2f(textures[5].x, textures[5].y); glVertex3f(x2,y2,z2); 
		glTexCoord2f(textures[5].z, textures[5].y); glVertex3f(x1,y2,z2); 
		glTexCoord2f(textures[5].z, textures[5].w); glVertex3f(x1,y2,z1); 
		glTexCoord2f(textures[5].x, textures[5].w); glVertex3f(x2,y2,z1); 
	}
	
	public static void renderBoxBatched(float x1, float y1, float z1, float x2, float y2, float z2, float minU, float minV, float maxU, float maxV){
		glNormal3f(0,0,-1);
		glTexCoord2f(minU, minV); glVertex3f(x1,y1,z1); 
		glTexCoord2f(maxU, minV); glVertex3f(x2,y1,z1);
		glTexCoord2f(maxU, maxV); glVertex3f(x2,y2,z1);
		glTexCoord2f(minU, maxV); glVertex3f(x1,y2,z1);
		
		glNormal3f(-1,0,0);
		glTexCoord2f(minU, minV); glVertex3f(x1,y1,z2);
		glTexCoord2f(maxU, minV); glVertex3f(x1,y1,z1);
		glTexCoord2f(maxU, maxV); glVertex3f(x1,y2,z1);
		glTexCoord2f(minU, maxV); glVertex3f(x1,y2,z2);
	
		glNormal3f(0,-1,0);
		glTexCoord2f(minU, minV); glVertex3f(x2,y1,z2); 
		glTexCoord2f(maxU, minV); glVertex3f(x1,y1,z2);
		glTexCoord2f(maxU, maxV); glVertex3f(x1,y1,z1);
		glTexCoord2f(minU, maxV); glVertex3f(x2,y1,z1);
		
		glNormal3f(0,0,1);
		glTexCoord2f(minU, minV); glVertex3f(x2,y1,z2); 
		glTexCoord2f(maxU, minV); glVertex3f(x1,y1,z2); 
		glTexCoord2f(maxU, maxV); glVertex3f(x1,y2,z2); 
		glTexCoord2f(minU, maxV); glVertex3f(x2,y2,z2); 
		
		glNormal3f(1,0,0);
		glTexCoord2f(minU, minV); glVertex3f(x2,y1,z1); 
		glTexCoord2f(maxU, minV); glVertex3f(x2,y1,z2); 
		glTexCoord2f(maxU, maxV); glVertex3f(x2,y2,z2); 
		glTexCoord2f(minU, maxV); glVertex3f(x2,y2,z1); 
	
		glNormal3f(0,1,0);
		glTexCoord2f(minU, minV); glVertex3f(x2,y2,z2); 
		glTexCoord2f(maxU, minV); glVertex3f(x1,y2,z2); 
		glTexCoord2f(maxU, maxV); glVertex3f(x1,y2,z1); 
		glTexCoord2f(minU, maxV); glVertex3f(x2,y2,z1); 
	}
	
	public static void renderBoxBatched(float x1, float y1, float z1, float x2, float y2, float z2){
		glNormal3f(0,0,-1);
		glVertex3f(x1,y1,z1); 
		glVertex3f(x2,y1,z1);
		glVertex3f(x2,y2,z1);
		glVertex3f(x1,y2,z1);
		
		glNormal3f(-1,0,0);
		glVertex3f(x1,y1,z2);
		glVertex3f(x1,y1,z1);
		glVertex3f(x1,y2,z1);
		glVertex3f(x1,y2,z2);
	
		glNormal3f(0,-1,0);
		glVertex3f(x2,y1,z2); 
		glVertex3f(x1,y1,z2);
		glVertex3f(x1,y1,z1);
		glVertex3f(x2,y1,z1);
		
		glNormal3f(0,0,1);
		glVertex3f(x2,y1,z2); 
		glVertex3f(x1,y1,z2); 
		glVertex3f(x1,y2,z2); 
		glVertex3f(x2,y2,z2); 
		
		glNormal3f(1,0,0);
		glVertex3f(x2,y1,z1); 
		glVertex3f(x2,y1,z2); 
		glVertex3f(x2,y2,z2); 
		glVertex3f(x2,y2,z1); 
	
		glNormal3f(0,1,0);
		glVertex3f(x2,y2,z2); 
		glVertex3f(x1,y2,z2); 
		glVertex3f(x1,y2,z1); 
		glVertex3f(x2,y2,z1); 
	}
}
