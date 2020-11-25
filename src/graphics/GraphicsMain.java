package graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public class GraphicsMain {
	static int screenWidth = 640;
	static int screenHeight = 480;
	
	static float x;
	
	static MovementComputation moveComp;
	static Thread moveCompThread;
	
	static ArrayList<GameObject> movingGameObjects;	//players and Zombies
	static ArrayList<GameObject> staticGameObjects;	//level
	
	public static void main(String[] args) {
		movingGameObjects = new ArrayList<GameObject>();
		staticGameObjects = new ArrayList<GameObject>();
		
		
		//lwjgl screen setup
		if(!glfwInit()) throw new IllegalStateException("failed to initialize glfw");
		
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		long window = glfwCreateWindow(screenWidth, screenHeight, "My LWJGL Test", 0, 0);	//if 3rd variable == glfwGetPrimaryMonitor() then the window is fullscreen
		if(window == 0) throw new IllegalStateException("failed to create window");
		
		GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (videoMode.width() - screenWidth) / 2, (videoMode.height() - screenHeight) / 2);
		
		glfwShowWindow(window);
		
		glfwMakeContextCurrent(window);
		
		GL.createCapabilities();
		
		
		//Initialize Thread for Input Control and Object moving
		Object TOKEN = new Object();
		moveComp = new MovementComputation(movingGameObjects, staticGameObjects, window, TOKEN);
		moveCompThread = new Thread(moveComp);
		moveCompThread.start();
		
		
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
					TOKEN.notify();	//syncs up position change with frame rated
				}
				
				glClear(GL_COLOR_BUFFER_BIT);
				
				for (GameObject go : movingGameObjects) {
					go.drawGraphic();
				}
				
				for (GameObject go : staticGameObjects) {
					go.drawGraphic();
				}
				
				glfwSwapBuffers(window);
				frames++;
			}
		}
		
		glfwTerminate();
	}
	
	static double getTime() {
		return (double)System.nanoTime() / (double)1000000000L;	// number is 1 billion --> casts nano time to seconds
	}
}
