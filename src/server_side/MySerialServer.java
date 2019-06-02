package server_side;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;


public class MySerialServer implements Server {
	
	volatile boolean stop;
	int port;
	
	public MySerialServer(int port) {
		this.port = port;
		stop = false;
	}

	@Override
	public void start(ClientHandler ch) throws Exception {
	new Thread(() -> {
			try {
				System.out.println("Server is open, waiting for problems to solve. . . ");
				runServer(port, ch);
			} catch (Exception e) {
				stop = true;
				e.printStackTrace();
			}
		}).start();
	}

	@Override
	public void stop() {
		this.stop = true;
	}

	
	private void runServer(int port, ClientHandler ch) throws Exception {
		ServerSocket server = new ServerSocket(port);
		server.setSoTimeout(1000);
		int i=0;

		while (!stop) {
			try {
				Socket aClient = server.accept(); // Client connected successfully
				System.out.println("Client " + aClient.getRemoteSocketAddress() + " is now Connected . . . ");
				// System.out.flush();
				try {
					while(!stop) {
						System.out.println("handling request #"+ i++);
						ch.handleClient(aClient.getInputStream(), aClient.getOutputStream());
					}
					aClient.close();
				} catch (IOException e) {
					// e.printStackTrace();
				}
			} catch (SocketTimeoutException e) {
				// e.printStackTrace();
			}

			// Closing sources...

			// aClient.getInputStream().close();
			// aClient.getOutputStream().close();

		}
		server.close();
	}

//	public void runAdministrator() {
//		Scanner sc = new Scanner(System.in);
//		System.out.println("Administrator is now connected . . . ");
//
//		while (!(sc.nextLine()).equals("close")) {
//		}
//
//		System.out.println("Server has been Shutdown by Admin");
//		stop();
//		sc.close();
//	}

	public static void main(String[] args) {
		Server s = null;
		try {
			s= new MySerialServer(1234);
			s.start(new MyClientHandler());
//			Scanner in = new Scanner(System.in);
//			in.nextLine();
//			s.stop();
//			in.close();
		} catch (Exception e) {}
	}
}
