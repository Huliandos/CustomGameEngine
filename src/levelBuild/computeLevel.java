package levelBuild;
import java.util.ArrayList;

import gameObjects.GameObject;
import gameObjects.Wall;

public class computeLevel {

	
	
<<<<<<< HEAD
	public static ArrayList<GameObject> drawWalls(String seed, float mazeSize, float tileSize) {
=======
	public ArrayList<GameObject> drawWalls(String seed, float mazeSize, float tileSize) {
>>>>>>> master
		ArrayList<GameObject> Walls = new ArrayList<GameObject>();
		String [] splitSeed = seed.split(",");
		int count = 0;
		for(int y=0; y<mazeSize; y++) {
			for(int x=0; x<mazeSize; x++) {
				
			    switch (splitSeed[count]) {
		        case "0":
		            break;
		        case "1": 
					Wall wall1o = new Wall(x*.5f, (y+tileSize/2)*.5f, true);  //Oben
					Walls.add(wall1o);
					count ++;
		            break;
		        case "2":
					Wall wall2r = new Wall((x+tileSize/2)*.5f, y*.5f, false); //Rechts
					Walls.add(wall2r);
					count ++;
		            break;
		        case "3":
					Wall wall3o = new Wall(x*.5f, (y+tileSize/2)*.5f, true);  //Oben
					Wall wall3r = new Wall((x+tileSize/2)*.5f, y*.5f, false); //Rechts
					Walls.add(wall3o);
					Walls.add(wall3r);
					count ++;
		            break;
		        case "4":
					Wall wall4u = new Wall(x*.5f, (y-tileSize/2)*.5f, true);  //Unten
					Walls.add(wall4u);
					count ++;
		            break;
		        case "5":
					Wall wall5o = new Wall(x*.5f, (y+tileSize/2)*.5f, true);  //Oben
					Wall wall5u = new Wall(x*.5f, (y-tileSize/2)*.5f, true);  //Unten
					Walls.add(wall5o);
					Walls.add(wall5u);
					count ++;
		            break;
		        case "6":
					Wall wall6u = new Wall(x*.5f, (y-tileSize/2)*.5f, true);  //Unten
					Wall wall6r = new Wall((x+tileSize/2)*.5f, y*.5f, false); //Rechts
					Walls.add(wall6u);
					Walls.add(wall6r);
					count ++;
		            break;
		        case "7":
					Wall wall7o = new Wall(x*.5f, (y+tileSize/2)*.5f, true);  //Oben
					Wall wall7u = new Wall(x*.5f, (y-tileSize/2)*.5f, true);  //Unten
					Wall wall7r = new Wall((x+tileSize/2)*.5f, y*.5f, false); //Rechts
					Walls.add(wall7o);
					Walls.add(wall7u);
					Walls.add(wall7r);
					count ++;
		            break;
		        case "8":
					Wall wall8l = new Wall((x-tileSize/2)*.5f, y*.5f, false); // links
					Walls.add(wall8l);
					count ++;
		            break;
		        case "9":
					Wall wall9o = new Wall(x*.5f, (y+tileSize/2)*.5f, true);  //Oben
					Wall wall9l = new Wall((x-tileSize/2)*.5f, y*.5f, false); // links
					Walls.add(wall9o);
					Walls.add(wall9l);
					count ++;
		            break;
		        case "10":
					Wall wall10l = new Wall((x-tileSize/2)*.5f, y*.5f, false); // links
					Wall wall10r = new Wall((x+tileSize/2)*.5f, y*.5f, false); //Rechts
					Walls.add(wall10l);
					Walls.add(wall10r);
					count ++;
		            break;
		        case "11":
					Wall wall11o = new Wall(x*.5f, (y+tileSize/2)*.5f, true);  //Oben
					Wall wall11l = new Wall((x-tileSize/2)*.5f, y*.5f, false); // links
					Wall wall11r = new Wall((x+tileSize/2)*.5f, y*.5f, false); //Rechts
					Walls.add(wall11o);
					Walls.add(wall11l);
					Walls.add(wall11r);
					count ++;
		            break;
		        case "12":
					Wall wall12u = new Wall(x*.5f, (y-tileSize/2)*.5f, true);  //Unten
					Wall wall12l = new Wall((x-tileSize/2)*.5f, y*.5f, false); // links
					Walls.add(wall12u);
					Walls.add(wall12l);
					count ++;
		            break;
		        case "13":
					Wall wall13o = new Wall(x*.5f, (y+tileSize/2)*.5f, true);  //Oben
					Wall wall13u = new Wall(x*.5f, (y-tileSize/2)*.5f, true);  //Unten
					Wall wall13l = new Wall((x-tileSize/2)*.5f, y*.5f, false); // links
					Walls.add(wall13o);
					Walls.add(wall13u);
					Walls.add(wall13l);
					count ++;
		            break;
		        case "14":
					Wall wall14l = new Wall((x-tileSize/2)*.5f, y*.5f, false); // links
					Wall wall14u = new Wall(x*.5f, (y-tileSize/2)*.5f, true);  //Unten
					Wall wall14r = new Wall((x+tileSize/2)*.5f, y*.5f, false); //Rechts
					Walls.add(wall14l);
					Walls.add(wall14u);
					Walls.add(wall14r);
					count ++;
		            break;
<<<<<<< HEAD
		       /* case "15":
=======
		        case "15":
>>>>>>> master
					Wall wall15o = new Wall(x*.5f, (y+tileSize/2)*.5f, true);  //Oben
					Wall wall15l = new Wall((x-tileSize/2)*.5f, y*.5f, false); // links
					Wall wall15u = new Wall(x*.5f, (y-tileSize/2)*.5f, true);  //Unten
					Wall wall15r = new Wall((x+tileSize/2)*.5f, y*.5f, false); //Rechts
					Walls.add(wall15o);
					Walls.add(wall15l);
					Walls.add(wall15u);
					Walls.add(wall15r);
					count ++;
<<<<<<< HEAD
		            break;*/
=======
		            break;
>>>>>>> master
		        default:
		        	System.out.println("default?");
		            break;
			    }
			}
		}
		return Walls;
	}
}
