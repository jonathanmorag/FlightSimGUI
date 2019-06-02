package server_side;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import models.MatrixModel;

public class MyClientHandler implements ClientHandler { //Client Handler that handles Strings inversions only
	
	Solver<List<String>,String> solver;
	CacheManager<List<String>, String> cm;
	
	public MyClientHandler() {
		solver = new MatrixModel();
		cm = new FileCacheManager<>();
	}
	
//	public MyClientHandler(Solver<List<String>,List<String> solver, CacheManager<String,String> cm) {
//		this.solver = solver;
//		this.cm = cm;
//	}

	
	
	@Override
	public void handleClient(InputStream in, OutputStream out) throws Exception {//left: talk to client stream,right:
		
		// Scanner userInput = new Scanner(new BufferedReader(new InputStreamReader(in)));
		BufferedReader userInput = new BufferedReader(new InputStreamReader(in));
		PrintWriter outToUser = new PrintWriter(out);
		//System.out.println("Handling Client . . . " );
		//System.out.flush();

		
		String line = "";
		List<String> input = new ArrayList<>();
		try {
			while (!(line = userInput.readLine()).equals("end"))
				input.add(line);	
			input.add("end");
			input.add(userInput.readLine());
			input.add(userInput.readLine());
			
			String answer;
			if((answer = cm.getSolution(input)) == null) {
				cm.saveSolution(input, solver.solve(input));
				answer = cm.getSolution(input);
			}
	
			outToUser.println(answer);
			outToUser.flush();
		} catch (IOException e1) { e1.printStackTrace(); }
	}

	
}
