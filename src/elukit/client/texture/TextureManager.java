package elukit.client.texture;

import static org.lwjgl.opengl.GL11.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import elukit.client.shader.ShaderManager;

public class TextureManager {	
	public static Map<String, Texture> textures = new HashMap<String, Texture>();
	
	public static Texture texTile, texFont, texPlayer, texParticle, texEntity;
	
	public static Texture currentTexture = null;
	
	public static void init(){
		textures.put("tile", texTile = loadTexture("assets/textures/tile.png"));
		textures.put("font", texFont = loadTexture("assets/textures/font.png"));
		textures.put("player", texPlayer = loadTexture("assets/textures/player.png"));
		textures.put("particle", texParticle = loadTexture("assets/textures/particle.png"));
		textures.put("entity", texEntity = loadTexture("assets/textures/entity.png"));
	}
	
	public static Texture loadTexture(String path){
		ByteBuffer buf = null;
		int tWidth = 0;
		int tHeight = 0;
		 
		try {
			InputStream in = new FileInputStream(path);
		    PNGDecoder decoder = new PNGDecoder(in);
		     
		    tWidth = decoder.getWidth();
		    tHeight = decoder.getHeight();
		     
		     
		    buf = ByteBuffer.allocateDirect(
		    		4 * decoder.getWidth() * decoder.getHeight());
		    		decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
		    		buf.flip();
		     
		    in.close();
		} catch (IOException e) {
		    e.printStackTrace();
		    System.exit(-1);
		}
		
		int textureId = glGenTextures();
		glBindTexture(GL_TEXTURE_2D,textureId);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL_TRUE);
		glTexParameteri (GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 10);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, tWidth, tHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
		glBindTexture(GL_TEXTURE_2D,0);
		
		return new Texture(tWidth,tHeight,textureId);
	}
	
	public static int getCurrentWidth(){
		return currentTexture == null ? 0 : currentTexture.width;
	}
	
	public static int getCurrentHeight(){
		return currentTexture == null ? 0 : currentTexture.height;
	}
	
	public static Texture get(String string){
		return textures.get(string);
	}
}
