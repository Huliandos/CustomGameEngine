package graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;

import java.util.ArrayList;
import java.util.Random;

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
		
		player = new Player(-.8f, -.8f);
		dynamicGameObjects.add(player);
		
		MovingBlock movB = new MovingBlock(0, 0);
		dynamicGameObjects.add(movB);
		movB = new MovingBlock(.3f, .4f);
		dynamicGameObjects.add(movB);
		movB = new MovingBlock(.7f, -.2f);
		dynamicGameObjects.add(movB);
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
		if(glfwGetMouseButton(window, 0) == GL_TRUE)	//0 = left click, 1 = right click, 2 = middle mouse button
			System.out.println("click");
		
		float x = 0, y = 0;
		
		if(glfwGetKey(window, GLFW_KEY_W) == GL_TRUE) {
			y+=.005f;
		}
		if(glfwGetKey(window, GLFW_KEY_A) == GL_TRUE) {
			x-=.005f;
		}
		if(glfwGetKey(window, GLFW_KEY_S) == GL_TRUE) {
			y-=.005f;
		}
		if(glfwGetKey(window, GLFW_KEY_D) == GL_TRUE) {
			x+=.005f;
		}
		
		player.move(x, y);

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
			}
		}
	}
}
