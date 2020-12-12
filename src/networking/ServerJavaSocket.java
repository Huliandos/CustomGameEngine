package networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerJavaSocket {
	static int port;
	
	public static void main(String[] args) {
		/*
		try {
			OneClientConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		if (args.length < 1) {
	        port = 4444;	//default port
        } else {
        	port = Integer.parseInt(args[0]);
        }
		
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			
			ServerInputHandler serverInputHandler = new ServerInputHandler();
			
			while(true) {
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
	}
}
