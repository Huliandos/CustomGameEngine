package gameObjects;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

public class MovingBlock extends GameObject{
	float randR, randG, randB;
	
	public MovingBlock(float posX, float posY) {
		this.posX = posX;
		this.posY = posY;
		
		Random r = new Random();
		randR = r.nextFloat();
		randG = r.nextFloat();
		randB = r.nextFloat();
	}
	
	@Override
	public void drawGraphic() {
		glBegin(GL_QUADS);
			glColor4f(randR, randG, randB, 0);
			glVertex2f(-.1f+posX-offsetX, .2f+posY-offsetY);
			glVertex2f(-.1f+posX-offsetX, -.2f+posY-offsetY);
			glVertex2f(.1f+posX-offsetX, -.2f+posY-offsetY);
			glVertex2f(.1f+posX-offsetX, .2f+posY-offsetY);
		glEnd();
	}
}
