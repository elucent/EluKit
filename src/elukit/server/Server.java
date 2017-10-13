package elukit.server;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FOG;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFrustum;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glViewport;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import elukit.client.Console;
import elukit.client.lighting.Light;
import elukit.client.shader.ShaderManager;
import elukit.client.texture.TextureManager;
import elukit.common.element.ElementRegistry;
import elukit.common.level.World;
import elukit.common.network.NetworkHelper;
import elukit.common.network.Side;
import elukit.common.network.packet.Packet;
import elukit.common.network.packet.SPacketClientConnect;
import elukit.common.network.packet.SPacketClientDisconnect;
import elukit.common.struct.Vec3f;
import sun.misc.Queue;

public class Server {
	public static Set<InetSocketAddress> connections = new HashSet<>();
	
	public static long window;
	
	public static int WIDTH = 960;
	public static int HEIGHT = 640;
	public static Console console = new Console(40);
	
	public static DatagramSocket socket = null;
	public static DatagramSocket outboundSocket = null;
	public static boolean open = true;
	public static ServerPacketHandler packetHandler;
	public static double time = 0;
	public static double deltaTime = 0;
	public static double lastTime = 0;
	public static double lastFrameTime = 0;
	
	public static Vec3f globalSpawn = new Vec3f(8f,8f,8f);
	
	public static World world = new World();
    
    public static Queue<Packet> packetQueue = new Queue<>();
    public static Queue<DatagramPacket> rawQueue = new Queue<>();
	
	public static void main(String[] args){
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");
		
		System.out.println("Running LWJGL version "+Version.getVersion());
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR,2);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR,1);
		
		window = glfwCreateWindow(WIDTH, HEIGHT, "JAVAGAME", MemoryUtil.NULL, MemoryUtil.NULL);
		
		if (window == MemoryUtil.NULL){
			throw new RuntimeException("Failed to create GLFW window!");
		}
		
		glfwMakeContextCurrent(window);

		glfwShowWindow(window);
		
		GL.createCapabilities();
		
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		glLineWidth(3);

		TextureManager.init();
		ElementRegistry.init();
		
        glfwSwapInterval(0);
        glDisable(GL_CULL_FACE);
        glDisable(GL_LIGHTING);
        glEnable(GL_DEPTH_TEST);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		socket = NetworkHelper.getSocket(1337);
		outboundSocket = NetworkHelper.getSocket();
		
		packetHandler = new ServerPacketHandler();
		packetHandler.start();
		console.addText("Hello world!");
		int i = 0;
		while (!glfwWindowShouldClose(window)){
			time = glfwGetTime();
			if (time-lastTime >= 1.0f/1000.0f){
				deltaTime = (float)(time-lastTime);
				lastTime = time;
				if (deltaTime < 0.5f){
					glfwPollEvents();
					int[] w = new int[]{WIDTH};
					int[] h = new int[]{HEIGHT};
					glfwGetWindowSize(window,w,h);
					WIDTH = w[0];
					HEIGHT = h[0];
					mainLoop();
				}
			}
			render(); 
			lastFrameTime = time;
		}
		for (InetSocketAddress addr : connections){
			NetworkHelper.sendPacket(socket, addr.getAddress(), addr.getPort(), new SPacketClientDisconnect());
		}
		System.exit(0);
	}
	
	public static void mainLoop(){
		while (!packetQueue.isEmpty()){
			try {
				packetQueue.dequeue().onReceive(Side.SERVER, rawQueue.dequeue());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (InetSocketAddress addr : connections){
			
		}
		world.tick();
	}
	
	public static void render(){
		glClearColor(0,0,0,1);
		glClear(GL_COLOR_BUFFER_BIT);
		glClear(GL_DEPTH_BUFFER_BIT);
		
		glViewport(0,0,WIDTH,HEIGHT);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0,WIDTH,HEIGHT,0,-1,1);

        glDisable(GL_FOG);
		glDisable(GL_DEPTH_TEST);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		console.render();
		glEnable(GL_DEPTH_TEST);
		
		glfwSwapBuffers(window);
	}
}
