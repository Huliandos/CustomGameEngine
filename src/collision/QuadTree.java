package collision;

import java.util.ArrayList;

import gameObjects.GameObject;
import gameObjects.Tile;

public class QuadTree {
	
	
	QuadTree TLChild, TRChild, BLChild, BRChild;	//T = Top, B = Bottom, L = Left, R = Right
	ArrayList<Tile> childTiles = new ArrayList<Tile>();
	float posX, posY;
	
	Collider collider;
	
	public QuadTree(ArrayList<Tile> tiles, int mazeSize, float tileSize, float lowestX, float highestX, float lowestY, float highestY) {
		
		childTiles = tiles;
		
		this.posX = (highestX + lowestX) / 2;
		this.posY = (highestY + lowestY) / 2;
		
		collider = new RectangleCollider((highestX+tileSize/2) - (lowestX-tileSize/2), (highestY+tileSize/2) - (lowestY-tileSize/2));
		
		if(tiles.size() == 4) {	//if we reached the bottom layer of the QuadTree
			//init children of QuadTree
			
			int childMazeSize = mazeSize / 2;

			//The Array Lists for each sub-Quad Tree
			ArrayList<Tile> TLChildTiles = new ArrayList<Tile>();
			ArrayList<Tile> TRChildTiles = new ArrayList<Tile>();
			ArrayList<Tile> BLChildTiles = new ArrayList<Tile>();
			ArrayList<Tile> BRChildTiles = new ArrayList<Tile>();
			
			
			for (int i=0; i<mazeSize*mazeSize; i++) {
				if(tiles.get(i).getXPosition() <= (highestX+lowestX)/2 && tiles.get(i).getYPosition() >= (highestY+lowestY)/2)	//Tile is in top left area
					TLChildTiles.add(tiles.get(i));
				else if(tiles.get(i).getXPosition() >= (highestX+lowestX)/2 && tiles.get(i).getYPosition() >= (highestY+lowestY)/2)	//Tile is in top right area
					TRChildTiles.add(tiles.get(i));
				else if(tiles.get(i).getXPosition() <= (highestX+lowestX)/2 && tiles.get(i).getYPosition() <= (highestY+lowestY)/2)	//Tile is in bottom left area
					BLChildTiles.add(tiles.get(i));
				else if(tiles.get(i).getXPosition() >= (highestX+lowestX)/2 && tiles.get(i).getYPosition() <= (highestY+lowestY)/2)	//Tile is in bottom right area
					BRChildTiles.add(tiles.get(i));
			}

			TLChild = new QuadTree(TLChildTiles, childMazeSize, tileSize, TLChildTiles.get(0).getXPosition(), TLChildTiles.get(0).getXPosition(), TLChildTiles.get(0).getYPosition(), TLChildTiles.get(0).getYPosition());
			TRChild = new QuadTree(TRChildTiles, childMazeSize, tileSize, TRChildTiles.get(0).getXPosition(), TRChildTiles.get(0).getXPosition(), TRChildTiles.get(0).getYPosition(), TRChildTiles.get(0).getYPosition());
			BLChild = new QuadTree(BLChildTiles, childMazeSize, tileSize, BLChildTiles.get(0).getXPosition(), BLChildTiles.get(0).getXPosition(), BLChildTiles.get(0).getYPosition(), BLChildTiles.get(0).getYPosition());
			BRChild = new QuadTree(BRChildTiles, childMazeSize, tileSize, BRChildTiles.get(0).getXPosition(), BRChildTiles.get(0).getXPosition(), BRChildTiles.get(0).getYPosition(), BRChildTiles.get(0).getYPosition());

		}
		else if(tiles.size() % 4 == 0) {	//if the number of tiles is a multiple of 4
			//System.out.println("QuadTree: creating Quadtree. Lowest X: " + lowestX + " highestX " + highestX + " Lowest Y:"  + lowestY + " highestY " + highestY);
			//System.out.println("QuadTree: tiles: " + tiles.size());
			//collider = new RectangleCollider(highestX - lowestX, highestY - lowestY);
			
			//init children of QuadTree
			int childMazeSize = mazeSize / 2;
			//System.out.println("QuadTree childMazeSize: " + childMazeSize + " Num of children in Tree: " + (childMazeSize*childMazeSize));

			//The Array Lists for each sub-Quad Tree
			ArrayList<Tile> TLChildTiles = new ArrayList<Tile>();
			ArrayList<Tile> TRChildTiles = new ArrayList<Tile>();
			ArrayList<Tile> BLChildTiles = new ArrayList<Tile>();
			ArrayList<Tile> BRChildTiles = new ArrayList<Tile>();
			
			for (int i=0; i<mazeSize*mazeSize; i++) {
				if(tiles.get(i).getXPosition() <= (highestX+lowestX)/2 && tiles.get(i).getYPosition() >= (highestY+lowestY)/2)	//Tile is in top left area
					TLChildTiles.add(tiles.get(i));
				else if(tiles.get(i).getXPosition() >= (highestX+lowestX)/2 && tiles.get(i).getYPosition() >= (highestY+lowestY)/2)	//Tile is in top right area
					TRChildTiles.add(tiles.get(i));
				else if(tiles.get(i).getXPosition() <= (highestX+lowestX)/2 && tiles.get(i).getYPosition() <= (highestY+lowestY)/2)	//Tile is in bottom left area
					BLChildTiles.add(tiles.get(i));
				else if(tiles.get(i).getXPosition() >= (highestX+lowestX)/2 && tiles.get(i).getYPosition() <= (highestY+lowestY)/2)	//Tile is in bottom right area
					BRChildTiles.add(tiles.get(i));
			}

			//System.out.println("TL child size: " + TLChildTiles.size() + " TR child size: " + TRChildTiles.size() + " BL child size: " + BLChildTiles.size() + " BR child size: " + BRChildTiles.size());
			//System.out.println("TL lowest X: " + lowestX + " TL highest X " + (childMazeSize/2-.5f+lowestX) + " TL lowest Y " + (childMazeSize/2+lowestY) + " TL highest Y" + highestY);
			//System.out.println("TR lowest X: " + (childMazeSize/2+lowestX) + " TR highest X " + highestX + " TR lowest Y " + (childMazeSize/2+lowestY) + " TR highest Y" + highestY);
			//System.out.println("BL lowest X: " + lowestX + " BL highest X " + (childMazeSize/2-.5f+lowestX) + " BL lowest Y " + lowestY + " BL highest Y" + (childMazeSize/2 -.5f+lowestY));
			//System.out.println("BR lowest X: " + (childMazeSize/2+lowestX) + " BR highest X " + highestX + " BR lowest Y " + lowestY + " BR highest Y" + (childMazeSize/2 -.5f+lowestY));
			
			TLChild = new QuadTree(TLChildTiles, childMazeSize, tileSize, lowestX, (childMazeSize/2-.5f+lowestX), (childMazeSize/2+lowestY), highestY);
			TRChild = new QuadTree(TRChildTiles, childMazeSize, tileSize, (childMazeSize/2+lowestX), highestX, (childMazeSize/2+lowestY), highestY);
			BLChild = new QuadTree(BLChildTiles, childMazeSize, tileSize, lowestX, (childMazeSize/2-.5f+lowestX), lowestY, (childMazeSize/2-.5f+lowestY));
			BRChild = new QuadTree(BRChildTiles, childMazeSize, tileSize, (childMazeSize/2+lowestX), highestX, lowestY, (childMazeSize/2-.5f+lowestY));
		}
		else {	//if the number of tiles isn't a multiple of 4
			//This moves the generated level size, so that the Quadtree separates
			int quotient = tiles.size() / 4;	//how often the number 4 fits into the Tile Size
			int lackingTiles = 4 - (tiles.size() - 4*quotient);	//how many tiles are missing for the tile num to be a multiple of 4
			
			//collider = new RectangleCollider(highestX - lowestX, highestY - lowestY);
			
		}
		
	}
	
	//recursive function that fetches all colliding tiles and returns them
	public ArrayList<Tile> getCollidingTiles(GameObject goToCheck) {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		
		//colliding with top-left zone
		if(CollisionDetection.scanForCollision(goToCheck, TLChild.collider, TLChild.posX, TLChild.posY)) {
			if(TLChild.getChildTiles().size() == 1) tiles.addAll(TLChild.getChildTiles());	//if we have reached a leaf tile
			else tiles.addAll(TLChild.getCollidingTiles(goToCheck));	//recall this method for non leaf tiles
		}
		//colliding with top-right zone
		if(CollisionDetection.scanForCollision(goToCheck, TRChild.collider, TRChild.posX, TRChild.posY)) {
			if(TRChild.getChildTiles().size() == 1) tiles.addAll(TRChild.getChildTiles());	//if we have reached a leaf tile
			else tiles.addAll(TRChild.getCollidingTiles(goToCheck));	//recall this method for non leaf tiles
		}
		//colliding with bottom-left zone
		if(CollisionDetection.scanForCollision(goToCheck, BLChild.collider, BLChild.posX, BLChild.posY)) {
			if(BLChild.getChildTiles().size() == 1) tiles.addAll(BLChild.getChildTiles());	//if we have reached a leaf tile
			else tiles.addAll(BLChild.getCollidingTiles(goToCheck));	//recall this method for non leaf tiles
		}
		//colliding with bottom-right zone
		if(CollisionDetection.scanForCollision(goToCheck, BRChild.collider, BRChild.posX, BRChild.posY)) {
			if(BRChild.getChildTiles().size() == 1) tiles.addAll(BRChild.getChildTiles());	//if we have reached a leaf tile
			else tiles.addAll(BRChild.getCollidingTiles(goToCheck));	//recall this method for non leaf tiles
		}
		
		
		return tiles;
	}
	
	//recursive function that fetches all colliding tiles and returns them WITH OFFSET
	public ArrayList<Tile> getCollidingTiles(GameObject goToCheck, float offsetX, float offsetY) {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		
		//colliding with top-left zone
		if(CollisionDetection.scanForCollision(goToCheck, offsetX, offsetY, TLChild.collider, TLChild.posX, TLChild.posY)) {
			if(TLChild.getChildTiles().size() == 1) tiles.addAll(TLChild.getChildTiles());	//if we have reached a leaf tile
			else tiles.addAll(TLChild.getCollidingTiles(goToCheck, offsetX, offsetY));	//recall this method for non leaf tiles
		}
		//colliding with top-right zone
		if(CollisionDetection.scanForCollision(goToCheck, offsetX, offsetY, TRChild.collider, TRChild.posX, TRChild.posY)) {
			if(TRChild.getChildTiles().size() == 1) tiles.addAll(TRChild.getChildTiles());	//if we have reached a leaf tile
			else tiles.addAll(TRChild.getCollidingTiles(goToCheck, offsetX, offsetY));	//recall this method for non leaf tiles
		}
		//colliding with bottom-left zone
		if(CollisionDetection.scanForCollision(goToCheck, offsetX, offsetY, BLChild.collider, BLChild.posX, BLChild.posY)) {
			if(BLChild.getChildTiles().size() == 1) tiles.addAll(BLChild.getChildTiles());	//if we have reached a leaf tile
			else tiles.addAll(BLChild.getCollidingTiles(goToCheck, offsetX, offsetY));	//recall this method for non leaf tiles
		}
		//colliding with bottom-right zone
		if(CollisionDetection.scanForCollision(goToCheck, offsetX, offsetY, BRChild.collider, BRChild.posX, BRChild.posY)) {
			if(BRChild.getChildTiles().size() == 1) tiles.addAll(BRChild.getChildTiles());	//if we have reached a leaf tile
			else tiles.addAll(BRChild.getCollidingTiles(goToCheck, offsetX, offsetY));	//recall this method for non leaf tiles
		}
		
		
		return tiles;
	}

	ArrayList<Tile> getChildTiles(){
		return childTiles;
	}
	
	float getPosX() {
		return posX;
	}
	
	float getPosY() {
		return posY;
	}
}
