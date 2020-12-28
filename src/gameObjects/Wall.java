package gameObjects;

import static org.lwjgl.opengl.GL11.*;

public class Wall extends GameObject{

	
	private float posX1, posX2, posY1, posY2;
	float width =.025f;
	float height = .5f;
	boolean horizontal;

	public Wall(float posX, float posY, boolean horizontal) {
		this.posX = posX;
		this.posY = posY;
		this.horizontal = horizontal;
	}

	@Override
	public void drawGraphic() {
		/*
		glBegin(GL_LINE);
			//glPushMatrix();
	
				//glTranslatef(-offsetX, -offsetY, 0);
				glColor4f(.76f,.4f,0,0);
				glLineWidth(3f);
				glVertex2f(posX1 , posY1 );
				glVertex2f(posX2 , posY2 );
				
			//glPopMatrix();
		glEnd();
		*/
		glBegin(GL_QUADS);
		glPushMatrix();

			//glTranslatef(-offsetX, -offsetY, 0);
			//if(horizontal) glRotatef(90, 0, 0, 1);
			glColor4f(.76f,.4f,0,0);
			glVertex2f(posX - width/2 - offsetX, posY + height/2 - offsetY);
			glVertex2f(posX + width/2 - offsetX, posY + height/2 - offsetY);
			glVertex2f(posX + width/2 - offsetX, posY - height/2 - offsetY);
			glVertex2f(posX - width/2 - offsetX, posY - height/2 - offsetY);
			if(horizontal) glRotatef(90, 0, 0, 1);
			
		glPopMatrix();
		glEnd();
	}
}