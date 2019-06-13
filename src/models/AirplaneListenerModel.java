package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Observable;

import matrix.Position;
import matrix.PositionConverter;
import server_side.Server;

public class AirplaneListenerModel extends Observable implements Server {

	int port;
	volatile boolean stop;
	volatile Position airplanePosition;
	public double startCooX;
	public double startCooY;
	
	public AirplaneListenerModel(int port) {
		this.port = port;
		stop = false;
	}

	public Position getAirplanePosition() {
		return airplanePosition;
	}
	
	public void setStartCooX(double startCooX) {
		this.startCooX = startCooX;
	}
	public void setStartCooY(double startCooY) {
		this.startCooY = startCooY;
	}
	
	@Override
	public void start() {
		new Thread(() -> {
			try {
				runServer();
			} catch (Exception e) { stop = true; }
		}).start();
	}
	
	@Override
	public void stop() {
		this.stop = true;
	}

	private void runServer() throws Exception {
		ServerSocket server = new ServerSocket(port);
		server.setSoTimeout(1000);

		while (!stop) {
			try {
				Socket aClient = server.accept(); // Client connected successfully
				System.out.println("client has connected...");
				try {
					BufferedReader userInput = new BufferedReader(new InputStreamReader(aClient.getInputStream()));
					while (true) {
						String[] tempCoo = userInput.readLine().split(","); // 32.0131110,34.8752310
						airplanePosition = PositionConverter.convert(
								Double.parseDouble(tempCoo[0]),Double.parseDouble(tempCoo[1]),startCooX, startCooY);
						setChanged();
						notifyObservers();
					}
				} 
				catch (IOException e) {} 
				finally {
					server.close();
				}
			} catch (SocketTimeoutException e) { }
		}
	}

//	public static void main(String[] args) {
//		AirplaneListener s = null;
//		try {
//			s= new AirplaneListener(5500);
//			s.start();
//			Scanner in = new Scanner(System.in);
//			in.nextLine();
//			s.stop();
//			in.close();
//		} catch (Exception e) {}
//	}
}
