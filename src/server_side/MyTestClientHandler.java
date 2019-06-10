package server_side;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class MyTestClientHandler implements ClientHandler { //Client Handler that handles Strings inversions only
	
	Solver<String,String> solver;
	CacheManager<String,String> cm;
	
	public MyTestClientHandler() {
		solver = s -> new StringBuilder(s).reverse().toString();
		cm = new FileCacheManager<>();
	}
	
	public MyTestClientHandler(Solver<String,String> solver, CacheManager<String,String> cm) {
		this.solver = solver;
		this.cm = cm;
	}

	
	
	@Override
	public void handleClient(InputStream in, OutputStream out) {//left: talk to client stream,right:
		
		// Scanner userInput = new Scanner(new BufferedReader(new InputStreamReader(in)));
		BufferedReader userInput = new BufferedReader(new InputStreamReader(in));
		PrintWriter outToUser = new PrintWriter(out);
		System.out.println("Handling Client . . . " );
		System.out.flush();

		
		String line = "";
		try {
			while (!(line = userInput.readLine()).equals("end")) {
					
				System.out.println("Client has requested to invert: " + line);
				if (cm.isSolutionExists(line)) {
					outToUser.println(cm.getSolution(line));
					outToUser.flush();
				} else {
					cm.saveSolution(line, solver.solve(line));
					outToUser.println(cm.getSolution(line));
					outToUser.flush();
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			userInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		outToUser.close();
	}
	
}
