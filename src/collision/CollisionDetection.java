package collision;

import gameObjects.GameObject;
import gameObjects.Tile;
import gameObjects.Wall;
import gameObjects.Zombie;
import gameObjects.Player;

public class CollisionDetection {
	
	//point collision
	public static boolean scanForCollision(GameObject go1, float posX, float posY) {
		if(go1.getCollider().getClass() == CircleCollider.class) {
			if(go1.getClass() == Zombie.class) 	return Math.pow((posX - go1.getXPosition()), 2) + Math.pow((posY - go1.getYPosition()), 2) < Math.pow(((Zombie)go1).getRadius(), 2);
			else 								return Math.pow((posX - go1.getXPosition()), 2) + Math.pow((posY - go1.getYPosition()), 2) < Math.pow(((Player)go1).getRadius(), 2);
		} 
		else if(go1.getCollider().getClass() == RectangleCollider.class) {
			if(go1.getClass() == Wall.class) 	return Math.pow((posX - go1.getXPosition()), 2) + Math.pow((posY - go1.getYPosition()), 2) < Math.pow(((Wall)go1).getWidth(), 2) &&
					Math.pow((posX - go1.getXPosition()), 2) + Math.pow((posY - go1.getYPosition()), 2) < Math.pow(((Wall)go1).getHeight(), 2);
			else 								return Math.pow((posX - go1.getXPosition()), 2) + Math.pow((posY - go1.getYPosition()), 2) < Math.pow(((Tile)go1).getWidth(), 2) &&
					Math.pow((posX - go1.getXPosition()), 2) + Math.pow((posY - go1.getYPosition()), 2) < Math.pow(((Tile)go1).getHeight(), 2);
		}
		
		return false;
	}
	
	public static boolean scanForCollision(GameObject go1, GameObject go2) {
		return go1.getCollider().isCollding(go1.getXPosition(), go1.getYPosition(), go2.getCollider(), go2.getXPosition(), go2.getYPosition());
	}
	
	public static boolean isInTriggerRange(GameObject go1, GameObject go2, float triggerRange) {
		return go1.getCollider().isInTriggerRange(go1.getXPosition(), go1.getYPosition(), go2.getCollider(), go2.getXPosition(), go2.getYPosition(), triggerRange);
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
	
	public static boolean scanForDynamicCollision(GameObject go1, GameObject go2, float movementVectorGo1X, float movementVectorGo1Y) {	//collision with line
		if(go1.getCollider().isCollding(go1.getXPosition(), go1.getYPosition(), go2.getCollider(), go2.getXPosition(), go2.getYPosition())) return true;
		else {	//this else only works, if go2 is a rectangle collider. Other collision cases with lines aren't yet needed for the engine
			float posX = go1.getXPosition();
			float posY = go1.getYPosition();
			float lastPosX = go1.getXPosition()-movementVectorGo1X;
			float lastPosY = go1.getYPosition()-movementVectorGo1Y;
			
			// check if the line has hit any of the rectangle's sides
			// uses the Line/Line function below
			
			return lineLine(lastPosX, lastPosY, posX, posY, go2.getXPosition(), go2.getYPosition(), ((Wall)go2).getWidth(), ((Wall)go2).getHeight());
		}
	}
	
	public static boolean inLineOfSight(float x1, float y1, float x2, float y2, GameObject go) {	//collision with line
		float posX = 0, posY = 0, width = 0, height = 0;
		
		if(go.getClass() == Wall.class) {
			posX = ((Wall)go).getXPosition();
			posY = ((Wall)go).getYPosition();
			width = ((Wall)go).getWidth();
			height = ((Wall)go).getHeight();
		}
		else if(go.getClass() == Tile.class) {
			posX = ((Tile)go).getXPosition();
			posY = ((Tile)go).getYPosition();
			width = ((Tile)go).getWidth();
			height = ((Tile)go).getHeight();
		}
		
		return lineLine(x1, y1, x2, y2, posX, posY, width, height);
	}
	
	// Line to line collision
	static boolean lineLine(float lineX1, float lineY1, float lineX2, float lineY2, float posX, float posY, float width, float height) {
		//left line
		float rectX2 = posX - width/2;
		float rectX1 = posX - width/2;
		float rectY1 = posY + height/2;
		float rectY2 = posY - height/2;
		
		// calculate the direction of the lines
		float uA = ((rectX2-rectX1)*(lineY1-rectY1) - (rectY2-rectY1)*(lineX1-rectX1)) / ((rectY2-rectY1)*(lineX2-lineX1) - (rectX2-rectX1)*(lineY2-lineY1));
		float uB = ((lineX2-lineX1)*(lineY1-rectY1) - (lineY2-lineY1)*(lineX1-rectX1)) / ((rectY2-rectY1)*(lineX2-lineX1) - (rectX2-rectX1)*(lineY2-lineY1));
		
		// if uA and uB are between 0-1, lines are colliding
		if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {
			return true;
		}
		
		
		//bottom line
		rectX1 = posX - width/2;
		rectX2 = posX + width/2;
		rectY1 = posY - height/2;
		rectY2 = posY - height/2;
		
		// calculate the direction of the lines
		uA = ((rectX2-rectX1)*(lineY1-rectY1) - (rectY2-rectY1)*(lineX1-rectX1)) / ((rectY2-rectY1)*(lineX2-lineX1) - (rectX2-rectX1)*(lineY2-lineY1));
		uB = ((lineX2-lineX1)*(lineY1-rectY1) - (lineY2-lineY1)*(lineX1-rectX1)) / ((rectY2-rectY1)*(lineX2-lineX1) - (rectX2-rectX1)*(lineY2-lineY1));
		
		// if uA and uB are between 0-1, lines are colliding
		if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {
			return true;
		}
		
		
		//right line
		rectX1 = posX + width/2;
		rectX2 = posX + width/2;
		rectY1 = posY - height/2;
		rectY2 = posY + height/2;
		
		// calculate the direction of the lines
		uA = ((rectX2-rectX1)*(lineY1-rectY1) - (rectY2-rectY1)*(lineX1-rectX1)) / ((rectY2-rectY1)*(lineX2-lineX1) - (rectX2-rectX1)*(lineY2-lineY1));
		uB = ((lineX2-lineX1)*(lineY1-rectY1) - (lineY2-lineY1)*(lineX1-rectX1)) / ((rectY2-rectY1)*(lineX2-lineX1) - (rectX2-rectX1)*(lineY2-lineY1));
		
		// if uA and uB are between 0-1, lines are colliding
		if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {
			return true;
		}
		
		
		//top line
		rectX1 = posX + width/2;
		rectX2 = posX - width/2;
		rectY1 = posY + height/2;
		rectY2 = posY + height/2;
		
		// calculate the direction of the lines
		uA = ((rectX2-rectX1)*(lineY1-rectY1) - (rectY2-rectY1)*(lineX1-rectX1)) / ((rectY2-rectY1)*(lineX2-lineX1) - (rectX2-rectX1)*(lineY2-lineY1));
		uB = ((lineX2-lineX1)*(lineY1-rectY1) - (lineY2-lineY1)*(lineX1-rectX1)) / ((rectY2-rectY1)*(lineX2-lineX1) - (rectX2-rectX1)*(lineY2-lineY1));
		
		// if uA and uB are between 0-1, lines are colliding
		if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {
			return true;
		}
		
		return false;
	}
}
