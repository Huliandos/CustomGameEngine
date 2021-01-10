package ai;

import java.util.ArrayList;

import gameObjects.Tile;

public class Node {
	
	Tile tile;
	int cost;
	ArrayList<Node> neighbors = new ArrayList<Node>();
	
	public Node(Tile tile, int cost) {
		this.tile = tile;
		this.cost = cost;
	}
	
	public Tile getTile() {
		return tile;
	}
	
	public int getCost() {
		return cost;
	}
	
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public void addNeighbor(Node neighbor) {
		neighbors.add(neighbor);
	}
	
	public ArrayList<Node> getNeighbors(){
		return neighbors;
	}
}
