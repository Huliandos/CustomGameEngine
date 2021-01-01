package collision;

import gameObjects.GameObject;

public class CollisionDetection {
	
	public static boolean scanForCollision(GameObject go1, GameObject go2) {
		return go1.getCollider().isCollding(go1.getXPosition(), go1.getYPosition(), go2.getCollider(), go2.getXPosition(), go2.getYPosition());
	}
	
	public static boolean scanForCollision(GameObject go1, GameObject go2, float offsetX, float offsetY) {
		return go1.getCollider().isCollding(go1.getXPosition() + offsetX, go1.getYPosition() + offsetY, go2.getCollider(), go2.getXPosition(), go2.getYPosition());
	}
}
