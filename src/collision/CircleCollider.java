package collision;

public class CircleCollider extends Collider {

	float radius;
	
	public CircleCollider(float radius) {
		this.radius = radius;
	}
	
	@Override
	protected boolean isCollding(float col1PosX, float col1PosY, Collider col2, float col2PosX, float col2PosY) {
		//collision of two axis aligned rectangles
		if(col2.getClass() == CircleCollider.class) {
			var dx = col1PosX - col2PosX;
			var dy = col1PosY - col2PosY;
			var distance = Math.sqrt(dx * dx + dy * dy);
			
			if (distance < radius + ((CircleCollider)col2).radius) {
			    // collision detected!
				return true;
			}
			
			return false;
		}
		//collision of rectangle with circle
		else if(col2.getClass() == RectangleCollider.class) {
			// Find the closest point to the circle within the rectangle
			//circle.X, rectangle.Left, rectangle.Right
			//circle.Y, rectangle.Bottom, rectangle.Top
			float closestX = clamp(col1PosX, col2PosX - ((RectangleCollider)col2).width/2, col2PosX + ((RectangleCollider)col2).width/2);
			float closestY = clamp(col1PosY, col2PosY - ((RectangleCollider)col2).height/2, col2PosY + ((RectangleCollider)col2).height/2);

			// Calculate the distance between the circle's center and this closest point
			//circle.X - closestX
			//circle.Y - closestY
			float distanceX = col1PosX - closestX;
			float distanceY = col1PosY - closestY;

			// If the distance is less than the circle's radius, an intersection occurs
			float distanceSquared = (distanceX * distanceX) + (distanceY * distanceY);
			return distanceSquared < (radius * radius);
		}
		return false;
	}
}
