package ai;

import java.util.ArrayList;

import collision.CollisionDetection;

import gameObjects.Player;
import gameObjects.Wall;
import gameObjects.Tile;
import gameObjects.Zombie;

public class AIBehaviour {
	
	//Idea: 
	
	public static void getShortestPath(Tile from, Tile to) {
		
	}
	
	public static int generateInputCode(Zombie zombie) {
		return 8;
	}
	
	//Logic:
	//1. check if raycast collides with any walls of this tile.
	//		If it does, then return false, als line of sight isn't established
	//2. check which neighbor Tiles are within the Raycast
	//3. recursively recall this function for all neighbor tiles
	
	static boolean isInLineOfSight(Zombie zombie, Player player, Tile currentTile, ArrayList<Tile> processedTiles) {
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
			else if(CollisionDetection.inLineOfSight(zombie.getXPosition(), zombie.getYPosition(), player.getXPosition(), player.getYPosition(), neighbor)) {
				 return isInLineOfSight(zombie, player, currentTile, processedTiles);
			}
		}
		
		//no collision detected and no unprocessed neighbors
		return true;
	}
}
