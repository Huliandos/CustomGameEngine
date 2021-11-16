package levelBuild;

import java.util.Random;

public class BuildLevel {

	//Important(!): Level Order is left to right and bottom to top.
	public static String generateLevel(int levelSize) {
		Tile[] tiles = new Tile[levelSize*levelSize];
		
		//init tiles equal to total number of level tiles
		for (int i=0; i<levelSize*levelSize; i++) {
			Tile tile = new Tile();
			tiles[i] = tile;
		}
		
		//populate neighbors array
		for (int y=0; y<levelSize; y++) {
			for (int x=0; x<levelSize; x++) {
				Tile[] neighbors = new Tile[4];
				
				if(x!=levelSize-1) neighbors[0] = tiles[x+1 + y*levelSize];		//right
				if(y!=0) neighbors[1] = tiles[x + (y-1)*levelSize];				//bottom
				if(x!=0) neighbors[2] = tiles[x-1 + y*levelSize];				//left
				if(y!=levelSize-1) neighbors[3] = tiles[x + (y+1)*levelSize];	//top
				
				/*
				String output = String.valueOf((x + (y-1)*levelSize));
				
				if(x!=levelSize-1) output += ("Right neighbor: " + (x+1 + y*levelSize));	//right
				if(y!=0) output += ("Bottom neighbor: " + (x + (y-1)*levelSize));			//bottom
				if(x!=0) output += ("Left neighbor: " + (x-1 + y*levelSize));				//left
				if(y!=levelSize-1) output += ("Top neighbor: " + (x + (y+1)*levelSize));	//top
				
				System.out.println(output);
				*/
				
				tiles[x + y*levelSize].setNeighbors(neighbors);
			}
		}

		Random rand = new Random(); 
		int randomStartingTile = rand.nextInt(levelSize * levelSize);
		
		generateLabyrinth(tiles[randomStartingTile]);
		
		String seed = "";
		
		for(int i=0; i<tiles.length; i++) {
			seed += tiles[i].getTileWallCode();
			if(i!=tiles.length-1) seed += ",";	//add a seperator after every number but the last one
		}
		
		return seed;
	}
	
	static void generateLabyrinth(Tile tile) {
		tile.processed = true;

		Random rand = new Random();
		
		Tile[] neighbors = tile.getNeighbors();
		int randNeighborIndex = rand.nextInt(tile.getNeighbors().length);
		Tile randomTile = neighbors[randNeighborIndex];
		
		//System.out.println((neighbors[0] != null) + " " + neighbors[0]);
		//System.out.println((neighbors[1] != null) + " " + neighbors[1]);
		//System.out.println((neighbors[2] != null) + " " + neighbors[2]);
		//System.out.println((neighbors[3] != null) + " " + neighbors[3]);
		
		//if neighbors == null then it shouldn't check if the neighbor is processed, as this will throw and Error
		//thus we check for the neighbor first and only if he isn't null, if he's processed
		if(!(neighbors[0] == null || neighbors[0].processed) ||
			!(neighbors[1] == null || neighbors[1].processed) ||
			!(neighbors[2] == null || neighbors[2].processed) ||
			!(neighbors[3] == null || neighbors[3].processed)) 		//if there's guaranteed one neighbor that isn't processed
		{
			while(randomTile == null || randomTile.processed) {		//select a random neighbor
				randNeighborIndex = rand.nextInt(tile.getNeighbors().length);
				randomTile = neighbors[randNeighborIndex];
			}
			
			//remove the wall between both Tiles
			if(randNeighborIndex == 0) {
				//right wall
				tile.setTileWallCode(tile.getTileWallCode()-2);
				//left wall
				neighbors[randNeighborIndex].setTileWallCode(neighbors[randNeighborIndex].getTileWallCode()-8);
			}
			else if(randNeighborIndex == 1) {
				//bottom wall
				tile.setTileWallCode(tile.getTileWallCode()-4);
				//top wall
				neighbors[randNeighborIndex].setTileWallCode(neighbors[randNeighborIndex].getTileWallCode()-1);
			}
			else if(randNeighborIndex == 2) {
				//left wall
				tile.setTileWallCode(tile.getTileWallCode()-8);
				//right wall
				neighbors[randNeighborIndex].setTileWallCode(neighbors[randNeighborIndex].getTileWallCode()-2);
			}
			else if(randNeighborIndex == 3) {
				//top wall
				tile.setTileWallCode(tile.getTileWallCode()-1);
				//bottom wall
				neighbors[randNeighborIndex].setTileWallCode(neighbors[randNeighborIndex].getTileWallCode()-4);
			}
			
			//recursively call this method on chosen neighbor
			generateLabyrinth(randomTile);
		}
		
		
		//once recursive calls are done check if there's still unprocessed neighbors left
		if(!(neighbors[0] == null || neighbors[0].processed) ||
				!(neighbors[1] == null || neighbors[1].processed) ||
				!(neighbors[2] == null || neighbors[2].processed) ||
				!(neighbors[3] == null || neighbors[3].processed)) 		//if there's guaranteed one neighbor that isn't processed
			{
				generateLabyrinth(tile);
			}
	}
}
