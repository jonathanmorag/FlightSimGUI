package Models;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;

public class ConnectModel extends Observable {
	PrintWriter outToSim;
	
	
	public void connect(String ip, int port) {
		Socket server = null;
		try {
			System.out.println(ip +" " + port);
			server = new Socket(ip, port);
			System.out.println("Client is connected to a remote Server.");
			outToSim = new PrintWriter(server.getOutputStream());
		} catch (IOException e) {}
	}

}
