package ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import collision.CollisionDetection;
import gameObjects.GameObject;
import gameObjects.Player;
import gameObjects.Wall;
import gameObjects.Tile;
import gameObjects.Zombie;

public class AIBehaviour {
	
	Zombie zombie;
	float zombieRadius;
	
	ArrayList<Tile> wayToFollow = new ArrayList<Tile>();
	
	public AIBehaviour(Zombie zombie){
		this.zombie = zombie;
		zombieRadius = zombie.getZombieSize()/2;
	}
	
	public int generateInputCode(ArrayList<Player> players, ArrayList<Tile> zombiePosTiles) {
		float shortestDistanceToPlayer = 999999;
		Player closestPlayer = null;
		
		//check line of sight towards each player
		for(Player player : players) {
			if(isInLineOfSight(zombie, player, zombiePosTiles.get(0), null)){
				//reset way to follow
				
				//1System.out.println("Caught line of sight of player");
				if(wayToFollow == null || !wayToFollow.isEmpty()) wayToFollow = new ArrayList<Tile>();
				
				float distanceToPlayer = distance(zombie, player);
				if(shortestDistanceToPlayer > distanceToPlayer) {
					closestPlayer = player;
					shortestDistanceToPlayer = distanceToPlayer;
				}
			}
		}
		
		//line of sight with player has been established
		if(closestPlayer != null) {
			return getInputCode(closestPlayer, zombiePosTiles.get(0));
		}
		
		
		//if path to next player has been computed then follow it
		//remove the first tile to follow of the zombie is already standing on it
		//if(wayToFollow != null && wayToFollow.size() > 0 && CollisionDetection.scanForCollision(zombie, wayToFollow.get(0))) {	//old way
		if(wayToFollow != null && wayToFollow.size() > 0 && CollisionDetection.scanForCollision(zombie, wayToFollow.get(0).getXPosition(), wayToFollow.get(0).getYPosition())) {
			wayToFollow.remove(0);
		}
		
		if(wayToFollow != null && wayToFollow.size() > 0) {
			return getInputCode(wayToFollow.get(0), zombiePosTiles.get(0));
		}
		//if path is empty then compute a new one
		else {
			wayToFollow = returnShortestPath(players, zombiePosTiles.get(0));
			
			if(wayToFollow == null || wayToFollow.isEmpty()) {
				//fallback random movement
				Random rand = new Random();
				int randOutput = rand.nextInt(9);	//generate a random num between 0-8
				
				if(randOutput == 1) return 1;		//right
				else if(randOutput == 2) return 3;	//bot-right
				else if(randOutput == 3) return 2;	//bot
				else if(randOutput == 4) return 6;	//bot-left
				else if(randOutput == 5) return 4;	//left
				else if(randOutput == 6) return 12;	//top-left
				else if(randOutput == 7) return 8;	//top
				else if(randOutput == 8) return 9;	//top-right
				else return 0;						//stand still
			}
			
			return getInputCode(wayToFollow.get(0), zombiePosTiles.get(0));
		}
	}
	
	//Logic:
	//1. check if raycast collides with any walls of this tile.
	//		If it does, then return false, as line of sight isn't established
	//2. check which neighbor Tiles are within the Raycast
	//3. recursively recall this function for all neighbor tiles
	
	boolean isInLineOfSight(Zombie zombie, Player player, Tile currentTile, ArrayList<Tile> processedTiles) {
		if(processedTiles == null) processedTiles = new ArrayList<Tile>();
		
		processedTiles.add(currentTile);

		for (Wall wall : currentTile.getWalls()) {
			if(CollisionDetection.inLineOfSight(zombie.getXPosition(), zombie.getYPosition(), player.getXPosition(), player.getYPosition(), wall)) {
				 //wall is in the way of the players line of sight
				 return false;
			}
		}

		//recursively go through all neighbors, cause neighbors are tiles connected without a wall in between them
		for (Tile neighbor : currentTile.getNeighbors()) {
			if(processedTiles.contains(neighbor)) {
				//do nothing
			}
			//neighbor is in sight. Check collision for it
			else if(CollisionDetection.inLineOfSight(zombie.getXPosition(), zombie.getYPosition(), player.getXPosition(), player.getYPosition(), neighbor)) {
				 return isInLineOfSight(zombie, player, neighbor, processedTiles);
			}
		}
		
		
		//zombies can see through the corner of tiles, even if the neighbors walls are blocking it
		//Thus you always have to check for collision for all neighbors walls before confirming line of sight
		for (Tile neighbor : currentTile.getNeighbors()) {
			//neighbor is in sight. Check collision for it
			for (Wall wall : neighbor.getWalls()) {
				if(CollisionDetection.inLineOfSight(zombie.getXPosition(), zombie.getYPosition(), player.getXPosition(), player.getYPosition(), wall)) {
					 //wall is in the way of the players line of sight
					 return false;
				}
			}
		}
		
		//no collision detected and no unprocessed neighbors
		return true;
	}
	
	float distance(GameObject go1, GameObject go2) {
		return (float) Math.sqrt(Math.pow(go1.getXPosition() - go2.getXPosition(), 2) + Math.pow(go1.getYPosition() - go2.getYPosition(), 2));
	}
	
	//Steering AI
	int getInputCode(GameObject go, Tile zombiePosTile) {
		//vector from zombie to player
		float dirX = go.getXPosition() - zombie.getXPosition();
		float dirY = go.getYPosition() - zombie.getYPosition();
		int inputCode = 0;
		
		ArrayList<Wall> walls = new ArrayList<Wall>();
		
		//add this and neighbor tile walls
		walls.addAll(zombiePosTile.getWalls());
		for (Tile neighbor : zombiePosTile.getNeighbors()) {
			walls.addAll(neighbor.getWalls());
		}
		
		for (Wall wall : walls) {
			//vector from wall to zombie, to push him away from it
			if(CollisionDetection.isInTriggerRange(zombie, wall, .02f)) {
				float vecX = zombie.getXPosition() - wall.getXPosition();
				float vecY = zombie.getYPosition() - wall.getYPosition();
				
				//System.out.println("Is in Trigger Range w/ " + wall);
				
				float distanceToObject = 0;
				
				//check if x is closer than y or the other way around
				if(vecX < vecY) {
					distanceToObject = Math.abs(vecX - zombie.getXPosition());
				}
				else {
					distanceToObject = Math.abs(vecY - zombie.getYPosition());
				}
				
				dirX += vecX/distanceToObject;
				dirY += vecY/distanceToObject;
			}
		}
		
		if(dirX>zombieRadius) {	//move right
			inputCode += 1;
		}
		else if(dirX<-zombieRadius) {	//move left
			inputCode += 2;
		}
		
		if(dirY>zombieRadius) {	//move up
			inputCode += 4;
		}
		else if(dirY<-zombieRadius) {	//move down
			inputCode += 8;
		}
		
		return inputCode;
	}
	
	//breadth first search. Finding the nearest player
	ArrayList<Tile> returnShortestPath(ArrayList<Player> players, Tile startingTile) {
		boolean noPathFindable = false;
		ArrayList<Tile> processedTiles = new ArrayList<Tile>();
		ArrayList<Tile> tilesInIteration = new ArrayList<Tile>();
		
		tilesInIteration.add(startingTile);
		
		while(!noPathFindable) {
			if(tilesInIteration.size() == 0) {
				noPathFindable = true;
			}
			else {
				Tile tileInIteration = tilesInIteration.remove(0);

				
				processedTiles.add(tileInIteration);
				
				for(Player player : players) {
					if(CollisionDetection.scanForCollision(player, tileInIteration)) {
						//return found path here somehow
						ArrayList<Tile> wayToFollow = new ArrayList<Tile>();
						
						wayToFollow = generateShortestPath(startingTile, tileInIteration, wayToFollow, processedTiles);
						Collections.reverse(wayToFollow);
						return wayToFollow;
						//turn reversed way to follow here and return that
					}
				}
				
				//System.out.println("Before for loop. Neighbor num: " + tileInIteration.getNeighbors().size());
				
				for(Tile neighbor : tileInIteration.getNeighbors()) {
					if(!processedTiles.contains(neighbor)) {
						tilesInIteration.add(neighbor);
					}
				}
			}
		}
		
		//System.out.println("returning no way found");
		
		return null;
	}
	
	ArrayList<Tile> generateShortestPath(Tile to, Tile from, ArrayList<Tile> wayToFollow, ArrayList<Tile> tilesToCheck){
		//System.out.println("goal: " + to.getXPosition() + "|" + to.getYPosition() + " from: " + from.getXPosition() + "|" + from.getYPosition());
		wayToFollow.add(from);
		
		if(from == to) {
			return wayToFollow;
		}
		for(Tile neighbor : from.getNeighbors()) {
			//System.out.println("neighbor: " + neighbor.getXPosition() + "|" + neighbor.getYPosition());
			if(tilesToCheck.contains(neighbor) && !wayToFollow.contains(neighbor))
			{
				//System.out.println("entered for");
				ArrayList<Tile> way = new ArrayList<Tile>();
				for(Tile tile : wayToFollow) {
					way.add(tile);
				}
				
				way = generateShortestPath(to, neighbor, way, tilesToCheck);
				
				if(way != null) { //if shortest way has been found
					return way;
				}
			}
		}
		
		return null;
	}
	
	
}
