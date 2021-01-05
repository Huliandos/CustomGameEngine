package gameObjects;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import collision.Collider;
import collision.RectangleCollider;

public class Tile extends GameObject {
	
	private float width, height;
	
	Collider collider;
	
	ArrayList<Wall> walls = new ArrayList<Wall>();
	ArrayList<Tile> neighbors = new ArrayList<Tile>();
	
	public Tile(float posX, float posY, float width, float height) {
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		
		collider = new RectangleCollider(width, height);
	}

	@Override
	public void drawGraphic() {
		glBegin(GL_QUADS);
			//glPushMatrix();

				//glTranslatef(-offsetX, -offsetY, 0);
				glColor4f(.8f,.8f,.8f,0);
				glVertex2f(posX - width/2 - offsetX, posY + height/2 - offsetY);
				glVertex2f(posX + width/2 - offsetX, posY + height/2 - offsetY);
				glVertex2f(posX + width/2 - offsetX, posY - height/2 - offsetY);
				glVertex2f(posX - width/2 - offsetX, posY - height/2 - offsetY);
				
			//glPopMatrix();
		glEnd();
	}

	@Override
	public Collider getCollider() {
		// TODO Auto-generated method stub
		return collider;
	}
	
	public void addWall(Wall wall) {
		walls.add(wall);
	}
	
	public ArrayList<Wall> getWalls(){
		return walls;
	}
	
	public void addNeighbor(Tile neighbor) {
		neighbors.add(neighbor);
	}
	
	public ArrayList<Tile> getNeighbors(){
		return neighbors;
	}
}