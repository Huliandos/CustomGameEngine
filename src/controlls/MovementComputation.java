package controlls;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;

import java.util.ArrayList;
import java.util.Random;

import gameObjects.GameObject;
import gameObjects.Player;

public class MovementComputation implements Runnable{
	ArrayList<GameObject> dynamicGameObjects;
	ArrayList<GameObject> staticGameObjects;
	
	Player player;
	float offsetX, offsetY;
	
	long window;
	
	Object TOKEN;
	
	public MovementComputation(ArrayList<GameObject> dynamicGameObjects, ArrayList<GameObject> staticGameObjects, long window, Object TOKEN) {
		this.dynamicGameObjects = dynamicGameObjects;
		this.staticGameObjects = staticGameObjects;
		this.window = window;
		this.TOKEN = TOKEN;
		
		player = (Player) dynamicGameObjects.get(0);
		
		/*
		MovingBlock movB = new MovingBlock(0, 0);
		dynamicGameObjects.add(movB);
		movB = new MovingBlock(.3f, .4f);
		dynamicGameObjects.add(movB);
		movB = new MovingBlock(.7f, -.2f);
		dynamicGameObjects.add(movB);
		*/
	}
	
	@Override
	public void run() {
		while(!glfwWindowShouldClose(window)) {
			manageInputs();	//for player
			
			moveObjects();
			
			
			try {
				synchronized(TOKEN) {
					TOKEN.wait();	//syncs up position change with frame rate
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	void manageInputs() {
		//if(glfwGetMouseButton(window, 0) == GL_TRUE)	//0 = left click, 1 = right click, 2 = middle mouse button
			//System.out.println("click");	//use this to shoot later
		
		float x = 0, y = 0;
		float moveSpeed = player.getMovementSpeed();
		
		//vertical movement
		if(glfwGetKey(window, GLFW_KEY_W) == GL_TRUE) {
			y+=moveSpeed;
		}
		else if(glfwGetKey(window, GLFW_KEY_S) == GL_TRUE) {
			y-=moveSpeed;
		}
		//horizontal movement
		if(glfwGetKey(window, GLFW_KEY_D) == GL_TRUE) {
			x+=moveSpeed;
		}
		else if(glfwGetKey(window, GLFW_KEY_A) == GL_TRUE) {
			x-=moveSpeed;
		}
		
		//adjust diagonal movement, so that diagonal movment isn't speed up
		if(x!=0 && y!=0) {
			float magnitude = (float)Math.sqrt((float)Math.pow(x, 2) + (float)Math.pow(y, 2));
			
			float factor = moveSpeed / magnitude;
			
			x *= factor;
			y *= factor;
		}

		//sprint button pressed
		if(glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GL_TRUE) {
			x *= 2;
			y *= 2;
		}
		
		player.move(x, y);
		setObjectAngle(player, x, y);

		if(glfwGetKey(window, GLFW_KEY_ESCAPE) == GL_TRUE) {
			//moveCompThread.stop();
			glfwSetWindowShouldClose(window, true);
		}
	}

	void moveObjects() {
		for(GameObject go : dynamicGameObjects) {
			if(go==player) {
				offsetX = player.getXPosition();
				offsetY = player.getYPosition();
			}
			else{
				go.setOffset(offsetX, offsetY);
				
				float x = 0, y = 0;
				
				//random Object Jitter
				//ToDo: add actual movement logic for stuff
				Random r = new Random();
				x = -.005f + (.005f - -.005f) * r.nextFloat();
				y = -.005f + (.005f - -.005f) * r.nextFloat();
				
				go.move(x, y);
				setObjectAngle(go, x, y);
			}
		}
		for(GameObject go : staticGameObjects) {
			go.setOffset(offsetX, offsetY);
		}
	}
	
	void setObjectAngle(GameObject go, float x, float y) {
		if(x==0 && y>0) {	//top
			go.setAngle(0);
		}
		if(x<0 && y>0) {	//top left
			go.setAngle(45);
		}
		if(x<0 && y==0) {	//left
			go.setAngle(90);
		}
		if(x<0 && y<0) {	//bottom left
			go.setAngle(135);
		}
		if(x==0 && y<0) {	//bottom
			go.setAngle(180);
		}
		if(x>0 && y<0) {	//bottom right
			go.setAngle(-135);
		}
		if(x>0 && y==0) {	//right
			go.setAngle(-90);
		}
		if(x>0 && y>0) {	//top right
			go.setAngle(-45);
		}
			
	}
}
