package gameObjects;

import static org.lwjgl.opengl.GL11.*;

public class Wall extends GameObject{

	
	private float posX1, posX2, posY1, posY2;

	public Wall(float posX1, float posY1, float posX2, float posY2) {
		this.posX1 = posX1;
		this.posY1 = posY1;
		this.posX2 = posX2;
		this.posY2 = posY2;
	}

	@Override
	public void drawGraphic() {
		glBegin(GL_LINE);
			//glPushMatrix();
	
				//glTranslatef(-offsetX, -offsetY, 0);
				glColor4f(.76f,.4f,0,0);
				glLineWidth(3f);
				glVertex2f(posX1 - offsetX, posY1 + offsetY);
				glVertex2f(posX2 + offsetX, posY2 - offsetY);
				
			//glPopMatrix();
		glEnd();
	}
}