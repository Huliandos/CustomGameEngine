package gameObjects;

import static org.lwjgl.opengl.GL11.*;

public class Player extends GameObject{
	
	float movementSpeed = .005f;
	
	public Player(float posX, float posY) {
		this.posX = posX;
		this.posY = posY;
	}

	//The Player is always centered. His position gets added as offset to all other GameObjects
	@Override
	public void drawGraphic() {
		glBegin(GL_TRIANGLES);
			glColor4f(0,0,1,0);
			glVertex2f(0, 0);
			glVertex2f(-.1f, -.15f);
			glVertex2f(.1f, -.15f);
		glEnd();
		
		glBegin(GL_TRIANGLES);
			glColor4f(.6f,0,1,0);
			glVertex2f(0, .05f);
			glVertex2f(-.1f, -.05f);
			glVertex2f(.1f, -.05f);
		glEnd();
	}
	
	@Override
	public void setOffset(float x, float y) {
		//do nothing
	}
	
	//for powerups maybe
	public void modifyMovementSpeed(float modifier) {
		movementSpeed *= modifier;
	}
	
	public float getMovementSpeed() {
		return movementSpeed;
	}
}
