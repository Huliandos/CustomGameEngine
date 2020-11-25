package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class UserThread implements Runnable {
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	ServerChatHandler scHandler;
	
	public UserThread(Socket socket, ServerChatHandler scHandler, int numOfUsers){
		try {
			this.socket = socket;
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			this.scHandler = scHandler;
			
			//Initiate conversation with client
		    out.println("Connected to Server with " + numOfUsers + " users");
		    out.println("Enter a username:");
		    out.println("Welcome " + in.readLine() + "!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String inputLine;
		
		try {
			while ((inputLine = in.readLine()) != null) {
				if(inputLine == "bye")	break;
			    //send message to distributor
			    scHandler.broadcastMessage(inputLine, this);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		scHandler.removeUser(this);
        try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        scHandler.broadcastMessage("User has quit the chat", this);	//ToDo fetch user name and display user that has left the chat
	}
	
	void sendMessage(String message) {
		out.println(message);
	}
}
