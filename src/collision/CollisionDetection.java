package collision;

import gameObjects.GameObject;
import gameObjects.Wall;

public class CollisionDetection {
	
	public static boolean scanForCollision(GameObject go1, GameObject go2) {
		return go1.getCollider().isCollding(go1.getXPosition(), go1.getYPosition(), go2.getCollider(), go2.getXPosition(), go2.getYPosition());
	}
	
	public static boolean scanForCollision(GameObject go1, GameObject go2, float offsetX, float offsetY) {
		return go1.getCollider().isCollding(go1.getXPosition() + offsetX, go1.getYPosition() + offsetY, go2.getCollider(), go2.getXPosition(), go2.getYPosition());
	}
	
	public static boolean scanForCollision(GameObject go, Collider col, float colPosX, float colPosY) {
		return go.getCollider().isCollding(go.getXPosition(), go.getYPosition(), col, colPosX, colPosY);
	}
	
	public static boolean scanForCollision(GameObject go, float offsetX, float offsetY, Collider col, float colPosX, float colPosY) {
		return go.getCollider().isCollding(go.getXPosition() + offsetX, go.getYPosition() + offsetY, col, colPosX, colPosY);
	}
	
	//Idea: This can also be used to detect line of sight for the zombies
	public static boolean scanForDynamicCollision(GameObject go1, GameObject go2, float movementVectorGo1X, float movementVectorGo1Y) {	//collision with line
		if(go1.getCollider().isCollding(go1.getXPosition(), go1.getYPosition(), go2.getCollider(), go2.getXPosition(), go2.getYPosition())) return true;
		else {	//this else only works, if go2 is a rectangle collider. Other collision cases with lines aren't yet needed for the engine
			float posX = go1.getXPosition();
			float posY = go1.getYPosition();
			float lastPosX = go1.getXPosition()-movementVectorGo1X;
			float lastPosY = go1.getYPosition()-movementVectorGo1Y;
			
			// check if the line has hit any of the rectangle's sides
			// uses the Line/Line function below
			boolean left = lineLine(lastPosX, lastPosY, posX, posY, 
					go2.getXPosition() - ((Wall)go2).getWidth()/2, go2.getYPosition() + ((Wall)go2).getHeight()/2, 
					go2.getXPosition() - ((Wall)go2).getWidth()/2, go2.getYPosition() - ((Wall)go2).getHeight()/2);	//top left corner of rect to bottom left corner
			boolean bottom = lineLine(lastPosX, lastPosY, posX, posY, 
					go2.getXPosition() - ((Wall)go2).getWidth()/2, go2.getYPosition() - ((Wall)go2).getHeight()/2, 
					go2.getXPosition() + ((Wall)go2).getWidth()/2, go2.getYPosition() - ((Wall)go2).getHeight()/2);	//bottom left to bottom right corner
			boolean right = lineLine(lastPosX, lastPosY, posX, posY, 
					go2.getXPosition() + ((Wall)go2).getWidth()/2, go2.getYPosition() - ((Wall)go2).getHeight()/2,
					go2.getXPosition() + ((Wall)go2).getWidth()/2, go2.getYPosition() + ((Wall)go2).getHeight()/2);	//bottom right to top right corner
			boolean top = lineLine(lastPosX, lastPosY, posX, posY, 
					go2.getXPosition() + ((Wall)go2).getWidth()/2, go2.getYPosition() + ((Wall)go2).getHeight()/2,
					go2.getXPosition() - ((Wall)go2).getWidth()/2, go2.getYPosition() + ((Wall)go2).getHeight()/2);	//top right to top left
			
			
			// if any of the above are true, the line has hit the rectangle
			if (left || right || top || bottom) {
				return true;
			}
			
		  	return false;
		}
	}
	
	// Line to line collision
	static boolean lineLine(float lineX1, float lineY1, float lineX2, float lineY2, float rectX1, float rectY1, float rectX2, float rectY2) {

	  // calculate the direction of the lines
	  float uA = ((rectX2-rectX1)*(lineY1-rectY1) - (rectY2-rectY1)*(lineX1-rectX1)) / ((rectY2-rectY1)*(lineX2-lineX1) - (rectX2-rectX1)*(lineY2-lineY1));
	  float uB = ((lineX2-lineX1)*(lineY1-rectY1) - (lineY2-lineY1)*(lineX1-rectX1)) / ((rectY2-rectY1)*(lineX2-lineX1) - (rectX2-rectX1)*(lineY2-lineY1));

	  // if uA and uB are between 0-1, lines are colliding
	  if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {
	    
	    return true;
	  }
	  return false;
	}
}
