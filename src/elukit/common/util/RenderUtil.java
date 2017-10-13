package elukit.common.util;

import static org.lwjgl.opengl.GL11.*;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import elukit.client.StateManager;
import elukit.client.texture.TextureManager;

public class RenderUtil {
	public static void draw2DQuad(float x, float y, float z, float w, float h, float minU, float minV, float maxU, float maxV, float texW, float texH){
		glTexCoord2f(minU/texW,minV/texH); glVertex3f(x,y,z);
		glTexCoord2f(maxU/texW,minV/texH); glVertex3f(x+w,y,z);
		glTexCoord2f(maxU/texW,maxV/texH); glVertex3f(x+w,y+h,z);
		glTexCoord2f(minU/texW,maxV/texH); glVertex3f(x,y+h,z);
	}
	
	static Set<Integer> lowered = new HashSet<>();
	static {
		lowered.add("g".getBytes(StandardCharsets.UTF_8)[0]-1);
		lowered.add("j".getBytes(StandardCharsets.UTF_8)[0]-1);
		lowered.add("p".getBytes(StandardCharsets.UTF_8)[0]-1);
		lowered.add("q".getBytes(StandardCharsets.UTF_8)[0]-1);
	}
	
	public static void drawString(float x, float y, String text){
		StateManager.bindTexture(TextureManager.texFont);
		glDepthMask(false);
		glBegin(GL_QUADS);
		byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
		for (int i = 0; i < text.length(); i ++){
			float down = 0;
			int asciiCode = bytes[i]-1;
			if (lowered.contains(asciiCode)){
				down = 2f;
			}
			int minU = (asciiCode%16)*8;
			int minV = (asciiCode/16)*8;
			draw2DQuad(x+i*7f,y+down,0,8f,8f,minU,minV,minU+8f,minV+8f,128f,128f);
		}
		glEnd();
		glDepthMask(true);
	}
	
	public static void drawStringScaled(float x, float y, float scale, String text){
		StateManager.bindTexture(TextureManager.texFont);
		glDepthMask(false);
		glBegin(GL_QUADS);
		byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
		for (int i = 0; i < text.length(); i ++){
			float down = 0;
			int asciiCode = bytes[i]-1;
			if (lowered.contains(asciiCode)){
				down = 2f;
			}
			int minU = (asciiCode%16)*8;
			int minV = (asciiCode/16)*8;
			draw2DQuad(x+i*7f*scale,y+down*scale,0,8f*scale,8f*scale,minU,minV,minU+8f,minV+8f,128f,128f);
		}
		glEnd();
		glDepthMask(true);
	}
	
	public static void drawString3D(float x, float y, float z, String text){
		StateManager.bindTexture(TextureManager.texFont);
		glDepthMask(false);
		glBegin(GL_QUADS);
		byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
		for (int i = 0; i < text.length(); i ++){
			int asciiCode = bytes[i]-1;
			int minU = (asciiCode%16)*8;
			int minV = (asciiCode/16)*8;
			draw2DQuad(x+i*0.4375f,y+0.5f,z,0.5f,0.5f,minU,minV,minU+8f,minV+8f,128f,128f);
		}
		glEnd();
		glDepthMask(true);
	}
}

