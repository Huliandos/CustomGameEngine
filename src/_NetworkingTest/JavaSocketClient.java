package _NetworkingTest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class JavaSocketClient {
	static int port;
	static String userName;
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		if (args.length < 1) {
	        port = 4444;	//default port
	        userName = "default_User";
        } else if (args.length < 2){
	        userName = "default_User";
        }
		else {
        	port = Integer.parseInt(args[0]);
        	userName = String.valueOf(args[1]);
        }
		
		Socket socket = new Socket(InetAddress.getByName(null), port);
		
		new ClientReadThread(socket).start();
        new ClientWriteThread(socket).start();
        
		//OneClientConnection();
	}
	
	public static void setUserName(String userName) {
		JavaSocketClient.userName = userName;
	}

	public static String getUserName() {
		return userName;
	}
	//Deprecated. Just for reference
	static void OneClientConnection() throws IOException {
		Socket socket = new Socket(InetAddress.getByName(null), port);
		//socket.getOutputStream().write(14);
		
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
	            
	    String fromServer, fromUser;
	    
	    while ((fromServer = in.readLine()) != null) {
	        System.out.println("Server: " + fromServer);

			fromUser = stdIn.readLine();
	        if (fromUser != null) {
	            System.out.println("My Message: " + fromUser);
	            out.println(fromUser);
	        }
	    }
	}
}
