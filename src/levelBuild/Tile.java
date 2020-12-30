package levelBuild;

public class Tile {
	Tile[] neighbors;
	int tileWallCode;
	
	public boolean processed;
	
	public Tile() {
		tileWallCode = 15;
	}
	
	public Tile[] getNeighbors() {
		return neighbors;
	}
	
	public void setNeighbors(Tile[] neighbors) {
		this.neighbors = neighbors;
	}
	
	public int getTileWallCode() {
		return tileWallCode;
	}
	
	public void setTileWallCode(int tileWallCode) {
		this.tileWallCode = tileWallCode;
	}
}
