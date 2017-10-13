package elukit.client;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;

import org.lwjgl.Version;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLineWidth;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import elukit.client.shader.ShaderManager;
import elukit.client.texture.TextureManager;
import elukit.common.element.ElementRegistry;

public class GameRenderer implements Runnable {
	Thread t = null;
	
	public Thread getThread(){
		return t;
	}
	
	boolean paused = false;
	public static float fps = 0;
	
	public void pause(){
		paused = true;
	}
	
	public void resume(){
		paused = false;
	}

	@Override
	public void run() {
		while (true){
			glfwMakeContextCurrent(Main.window);
			GL.setCapabilities(Main.capabilities);
			if (!paused){
				float frameStart = (float)glfwGetTime();
				Main.render();
				float frameEnd = (float)glfwGetTime();
				fps = fps*0.5f + 0.5f/(frameEnd-frameStart);
			}
		}
	}
	
	public void start(){
		if (t == null){
			t = new Thread(this, "gameRenderer");
			t.start();
		}
	}

}
