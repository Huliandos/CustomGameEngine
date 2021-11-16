package gameObjects;

import static org.lwjgl.opengl.GL11.*;

import collision.CircleCollider;
import collision.Collider;

public class Player extends GameObject{
	
	float movementSpeed = .005f;
	float playerSize = .1f;
	
	int numOfCirclePoints = 90;
	
	int playerNum;	
	boolean localPlayer;
	
	int viewDirection = 0; //0 = top, 1 = top-right, 2 = right, 3 = bottom-right, 4 = bottom, 5 = bottom-left, 6 = left, 7 = top-left
	
	float r, g, b;
	
	boolean dead;
	
	Collider collider;
	
	public Player(float posX, float posY, int playerNum, boolean localPlayer) {
		this.posX = posX;
		this.posY = posY;
		this.localPlayer = localPlayer;
		
		this.playerNum = playerNum;
		
		if(playerNum == 0) {
			r = 0;
			g = .8f;
			b = 0;
		}
		else if(playerNum == 1) {
			r = .8f;
			g = 0;
			b = 0;
		}
		else if(playerNum == 2) {
			r = 0;
			g = 0;
			b = .8f;
		}
		else if(playerNum == 3) {
			r = .9f;
			g = .9f;
			b = 0;
		}
		else if(playerNum == 4) {
			r = .6f;
			g = 0;
			b = .6f;
		}
		else if(playerNum == 5) {
			r = 0;
			g = .8f;
			b = .8f;
		}
		
		
		float diagonalPlayerSize = (float) Math.sqrt(Math.pow(playerSize/2, 2) + Math.pow(playerSize/2, 2));
		collider = new CircleCollider(diagonalPlayerSize);
	}

	//The local Player is always centered. His position gets added as offset to all other GameObjects
	@Override
	public void drawGraphic() {
		if(!localPlayer && dead) {
			//do nothing
		}
		else {
			glPushMatrix();
	
				glTranslatef(-offsetX, -offsetY, 0);	//offset to main player
				if(!localPlayer)
					glTranslatef(posX, posY, 0);	//move networked players to their position in the world
				
				glRotatef (angle, 0, 0, 1);
				
				if(dead) glColor4f(.9f,.9f,.9f,0);	//if local player is dead color him white
				else glColor4f(r,g,b,0);
				
				
				//draws a circle for player body
			    for (int i = 0; i < numOfCirclePoints; i++)   {
			        float theta = 360f / (float)numOfCirclePoints * i ;	//get the current angle 
			        
					glPushMatrix();
	
						glRotatef (theta, 0, 0, 1);
						
						glBegin(GL_QUADS);
							glVertex2f(-playerSize/2, -playerSize/2);
							glVertex2f(-playerSize/2, playerSize/2);
							glVertex2f(playerSize/2, playerSize/2);
							glVertex2f(playerSize/2, -playerSize/2);
					    glEnd();
		
					glPopMatrix();
			    }
		    
			    //draws player arms to show firing direction
				glPushMatrix();
				
					glTranslatef((float)Math.sqrt(Math.pow(playerSize/2, 2) + Math.pow(playerSize/2, 2)), 0, 0);	//offset from body
			
					//glColor4f(0,0,0,0);
				    glBegin(GL_QUADS);
						glVertex2f(-.02f, 0f);
						glVertex2f(0f, 0f);
						glVertex2f(0, .12f);
						glVertex2f(-.02f, .12f);
					glEnd();
				
				glPopMatrix();
		    
			glPopMatrix();
		}
	}
	
	public float getMovementSpeed() {
		return movementSpeed;
	}
	
	public boolean getLocalPlayer() {
		return localPlayer;
	}
	
	public int getPlayerNum() {
		return playerNum;
	}
	
	//for powerups maybe
	public void modifyMovementSpeed(float modifier) {
		movementSpeed *= modifier;
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
	
	public void setDead(boolean dead) {
		this.dead = dead;
		collider = null;	//remove the players collider
	}
	
	public boolean getDead() {
		return dead;
	}
	
	public float getRadius() {
		return (float) Math.sqrt(Math.pow(playerSize/2, 2) + Math.pow(playerSize/2, 2));
	}
}
