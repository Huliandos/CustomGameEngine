package collision;

import gameObjects.GameObject;

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
}
