package graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import controlls.MovementComputation;
import gameObjects.Bullet;
import gameObjects.GameObject;
import gameObjects.Player;
import gameObjects.Tile;
import gameObjects.Wall;

import levelBuild.ComputeLevel;
import levelBuild.BuildLevel;

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
		int mazeSize = 4;
		float tileSize = .5f;
		for(int y=0; y<mazeSize; y++) {
			for(int x=0; x<mazeSize; x++) {
				Tile tile = new Tile(x*.5f, y*.5f, tileSize*.98f, tileSize*.98f);
				staticGameObjects.add(tile);
			}
		}
		
		String seed = BuildLevel.generateLevel(mazeSize);
		System.out.print(seed);
		
		//Generated walls
		ArrayList<GameObject> walls = ComputeLevel.drawWalls(seed, mazeSize, tileSize);
		
		//Malin Custom
		//ArrayList<GameObject> walls = ComputeLevel.drawWalls("12,12,4,6,10,3,2,2,8,1,3,2,13,1,1,3", mazeSize, tileSize);
		
		//Julian Custom
		//ArrayList<GameObject> walls = ComputeLevel.drawWalls("13,6,4,6,10,2,2,2,10,1,3,2,9,1,1,3", mazeSize, tileSize);
		
		//Outer walls only
		//ArrayList<GameObject> walls = ComputeLevel.drawWalls("12,4,4,6,8,0,0,2,8,0,0,2,9,1,1,3", mazeSize, tileSize);
		
		for (GameObject wall : walls) {
			staticGameObjects.add(wall);
		}
		
		//w‰nde
		/*//Auﬂenw‰nde
		Wall o = new Wall(0-tileSize/2, 0, true);
		Wall l = new Wall(0-tileSize/2, 0, false);
		Wall r = new Wall(0+tileSize/2, 0, true);
		Wall u = new Wall(0-tileSize/2, 0, false);
		staticGameObjects.add(o);
		staticGameObjects.add(l);
		staticGameObjects.add(r);
		staticGameObjects.add(u);
		
		//Innenw‰nde
		for(int y=0; y<mazeSize; y++) {
			for(int x=0; x<mazeSize; x++) {
				double random = Math.random();
				if(random < 0.1) {
					Wall wall = new Wall(x*.5f, (y+tileSize/2)*.5f, true);  //Oben
					staticGameObjects.add(wall);
				}
				else if(random < 0.2 && random >= 0.1) {
					Wall wall = new Wall((x+tileSize/2)*.5f, y*.5f, false); // rechts
					staticGameObjects.add(wall);
				}
				else if(random < 0.3 && random >=0.2) {
					Wall wall = new Wall((x-tileSize/2)*.5f, y*.5f, false); // links
					staticGameObjects.add(wall);
				}
				if(random < 0.4 && random >= 0.3) {
					Wall wall = new Wall(x*.5f, (y-tileSize/2)*.5f, true); // unten
					staticGameObjects.add(wall);
				}
			}
		}*/
		
		
		//power ups
		
		//dynamic objects

		//add local player
		Player player = new Player(0, 0, clientJavaSocket.getLocalPlayerNum(), true);
		dynamicGameObjects.add(player);

		//loop through num of connected players within network
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


				synchronized(dynamicGameObjects) {
					for (GameObject go : dynamicGameObjects) {
						go.drawGraphic();
					}
				}
				
				glfwSwapBuffers(window);
				frames++;
			}
		}
			
		glfwTerminate();
	}
	
	//doesn't work?
	public static void shutdown() {
		try {
			serverJavaSocket.serverSocket.close();
			System.out.println("Socket closed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(serverJavaSocket != null) sjsThread.interrupt();
		
		moveCompThread.interrupt();
	}
	
	//Utility methods
	public static double getTime() {
		return (double)System.nanoTime() / (double)1000000000L;	// number is 1 billion --> casts nano time to seconds
	}
	
	public static MovementComputation getMoveComp(){
		return moveComp;
	}
	
	public static void spawnBullet(float posX, float posY, int playerNumber, int viewDirection) {

		Bullet bullet = new Bullet(posX, posY, playerNumber, viewDirection);
		synchronized(dynamicGameObjects) {
			dynamicGameObjects.add(bullet);
		}
	}
}
