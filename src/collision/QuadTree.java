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
		System.out.println("-- Creating new QuadTree. Tile Size: " + tiles.size() + " --");
		childTiles = tiles;
		
		this.posX = (highestX + lowestX) / 2;
		this.posY = (highestY + lowestY) / 2;
		
		collider = new RectangleCollider((highestX+tileSize/2) - (lowestX-tileSize/2), (highestY+tileSize/2) - (lowestY-tileSize/2));
		
		if(tiles.size() == 1) {
			//This is a leaf Quad tree. Do nothing
		}
		else if(tiles.size() < 4) {	//if we reached the bottom layer of the QuadTree, for a quad not divisible by 4
			System.out.println("Reached leaf child");
			//init children of QuadTree
			
			int childMazeSize = mazeSize / 2;

			//The Array Lists for each sub-Quad Tree
			ArrayList<Tile> TLChildTiles = new ArrayList<Tile>();
			ArrayList<Tile> TRChildTiles = new ArrayList<Tile>();
			ArrayList<Tile> BLChildTiles = new ArrayList<Tile>();
			ArrayList<Tile> BRChildTiles = new ArrayList<Tile>();
			
			
			for (int i=0; i<mazeSize*mazeSize; i++) {
				if(tiles.size() <= i) break;
				
				if(tiles.get(i).getXPosition() <= (highestX+lowestX)/2 && tiles.get(i).getYPosition() >= (highestY+lowestY)/2)	//Tile is in top left area
					TLChildTiles.add(tiles.get(i));
				else if(tiles.get(i).getXPosition() >= (highestX+lowestX)/2 && tiles.get(i).getYPosition() >= (highestY+lowestY)/2)	//Tile is in top right area
					TRChildTiles.add(tiles.get(i));
				else if(tiles.get(i).getXPosition() <= (highestX+lowestX)/2 && tiles.get(i).getYPosition() <= (highestY+lowestY)/2)	//Tile is in bottom left area
					BLChildTiles.add(tiles.get(i));
				else if(tiles.get(i).getXPosition() >= (highestX+lowestX)/2 && tiles.get(i).getYPosition() <= (highestY+lowestY)/2)	//Tile is in bottom right area
					BRChildTiles.add(tiles.get(i));
			}
			
			System.out.println("-- <4 --");
			System.out.println("TL child size: " + TLChildTiles.size() + " TR child size: " + TRChildTiles.size() + " BL child size: " + BLChildTiles.size() + " BR child size: " + BRChildTiles.size());
			if(TLChildTiles.size() >= 1)
				System.out.println("TL lowest X: " + TLChildTiles.get(0).getXPosition() + " highest X " + TLChildTiles.get(0).getXPosition() 
						+ " lowest Y " + TLChildTiles.get(0).getYPosition() + " highest Y" + TLChildTiles.get(0).getYPosition());
			if(TRChildTiles.size() >= 1)
				System.out.println("TR lowest X: " + TRChildTiles.get(0).getXPosition() + " highest X " + TRChildTiles.get(0).getXPosition() 
						+ " lowest Y " + TRChildTiles.get(0).getYPosition() + " highest Y" + TRChildTiles.get(0).getYPosition());
			if(BLChildTiles.size() >= 1)
				System.out.println("BL lowest X: " + BLChildTiles.get(0).getXPosition() + " highest X " + BLChildTiles.get(0).getXPosition() 
						+ " lowest Y " + BLChildTiles.get(0).getYPosition() + " highest Y" + BLChildTiles.get(0).getYPosition());
			if(BRChildTiles.size() >= 1)
				System.out.println("BR lowest X: " + BRChildTiles.get(0).getXPosition() + " highest X " + BRChildTiles.get(0).getXPosition() 
						+ " lowest Y " + BRChildTiles.get(0).getYPosition() + " highest Y" + BRChildTiles.get(0).getYPosition());
			
			if(TLChildTiles.size() >= 1)
				TLChild = new QuadTree(TLChildTiles, childMazeSize, tileSize, 
						TLChildTiles.get(0).getXPosition(), TLChildTiles.get(0).getXPosition(), TLChildTiles.get(0).getYPosition(), TLChildTiles.get(0).getYPosition());
			if(TRChildTiles.size() >= 1)
				TRChild = new QuadTree(TRChildTiles, childMazeSize, tileSize, 
						TRChildTiles.get(0).getXPosition(), TRChildTiles.get(0).getXPosition(), TRChildTiles.get(0).getYPosition(), TRChildTiles.get(0).getYPosition());
			if(BLChildTiles.size() >= 1)
				BLChild = new QuadTree(BLChildTiles, childMazeSize, tileSize, 
						BLChildTiles.get(0).getXPosition(), BLChildTiles.get(0).getXPosition(), BLChildTiles.get(0).getYPosition(), BLChildTiles.get(0).getYPosition());
			if(BRChildTiles.size() >= 1)
				BRChild = new QuadTree(BRChildTiles, childMazeSize, tileSize, 
						BRChildTiles.get(0).getXPosition(), BRChildTiles.get(0).getXPosition(), BRChildTiles.get(0).getYPosition(), BRChildTiles.get(0).getYPosition());

		}
		else if(tiles.size() == 4) {	//if we reached the bottom layer of the QuadTree
			System.out.println("Reached leaf child");
			//init children of QuadTree
			
			//int childMazeSize = mazeSize / 2;
			int childMazeSize = 0;

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

			System.out.println("-- ==4 --");
			System.out.println("mazeSize: " + mazeSize + "X: " + lowestX + " - " + highestX + " Y: " + lowestY + " - " + highestY);
			for (Tile tile : tiles) System.out.print(" Tile pos X: " + tile.getXPosition() + " Tile pos y: " + tile.getYPosition());
			
			System.out.println("TL child size: " + TLChildTiles.size() + " TR child size: " + TRChildTiles.size() + " BL child size: " + BLChildTiles.size() + " BR child size: " + BRChildTiles.size());
			System.out.println("TL lowest X: " + TLChildTiles.get(0).getXPosition() + " highest X " + TLChildTiles.get(0).getXPosition() 
					+ " lowest Y " + TLChildTiles.get(0).getYPosition() + " highest Y" + TLChildTiles.get(0).getYPosition());
			System.out.println("TR lowest X: " + TRChildTiles.get(0).getXPosition() + " TR highest X " + TRChildTiles.get(0).getXPosition() 
					+ " lowest Y " + TRChildTiles.get(0).getYPosition() + " highest Y" + TRChildTiles.get(0).getYPosition());
			System.out.println("BL lowest X: " + BLChildTiles.get(0).getXPosition() + " highest X " + BLChildTiles.get(0).getXPosition() 
					+ " lowest Y " + BLChildTiles.get(0).getYPosition() + " highest Y" + BLChildTiles.get(0).getYPosition());
			System.out.println("BR lowest X: " + BRChildTiles.get(0).getXPosition() + " highest X " + BRChildTiles.get(0).getXPosition() 
					+ " lowest Y " + BRChildTiles.get(0).getYPosition() + " highest Y" + BRChildTiles.get(0).getYPosition());
			
			TLChild = new QuadTree(TLChildTiles, childMazeSize, tileSize, 
					TLChildTiles.get(0).getXPosition(), TLChildTiles.get(0).getXPosition(), TLChildTiles.get(0).getYPosition(), TLChildTiles.get(0).getYPosition());
			TRChild = new QuadTree(TRChildTiles, childMazeSize, tileSize, 
					TRChildTiles.get(0).getXPosition(), TRChildTiles.get(0).getXPosition(), TRChildTiles.get(0).getYPosition(), TRChildTiles.get(0).getYPosition());
			BLChild = new QuadTree(BLChildTiles, childMazeSize, tileSize,
					BLChildTiles.get(0).getXPosition(), BLChildTiles.get(0).getXPosition(), BLChildTiles.get(0).getYPosition(), BLChildTiles.get(0).getYPosition());
			BRChild = new QuadTree(BRChildTiles, childMazeSize, tileSize, 
					BRChildTiles.get(0).getXPosition(), BRChildTiles.get(0).getXPosition(), BRChildTiles.get(0).getYPosition(), BRChildTiles.get(0).getYPosition());

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

			System.out.println("-- %4 --");
			System.out.println("mazeSize: " + mazeSize + "X: " + lowestX + " - " + highestX + " Y: " + lowestY + " - " + highestY);
			System.out.println("TL child size: " + TLChildTiles.size() + " TR child size: " + TRChildTiles.size() + " BL child size: " + BLChildTiles.size() + " BR child size: " + BRChildTiles.size());
			
			float[] values = getTilesExtent(TLChildTiles);	//Order is lowestX, highestX, lowestY, highestY
			
			System.out.println("TL lowest X: " + values[0] + " TL highest X " + values[1] + " TL lowest Y " + values[2] + " TL highest Y" + values[3]);
			TLChild = new QuadTree(TLChildTiles, childMazeSize, tileSize, values[0], values[1], values[2], values[3]);
			
			values = getTilesExtent(TRChildTiles);	//Order is lowestX, highestX, lowestY, highestY
			
			System.out.println("TR lowest X: " + values[0] + " TR highest X " + values[1] + " TR lowest Y " + values[2] + " TR highest Y" + values[3]);
			TRChild = new QuadTree(TRChildTiles, childMazeSize, tileSize, values[0], values[1], values[2], values[3]);
			
			values = getTilesExtent(BLChildTiles);	//Order is lowestX, highestX, lowestY, highestY
			
			System.out.println("BL lowest X: " + values[0] + " BL highest X " + values[1] + " BL lowest Y " + values[2] + " BL highest Y" + values[3]);
			BLChild = new QuadTree(BLChildTiles, childMazeSize, tileSize, values[0], values[1], values[2], values[3]);
			
			values = getTilesExtent(BRChildTiles);	//Order is lowestX, highestX, lowestY, highestY
			
			System.out.println("BR lowest X: " + values[0] + " BR highest X " + values[1] + " BR lowest Y " + values[2] + " BR highest Y" + values[3]);
			BRChild = new QuadTree(BRChildTiles, childMazeSize, tileSize, values[0], values[1], values[2], values[3]);
			
			//TLChild = new QuadTree(TLChildTiles, childMazeSize, tileSize, lowestX, (childMazeSize/2-.5f+lowestX), (childMazeSize/2+lowestY), highestY);
			//TRChild = new QuadTree(TRChildTiles, childMazeSize, tileSize, (childMazeSize/2+lowestX), highestX, (childMazeSize/2+lowestY), highestY);
			//BLChild = new QuadTree(BLChildTiles, childMazeSize, tileSize, lowestX, (childMazeSize/2-.5f+lowestX), lowestY, (childMazeSize/2-.5f+lowestY));
			//BRChild = new QuadTree(BRChildTiles, childMazeSize, tileSize, (childMazeSize/2+lowestX), highestX, lowestY, (childMazeSize/2-.5f+lowestY));
		}
		else {	//if the number of tiles isn't a multiple of 4
			
			float childMazeSize = mazeSize / 2;	//if the maze size isn't even, then this can become a floating point number, too
			//System.out.println("QuadTree childMazeSize: " + childMazeSize + " Num of children in Tree: " + (childMazeSize*childMazeSize));

			//The Array Lists for each sub-Quad Tree
			ArrayList<Tile> TLChildTiles = new ArrayList<Tile>();
			ArrayList<Tile> TRChildTiles = new ArrayList<Tile>();
			ArrayList<Tile> BLChildTiles = new ArrayList<Tile>();
			ArrayList<Tile> BRChildTiles = new ArrayList<Tile>();
			
			if(mazeSize % 2 == 0) {	//if it's 2 tiles over a maze divisible by 4
				for (int i=0; i<mazeSize*mazeSize; i++) {
					if(tiles.size() <= i) break;
					
					if(tiles.get(i).getXPosition() <= (highestX+lowestX)/2 && tiles.get(i).getYPosition() >= (highestY+lowestY)/2)	//Tile is in top left area
						TLChildTiles.add(tiles.get(i));
					else if(tiles.get(i).getXPosition() >= (highestX+lowestX)/2 && tiles.get(i).getYPosition() >= (highestY+lowestY)/2)	//Tile is in top right area
						TRChildTiles.add(tiles.get(i));
					else if(tiles.get(i).getXPosition() <= (highestX+lowestX)/2 && tiles.get(i).getYPosition() <= (highestY+lowestY)/2)	//Tile is in bottom left area
						BLChildTiles.add(tiles.get(i));
					else if(tiles.get(i).getXPosition() >= (highestX+lowestX)/2 && tiles.get(i).getYPosition() <= (highestY+lowestY)/2)	//Tile is in bottom right area
						BRChildTiles.add(tiles.get(i));
				}

				System.out.println("-- %2 --");
				System.out.println("TL child size: " + TLChildTiles.size() + " TR child size: " + TRChildTiles.size() + " BL child size: " + BLChildTiles.size() + " BR child size: " + BRChildTiles.size());
				
				if(TLChildTiles.size() >= 1) {
					float[] values = getTilesExtent(TLChildTiles);	//Order is lowestX, highestX, lowestY, highestY
					
					System.out.println("TL lowest X: " + values[0] + " highest X " + values[1] + " lowest Y " + values[2] + " highest Y" + values[3]);
					
					TLChild = new QuadTree(TLChildTiles, (int)childMazeSize, tileSize, values[0], values[1], values[2], values[3]);
				}
				if(TRChildTiles.size() >= 1) {
					float[] values = getTilesExtent(TRChildTiles);	//Order is lowestX, highestX, lowestY, highestY
					
					System.out.println("TR lowest X: " + values[0] + " highest X " + values[1] + " lowest Y " + values[2] + " highest Y" + values[3]);
					
					TRChild = new QuadTree(TRChildTiles, (int)childMazeSize, tileSize, values[0], values[1], values[2], values[3]);
				}
				if(BLChildTiles.size() >= 1) {
					float[] values = getTilesExtent(BLChildTiles);	//Order is lowestX, highestX, lowestY, highestY
					
					System.out.println("BL lowest X: " + values[0] + " highest X " + values[1] + " lowest Y " + values[2] + " highest Y" + values[3]);
					
					BLChild = new QuadTree(BLChildTiles, (int)childMazeSize, tileSize, values[0], values[1], values[2], values[3]);
				}
				if(BRChildTiles.size() >= 1) {
					float[] values = getTilesExtent(BRChildTiles);	//Order is lowestX, highestX, lowestY, highestY
					
					System.out.println("BR lowest X: " + values[0] + " highest X " + values[1] + " lowest Y " + values[2] + " highest Y" + values[3]);
					
					BRChild = new QuadTree(BRChildTiles, (int)childMazeSize, tileSize, values[0], values[1], values[2], values[3]);
				}
			}
			else {	//if it's 1 or 3 tiles over a maze divisible by 4
				
				//This moves the generated level size, so that the Quadtree separates
				//int quotient = tiles.size() / 4;	//how often the number 4 fits into the Tile Size
				//int lackingTiles = 4 - (tiles.size() - 4*quotient);	//how many tiles are missing for the tile num to be a multiple of 4
				
				int tooManyTiles = mazeSize % 4;	//how often the number 4 fits into the Tile Size
				int lackingTiles = 4 - tooManyTiles;	//how many tiles are missing for the tile num to be a multiple of 4
				
				for (int i=0; i<mazeSize*mazeSize; i++) {
					if(tiles.size() <= i) break;

					//we move the cutting point by .25f (tileSize/2), so that it cuts along the uneven maze tile edges and not right through the middle of a tile
					if(tiles.get(i).getXPosition() <= (highestX+lowestX)/2 + tileSize/2 && tiles.get(i).getYPosition() >= (highestY+lowestY)/2 - tileSize/2)	//Tile is in top left area
						TLChildTiles.add(tiles.get(i));
					else if(tiles.get(i).getXPosition() >= (highestX+lowestX)/2 + tileSize/2 && tiles.get(i).getYPosition() >= (highestY+lowestY)/2 - tileSize/2)	//Tile is in top right area
						TRChildTiles.add(tiles.get(i));
					else if(tiles.get(i).getXPosition() <= (highestX+lowestX)/2 + tileSize/2 && tiles.get(i).getYPosition() <= (highestY+lowestY)/2 - tileSize/2)	//Tile is in bottom left area
						BLChildTiles.add(tiles.get(i));
					else if(tiles.get(i).getXPosition() >= (highestX+lowestX)/2 + tileSize/2 && tiles.get(i).getYPosition() <= (highestY+lowestY)/2 - tileSize/2)	//Tile is in bottom right area
						BRChildTiles.add(tiles.get(i));
				}

				System.out.println("-- 1 or 3 too much. quotient: " + tooManyTiles + " lacking tiles: " + lackingTiles + " mazeSize: " + mazeSize + " child maze size: " + (((float)mazeSize)/2 + tileSize*lackingTiles) + " --");
				System.out.println("TL child size: " + TLChildTiles.size() + " TR child size: " + TRChildTiles.size() + " BL child size: " + BLChildTiles.size() + " BR child size: " + BRChildTiles.size());
				
				if(TLChildTiles.size() >= 1)
				{
					float[] values = getTilesExtent(TLChildTiles);	//Order is lowestX, highestX, lowestY, highestY
					
					//childMazeSize = roundUpwards((float) Math.sqrt(TLChildTiles.size()));
					
					if(TLChildTiles.size() % 4 == 0) childMazeSize = (float) Math.sqrt(TLChildTiles.size());
					else childMazeSize = (((float)mazeSize)/2 + tileSize*lackingTiles);
					
					System.out.println("TL lowest X: " + values[0] + " TL highest X " + values[1] + " TL lowest Y " + values[2] + " TL highest Y " + values[3]);
					
					TLChild = new QuadTree(TLChildTiles, (int)childMazeSize, tileSize, values[0], values[1], values[2], values[3]);
				}

				if(TRChildTiles.size() >= 1) {
					float[] values = getTilesExtent(TRChildTiles);	//Order is lowestX, highestX, lowestY, highestY
					
					//childMazeSize = roundUpwards((float) Math.sqrt(TRChildTiles.size()));
					
					if(TRChildTiles.size() % 4 == 0) childMazeSize = (float) Math.sqrt(TRChildTiles.size());
					else childMazeSize = (((float)mazeSize)/2 + tileSize*lackingTiles);
					
					System.out.println("TR lowest X: " + values[0] + " TR highest X " + values[1] + " TR lowest Y " + values[2] + " TR highest Y " + values[3]);
					
					TRChild = new QuadTree(TRChildTiles, (int)childMazeSize, tileSize, values[0], values[1], values[2], values[3]);
				}
				
				if(BLChildTiles.size() >= 1) {
					float[] values = getTilesExtent(BLChildTiles);	//Order is lowestX, highestX, lowestY, highestY
					
					//childMazeSize = roundUpwards((float) Math.sqrt(BLChildTiles.size()));
					
					if(BLChildTiles.size() % 4 == 0) childMazeSize = (float) Math.sqrt(BLChildTiles.size());
					else childMazeSize = (((float)mazeSize)/2 + tileSize*lackingTiles);

					System.out.println("BL lowest X: " + values[0] + " BL highest X " + values[1] + " BL lowest Y " + values[2] + " BL highest Y " + values[3]);
					
					BLChild = new QuadTree(BLChildTiles, (int)childMazeSize, tileSize, values[0], values[1], values[2], values[3]);
				}
				
				if(BRChildTiles.size() >= 1) {
					float[] values = getTilesExtent(BRChildTiles);	//Order is lowestX, highestX, lowestY, highestY
					
					//childMazeSize = roundUpwards((float) Math.sqrt(BRChildTiles.size()));
					
					if(BRChildTiles.size() % 4 == 0) childMazeSize = (float) Math.sqrt(BRChildTiles.size());
					else childMazeSize = (((float)mazeSize)/2 + tileSize*lackingTiles);

					System.out.println("BR lowest X: " + values[0] + " BR highest X " + values[1] + " BR lowest Y " + values[2] + " BR highest Y " + values[3]);
					
					BRChild = new QuadTree(BRChildTiles, (int)childMazeSize, tileSize, values[0], values[1], values[2], values[3]);
				}
			}
		}
		
	}
	
	//recursive function that fetches all colliding tiles and returns them
	public ArrayList<Tile> getCollidingTiles(GameObject goToCheck) {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		
		//colliding with top-left zone
		if(TLChild != null) {
			if(CollisionDetection.scanForCollision(goToCheck, TLChild.collider, TLChild.posX, TLChild.posY)) {
				if(TLChild.getChildTiles().size() == 1) tiles.addAll(TLChild.getChildTiles());	//if we have reached a leaf tile
				else tiles.addAll(TLChild.getCollidingTiles(goToCheck));	//recall this method for non leaf tiles
			}
		}
		//colliding with top-right zone
		if(TRChild != null) {
			if(CollisionDetection.scanForCollision(goToCheck, TRChild.collider, TRChild.posX, TRChild.posY)) {
				if(TRChild.getChildTiles().size() == 1) tiles.addAll(TRChild.getChildTiles());	//if we have reached a leaf tile
				else tiles.addAll(TRChild.getCollidingTiles(goToCheck));	//recall this method for non leaf tiles
			}
		}
		//colliding with bottom-left zone
		if(BLChild != null) {
			if(CollisionDetection.scanForCollision(goToCheck, BLChild.collider, BLChild.posX, BLChild.posY)) {
				if(BLChild.getChildTiles().size() == 1) tiles.addAll(BLChild.getChildTiles());	//if we have reached a leaf tile
				else tiles.addAll(BLChild.getCollidingTiles(goToCheck));	//recall this method for non leaf tiles
			}
		}
		//colliding with bottom-right zone
		if(BRChild != null) {
			if(CollisionDetection.scanForCollision(goToCheck, BRChild.collider, BRChild.posX, BRChild.posY)) {
				if(BRChild.getChildTiles().size() == 1) tiles.addAll(BRChild.getChildTiles());	//if we have reached a leaf tile
				else tiles.addAll(BRChild.getCollidingTiles(goToCheck));	//recall this method for non leaf tiles
			}
		}
		
		return tiles;
	}
	
	//recursive function that fetches all colliding tiles and returns them WITH OFFSET
	public ArrayList<Tile> getCollidingTiles(GameObject goToCheck, float offsetX, float offsetY) {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		
		//colliding with top-left zone
		if(TLChild != null) {
			if(CollisionDetection.scanForCollision(goToCheck, offsetX, offsetY, TLChild.collider, TLChild.posX, TLChild.posY)) {
				if(TLChild.getChildTiles().size() == 1) tiles.addAll(TLChild.getChildTiles());	//if we have reached a leaf tile
				else tiles.addAll(TLChild.getCollidingTiles(goToCheck, offsetX, offsetY));	//recall this method for non leaf tiles
			}
		}
		//colliding with top-right zone
		if(TRChild != null) {
			if(CollisionDetection.scanForCollision(goToCheck, offsetX, offsetY, TRChild.collider, TRChild.posX, TRChild.posY)) {
				if(TRChild.getChildTiles().size() == 1) tiles.addAll(TRChild.getChildTiles());	//if we have reached a leaf tile
				else tiles.addAll(TRChild.getCollidingTiles(goToCheck, offsetX, offsetY));	//recall this method for non leaf tiles
			}
		}
		//colliding with bottom-left zone
		if(BLChild != null) {
			if(CollisionDetection.scanForCollision(goToCheck, offsetX, offsetY, BLChild.collider, BLChild.posX, BLChild.posY)) {
				if(BLChild.getChildTiles().size() == 1) tiles.addAll(BLChild.getChildTiles());	//if we have reached a leaf tile
				else tiles.addAll(BLChild.getCollidingTiles(goToCheck, offsetX, offsetY));	//recall this method for non leaf tiles
			}
		}
		//colliding with bottom-right zone
		if(BRChild != null) {
			if(CollisionDetection.scanForCollision(goToCheck, offsetX, offsetY, BRChild.collider, BRChild.posX, BRChild.posY)) {
				if(BRChild.getChildTiles().size() == 1) tiles.addAll(BRChild.getChildTiles());	//if we have reached a leaf tile
				else tiles.addAll(BRChild.getCollidingTiles(goToCheck, offsetX, offsetY));	//recall this method for non leaf tiles
			}
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
	
	float[] getTilesExtent(ArrayList<Tile> tiles) {
		float LowestX, HighestX, LowestY, HighestY;
		LowestX = HighestX = tiles.get(0).getXPosition();
		LowestY = HighestY = tiles.get(0).getYPosition();

		for (Tile tile : tiles) {
			if(tile.getXPosition() < LowestX)	LowestX = tile.getXPosition();
			if(tile.getXPosition() > HighestX)	HighestX = tile.getXPosition();
			if(tile.getYPosition() < LowestY)	LowestY = tile.getYPosition();
			if(tile.getYPosition() > HighestY)	HighestY = tile.getYPosition();
		}
		
		return new float[] {LowestX, HighestX, LowestY, HighestY};
	}
	
	float roundUpwards(float number) {
		int numberInt = (int) number;
		
		if(numberInt == number) return number;
		else return numberInt++;
	}
}
