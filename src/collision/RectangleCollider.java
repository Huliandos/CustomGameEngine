package collision;

public class RectangleCollider extends Collider {
	
	float width;
	float height;
	
	public RectangleCollider(float width, float height) {
		this.width = width;
		this.height = height;
	}

	@Override
	protected boolean isCollding(float col1PosX, float col1PosY, Collider col2, float col2PosX, float col2PosY) {
		//collision of two circles
		if(col2.getClass() == RectangleCollider.class) {
			if (col1PosX < col2PosX + ((RectangleCollider)col2).width &&
					col1PosX + width > col2PosX &&
					col1PosY < col2PosY + ((RectangleCollider)col2).height &&
					col1PosY + height > col2PosY) {
					return true;
				}
				
				return false;
		}
		//collision of circle with rectangle
		else if(col2.getClass() == CircleCollider.class) {
			// Find the closest point to the circle within the rectangle
			//circle.X, rectangle.Left, rectangle.Right
			//circle.Y, rectangle.Bottom, rectangle.Top
			float closestX = clamp(col2PosX, col1PosX - width/2, col1PosX + width/2);
			float closestY = clamp(col2PosY, col1PosY - height/2, col1PosY + height/2);

			// Calculate the distance between the circle's center and this closest point
			//circle.X - closestX
			//circle.Y - closestY
			float distanceX = col2PosX - closestX;
			float distanceY = col2PosY - closestY;

			// If the distance is less than the circle's radius, an intersection occurs
			float distanceSquared = (distanceX * distanceX) + (distanceY * distanceY);
			return distanceSquared < (((CircleCollider)col2).radius * ((CircleCollider)col2).radius);
		}
		
		return false;
	}
	
	@Override
	protected boolean isInTriggerRange(float col1PosX, float col1PosY, Collider col2, float col2PosX, float col2PosY, float triggerRange) {
		float width = this.width + triggerRange;
		float height = this.height + triggerRange;
		
		if(col2.getClass() == RectangleCollider.class) {
			if (col1PosX < col2PosX + ((RectangleCollider)col2).width &&
					col1PosX + width > col2PosX &&
					col1PosY < col2PosY + ((RectangleCollider)col2).height &&
					col1PosY + height > col2PosY) {
					return true;
				}
				
				return false;
		}
		//collision of circle with rectangle
		else if(col2.getClass() == CircleCollider.class) {
			// Find the closest point to the circle within the rectangle
			//circle.X, rectangle.Left, rectangle.Right
			//circle.Y, rectangle.Bottom, rectangle.Top
			float closestX = clamp(col2PosX, col1PosX - width/2, col1PosX + width/2);
			float closestY = clamp(col2PosY, col1PosY - height/2, col1PosY + height/2);

			// Calculate the distance between the circle's center and this closest point
			//circle.X - closestX
			//circle.Y - closestY
			float distanceX = col2PosX - closestX;
			float distanceY = col2PosY - closestY;

			// If the distance is less than the circle's radius, an intersection occurs
			float distanceSquared = (distanceX * distanceX) + (distanceY * distanceY);
			return distanceSquared < (((CircleCollider)col2).radius * ((CircleCollider)col2).radius);
		}
		
		return false;
	}
	
}
