package elukit.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import elukit.client.lighting.Light;
import elukit.client.shader.ShaderManager;
import elukit.client.texture.TextureManager;
import elukit.common.element.ElementRegistry;
import elukit.common.entity.EntityMovingCube;
import elukit.common.entity.EntityPlayer;
import elukit.common.level.Chunk;
import elukit.common.level.World;
import elukit.common.network.NetworkHelper;
import elukit.common.network.Side;
import elukit.common.network.packet.CPacketConnectRequest;
import elukit.common.network.packet.CPacketDisconnectRequest;
import elukit.common.network.packet.Packet;
import elukit.common.struct.Vec2i;
import elukit.common.struct.Vec3i;
import elukit.common.util.DataHelper;
import elukit.common.util.NoiseGenUtil;
import elukit.common.util.Primitives;
import sun.misc.Queue;

public class Main {
	public static long window;
	
	public static int WIDTH = 960;
	public static int HEIGHT = 640;
	
	public static float fov = 120.0f;
	
	public static float deltaTime = 0;
	public static double time = 0;
	public static double lastTime = 0;
	public static double lastFrameTime = 0;
	public static float partialTicks = 0;
	public static int ticks = 0;

	public static Random random = new Random();
	public static int seed = (int)(random.nextLong());
	
	public static EntityPlayer player = new EntityPlayer(true);
	public static Console console = new Console(10);
	
	public static World world = null;
	public static Set<Integer> tappedKeys = new HashSet<Integer>();
	public static Set<Integer> heldKeys = new HashSet<Integer>();
	
	public static boolean connectedMP = false;
    public static DatagramSocket socket = null;
    public static InetSocketAddress address = null;
    public static ClientPacketHandler packetHandler = new ClientPacketHandler();
    
    public static GameRenderer gameRenderer = new GameRenderer();
    
    public static Queue<Packet> packetQueue = new Queue<>();
    public static Queue<DatagramPacket> rawQueue = new Queue<>();
    
    public static GLCapabilities capabilities = null;
    
    public static float drawDist = 64f;
    
    public static float tickTime = 1f/100f;
	
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
		
		Main.window = glfwCreateWindow(Main.WIDTH, Main.HEIGHT, "JAVAGAME", MemoryUtil.NULL, MemoryUtil.NULL);
		
		if (Main.window == MemoryUtil.NULL){
			throw new RuntimeException("Failed to create GLFW window!");
		}
		
		glfwMakeContextCurrent(Main.window);
		glfwShowWindow(Main.window);
		capabilities = GL.createCapabilities();
		
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		glLineWidth(3);

		TextureManager.init();
		ShaderManager.init();
		ElementRegistry.init();
		
        glfwSwapInterval(0);
        glDisable(GL_CULL_FACE);
        glDisable(GL_LIGHTING);
        glEnable(GL_DEPTH_TEST);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		world = new World();
		for (int i = -2; i < 3; i ++){
			for (int j = -2; j < 3; j ++){
				world.genChunk(i, j);
				
				world.addEntity(new EntityMovingCube(
						new Vec3i(NoiseGenUtil.random.nextInt(16)-8,NoiseGenUtil.random.nextInt(8),NoiseGenUtil.random.nextInt(16)-8).add(i*Chunk.DIM, 6, j*Chunk.DIM), 
						new Vec3i(NoiseGenUtil.random.nextInt(16)-8,NoiseGenUtil.random.nextInt(8),NoiseGenUtil.random.nextInt(16)-8).add(i*Chunk.DIM, 6, j*Chunk.DIM), 
						4f));
			}
		}
		world.addEntity(player);
		//currentLevel.load();
		player.x = 0f;
		player.y = 9f;
		
		GLFWKeyCallback keyCallback;
		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback(){
		    @Override
		    public void invoke (long window, int key, int scancode, int action, int mods) {
		    	if (action == GLFW_PRESS){
		    		if (!tappedKeys.contains(key)){
		    			tappedKeys.add(key);
		    		}
		    		if (!heldKeys.contains(key)){
		    			heldKeys.add(key);
		    		}
		    	}
		    	if (action == GLFW_RELEASE){
		    		if (heldKeys.contains(key)){
		    			heldKeys.remove(key);
		    		}
		    	}
		    }
		});

		GLFWScrollCallback scrollCallback;
		glfwSetScrollCallback(window, scrollCallback = new GLFWScrollCallback(){
			@Override
			public void invoke(long window, double x, double y) {
				//
			}
		});
		
		glfwMakeContextCurrent(0);
		
		while (!glfwWindowShouldClose(window)){
			gameRenderer.start();
			time = glfwGetTime();
			partialTicks = (float)(time-lastTime)/tickTime;
			if (time-lastTime >= tickTime){
				int[] w = new int[]{WIDTH};
				int[] h = new int[]{HEIGHT};
				glfwGetWindowSize(window,w,h);
				WIDTH = w[0];
				HEIGHT = h[0];
				deltaTime = (float)(time-lastTime);
				lastTime = time;
				if (deltaTime < 0.5f){
					tappedKeys.clear();
					glfwPollEvents();
					mainLoop();
				}
				ticks ++;
			}
			lastFrameTime = time;
		}
		
		keyCallback.free();
		scrollCallback.free();
		System.exit(0);
	}
	
	public static void mainLoop(){
		float time = 0f;
		while (!packetQueue.isEmpty() && time < 0.0025f){
			long l = System.nanoTime();
			try {
				packetQueue.dequeue().onReceive(Side.CLIENT, rawQueue.dequeue());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			time += (System.nanoTime()-l)/1000000000f;
		}
		/*if (heldKeys.contains(GLFW_KEY_LEFT_CONTROL) && tappedKeys.contains(GLFW_KEY_M)){
			if (!connectedMP){
				if (socket == null){
					socket = NetworkHelper.getSocket();
				}
				packetHandler.start();
		        InetAddress receiverAddress = NetworkHelper.localhost();
		        console.addText("Attempting connection...");
		        NetworkHelper.sendPacket(socket, receiverAddress, 1337, new CPacketConnectRequest());
			}
			else {
				console.addText("Disconnecting...");
		        InetAddress receiverAddress = NetworkHelper.localhost();
		        NetworkHelper.sendPacket(socket, receiverAddress, 1337, new CPacketDisconnectRequest());
			}
		}
		if (tappedKeys.contains(GLFW_KEY_Q)){
			byte[] data = "TEST".getBytes();
			byte[] ser = DataHelper.serialize(data);
			console.addText(new String((byte[])DataHelper.deserialize(ser).get(0)));
		}*/
		if (!connectedMP){
			world.tick();
		}
		else {
			player.update();
		}
	}
	
	public static void setWorld(World l){
		world.removeEntity(player);
		world.tickDeletions();
		l.addEntity(player);
		world = l;
	}
	
	public static float getAspect(){
		return (float)WIDTH/(float)HEIGHT;
	}
	
	public static void render(){
		StateManager.useShader(ShaderManager.defaultShader);
		glClearColor(0.75f, 0.75f, 0.75f,1);
		glClear(GL_COLOR_BUFFER_BIT);
		glClear(GL_DEPTH_BUFFER_BIT);
		
		glViewport(0,0,WIDTH,HEIGHT);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		//glOrtho(-10*getAspect(),10*getAspect(),-10,10,-10,10);
		float height = (float)Math.tan(Math.toRadians(fov))/20.0f;
		float width = height*getAspect();
		glFrustum(width, -width, height, -height, 0.1f, 1000.0f);
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		StateManager.getShader().uniform3f("playerPos", player.getIX(), player.getIY(), player.getIZ());
		
		StateManager.getShader().uniform1f("fogDist", drawDist);
		StateManager.getShader().uniform4f("fogColor", 0.75f, 0.75f, 0.75f, 1.0f);
		
		List<Light> lights = new ArrayList<Light>();
		
		world.uploadLights(lights);
		
		lights.sort(Light.distComparator);
		
		int lcount = 0;
		for (int i = 0; i < lights.size(); i++){
			if (i < lights.size()){
				Light l = lights.get(i);
				float coeff = 1f-((float)Math.pow(l.x-player.x,2f)+(float)Math.pow(l.y-player.y,2f)+(float)Math.pow(l.z-player.z,2f))/(drawDist*drawDist);
				if (coeff > 0){
					StateManager.getShader().uniform3f("lights["+lcount+"].position", l.x, l.y, l.z);
					StateManager.getShader().uniform4f("lights["+lcount+"].color", l.r, l.g, l.b, l.a*coeff);
					StateManager.getShader().uniform1f("lights["+lcount+"].radius", l.rad*coeff);
					lcount ++;
				}
			}
		}
		StateManager.getShader().uniform1i("lightCount", lcount);
		
		float zBack = 0f;
		glTranslatef(0,0,-zBack);
		glRotatef(player.cameraPitch,1,0,0);
		glRotatef(player.cameraYaw,0,1,0);
		glTranslatef(-player.getIX(),-(player.getIY()+player.eyeHeight),-player.getIZ());

		world.renderLevel();
		/*for (Vec2i k : levels.keySet()){
			Chunk l = levels.get(k);
			float translateX = k.x*Chunk.minW;
			float translateY = k.y*Chunk.minH;
			glPushMatrix();
			glTranslatef(translateX,translateY,0);
			GL20.glUniform3f(translation, translateX, translateY, 0);
			l.renderLevel();
			glPopMatrix();
		}*/
		world.renderEntities();
		
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
