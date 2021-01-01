package collision;

public abstract class Collider {
	
	//collision of two axis aligned rectangles
	/*
	public static boolean isCollding(RectangleCollider col1, float col1PosX, float col1PosY, RectangleCollider col2, float col2PosX, float col2PosY) {
		if (col1PosX < col2PosX + col2.width &&
			col1PosX + col1.width > col2PosX &&
			col1PosY < col2PosY + col2.height &&
			col1PosY + col1.height > col2PosY) {
			return true;
		}
		
		return false;
	}
	
	//collision of two circles
	public static boolean isCollding(CircleCollider col1, float col1PosX, float col1PosY, CircleCollider col2, float col2PosX, float col2PosY) {
		var dx = col1PosX - col2PosX;
		var dy = col1PosY - col2PosY;
		var distance = Math.sqrt(dx * dx + dy * dy);
		
		if (distance < col1.radius + col2.radius) {
		    // collision detected!
			return true;
		}
		
		return false;
	}

	//collision of circle with rectangle
	public static boolean isCollding(CircleCollider col1, float col1PosX, float col1PosY, RectangleCollider col2, float col2PosX, float col2PosY) {
		// Find the closest point to the circle within the rectangle
		//circle.X, rectangle.Left, rectangle.Right
		//circle.Y, rectangle.Bottom, rectangle.Top
		float closestX = clamp(col1PosX, col2PosX - col2.width/2, col2PosX + col2.width/2);
		float closestY = clamp(col1PosY, col2PosY - col2.height/2, col2PosY + col2.height/2);

		// Calculate the distance between the circle's center and this closest point
		//circle.X - closestX
		//circle.Y - closestY
		float distanceX = col1PosX - closestX;
		float distanceY = col1PosY - closestY;

		// If the distance is less than the circle's radius, an intersection occurs
		float distanceSquared = (distanceX * distanceX) + (distanceY * distanceY);
		return distanceSquared < (col1.radius * col1.radius);
	}

	//collision of rectangle with circle
	public static boolean isCollding(RectangleCollider col1, float col1PosX, float col1PosY, CircleCollider col2, float col2PosX, float col2PosY) {
		// Find the closest point to the circle within the rectangle
		//circle.X, rectangle.Left, rectangle.Right
		//circle.Y, rectangle.Bottom, rectangle.Top
		float closestX = clamp(col2PosX, col1PosX - col1.width/2, col1PosX + col1.width/2);
		float closestY = clamp(col2PosY, col1PosY - col1.height/2, col1PosY + col1.height/2);

		// Calculate the distance between the circle's center and this closest point
		//circle.X - closestX
		//circle.Y - closestY
		float distanceX = col2PosX - closestX;
		float distanceY = col2PosY - closestY;

		// If the distance is less than the circle's radius, an intersection occurs
		float distanceSquared = (distanceX * distanceX) + (distanceY * distanceY);
		return distanceSquared < (col2.radius * col2.radius);
	}
	
	
	}
	 */

	protected abstract boolean isCollding(float xPosition, float yPosition, Collider collider2, float xPosition2, float yPosition2);
	
	float clamp(float value, float min, float max) {
		if(value < min)	return min;
		else if(value > max) return max;
		else return value;
	}
}
