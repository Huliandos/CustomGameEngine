package gameObjects;

public abstract class GameObject {
	float posX, offsetX;
	float posY, offsetY;
	
	float angle = 0f;
	
	public abstract void drawGraphic(); //How to Draw Graphic in here
	
	public void setPosition(float x, float y) {
		posX = x;
		posY = y;
	}

	public void move(float x, float y) {
		posX += x;
		posY += y;
	}
	
	public void setOffset(float x, float y) {
		offsetX = x;
		offsetY = y;
	}
	
	public float getXPosition() {
		return posX;
	}
	
	public float getYPosition() {
		return posY;
	}
	
	public void setAngle(float angle) {
		this.angle = angle;
	}
}
