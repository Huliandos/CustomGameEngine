package graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public class MainTest {
	static int width = 640;
	static int height = 480;
	
	static float x;
	
	public static void main(String[] args) {
		if(!glfwInit()) throw new IllegalStateException("failed to initialize glfw");
		
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		long window = glfwCreateWindow(width, height, "My LWJGL Test", 0, 0);	//if 3rd variable == glfwGetPrimaryMonitor() then the window is fullscreen
		if(window == 0) throw new IllegalStateException("failed to create window");
		
		GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
		
		glfwShowWindow(window);
		
		glfwMakeContextCurrent(window);
		
		GL.createCapabilities();
		
		//glClearColor(COLOR);	--> used to set cleared window color
		
		double frame_cap = 1/60d;	//1 frame per 60 seconds
		
		double frame_time = 0;
		int frames = 0;
		
		double time = getTime();
		double unprocessed = 0;		//time where game hasn't been processed yet
		
		
		while(!glfwWindowShouldClose(window)) {
			boolean screenUpdated = false;
			
			double time_2 = getTime();
			double passed = time_2 - time;
			unprocessed += passed;
			frame_time += passed;
			
			time = time_2;
			
			while(unprocessed >= frame_cap) {	//put everything in here that isn't rendering
				unprocessed -= frame_cap;
				screenUpdated = true;
				
				manageInputs(window);
				
				glfwPollEvents();
				if(frame_time >= 1) {
					frame_time = 0;
					System.out.println("FPS: " + frames);
					frames = 0;
				}
			}
			
			if(screenUpdated) {
				glClear(GL_COLOR_BUFFER_BIT);
				
				drawColorfulQuad();
				
				glfwSwapBuffers(window);
				frames++;
			}
		}
		
		glfwTerminate();
	}
	
	static void drawColorfulQuad() {
		glBegin(GL_QUADS);
			glColor4f(1,0,0,0);
			glVertex2f(-.5f+x, .5f);
			
			glColor4f(0,1,0,0);
			glVertex2f(.5f+x, .5f);
			
			glColor4f(0,0,1,0);
			glVertex2f(.5f+x, -.5f);
			
			glColor4f(1,1,1,0);
			glVertex2f(-.5f+x, -.5f);
		glEnd();
	}
	
	static void manageInputs(long window) {
		if(glfwGetMouseButton(window, 0) == GL_TRUE)	//0 = left click, 1 = right click, 2 = middle mouse button
			System.out.println("click");
		
		if(glfwGetKey(window, GLFW_KEY_A) == GL_TRUE) {
			x-=.001f;
		}
		

		if(glfwGetKey(window, GLFW_KEY_ESCAPE) == GL_TRUE) {
			glfwSetWindowShouldClose(window, true);
		}
	}
	
	static double getTime() {
		return (double)System.nanoTime() / (double)1000000000L;	// number is 1 billion --> casts nano time to seconds
	}
}
