package gameObjects;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import collision.CircleCollider;
import collision.Collider;

public class Zombie extends GameObject {

	//unique ID to identify zombie over network for its destruction
	int id;
	
	float movementSpeed = .005f;
	float zombieSize = .1f;
	
	int numOfCirclePoints = 45;
	
	int viewDirection = 0; //0 = top, 1 = top-right, 2 = right, 3 = bottom-right, 4 = bottom, 5 = bottom-left, 6 = left, 7 = top-left
	
	Collider collider;
	
	public Zombie(int id, float posX, float posY) {
		this.id = id;
		this.posX = posX;
		this.posY = posY;
		
		float diagonalZombieSize = (float) Math.sqrt(Math.pow(zombieSize/2, 2) + Math.pow(zombieSize/2, 2));
		collider = new CircleCollider(diagonalZombieSize);
	}

	//The Player is always centered. His position gets added as offset to all other GameObjects
	@Override
	public void drawGraphic() {
		glPushMatrix();

			glTranslatef(-offsetX, -offsetY, 0);	//offset to main player
			glTranslatef(posX, posY, 0);
			glRotatef (angle, 0, 0, 1);
			glColor4f(.25f,.5f,.2f,0);
			
			
			//draws a circle for zombie body
		    for (int i = 0; i < numOfCirclePoints; i++)   {
		        float theta = 360f / (float)numOfCirclePoints * i ;	//get the current angle 
		        
				glPushMatrix();

					glRotatef (theta, 0, 0, 1);
					
					glBegin(GL_QUADS);
						glVertex2f(-zombieSize/2, -zombieSize/2);
						glVertex2f(-zombieSize/2, zombieSize/2);
						glVertex2f(zombieSize/2, zombieSize/2);
						glVertex2f(zombieSize/2, -zombieSize/2);
				    glEnd();
	
				glPopMatrix();
		    }
		    
		    //draws player arms to show firing direction
			glPushMatrix();
				
				float offsetForArms = (float)Math.sqrt(Math.pow(zombieSize/2, 2) + Math.pow(zombieSize/2, 2));
				glTranslatef(offsetForArms, 0, 0);	//offset from body
		
				//glColor4f(0,0,0,0);
			    glBegin(GL_QUADS);
					glVertex2f(-.02f, 0f);
					glVertex2f(0f, 0f);
					glVertex2f(0, .12f);
					glVertex2f(-.02f, .12f);
				glEnd();
			
			glPopMatrix();
			
			glPushMatrix();
			
				glTranslatef(-offsetForArms, 0, 0);	//offset from body
		
				//glColor4f(0,0,0,0);
			    glBegin(GL_QUADS);
					glVertex2f(.02f, 0f);
					glVertex2f(0f, 0f);
					glVertex2f(0, .12f);
					glVertex2f(.02f, .12f);
				glEnd();
		
			glPopMatrix();
		
		glPopMatrix();
	}
	
	public float getMovementSpeed() {
		return movementSpeed;
	}
	
	public void setViewDirection(int dir) {
		viewDirection = dir;
	} 
	
	public int getViewDirection() {
		return viewDirection;
	}

	@Override
	public Collider getCollider() {
		return collider;
	} 
	
	public int getID() {
		return id;
	}
}
