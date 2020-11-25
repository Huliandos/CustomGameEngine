package graphics;

import static org.lwjgl.opengl.GL11.*;

public class PlayerTwo extends GameObject {
	
	public PlayerTwo(float posX, float posY) {
		this.posX = posX;
		this.posY = posY;
	}
	
	@Override
	public void drawGraphic() {
		glBegin(GL_TRIANGLES);
			glColor4f(1,0,0,0);
			glVertex2f(0+posX-offsetX, 0+posY-offsetY);
			glVertex2f(-.1f+posX-offsetX, -.15f+posY-offsetY);
			glVertex2f(.1f+posX-offsetX, -.15f+posY-offsetY);
		glEnd();
		
		glBegin(GL_TRIANGLES);
			glColor4f(0,1,0,0);
			glVertex2f(0+posX-offsetX, .05f+posY-offsetY);
			glVertex2f(-.1f+posX-offsetX, -.05f+posY-offsetY);
			glVertex2f(.1f+posX-offsetX, -.05f+posY-offsetY);
		glEnd();
	}
}
