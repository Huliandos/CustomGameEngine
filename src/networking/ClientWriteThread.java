package networking;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import graphics.MainThread;

public class ClientWriteThread extends Thread{
	PrintWriter out;
	BufferedReader in;
	Socket socket;
	
	ClientJavaSocket clientJavaSocket;
	
	//to end Thread on window closed
	long window;
	
	//to sync up position sending to framerate, so that it matches Input rate
	Object TOKEN;
 
    public ClientWriteThread(Socket socket, ClientJavaSocket clientJavaSocket, long window, Object TOKEN) {
    	this.socket = socket;
    	this.clientJavaSocket = clientJavaSocket;
    	
    	this.window = window;
    	
    	this.TOKEN = TOKEN;
    	
        try {
        	out = new PrintWriter(socket.getOutputStream(), true);
    		in = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 
    public void run() {
    	//System.out.println("Client Write Thread started");
    	
        while(!glfwWindowShouldClose(window)) {
        	if(MainThread.getMoveComp() != null) {
	        	int inputCode = MainThread.getMoveComp().getPlayerInputCode();	//fetch local Input code
	        	
	        	String inputText = String.valueOf(inputCode);	//convert it to a string
	            out.println(clientJavaSocket.getLocalPlayerNum() + " " + inputText);	//write it into the output. Text equals: "NumberOfPlayer_Sending_the_command command_to_execute"
            }
        	
        	try {
				synchronized(TOKEN) {
					TOKEN.wait();	//syncs up position change with frame rate
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		
        try {
            socket.close();
        } catch (IOException ex) {
 
            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
}
