package gameObjects;

import static org.lwjgl.opengl.GL11.*;

public class Wall extends GameObject{

<<<<<<< HEAD
	
	private float posX1, posX2, posY1, posY2;

	public Wall(float posX1, float posY1, float posX2, float posY2) {
		this.posX1 = posX1;
		this.posY1 = posY1;
		this.posX2 = posX2;
		this.posY2 = posY2;
=======
	private float width, height;
	
	public Wall(float posX, float posY, float width, float height) {
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
>>>>>>> master
	}

	@Override
	public void drawGraphic() {
<<<<<<< HEAD
		glBegin(GL_LINE);
			//glPushMatrix();
	
				//glTranslatef(-offsetX, -offsetY, 0);
				glColor4f(.76f,.4f,0,0);
				glLineWidth(3f);
				glVertex2f(posX1 - offsetX, posY1 + offsetY);
				glVertex2f(posX2 + offsetX, posY2 - offsetY);
=======
		glBegin(GL_QUADS);
			//glPushMatrix();
	
				//glTranslatef(-offsetX, -offsetY, 0);
				glColor4f(.8f,.8f,.8f,0);
				glVertex2f(posX - width/2 - offsetX, posY + height/2 - offsetY);
				glVertex2f(posX + width/2 - offsetX, posY + height/2 - offsetY);
				glVertex2f(posX + width/2 - offsetX, posY - height/2 - offsetY);
				glVertex2f(posX - width/2 - offsetX, posY - height/2 - offsetY);
>>>>>>> master
				
			//glPopMatrix();
		glEnd();
	}
}