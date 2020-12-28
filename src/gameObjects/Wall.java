package gameObjects;

import static org.lwjgl.opengl.GL11.*;

public class Wall extends GameObject{

	
	float width =.025f;
	float height = .5f;
	boolean vertical;

	public Wall(float posX, float posY, boolean vertical) {
		this.posX = posX;
		this.posY = posY;
		this.vertical = vertical;
	}

	@Override
	public void drawGraphic() {
		if(!vertical) {
			glRotatef(90, 0f, 0f, 1f);
		}
		glBegin(GL_QUADS);
		//glPushMatrix();

			//glTranslatef(-offsetX, -offsetY, 0);
<<<<<<< Updated upstream
			//if(horizontal) glRotatef(90, 0, 0, 1);
=======

>>>>>>> Stashed changes
			glColor4f(.76f,.4f,0,0);
			glVertex2f(posX - width/2 - offsetX, posY + height/2 - offsetY);
			glVertex2f(posX + width/2 - offsetX, posY + height/2 - offsetY);
			glVertex2f(posX + width/2 - offsetX, posY - height/2 - offsetY);
			glVertex2f(posX - width/2 - offsetX, posY - height/2 - offsetY);
			if(horizontal) glRotatef(90, 0, 0, 1);
			
		//glPopMatrix();
		glEnd();
	}
}