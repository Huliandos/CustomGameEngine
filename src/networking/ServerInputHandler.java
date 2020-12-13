package networking;

import java.net.Socket;
import java.util.ArrayList;

public class ServerInputHandler {
	//ArrayList<PrintWriter> outputStreams = new ArrayList<PrintWriter>();
	ArrayList<ServerUserThread> users = new ArrayList<ServerUserThread>();
	Object START_GAME_TOKEN;
	
	public ServerInputHandler(Object START_GAME_TOKEN) {
		this.START_GAME_TOKEN = START_GAME_TOKEN;
	}
	
	public void addSocket(Socket socket) {

		ServerUserThread user = new ServerUserThread(socket, this, users.size(), START_GAME_TOKEN);
		//add user to distributor list
		users.add(user);
		
		//initialize new Thread for message Handling
		Thread userThread = new Thread(user);
		userThread.start();
	}
	
	public void removeUser(ServerUserThread user) {
		//ToDo:
		//Remove Socket when intentional or forced disconnect
		//debug message to all other clients
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
