package _NetworkingTest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class JavaSocketServer {
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
			
			ServerChatHandler serverChatHandler = new ServerChatHandler();
			
			while(true) {
				try {
					Socket socket = serverSocket.accept();
					
					serverChatHandler.addSocket(socket);
					
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
	
	//Deprecated. Just for reference
	static void OneClientConnection() throws IOException {
		ServerSocket serverSocket = new ServerSocket(4444);

		Socket socket = serverSocket.accept();
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		String inputLine, outputLine;
        
	    // Initiate conversation with client
	    outputLine = "Hello there";
	    out.println(outputLine);

	    while ((inputLine = in.readLine()) != null) {
	        if(inputLine.equals("General Kenobi!")) outputLine = "Bzzzzzz";
	        else if(inputLine.equals("Bye.")) outputLine = "Bye.";
	        else outputLine = "what?";
	        
	        out.println(outputLine);
        }
	}
}
