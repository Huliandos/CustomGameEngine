package networking;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import graphics.MainThread;

public class ClientReadThread extends Thread {
	private BufferedReader in;
    String userName;
    
    ClientJavaSocket clientJavaSocket;
    ClientWriteThread cwt;
    
    long window;
    
    public ClientReadThread(Socket socket, ClientJavaSocket clientJavaSocket, long window, ClientWriteThread cwt) {
    	this.clientJavaSocket = clientJavaSocket;
    	this.window = window;
    	this.cwt = cwt;
    	
        try {
    	    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 
    public void run() {
        try {
        	//get local player num
            String playerNumString = in.readLine();
            //System.out.println("ClientReadThread: " + Integer.valueOf(playerNumString));
            
            clientJavaSocket.setLocalPlayerNum(Integer.valueOf(playerNumString));
            
        	//get total player num
            String totalPlayerNum = in.readLine();		//BUG: This gives weird output on Client (in two player game 2 0) even though just 2 ist expected.
            //System.out.println("ClientReadThread: " + Integer.valueOf(totalPlayerNum));
            
            clientJavaSocket.setTotalPlayerNum(Integer.valueOf(totalPlayerNum));	//charAt is just a hotfix cause of the bug above. Find a way to properly manage the issue
            
            //get level seed
            String levelSeed = in.readLine();
            //System.out.println("Client Read Thread: " + levelSeed);
            
            MainThread.setLevelSeed(levelSeed);
            
            //at this point the game has started, so start input writing
            cwt.start();
            
            while (!glfwWindowShouldClose(window)) {
                String[] response = in.readLine().split(" ");

                //ToDo: differentiate between different message types
                //1: player input
                //2: player and bullet collision
                
                //extract data out of send string
                int playerNum = Integer.valueOf(response[0]);
                int inputCode = Integer.valueOf(response[1]);

	        	//System.out.println("ClientReadThread: reading input text " + inputCode + " from player " + playerNum);
                //execute the command that this player has send
            	if(MainThread.getMoveComp() != null) {
            		MainThread.getMoveComp().setPlayerInput(playerNum, inputCode);
            	}
                //System.out.println("playerNum: " + response[0] + " inputCode: " + response[1]);
            }
        } catch (IOException ex) {
            System.out.println("Error reading from server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
