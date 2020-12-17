package gameObjects;

import static org.lwjgl.opengl.GL11.*;

public class Wall extends GameObject{

	private float width, height;
	
	public Wall(float posX, float posY, float width, float height) {
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
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
}