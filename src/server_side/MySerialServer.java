package server_side;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;


public class MySerialServer implements Server {
	
	volatile boolean stop;
	int port;
	ClientHandler ch;
	
	public MySerialServer(int port, ClientHandler ch) {
		this.port = port;
		this.ch = ch;
		stop = false;
	}
	
	public MySerialServer(int port) {
		this.port = port;
		stop = false;
	}
	
	public void setClientHandler(ClientHandler ch) {
		this.ch = ch;
	}

	@Override
	public void start() {
	new Thread(() -> {
			try {
				runServer();
			} catch (Exception e) {}
		}).start();
	}

	@Override
	public void stop() {
		this.stop = true;
	}

	
	private void runServer() throws Exception {
		ServerSocket server = new ServerSocket(port);
		System.out.println("Server is open, waiting for problems to solve. . . ");
		server.setSoTimeout(1000);

		while (!stop) {
			try {
				Socket aClient = server.accept(); // Client connected successfully
				try {
					while(aClient.isConnected()) {
						ch.handleClient(aClient.getInputStream(), aClient.getOutputStream());
					}
					stop=true;
					aClient.close();
				} catch (IOException e) {server.close();}
			} catch (SocketTimeoutException e) {}

		}
		server.close();
	}
	public static void main(String[] args) {

		Server s = null;
		
		try {
			s = new MySerialServer(1234, new MyClientHandler());
			s.start();
			Thread.sleep(100);
			System.out.println("Press any key to close the server");
			Scanner scanner = new Scanner(System.in);
			scanner.nextLine();
			scanner.close();
		} catch (Exception e) {}
	}
}
