package networking;

import java.net.Socket;
import java.util.ArrayList;

public class ServerInputHandler {
	//ArrayList<PrintWriter> outputStreams = new ArrayList<PrintWriter>();
		ArrayList<ServerUserThread> users = new ArrayList<ServerUserThread>();
		
		public ServerInputHandler() {
			
		}
		
		public void addSocket(Socket socket) {
			//message other users about new joined client
			for (ServerUserThread user : users) {
				user.sendMessage("User " + socket.getLocalAddress() + " connected to server");
			}

			ServerUserThread user = new ServerUserThread(socket, this, users.size());
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
		
		public void broadcastMessage(String message, ServerUserThread excludeUser) {
			for(ServerUserThread user : users)
				if(user != excludeUser)
					user.sendMessage(message);
		}
}
