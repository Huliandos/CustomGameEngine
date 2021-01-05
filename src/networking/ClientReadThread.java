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
                //ToDo: differentiate between different message types
                //1: player input
                //2: player and bullet collision
                
                String[] response = in.readLine().split(":");
                int commandCode = Integer.valueOf(response[0]);
                if(commandCode == 0) {	//input command
	                String[] input = response[1].split(" ");
	
	                //extract data out of send string
	                int playerNum = Integer.valueOf(input[0]);
	                int inputCode = Integer.valueOf(input[1]);
	                
	                if(MainThread.getMoveComp() != null) {
	            		MainThread.getMoveComp().setPlayerInput(playerNum, inputCode);
	            	}
                }
                else if(commandCode == 1) {	//player position
                	System.out.println(response[1]);
	                String[] playerPositions = response[1].split(",");

	                int[] playerNums = new int[playerPositions.length];
	                int[] tilePos = new int[playerPositions.length];
	                
	                for (int i=0; i<playerPositions.length; i++) {
	                	String[] code = playerPositions[i].split("-");
	                	
	                	playerNums[i] = Integer.valueOf(code[0]);
	                	tilePos[i] = Integer.valueOf(code[1]);
	                }
	                
	                MainThread.storePlayerPositions(playerNums, tilePos);
                }
                else if(commandCode == 2) {	//player death
                	int playerNum = Integer.valueOf(response[1]);
                	
                	MainThread.killPlayer(playerNum);
                }
                else if(commandCode == 3) {	//zombie movement
                	
                }
                else if(commandCode == 4) { //zombie death
                	
                }
            }
        } catch (IOException ex) {
            System.out.println("Error reading from server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
