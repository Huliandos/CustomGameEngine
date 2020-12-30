package networking;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerJavaSocket implements Runnable  {
	int port;
	long window;
	
	Object START_GAME_TOKEN;
	
	public ServerSocket serverSocket;
	
	public ServerJavaSocket(int port, String userName, long window, Object START_GAME_TOKEN) {
		this.port = port;
		this.window = window;
		this.START_GAME_TOKEN = START_GAME_TOKEN;
	}
	
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			
			ServerInputHandler serverInputHandler = new ServerInputHandler(START_GAME_TOKEN);
			
			while(!glfwWindowShouldClose(window)) {
				try {
					Socket socket = serverSocket.accept();
					
					
					serverInputHandler.addSocket(socket);
					
					System.out.println("Client connected to server");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Server Socket finished");
	}
}
