package gameObjects;

import static org.lwjgl.opengl.GL11.*;

public class Player extends GameObject{
	
	float movementSpeed = .005f;
	float playerSize = .05f;
	
	int numOfCirclePoints = 90;
	
	int playerNum;	//delete it this isn't used later
	boolean localPlayer;
	
	float r, g, b;
	
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
	}

	//The Player is always centered. His position gets added as offset to all other GameObjects
	@Override
	public void drawGraphic() {
		glPushMatrix();
		
			glRotatef (angle, 0, 0, 1);
			glColor4f(r,g,b,0);
			
			
			//draws a circle for player body
		    for (int i = 0; i < numOfCirclePoints; i++)   {
		        float theta = 360f / (float)numOfCirclePoints * i ;	//get the current angle 
		        
				glPushMatrix();
				
					glRotatef (theta, 0, 0, 1);
					
					glBegin(GL_QUADS);
						glVertex2f(-playerSize, -playerSize);
						glVertex2f(-playerSize, playerSize);
						glVertex2f(playerSize, playerSize);
						glVertex2f(playerSize, -playerSize);
				    glEnd();
	
				glPopMatrix();
		    }
		    
		    //draws player arms to show firing direction
			glPushMatrix();
			
				glTranslatef((float)Math.sqrt(Math.pow(playerSize, 2) + Math.pow(playerSize, 2)), 0, 0);
		
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
	
	public float getMovementSpeed() {
		return movementSpeed;
	}
	
	public boolean getLocalPlayer() {
		return localPlayer;
	}
	
	//for powerups maybe
	public void modifyMovementSpeed(float modifier) {
		movementSpeed *= modifier;
	}
}
