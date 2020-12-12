package networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientJavaSocket {
	int port;
	String userName;
	
	public ClientJavaSocket(int port, String userName) throws UnknownHostException, IOException {
		this.port = port;
		this.userName = userName;
		
		Socket socket = new Socket(InetAddress.getByName(null), port);
		
		new ClientReadThread(socket, this).start();
        new ClientWriteThread(socket, this).start();
        
		//OneClientConnection();
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}
}
