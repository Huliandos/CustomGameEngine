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
			    
			    if(seedValue / 8 == 1)	{	//4th bit == 1 --> left
			    	tile.addNeighbor(tiles.get(y * mazeSize + (x-1)));
			    	
			    	seedValue -= 8;
				}
				if(seedValue / 4 == 1)	{	//3th bit == 1 --> bottom
			    	tile.addNeighbor(tiles.get((y-1) * mazeSize + x));
			    	
					seedValue -= 4;
				}
				if(seedValue / 2 == 1)	{	//2th bit == 1 --> right
			    	tile.addNeighbor(tiles.get(y * mazeSize + (x+1)));
			    	
					seedValue -= 2;
				}
				if(seedValue / 1 == 1)	{	//1th bit == 1 --> top
			    	tile.addNeighbor(tiles.get((y+1) * mazeSize + x));
			    	
					seedValue -= 1;
				}
				
			}
		}
	}
}
