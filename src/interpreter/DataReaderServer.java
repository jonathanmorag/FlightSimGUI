package interpreter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class DataReaderServer {
	int port;
	int latency;
	volatile static boolean stop;
	Object notifier;
	
	public DataReaderServer(int port, int latency) {
		this.port = port;
		this.latency = latency;
		stop = false;
		notifier = OpenServerCommand.notifier;
	}

	public void runServer() {
		try {
			ServerSocket server = new ServerSocket(port);
			Socket client = server.accept();
			System.out.println("Client has connected to my Server.");
			String cLine;
			String[] varNames = { "simX", "simY", "simZ" };
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			while (!stop) {
				cLine = in.readLine();
				try {
					Thread.sleep(1000 / latency);
				} catch (InterruptedException e) {}
				if (cLine != null) {
					String[] varValues = cLine.split(",");
					// System.out.println("Client sent: " + cLine);
					for (int i = 0; i < varNames.length; i++) {
						Interpreter.symTable.put(varNames[i], Double.parseDouble(varValues[i]));
						synchronized (notifier) {
							notifier.notify();
						}
					}
				}
			}
			if (stop)
				server.close();

		} catch (IOException e) {}
	}

	public static void stop() {
		stop = true;
	}


}
