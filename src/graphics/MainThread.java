package graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import gameObjects.GameObject;
import gameObjects.Tile;

public class MainThread {
	
	//Networking params
	static Object TOKEN;
	
	//Graphics params
	static int screenWidth = 800; //640
	static int screenHeight = 800; //480
	
	static long window;
	
	//Object movement computation
	static MovementComputation moveComp;
	static Thread moveCompThread;
	
	
	//Object collections
	static ArrayList<GameObject> movingGameObjects;	//players and Zombies
	static ArrayList<GameObject> staticGameObjects;	//level
	
	public static void main(String[] args) {
		
		initGraphics();
		
		initMovementComputation();
		
		gameloop();
		
	}
	
	static void initNetworking() {
		
	}

	static void initGraphics() {
		movingGameObjects = new ArrayList<GameObject>();
		staticGameObjects = new ArrayList<GameObject>();
		
		//static objects
		//tiles
		//wände
		//power ups
		for(int y=0; y<8; y++) {
			for(int x=0; x<8; x++) {
				Tile tile = new Tile(x*.51f, y*.51f, .5f, .5f);
				staticGameObjects.add(tile);
			}
		}
		
		//lwjgl screen setup
		if(!glfwInit()) throw new IllegalStateException("failed to initialize glfw");
		
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		window = glfwCreateWindow(screenWidth, screenHeight, "My LWJGL Test", 0, 0);	//if 3rd variable == glfwGetPrimaryMonitor() then the window is fullscreen
		if(window == 0) throw new IllegalStateException("failed to create window");
		
		GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (videoMode.width() - screenWidth) / 2, (videoMode.height() - screenHeight) / 2);
		
		glfwShowWindow(window);
		
		glfwMakeContextCurrent(window);
		
		GL.createCapabilities();
	}
	
	static void initMovementComputation(){
		//Initialize Thread for Input Control and Object moving
		TOKEN = new Object();
		moveComp = new MovementComputation(movingGameObjects, staticGameObjects, window, TOKEN);
		moveCompThread = new Thread(moveComp);
		moveCompThread.start();
		
	}
	
	static void gameloop() {
		//fps variables
		double frameCap = 1/60d;	//1 frame per 60 seconds

		double time = getTime();
		double unprocessed = 0;		//time where game hasn't been processed yet
		
		//for debugging only
		double frameTime = 0;
		int frames = 0;
		
		
		//while loop drawing the scene
		while(!glfwWindowShouldClose(window)) {
			boolean screenUpdated = false;
			
			double time_2 = getTime();
			double passed = time_2 - time;
			unprocessed += passed;
			frameTime += passed;
			
			time = time_2;
			
			while(unprocessed >= frameCap) {	//put everything in here that isn't rendering
				unprocessed -= frameCap;
				screenUpdated = true;
				
				glfwPollEvents();
				if(frameTime >= 1) {
					frameTime = 0;
					System.out.println("FPS: " + frames);
					frames = 0;
				}
			}
			
			if(screenUpdated) {
				synchronized(TOKEN) {
					TOKEN.notify();	//syncs up position change with frame rate
					}
					
					glClear(GL_COLOR_BUFFER_BIT);
					
					for (GameObject go : staticGameObjects) {
						go.drawGraphic();
					}

					for (GameObject go : movingGameObjects) {
						go.drawGraphic();
					}
					
					glfwSwapBuffers(window);
					frames++;
				}
			}
			
			glfwTerminate();
	}
	
	//Utility methods
	static double getTime() {
		return (double)System.nanoTime() / (double)1000000000L;	// number is 1 billion --> casts nano time to seconds
	}
}
