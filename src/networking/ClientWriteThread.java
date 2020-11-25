package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientWriteThread extends Thread{
	PrintWriter out;
	BufferedReader in;
	Socket socket;
 
    public ClientWriteThread(Socket socket) {
    	this.socket = socket;
    	
        try {
        	out = new PrintWriter(socket.getOutputStream(), true);
    		in = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 
    public void run() {
 
        String userName = "default";	
		try {
			userName = in.readLine();	//find a better way to enter username
			out.println(userName);
			
	        JavaSocketClient.setUserName(userName);
	 
	        String text;
	        
	        while((text = in.readLine()) != null) {
	        	if(text.equals("bye")) break;
	        	text = "[" + userName + "]: " + text;
	            out.println(text);
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        try {
            socket.close();
        } catch (IOException ex) {
 
            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
}
