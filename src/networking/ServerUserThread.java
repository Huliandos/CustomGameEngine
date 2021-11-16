package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import graphics.MainThread;
import levelBuild.BuildLevel;

public class ServerUserThread implements Runnable {
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	ServerInputHandler serverInputHandler;
	boolean hostThread;
	
	Object START_GAME_TOKEN;
	
	public ServerUserThread(Socket socket, ServerInputHandler serverInputHandler, int numOfUsers, Object START_GAME_TOKEN, boolean hostThread){
		this.START_GAME_TOKEN = START_GAME_TOKEN;
		this.hostThread = hostThread;
		
		try {
			this.socket = socket;
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			this.serverInputHandler = serverInputHandler;
			
			//Initiate conversation with client
		    out.println(numOfUsers);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		if(hostThread) {	//host sends init data to every client
			String levelSeed = BuildLevel.generateLevel(MainThread.getLevelSize());
			try {
				synchronized(START_GAME_TOKEN) {START_GAME_TOKEN.wait();}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
			int numOfUsersConnected = serverInputHandler.getNumOfUsersConnected();
			
			//send num of users over network
			serverInputHandler.broadcastInput(String.valueOf(numOfUsersConnected), this);
			//set num of users on local client
			sendStartGame(String.valueOf(numOfUsersConnected));
			
			//Send level seed
			serverInputHandler.broadcastInput(levelSeed, this);
			sendStartGame(levelSeed);
		}
		//client can start listening instantaneously
		
		String inputLine;
		
		try {
			while ((inputLine = in.readLine()) != null) {
				serverInputHandler.broadcastInput(inputLine, this);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		serverInputHandler.removeUser(this);
        try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//send Inputs every frame
	void sendInputs(String input) {
		out.println(input);
	}
	
	void sendStartGame(String str) {
		out.println(str);
	}
}
