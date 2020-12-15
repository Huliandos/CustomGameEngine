package graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import controlls.MovementComputation;

import gameObjects.GameObject;
import gameObjects.Player;
import gameObjects.Tile;

import networking.ClientJavaSocket;
import networking.ServerJavaSocket;

public class MainThread {
	
	//networking params
	static boolean server;	//is user server or client?
	static int port;
	static String userName;
	static ServerJavaSocket serverJavaSocket;
	static Thread sjsThread;
	static ClientJavaSocket clientJavaSocket;
	
	//Multi threading params
	static Object FPS_SYNC_TOKEN;
	static Object START_GAME_TOKEN;
	
	//Graphics params
	static int screenWidth = 800; //640
	static int screenHeight = 800; //480
	
	static long window;
	
	//Object movement computation
	static MovementComputation moveComp;
	static Thread moveCompThread;
	
	//Object collections
	static ArrayList<GameObject> dynamicGameObjects;	//players and Zombies
	static ArrayList<GameObject> staticGameObjects;	//level
	
	//display text
	
	//game controlling variables
	public static boolean startGame = false;
	
	
	public static void main(String[] args) {	//args: bool for server, port, userName
		
		//reading args, or setting default values if not enough args were provided
		if (args.length < 1) {
	        server = false;
	        port = 4444;
	        userName = "_default";
        } else if (args.length < 2) {
        	if(args[0] == "true")	server = true;
			else server = false;
        	
	        port = 4444;
	        userName = "_default";
        }
        else if (args.length < 3) {
        	if(args[0] == "true")	server = true;
			else server = false;
        	
	        port = Integer.parseInt(args[1]);
	        
	        userName = "_default";
        }
		else {
			if(args[0].contains("true")) server = true;
			else server = false;
        	
	        port = Integer.parseInt(args[1]);
	        
	        userName = args[2];
        }
		FPS_SYNC_TOKEN = new Object();
		START_GAME_TOKEN = new Object();
		
		initGraphics();
		
		initNetworking();

		preGameloop();
		
		initObjects();
		
		initMovementComputation();
		
		gameloop();
	}

	static void initGraphics() {
		dynamicGameObjects = new ArrayList<GameObject>();
		staticGameObjects = new ArrayList<GameObject>();
		
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
	
	static void initNetworking() {
		if (server) {
			System.out.println("Server client");
			serverJavaSocket = new ServerJavaSocket(port, userName, window, START_GAME_TOKEN);
			
			sjsThread = new Thread(serverJavaSocket);
			sjsThread.start();
			
			clientJavaSocket = new ClientJavaSocket(port, userName, window, FPS_SYNC_TOKEN);
		}
		else {
			clientJavaSocket = new ClientJavaSocket(port, userName, window, FPS_SYNC_TOKEN);
		}
	}
	
	static void preGameloop() {
		
		double frameCap = 1/60d;	//1 frame per 60 seconds

		double time = getTime();
		double unprocessed = 0;		//time where game hasn't been processed yet

		//while loop drawing the scene
		while(!startGame) {
			boolean screenUpdated = false;
			
			double time_2 = getTime();
			double passed = time_2 - time;
			unprocessed += passed;
			
			time = time_2;
			
			while(unprocessed >= frameCap) {	//put everything in here that isn't rendering
				unprocessed -= frameCap;
				screenUpdated = true;
				
				glfwPollEvents();
			}
			
			if(screenUpdated) {
				glClear(GL_COLOR_BUFFER_BIT);
				glLoadIdentity();
				
				glClear(GL_COLOR_BUFFER_BIT);
				
				if (server) {	//if player is host
					if(glfwGetKey(window, GLFW_KEY_SPACE) == GL_TRUE) {
						startGame = true;
						synchronized(START_GAME_TOKEN) {START_GAME_TOKEN.notify();}
						
						//ToDo: 
						//Send start game over server to clients together with how many players are in the game --> Handled by UserThread in networking
					}
					
					//ToDo: Find a way to display players text here
					
					//font.drawString(100, 50, "Players connected: ", Color.white);
					//font.drawString(100, 100, "Press SPACE to start game..", Color.gray);
				}
				else {
					//font.drawString(100, 50, "Waiting for Host to start game... ", Color.white);
					if(clientJavaSocket.getTotalPlayerNum() != 0) {	//value has been initialized, meaning that game has started
						startGame = true;
					}
				}
				
				glfwSwapBuffers(window);
			}
		}
	}
	
	static void initObjects() {
		//static objects
		//tiles
		//w�nde
		//power ups
		for(int y=0; y<8; y++) {
			for(int x=0; x<8; x++) {
				Tile tile = new Tile(x*.51f, y*.51f, .5f, .5f);
				staticGameObjects.add(tile);
			}
		}
		
		//dynamic objects
		//loop through num of connected players within network

		//add local player
		Player player = new Player(0, 0, clientJavaSocket.getLocalPlayerNum(), true);
		dynamicGameObjects.add(player);

		for (int i=0; i<clientJavaSocket.getTotalPlayerNum(); i++) {
			if(i != clientJavaSocket.getLocalPlayerNum()) {
				player = new Player(0, 0, i, false);
				dynamicGameObjects.add(player);
			}
		}
	}
	
	static void initMovementComputation(){
		//Initialize Thread for Input Control and Object moving
		moveComp = new MovementComputation(dynamicGameObjects, staticGameObjects, window, FPS_SYNC_TOKEN);
		
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
					//System.out.println("FPS: " + frames);
					frames = 0;
				}
			}
			
			if(screenUpdated) {
				synchronized(FPS_SYNC_TOKEN) {
					FPS_SYNC_TOKEN.notify();	//syncs up position change with frame rate
				}
					
				glClear(GL_COLOR_BUFFER_BIT);
				glLoadIdentity();
				
				for (GameObject go : staticGameObjects) {
					go.drawGraphic();
				}

				for (GameObject go : dynamicGameObjects) {
					go.drawGraphic();
				}
				
				glfwSwapBuffers(window);
				frames++;
			}
		}
			
		glfwTerminate();
	}
	
	//doesn't work?
	static void shutdown() {
		if(serverJavaSocket != null) sjsThread.interrupt();
		
		moveCompThread.interrupt();
	}
	
	//Utility methods
	static double getTime() {
		return (double)System.nanoTime() / (double)1000000000L;	// number is 1 billion --> casts nano time to seconds
	}
	
	public static MovementComputation getMoveComp(){
		return moveComp;
	}
}
