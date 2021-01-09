package controlls;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;

import java.util.ArrayList;

import ai.AIBehaviour;
import collision.CollisionDetection;
import collision.QuadTree;

import gameObjects.Bullet;
import gameObjects.GameObject;
import gameObjects.Player;
import gameObjects.Tile;
import gameObjects.Zombie;
import graphics.MainThread;

public class MovementComputation implements Runnable{
	ArrayList<GameObject> dynamicGameObjects;
	ArrayList<GameObject> staticGameObjects;
	
	Player player;
	float offsetX, offsetY;
	
	long window;
	
	int playerInputCode = 0;	//in byte: bit 1: right, bit 2: left, bit 3: up, bit 4: down, bit 5 sprint, bit 6 shoot
	int[] playerInputCodes = new int[6];
	
	Object TOKEN;
	
	//shot cooldown
	double shotCooldown = .5f;
	double timeStampLastShot;
	
	//collision
	QuadTree quadTree;
	
	public MovementComputation(ArrayList<GameObject> dynamicGameObjects, ArrayList<GameObject> staticGameObjects, long window, Object TOKEN) {
		this.dynamicGameObjects = dynamicGameObjects;
		this.staticGameObjects = staticGameObjects;
		this.window = window;
		this.TOKEN = TOKEN;
		
		player = (Player) dynamicGameObjects.get(0);
		
		quadTree = MainThread.getQuadTree();
	}
	
	@Override
	public void run() {
		timeStampLastShot = MainThread.getTime() + shotCooldown;
		
		while(!glfwWindowShouldClose(window)) {
			manageInputs();	//for player
			
			moveObjects();
			
			
			try {
				synchronized(TOKEN) {
					TOKEN.wait();	//syncs up position change with frame rate
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Movement Comp Thread finished");
	}
	
	void manageInputs() {
		playerInputCode = 0;
		
		//if(glfwGetMouseButton(window, 0) == GL_TRUE)	//0 = left click, 1 = right click, 2 = middle mouse button
			//System.out.println("click");
		
		float x = 0, y = 0;
		float moveSpeed = player.getMovementSpeed();

		//horizontal movement
		if(glfwGetKey(window, GLFW_KEY_D) == GL_TRUE) {	//right
			canceledMovement:{
				
				if(!player.getDead()) {	//dead players don't have a collider
					ArrayList<Tile> tiles = quadTree.getCollidingTiles(player, moveSpeed, 0);
					for (GameObject tile : tiles) {
						//go through all walls of the tile
						for (GameObject wall : ((Tile)tile).getWalls()) {
							//if the player collides with a wall after applying movement
							if(CollisionDetection.scanForCollision(player, wall, moveSpeed, 0)) {
								break canceledMovement;
							}
						}
					}
				}
				
				x+=moveSpeed;
				playerInputCode += 1;	//first bit
			}
		}
		else if(glfwGetKey(window, GLFW_KEY_A) == GL_TRUE) { //left
			canceledMovement:{

				if(!player.getDead()) {	//dead players don't have a collider
					ArrayList<Tile> tiles = quadTree.getCollidingTiles(player, -moveSpeed, 0);
					for (GameObject tile : tiles) {
						//go through all walls of the tile
						for (GameObject wall : ((Tile)tile).getWalls()) {
							//if the player collides with a wall after applying movement
							if(CollisionDetection.scanForCollision(player, wall, -moveSpeed, 0)) {
								break canceledMovement;
							}
						}
					}
				}
				
				x-=moveSpeed;
				playerInputCode += 2;	//second bit
			}
		}
		//vertical movement
		if(glfwGetKey(window, GLFW_KEY_W) == GL_TRUE) { //up
			canceledMovement:{
			

				if(!player.getDead()) {	//dead players don't have a collider
					ArrayList<Tile> tiles = quadTree.getCollidingTiles(player, 0, moveSpeed);
					for (GameObject tile : tiles) {
						//go through all walls of the tile
						for (GameObject wall : ((Tile)tile).getWalls()) {
							//if the player collides with a wall after applying movement
							if(CollisionDetection.scanForCollision(player, wall, 0, moveSpeed)) {
								break canceledMovement;
							}
						}
					}
				}
				
				y+=moveSpeed;
				playerInputCode += 4;	//third bit
			}
		}
		else if(glfwGetKey(window, GLFW_KEY_S) == GL_TRUE) { //down
			canceledMovement:{
				

				if(!player.getDead()) {	//dead players don't have a collider
					ArrayList<Tile> tiles = quadTree.getCollidingTiles(player, 0, -moveSpeed);
					for (GameObject tile : tiles) {
						//go through all walls of the tile
						for (GameObject wall : ((Tile)tile).getWalls()) {
							//if the player collides with a wall after applying movement
							if(CollisionDetection.scanForCollision(player, wall, 0, -moveSpeed)) {
								break canceledMovement;
							}
						}
					}
				}
				
				y-=moveSpeed;
				playerInputCode += 8;	//fourth bit
			}
		}
		
		//adjust diagonal movement, so that diagonal movement isn't speed up
		if(x!=0 && y!=0) {
			float magnitude = (float)Math.sqrt((float)Math.pow(x, 2) + (float)Math.pow(y, 2));
			
			float factor = moveSpeed / magnitude;
			
			x *= factor;
			y *= factor;
		}

		//sprint button pressed
		if(glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GL_TRUE) {
			x *= 2;
			y *= 2;

			playerInputCode += 16;	//fifth bit
		}
		
		if(glfwGetKey(window, GLFW_KEY_SPACE) == GL_TRUE) {

			if(!player.getDead()) {	//Player can only shoot if he or she isn't dead
				//shoot
				if(MainThread.getTime() > timeStampLastShot + shotCooldown) {	//shoot if shooting cd has passed
					//System.out.println("pew");
					//MainThread.spawnBullet(player.getXPosition(), player.getYPosition(), player.getPlayerNum(), x, y);
					
					Bullet bullet = new Bullet(player.getXPosition(), player.getYPosition(), player.getPlayerNum(), player.getViewDirection());
					synchronized(dynamicGameObjects) {
						dynamicGameObjects.add(bullet);
					}
					
					playerInputCode += 32;	//sixth bit
					
					timeStampLastShot = MainThread.getTime();
				}
			}
		}
		
		player.move(x, y);
		setObjectAngle(player, x, y);

		if(glfwGetKey(window, GLFW_KEY_ESCAPE) == GL_TRUE) {
			//moveCompThread.stop();
			MainThread.shutdown();
			glfwSetWindowShouldClose(window, true);
		}
	}
	
	public void applyInputCode(Player player, int inputCode) {
		float x = 0, y = 0;
		float moveSpeed = player.getMovementSpeed();
		boolean sprint = false, spawnBullet = false;
		
		if(inputCode / 32 == 1)	{	//6th bit == 1 --> shoot
			//System.out.println("pew");
			spawnBullet = true;
			inputCode -= 32;
		}
		if(inputCode / 16 == 1)	{	//5th bit == 1 --> sprint
			sprint = true;
			inputCode -= 16;
		}
		if(inputCode / 8 == 1)	{	//4th bit == 1 --> down
			y-=moveSpeed;
			inputCode -= 8;
		}
		if(inputCode / 4 == 1)	{	//3th bit == 1 --> up
			y+=moveSpeed;
			inputCode -= 4;
		}
		if(inputCode / 2 == 1)	{	//2th bit == 1 --> left
			x-=moveSpeed;
			inputCode -= 2;
		}
		if(inputCode / 1 == 1)	{	//1th bit == 1 --> right
			x+=moveSpeed;
			inputCode -= 1;
		}
		
		if(x!=0 && y!=0) {
			float magnitude = (float)Math.sqrt((float)Math.pow(x, 2) + (float)Math.pow(y, 2));
			
			float factor = moveSpeed / magnitude;
			
			x *= factor;
			y *= factor;
		}

		//sprint button pressed
		if(sprint) {
			x *= 2;
			y *= 2;
		}
		
		if(spawnBullet) {
			//MainThread.spawnBullet(player.getXPosition(), player.getYPosition(), player.getPlayerNum(), player.getViewDirection());

			Bullet bullet = new Bullet(player.getXPosition(), player.getYPosition(), player.getPlayerNum(), player.getViewDirection());
			synchronized(dynamicGameObjects) {
				dynamicGameObjects.add(bullet);
			}
		}
		
		player.move(x, y);
		setObjectAngle(player, x, y);
	}
	
	public void applyInputCode(Zombie zombie) {
		float x = 0, y = 0;
		float moveSpeed = zombie.getMovementSpeed();
		int inputCode = zombie.getInputCode();
		
		if(inputCode / 8 == 1)	{	//4th bit == 1 --> down
			y-=moveSpeed;
			inputCode -= 8;
		}
		if(inputCode / 4 == 1)	{	//3th bit == 1 --> up
			y+=moveSpeed;
			inputCode -= 4;
		}
		if(inputCode / 2 == 1)	{	//2th bit == 1 --> left
			x-=moveSpeed;
			inputCode -= 2;
		}
		if(inputCode / 1 == 1)	{	//1th bit == 1 --> right
			x+=moveSpeed;
			inputCode -= 1;
		}
		
		if(x!=0 && y!=0) {
			float magnitude = (float)Math.sqrt((float)Math.pow(x, 2) + (float)Math.pow(y, 2));
			
			float factor = moveSpeed / magnitude;
			
			x *= factor;
			y *= factor;
		}
		
		zombie.move(x, y);
		setObjectAngle(zombie, x, y);
	}
	
	//adjusts Input Code to consider collision
	int adjustInputCode(GameObject go, int inputCode) {
		float moveSpeed = .5f;
		if(go.getClass() == Zombie.class) moveSpeed = ((Zombie)go).getMovementSpeed();
		else if(go.getClass() == Player.class) moveSpeed = ((Player)go).getMovementSpeed();
			
		int correctedInputCode = 0;
		
		if(inputCode / 8 == 1)	{	//4th bit == 1 --> down
			canceledMovement:{
				ArrayList<Tile> tiles = quadTree.getCollidingTiles(go, 0, -moveSpeed);
				for (GameObject tile : tiles) {
					//go through all walls of the tile
					for (GameObject wall : ((Tile)tile).getWalls()) {
						//if the player collides with a wall after applying movement
						if(CollisionDetection.scanForCollision(go, wall, 0, -moveSpeed)) {
							break canceledMovement;
						}
					}
				}

				correctedInputCode += 8;
				inputCode -= 8;
			}
		}
		if(inputCode / 4 == 1)	{	//3th bit == 1 --> up
			canceledMovement:{
			ArrayList<Tile> tiles = quadTree.getCollidingTiles(go, 0, moveSpeed);
			for (GameObject tile : tiles) {
				//go through all walls of the tile
				for (GameObject wall : ((Tile)tile).getWalls()) {
					//if the player collides with a wall after applying movement
					if(CollisionDetection.scanForCollision(go, wall, 0, moveSpeed)) {
						break canceledMovement;
					}
				}
			}

			correctedInputCode += 4;
			inputCode -= 4;
			}
		}
		if(inputCode / 2 == 1)	{	//2th bit == 1 --> left
			canceledMovement:{
			ArrayList<Tile> tiles = quadTree.getCollidingTiles(go, -moveSpeed, 0);
			for (GameObject tile : tiles) {
				//go through all walls of the tile
				for (GameObject wall : ((Tile)tile).getWalls()) {
					//if the player collides with a wall after applying movement
					if(CollisionDetection.scanForCollision(go, wall, -moveSpeed, 0)) {
						break canceledMovement;
					}
				}
			}

			correctedInputCode += 2;
			inputCode -= 2;
			}
		}
		if(inputCode / 1 == 1)	{	//1th bit == 1 --> right
			canceledMovement:{
			ArrayList<Tile> tiles = quadTree.getCollidingTiles(go, moveSpeed, 0);
			for (GameObject tile : tiles) {
				//go through all walls of the tile
				for (GameObject wall : ((Tile)tile).getWalls()) {
					//if the player collides with a wall after applying movement
					if(CollisionDetection.scanForCollision(go, wall, moveSpeed, 0)) {
						break canceledMovement;
					}
				}
			}
			
			correctedInputCode += 1;
			inputCode -= 1;
			}
		}
		
		return correctedInputCode;
	}
	
	void moveObjects() {
		//cloning dynamic objects
		/*
		ArrayList<GameObject> copyOfDGO = new ArrayList<GameObject>();
		synchronized (dynamicGameObjects){
			for (GameObject go : dynamicGameObjects) copyOfDGO.add(go);
		}
		*/
		ArrayList<GameObject> copyOfDGO = MainThread.getCopyOfDynamicObjects();
		
		for(int i=0; i<copyOfDGO.size(); i++){
			GameObject go = copyOfDGO.get(i);
			
			
			if(go==player) {	//local player
				offsetX = player.getXPosition();
				offsetY = player.getYPosition();
			}
			else if(go.getClass() == Player.class) {	//networked players
				go.setOffset(offsetX, offsetY);
				applyInputCode((Player)go, playerInputCodes[((Player)go).getPlayerNum()]);
			}
			else if(go.getClass() == Bullet.class) {	//Bullets
				go.setOffset(offsetX, offsetY);
				((Bullet)go).move();
				
				///Collision\\\
				//bullet collision is only against players and walls
				//Server checks for Collision for all Clients and sends the solution over to the over clients over the network
				if(MainThread.isLocalClientServer()) {
					for (GameObject player : copyOfDGO) {
						//if all players have been checked then stop this loop
						if(player.getClass() != Player.class) break;	
						//don't collide with player that shot the bullet
						else if(((Player)player).getCollider() != null & ((Player)player).getPlayerNum() != ((Bullet)go).getBulletPlayerNum()){	//Collider can be null once player died
							if(CollisionDetection.scanForCollision(go, player)) {
								MainThread.broadcastKillPlayer(((Player)player).getPlayerNum());
								
								System.out.println(go + " killed: " + player);
							}
						}
					}

					//Collision against Zombies
					for (GameObject zombie : copyOfDGO) {
						if(zombie.getClass() == Zombie.class) {
							if(CollisionDetection.scanForCollision(go, zombie)) {
								MainThread.sendKillZombie(((Zombie)zombie).getID());
								
								System.out.println(go + " killed: " + zombie);
							}
						}
					}
				}
				
				//Collision against Tiles
				bulletDestroyed:{
					ArrayList<Tile> tiles = quadTree.getCollidingTiles(go);
					if(tiles.size() == 0) {	//if bullet is out of bounds
						MainThread.destroyBullet(go);

						System.out.println("MovementComp: " + go + " out of bounds");
						break bulletDestroyed;
					}
					for (GameObject tile : tiles) {
						//go through all walls of the tile
						for (GameObject wall : ((Tile)tile).getWalls()) {
							//if the player collides with a wall after applying movement
							/*
							if(CollisionDetection.scanForCollision(go, wall)) {
								MainThread.destroyBullet(go);

								//System.out.println(go + " destroyed");
								break bulletDestroyed;
							}
							*/
							if(CollisionDetection.scanForDynamicCollision(go, wall, ((Bullet)go).getMovementVectorX(), ((Bullet)go).getMovementVectorY())) {
								MainThread.destroyBullet(go);

								//System.out.println(go + " destroyed");
								break bulletDestroyed;
							}
						}
					}
				}
			}
			else{	//zombies
				go.setOffset(offsetX, offsetY);
				
				if(MainThread.isLocalClientServer()) {
					for (GameObject player : copyOfDGO) {
						//if all players have been checked then stop this loop
						if(player.getClass() != Player.class) break;	
						//don't collide with dead players
						else if(((Player)player).getCollider() != null){	//Collider can be null once player died
							if(CollisionDetection.scanForCollision(go, player)) {
								MainThread.broadcastKillPlayer(((Player)player).getPlayerNum());
								
								System.out.println(go + " killed: " + player);
							}
						}
					}

					String zombieInputCode = "";
					for (GameObject zombie : copyOfDGO) {
						//if all players have been checked then stop this loop
						if(zombie.getClass() != Zombie.class) {
							
						}	
						//don't collide with dead players
						else {
							//Generate Input Code for Zombie
							//((Zombie)zombie).setInputCode(AIBehaviour.generateInputCode((Zombie)zombie));
							
							//zombieInputCode += ((Zombie)zombie).getID() + "-" + ((Zombie)zombie).getInputCode();
							zombieInputCode += ((Zombie)zombie).getID() + "-" + adjustInputCode(zombie, AIBehaviour.generateInputCode((Zombie)zombie));
							
							//if zombie isn't last child of dynamic objects
							if(zombie != copyOfDGO.get(copyOfDGO.size()-1)) {
								zombieInputCode += ",";
							}
						}
					}
					
					MainThread.sendZombieInputCode(zombieInputCode);
				}
				
				applyInputCode((Zombie)go);
				
				
				//float x = 0, y = 0;
				
				//random Object Jitter
				//ToDo: add actual movement logic for stuff
				//y = .005f;
				
				//go.move(x, y);
				//setObjectAngle(go, x, y);
			}
		}
		
		for(GameObject go : staticGameObjects) {
			go.setOffset(offsetX, offsetY);
		}
	}
	
	void setObjectAngle(GameObject go, float x, float y) {
		if(x==0 && y>0) {	//top
			go.setAngle(0);
			
			if(go.getClass() == Player.class) ((Player)go).setViewDirection(0);
		}
		if(x<0 && y>0) {	//top left
			go.setAngle(45);
			
			if(go.getClass() == Player.class) ((Player)go).setViewDirection(7);
		}
		if(x<0 && y==0) {	//left
			go.setAngle(90);
			
			if(go.getClass() == Player.class) ((Player)go).setViewDirection(6);
		}
		if(x<0 && y<0) {	//bottom left
			go.setAngle(135);
			
			if(go.getClass() == Player.class) ((Player)go).setViewDirection(5);
		}
		if(x==0 && y<0) {	//bottom
			go.setAngle(180);
			
			if(go.getClass() == Player.class) ((Player)go).setViewDirection(4);
		}
		if(x>0 && y<0) {	//bottom right
			go.setAngle(-135);
			
			if(go.getClass() == Player.class) ((Player)go).setViewDirection(3);
		}
		if(x>0 && y==0) {	//right
			go.setAngle(-90);
			
			if(go.getClass() == Player.class) ((Player)go).setViewDirection(2);
		}
		if(x>0 && y>0) {	//top right
			go.setAngle(-45);
			
			if(go.getClass() == Player.class) ((Player)go).setViewDirection(1);
		}
	}
	
	public int getPlayerInputCode() {
		return playerInputCode;
	}
	
	public void setPlayerInput(int playerNum, int inputCode) {
		//System.out.println("Setting player " + playerNum + " input to: " + inputCode);
		playerInputCodes[playerNum] = inputCode;
	}
}
