package collision;

public abstract class Collider {
	
	protected abstract boolean isCollding(float xPosition, float yPosition, Collider collider2, float xPosition2, float yPosition2);
	
	protected abstract boolean isInTriggerRange(float xPosition, float yPosition, Collider collider2, float xPosition2, float yPosition2, float triggerRange);
	
	float clamp(float value, float min, float max) {
		if(value < min)	return min;
		else if(value > max) return max;
		else return value;
	}
}
