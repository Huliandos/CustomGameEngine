package networking;

import java.net.Socket;
import java.util.ArrayList;

import graphics.MainThread;

public class ServerInputHandler {
	//ArrayList<PrintWriter> outputStreams = new ArrayList<PrintWriter>();
	ArrayList<ServerUserThread> users = new ArrayList<ServerUserThread>();
	Object START_GAME_TOKEN;
	
	public ServerInputHandler(Object START_GAME_TOKEN) {
		if(MainThread.isLocalClientServer()) {
			MainThread.setServerInputHandler(this);
		}
		
		this.START_GAME_TOKEN = START_GAME_TOKEN;
	}
	
	public void addSocket(Socket socket) {
		ServerUserThread user;
		if(users.size() == 0)	user = new ServerUserThread(socket, this, users.size(), START_GAME_TOKEN, true);
		else user = new ServerUserThread(socket, this, users.size(), START_GAME_TOKEN, false);
		//add user to distributor list
		users.add(user);
		
		//initialize new Thread for message Handling
		Thread userThread = new Thread(user);
		userThread.start();
		System.out.println("ServerInputHandler: added user to socket");
	}
	
	public void removeUser(ServerUserThread user) {
		users.remove(user);
	}
	
	public void broadcastInput(String input, ServerUserThread excludeUser) {
		for(ServerUserThread user : users)
			if(user != excludeUser)
				user.sendInputs(input);
	}
	
	int getNumOfUsersConnected() {
		return users.size();
	}
}
