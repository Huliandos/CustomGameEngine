package networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientJavaSocket {
	int port;
	String userName;
	
	int userPlayerNum = -1;
	int totalPlayerNum = 0;
	
	public ClientJavaSocket(int port, String userName, long window, Object TOKEN){
		try {
			this.port = port;
			this.userName = userName;
			
			Socket socket = new Socket(InetAddress.getByName(null), port);
			
			ClientWriteThread cwt = new ClientWriteThread(socket, this, window, TOKEN);
			
			new ClientReadThread(socket, this, window, cwt).start();
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}
	
	void setLocalPlayerNum(int playerNum) {
		this.userPlayerNum = playerNum;
	}
	
	int getLocalPlayerNum() {
		return userPlayerNum;
	}
	
	void setTotalPlayerNum(int totalPlayerNum) {
		this.totalPlayerNum = totalPlayerNum;
	}
	
	public int getTotalPlayerNum() {
		return totalPlayerNum;
	}
}
