package ai;

import java.util.ArrayList;

import gameObjects.Tile;

public class LevelMapper {
	
	public static void mapLevel(ArrayList<Tile> tiles, int mazeSize, String levelSeed) {
		String [] splitSeed = levelSeed.split(",");
		
		for(int y=0; y<mazeSize; y++) {
			for(int x=0; x<mazeSize; x++) {
				
				Tile tile = tiles.get(y * mazeSize + x);
			    int seedValue = Integer.valueOf(splitSeed[y * mazeSize + x]);
			    boolean left, bottom, right, top;
			    left = true;
			    bottom = true;
			    right = true;
			    top = true;
			    
			    if(seedValue / 8 == 1)	{	//4th bit == 1 --> left wall
			    	left = false;
			    	
			    	seedValue -= 8;
				}
				if(seedValue / 4 == 1)	{	//3th bit == 1 --> bottom wall
			    	bottom = false;
			    	
					seedValue -= 4;
				}
				if(seedValue / 2 == 1)	{	//2th bit == 1 --> right wall
			    	right = false;
			    	
					seedValue -= 2;
				}
				if(seedValue / 1 == 1)	{	//1th bit == 1 --> top wall
			    	top = false;
			    	
					seedValue -= 1;
				}
				
				if(left) tile.addNeighbor(tiles.get(y * mazeSize + (x-1)));
				if(bottom) tile.addNeighbor(tiles.get((y-1) * mazeSize + x));
				if(right) tile.addNeighbor(tiles.get(y * mazeSize + (x+1)));
				if(top) tile.addNeighbor(tiles.get((y+1) * mazeSize + x));
			}
		}
	}
}
