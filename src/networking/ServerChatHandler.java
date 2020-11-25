package networking;

import java.net.Socket;
import java.util.ArrayList;

public class ServerChatHandler {
	//ArrayList<PrintWriter> outputStreams = new ArrayList<PrintWriter>();
	ArrayList<UserThread> users = new ArrayList<UserThread>();
	
	public ServerChatHandler() {
		
	}
	
	public void addSocket(Socket socket) {
		//message other users about new joined client
		for (UserThread user : users) {
			user.sendMessage("User " + socket.getLocalAddress() + " connected to server");
		}

		UserThread user = new UserThread(socket, this, users.size());
		//add user to distributor list
		users.add(user);
		
		//initialize new Thread for message Handling
		Thread userThread = new Thread(user);
		userThread.start();
	}
	
	public void removeUser(UserThread user) {
		//ToDo:
		//Remove Socket when intentional or forced disconnect
		//debug message to all other clients
		users.remove(user);
	}
	
	public void broadcastMessage(String message, UserThread excluseUser) {
		for(UserThread user : users)
			if(user != excluseUser)
				user.sendMessage(message);
	}
}
