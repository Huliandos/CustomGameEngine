package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReadThread extends Thread {
	private BufferedReader in;
    String userName;
    
    ClientJavaSocket clientJavaSocket;
    
    public ClientReadThread(Socket socket, ClientJavaSocket clientJavaSocket) {
    	this.clientJavaSocket = clientJavaSocket;
    	
        try {
    	    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 
    public void run() {
        while (true) {
            try {
                //String response = in.readLine();
                byte[] response = in.readLine().getBytes();
                System.out.println("\n" + response);
                
                // prints the username after displaying the server's message
                if (clientJavaSocket.getUserName() != null) {
                    System.out.print("[" + clientJavaSocket.getUserName() + "]: ");
                }
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
}
