package graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import ai.LevelMapper;
import collision.QuadTree;

import controlls.MovementComputation;
import events.EventThread;
import events.ZombieSpawner;
import gameObjects.Bullet;
import gameObjects.GameObject;
import gameObjects.Player;
import gameObjects.Tile;
import gameObjects.Zombie;
import levelBuild.ComputeLevel;

import networking.ClientJavaSocket;
import networking.ServerInputHandler;
import networking.ServerJavaSocket;

public class MainThread {
	
	//networking params
	static boolean server;	//is user server or client?
	static int port;
	static String userName;
	static ServerJavaSocket serverJavaSocket;
	static Thread sjsThread;
	static ClientJavaSocket clientJavaSocket;
	static ServerInputHandler serverInputHandler;
	
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
	
	//Level Generation variables
	static String levelSeed = "";
	static int levelSize = 8;	//8 is regular size
	static float tileSize = .5f;
	
	//game controlling variables
	public static boolean startGame = false;
	
	//Colliders
	static QuadTree quadTree;
	
	//player Spawning
	static int[] playerNum; 
	static int[] playerTile;
	
	//Events
	static EventThread eventThread;
	static Thread eThread;
	
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
						//startGame = true;
						synchronized(START_GAME_TOKEN) {START_GAME_TOKEN.notify();}
						
						//ToDo: 
						//Send start game over server to clients together with how many players are in the game --> Handled by UserThread in networking
					}
					
					//ToDo: Find a way to display players text here
					
					//font.drawString(100, 50, "Players connected: ", Color.white);
					//font.drawString(100, 100, "Press SPACE to start game..", Color.gray);
				}
				//else {
				//font.drawString(100, 50, "Waiting for Host to start game... ", Color.white);
				if(clientJavaSocket.getTotalPlayerNum() != 0) {	//value has been initialized, meaning that game has started
					startGame = true;
				}
				//}
				
				glfwSwapBuffers(window);
			}
		}
	}
	
	static void initObjects() {
		////static objects\\\\

		ArrayList<Tile> tiles = new ArrayList<Tile>();
		
		///tiles\\\
		for(int y=0; y<levelSize; y++) {
			for(int x=0; x<levelSize; x++) {
				Tile tile = new Tile(x*.5f, y*.5f, tileSize*.98f, tileSize*.98f);
				tiles.add(tile);
			}
		}
		
		staticGameObjects.addAll(tiles);
		
		//init QuadTree
		quadTree = new QuadTree(tiles, levelSize, tileSize, 0, levelSize*.5f - .5f, 0, levelSize*.5f - .5f);
		
		//Generated walls
		ArrayList<GameObject> walls = ComputeLevel.drawWalls(levelSeed, levelSize, tileSize, staticGameObjects);
		
		//Malin Custom
		//ArrayList<GameObject> walls = ComputeLevel.drawWalls("12,12,4,6,10,3,2,2,8,1,3,2,13,1,1,3", 4, tileSize);
		
		//Julian Custom
		//ArrayList<GameObject> walls = ComputeLevel.drawWalls("13,6,4,6,10,2,2,2,10,1,3,2,9,1,1,3", 4, tileSize);
		
		//Outer walls only
		//ArrayList<GameObject> walls = ComputeLevel.drawWalls("12,4,4,6,8,0,0,2,8,0,0,2,9,1,1,3", 4, tileSize);
		
		for (GameObject wall : walls) {
			staticGameObjects.add(wall);
		}
		
		////dynamic objects\\\\

		///players\\\
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
		
		
		//generate Position for every player
		if(server) {
			int[] randPlayerTiles = new int[clientJavaSocket.getTotalPlayerNum()];
			int[] playerNums = new int[clientJavaSocket.getTotalPlayerNum()];
			
			for(int i=0; i<clientJavaSocket.getTotalPlayerNum(); i++) {
				Random rand = new Random();
				int randomNum = rand.nextInt(levelSize*levelSize);
				
				int j= i-1;
				
				//if there's enough tiles for all players
				if(clientJavaSocket.getTotalPlayerNum() > levelSize*levelSize) {
					//go through all already generated positions and check if this tile has already been applied to a player
					while (j>=0) {
						if(randPlayerTiles[j] == randomNum) {
							randomNum = rand.nextInt(levelSize*levelSize);
							j = i-1;
						}
					}
				}
				
				randPlayerTiles[i] = randomNum;
				playerNums[i] = i;
			}
			
			sendPlayerPositions(randPlayerTiles);
			//storePlayerPositions(playerNums, randPlayerTiles);
			//applyPlayerPositions();
		}
		
		
		///zombies\\
		if(server) {
			//Graph representation of level
			LevelMapper.mapLevel(tiles, levelSize, levelSeed);
			
			eventThread = new EventThread();
			
			//Have three different Zombie spawners at the same time
			//ZombieSpawner zombieSpawn = new ZombieSpawner(levelSize, tileSize);
			ZombieSpawner zombieSpawn = new ZombieSpawner(levelSize, tileSize, 0);
			eventThread.add(zombieSpawn);
			
			//zombieSpawn = new ZombieSpawner(levelSize, tileSize);
			zombieSpawn = new ZombieSpawner(levelSize, tileSize, 750);
			eventThread.add(zombieSpawn);
			
			//zombieSpawn = new ZombieSpawner(levelSize, tileSize);
			zombieSpawn = new ZombieSpawner(levelSize, tileSize, 1500);
			eventThread.add(zombieSpawn);
			
			eThread = new Thread(eventThread);
			eThread.start();
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
		//double frameTime = 0;
		//int frames = 0;
		
		
		//while loop drawing the scene
		while(!glfwWindowShouldClose(window)) {
			boolean screenUpdated = false;
			
			double time_2 = getTime();
			double passed = time_2 - time;
			unprocessed += passed;
			//frameTime += passed;
			
			time = time_2;
			
			while(unprocessed >= frameCap) {	//put everything in here that isn't rendering
				unprocessed -= frameCap;
				screenUpdated = true;
				
				glfwPollEvents();
				//if(frameTime >= 1) {
				//	frameTime = 0;
					//System.out.println("FPS: " + frames);
				//	frames = 0;
				//}
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
				//frames++;
			}
		}
			
		glfwTerminate();
	}
	
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
		eThread.interrupt();
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
	
	public static void destroyBullet(GameObject bullet) {
		synchronized(dynamicGameObjects) {
			dynamicGameObjects.remove(bullet);
		}
	}
	
	public static void setLevelSeed(String sendLevelSeed){
		levelSeed = sendLevelSeed;
	}
	
	public static int getLevelSize() {
		return levelSize;
	}
	
	public static boolean isLocalClientServer() {
		return server;
	}
	
	public static ArrayList<GameObject> getCopyOfDynamicObjects(){
		ArrayList<GameObject> clone = new ArrayList<GameObject>();
		for (GameObject go : dynamicGameObjects) {
			clone.add(go);
		}
		return clone;
	}
	
	
	//collision
	public static QuadTree getQuadTree() {
		return quadTree;
	}
	
	
	//player and zombie killing
	public static void setServerInputHandler(ServerInputHandler SIH) {
		serverInputHandler = SIH;
	}
	
	public static void broadcastKillPlayer(int playerNum) {
		serverInputHandler.broadcastInput("2:" + playerNum, null);
	}
	
	public static void killPlayer(int playerNum) {
		for(GameObject go : dynamicGameObjects) {
			if(go.getClass() != Player.class) break;
			
			if(((Player)go).getPlayerNum() == playerNum) {
				((Player)go).setDead(true);
				
				break;
			}
		}
	}
	
	//player Spawning
	public static void sendPlayerPositions(int[] playerTile) {
		String playerPosString = "";
		
		for (int i=0; i<playerTile.length; i++) {
			playerPosString = playerPosString + i + "-" + playerTile[i];
			if(i != playerTile.length-1) playerPosString = playerPosString + ",";
		}
		
		serverInputHandler.broadcastInput("1:" + playerPosString, null);
	}
	
	public static void storePlayerPositions(int[] playerNumbers, int[] playerTiles) {
		//Stores player Positions, because clients don't have their players initialized when the other player positions arrive from the server
		
		playerNum = playerNumbers;
		playerTile = playerTiles;
		
		applyPlayerPositions();
	}
	
	public static void applyPlayerPositions() {
		for (int i=0; i<playerNum.length; i++) {
			Player player = (Player)dynamicGameObjects.get(i);
			
			//cause the first player in the list is always the local player
			float posX = (playerTile[player.getPlayerNum()]%levelSize)*tileSize;	//-.5f is tileSize
			float posY = playerTile[player.getPlayerNum()]/levelSize*tileSize;
			
			player.setPosition(posX, posY);
		}
	}
	
	//Zombie spawning
	public static void sendZombieSpawn(int id, int spawnTile) {
		String zombieSpawnString = id + "," + spawnTile;
		
		serverInputHandler.broadcastInput("3:" + zombieSpawnString, null);
	}
	
	public static void spawnZombie(int id, int spawnTile) {
		float posX = (spawnTile%levelSize)*tileSize;	//-.5f is tileSize
		float posY = (spawnTile/levelSize)*tileSize;
		
		Zombie zombie = new Zombie(id, posX, posY);
		
		System.out.println("Spawning Zombie " + id + " at pos: " + posX + "|" + posY);
		
		synchronized(dynamicGameObjects) {
			dynamicGameObjects.add(zombie);
		}
	}
	
	public static void sendKillZombie(int id) {
		String zombieKillString = String.valueOf(id);

		serverInputHandler.broadcastInput("5:" + zombieKillString, null);
	}
	
	public static void killZombie(int id) {
		synchronized(dynamicGameObjects) {
			Zombie zombie = null; 
			for(GameObject go : dynamicGameObjects) {
				if(go.getClass() == Zombie.class) {
					if(((Zombie)go).getID() == id) {
						zombie = (Zombie)go;
						break;
					}
				}
			}
			dynamicGameObjects.remove(zombie);
		}
	}
	
	public static void sendZombieInputCode(String inputCode) {
		serverInputHandler.broadcastInput("4:" + inputCode, null);
	}
	
	public static void setZombieInputCode(ArrayList<Integer> IDs, int[] inputCodes) {
		
		for(int i=0; i<dynamicGameObjects.size(); i++) {
			if(dynamicGameObjects.get(i).getClass() == Zombie.class) {
				Zombie zombie = (Zombie)dynamicGameObjects.get(i); 
				
				int zombieId = zombie.getID();
				int inputId = IDs.indexOf(zombieId);
				
				if(inputId != -1) zombie.setInputCode(inputCodes[inputId]);
			}
		}
	}
}
