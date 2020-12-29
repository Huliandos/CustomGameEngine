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
		glBegin(GL_QUADS);
		//glPushMatrix();

			//glTranslatef(-offsetX, -offsetY, 0);

			if(vertical) {
				glColor4f(.76f,.4f,0,0);
				glVertex2f(posX - width/2 - offsetX, posY + height/2 - offsetY);
				glVertex2f(posX + width/2 - offsetX, posY + height/2 - offsetY);
				glVertex2f(posX + width/2 - offsetX, posY - height/2 - offsetY);
				glVertex2f(posX - width/2 - offsetX, posY - height/2 - offsetY);
			}
			else {
				//System.out.println("Rotating Wall");
				//glRotatef(90, 90f, 90f, 1f);

				glColor4f(.76f,.4f,0,0);
				glVertex2f(posX - height/2 - offsetX, posY + width/2 - offsetY);
				glVertex2f(posX + height/2 - offsetX, posY + width/2 - offsetY);
				glVertex2f(posX + height/2 - offsetX, posY - width/2 - offsetY);
				glVertex2f(posX - height/2 - offsetX, posY - width/2 - offsetY);
			}
			
		//glPopMatrix();
		glEnd();
	}
}