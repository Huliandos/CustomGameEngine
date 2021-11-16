package gameObjects;

import static org.lwjgl.opengl.GL11.*;

import collision.CircleCollider;
import collision.Collider;

public class Bullet extends GameObject{
	
	float movementSpeed = .05f;
	float bulletSize = .02f;
	
	int numOfCirclePoints = 45;
	
	int shooterDir; //0 = top, 1 = top-right, 2 = right, 3 = bottom-right, 4 = bottom, 5 = bottom-left, 6 = left, 7 = top-left
	
	int playerNum;
	
	float r, g, b;
	
	Collider collider;
	
	public Bullet(float posX, float posY, int playerNum, int shooterDir) {
		this.posX = posX;
		this.posY = posY;
		
		this.playerNum = playerNum;
		
		if(playerNum == 0) {
			r = 0;
			g = .4f;
			b = 0;
		}
		else if(playerNum == 1) {
			r = .4f;
			g = 0;
			b = 0;
		}
		else if(playerNum == 2) {
			r = 0;
			g = 0;
			b = .4f;
		}
		else if(playerNum == 3) {
			r = .45f;
			g = .45f;
			b = 0;
		}
		else if(playerNum == 4) {
			r = .3f;
			g = 0;
			b = .3f;
		}
		else if(playerNum == 5) {
			r = 0;
			g = .4f;
			b = .4f;
		}
		
		this.shooterDir = shooterDir;

		float diagonalBulletSize = (float) Math.sqrt(Math.pow(bulletSize/2, 2) + Math.pow(bulletSize/2, 2));
		collider = new CircleCollider(diagonalBulletSize);
	}

	@Override
	public void drawGraphic() {
		glPushMatrix();

			glTranslatef(-offsetX, -offsetY, 0);	//offset to main player
			glTranslatef(posX, posY, 0);	//move networked players to their position in the world
			
			glRotatef (angle, 0, 0, 1);
			glColor4f(r,g,b,0);
			
			
			//draws a circle for player body
		    for (int i = 0; i < numOfCirclePoints; i++)   {
		        float theta = 360f / (float)numOfCirclePoints * i ;	//get the current angle 
		        
				glPushMatrix();

					glRotatef (theta, 0, 0, 1);
					
					glBegin(GL_QUADS);
						glVertex2f(-bulletSize/2, -bulletSize/2);
						glVertex2f(-bulletSize/2, bulletSize/2);
						glVertex2f(bulletSize/2, bulletSize/2);
						glVertex2f(bulletSize/2, -bulletSize/2);
				    glEnd();
	
				glPopMatrix();
		    }
	    
		glPopMatrix();
	}
	
	public float getMovementSpeed() {
		return movementSpeed;
	}
	
	public int getBulletPlayerNum() {
		return playerNum;
	}
	
	//for powerups maybe
	public void modifyMovementSpeed(float modifier) {
		movementSpeed *= modifier;
	}
	
	public void move() {
		if(shooterDir == 0) {
			posX += 0;
			posY += movementSpeed;
		}
		else if(shooterDir == 1) {
			//secures that the bullet doesn't speed up diagonally
			float magnitude = (float)Math.sqrt((float)Math.pow(movementSpeed, 2) + (float)Math.pow(movementSpeed, 2));
			float factor = movementSpeed / magnitude;
			
			posX += movementSpeed * factor;
			posY += movementSpeed * factor;
		}
		else if(shooterDir == 2) {
			posX += movementSpeed;
			posY += 0;
		}
		else if(shooterDir == 3) {
			//secures that the bullet doesn't speed up diagonally
			float magnitude = (float)Math.sqrt((float)Math.pow(movementSpeed, 2) + (float)Math.pow(movementSpeed, 2));
			float factor = movementSpeed / magnitude;
			
			posX += movementSpeed * factor;
			posY -= movementSpeed * factor;
		}
		else if(shooterDir == 4) {
			posX += 0;
			posY -= movementSpeed;
		}
		else if(shooterDir == 5) {
			//secures that the bullet doesn't speed up diagonally
			float magnitude = (float)Math.sqrt((float)Math.pow(movementSpeed, 2) + (float)Math.pow(movementSpeed, 2));
			float factor = movementSpeed / magnitude;
			
			posX -= movementSpeed * factor;
			posY -= movementSpeed * factor;
		}
		else if(shooterDir == 6) {
			posX -= movementSpeed;
			posY += 0;
		}
		else if(shooterDir == 7) {
			//secures that the bullet doesn't speed up diagonally
			float magnitude = (float)Math.sqrt((float)Math.pow(movementSpeed, 2) + (float)Math.pow(movementSpeed, 2));
			float factor = movementSpeed / magnitude;
			
			posX -= movementSpeed * factor;
			posY += movementSpeed * factor;
		}
	}
	
	public float getMovementVectorX() {
		if(shooterDir == 0) {
			return 0;
		}
		else if(shooterDir == 1) {
			//secures that the bullet doesn't speed up diagonally
			float magnitude = (float)Math.sqrt((float)Math.pow(movementSpeed, 2) + (float)Math.pow(movementSpeed, 2));
			float factor = movementSpeed / magnitude;
			
			return movementSpeed * factor;
		}
		else if(shooterDir == 2) {
			return movementSpeed;
		}
		else if(shooterDir == 3) {
			//secures that the bullet doesn't speed up diagonally
			float magnitude = (float)Math.sqrt((float)Math.pow(movementSpeed, 2) + (float)Math.pow(movementSpeed, 2));
			float factor = movementSpeed / magnitude;
			
			return movementSpeed * factor;
		}
		else if(shooterDir == 4) {
			return 0;
		}
		else if(shooterDir == 5) {
			//secures that the bullet doesn't speed up diagonally
			float magnitude = (float)Math.sqrt((float)Math.pow(movementSpeed, 2) + (float)Math.pow(movementSpeed, 2));
			float factor = movementSpeed / magnitude;
			
			return -movementSpeed * factor;
		}
		else if(shooterDir == 6) {
			return -movementSpeed;
		}
		else if(shooterDir == 7) {
			//secures that the bullet doesn't speed up diagonally
			float magnitude = (float)Math.sqrt((float)Math.pow(movementSpeed, 2) + (float)Math.pow(movementSpeed, 2));
			float factor = movementSpeed / magnitude;
			
			return -movementSpeed * factor;
		}
		return 0;
	}
	
	public float getMovementVectorY() {
		if(shooterDir == 0) {
			return movementSpeed;
		}
		else if(shooterDir == 1) {
			//secures that the bullet doesn't speed up diagonally
			float magnitude = (float)Math.sqrt((float)Math.pow(movementSpeed, 2) + (float)Math.pow(movementSpeed, 2));
			float factor = movementSpeed / magnitude;
			
			return movementSpeed * factor;
		}
		else if(shooterDir == 2) {
			return 0;
		}
		else if(shooterDir == 3) {
			//secures that the bullet doesn't speed up diagonally
			float magnitude = (float)Math.sqrt((float)Math.pow(movementSpeed, 2) + (float)Math.pow(movementSpeed, 2));
			float factor = movementSpeed / magnitude;
			
			return -movementSpeed * factor;
		}
		else if(shooterDir == 4) {
			return -movementSpeed;
		}
		else if(shooterDir == 5) {
			//secures that the bullet doesn't speed up diagonally
			float magnitude = (float)Math.sqrt((float)Math.pow(movementSpeed, 2) + (float)Math.pow(movementSpeed, 2));
			float factor = movementSpeed / magnitude;
			
			return -movementSpeed * factor;
		}
		else if(shooterDir == 6) {
			return 0;
		}
		else if(shooterDir == 7) {
			//secures that the bullet doesn't speed up diagonally
			float magnitude = (float)Math.sqrt((float)Math.pow(movementSpeed, 2) + (float)Math.pow(movementSpeed, 2));
			float factor = movementSpeed / magnitude;
			
			return movementSpeed * factor;
		}
		return 0;
	}
	
	@Override
	public Collider getCollider() {
		return collider;
	} 
}
