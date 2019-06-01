//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.net.SocketTimeoutException;
//import java.util.Observable;
//import java.util.Scanner;
//
//import matrix.Position;
//import matrix.PositionConverter;
//import view.MainWindowController;
//
//
//public class SimulatorModel extends Observable {
//	
//	volatile boolean stop;
//	int port;
//	public static volatile Position airplanePosition;
//	
//	public SimulatorModel(int port) {
//		this.port = port;
//		stop = false;
//	}
//
//	public void start() throws Exception {
//	new Thread(() -> {
//			try {
//				System.out.println("Server is open and waiting for Clients . . . ");
//				runServer();
//			} catch (Exception e) {
//				stop = true;
//				e.printStackTrace();
//			}
//		}).start();
//	}
//
//	public void stop() {
//		this.stop = true;
//	}
//
//	
//	private void runServer() throws Exception {
//		ServerSocket server = new ServerSocket(port);
//		server.setSoTimeout(1000);
//
//		while (!stop) {
//			try {
//				Socket aClient = server.accept(); // Client connected successfully
//				System.out.println("Client " + aClient.getRemoteSocketAddress() + " is now Connected . . . ");
//				// System.out.flush();
//				try {
//					BufferedReader userInput = new BufferedReader(new InputStreamReader(aClient.getInputStream()));
//					while(true) {
//						String[] tempCoo = userInput.readLine().split(",");  // 32.0131110,34.8752310
//						airplanePosition = PositionConverter
//								.convert(Double.parseDouble(tempCoo[0]),Double.parseDouble(tempCoo[1]),MainWindowController.startX,MainWindowController.startY);
//								setChanged();
//								notifyObservers();
//					}
////					aClient.close();
//				} catch (IOException e) {
//					// e.printStackTrace();
//				}
//			} catch (SocketTimeoutException e) {
//				// e.printStackTrace();
//			}
//
//			// Closing sources...
//
//			// aClient.getInputStream().close();
//			// aClient.getOutputStream().close();
//
//		}
//		server.close();
//	}
//
////	public void runAdministrator() {
////		Scanner sc = new Scanner(System.in);
////		System.out.println("Administrator is now connected . . . ");
////
////		while (!(sc.nextLine()).equals("close")) {
////		}
////
////		System.out.println("Server has been Shutdown by Admin");
////		stop();
////		sc.close();
////	}
//
//	public static void main(String[] args) {
//		SimulatorModel s = null;
//		try {
//			s= new SimulatorModel(5500);
//			s.start();
//			Scanner in = new Scanner(System.in);
//			in.nextLine();
//			s.stop();
//			in.close();
//		} catch (Exception e) {}
//	}
//}
