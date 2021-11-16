package levelBuild;
import java.util.ArrayList;

import gameObjects.GameObject;
import gameObjects.Wall;

public class ComputeLevel {

	
	public static ArrayList<GameObject> drawWalls(String seed, int mazeSize, float tileSize, ArrayList<GameObject> tileGOs) {
		ArrayList<GameObject> Walls = new ArrayList<GameObject>();
		ArrayList<gameObjects.Tile> tiles = new ArrayList<gameObjects.Tile>();	//These are the Tile GameObjects. Not the TIles used to compute the level
		for (GameObject tile : tileGOs) {
			tiles.add((gameObjects.Tile)tile);
		}
		String [] splitSeed = seed.split(",");
		
		for(int y=0; y<mazeSize; y++) {
			for(int x=0; x<mazeSize; x++) {
			    
				gameObjects.Tile tile = tiles.get(y * mazeSize + x);
			    int seedValue = Integer.valueOf(splitSeed[y * mazeSize + x]);
			    
			    if(seedValue / 8 == 1)	{	//4th bit == 1 --> left
			    	Wall wall = new Wall(x*.5f-tileSize/2, y*.5f, true);
			    	Walls.add(wall);
			    	tile.addWall(wall);
			    	seedValue -= 8;
				}
				if(seedValue / 4 == 1)	{	//3th bit == 1 --> bottom
					Wall wall = new Wall(x*.5f, y*.5f-tileSize/2, false); 
			    	Walls.add(wall);
			    	tile.addWall(wall);
					seedValue -= 4;
				}
				if(seedValue / 2 == 1)	{	//2th bit == 1 --> right
					Wall wall = new Wall(x*.5f+tileSize/2, y*.5f, true);
			    	Walls.add(wall);
			    	tile.addWall(wall);
					seedValue -= 2;
				}
				if(seedValue / 1 == 1)	{	//1th bit == 1 --> top
					Wall wall = new Wall(x*.5f, y*.5f+tileSize/2, false); 
			    	Walls.add(wall);
			    	tile.addWall(wall);
					seedValue -= 1;
				}
			}
		}
		return Walls;
	}
}
