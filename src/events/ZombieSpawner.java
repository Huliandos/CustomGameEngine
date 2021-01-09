package events;

import java.util.ArrayList;
import java.util.Random;

import gameObjects.GameObject;
import gameObjects.Zombie;

import graphics.MainThread;

public class ZombieSpawner extends Event {

	int levelSize;
	float tileSize;
	
	public ZombieSpawner(int levelSize, float tileSize) {
		this.levelSize = levelSize;
		this.tileSize = tileSize;

		Random rand = new Random();
		long spawnDelay = (long)rand.nextInt(4) * 1000;	//0-3 seconds delay
		
		time = System.currentTimeMillis() + spawnDelay;
	}
	
	public ZombieSpawner(int levelSize, float tileSize, long spawnDelay) {
		this.levelSize = levelSize;
		this.tileSize = tileSize;

		time = System.currentTimeMillis() + spawnDelay;
	}
	
	@Override
	public void execute() {
		Random rand = new Random();
		
		int spawnTile = rand.nextInt(levelSize*levelSize);
		
		ArrayList<GameObject> dynamicObjectsClone = MainThread.getCopyOfDynamicObjects();
		
		int id = 0;
		
		for(GameObject go : dynamicObjectsClone) {
			if(go.getClass() == Zombie.class) {
				if(((Zombie) go).getID() >= id) id = ((Zombie)go).getID() + 1;
			}
		}
		
		MainThread.sendZombieSpawn(id, spawnTile);
		
		time = System.currentTimeMillis() + (rand.nextInt(11) + 5)  * 1000;	//next Zombie spawn after 5-15 seconds
	}

}
