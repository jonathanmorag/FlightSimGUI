package server_side;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import matrix.MatrixConverter;
import matrix.MatrixSearchable;
import models.MatrixModel;

public class MyClientHandler implements ClientHandler { // Client Handler that handles Matrix solving problems only

	Solver<MatrixSearchable, String> solver;
	CacheManager<MatrixSearchable, String> cm;

	public MyClientHandler() {
		solver = new MatrixModel();
		cm = new FileCacheManager<>();
	}

	@Override
	public void handleClient(InputStream in, OutputStream out) {

		BufferedReader userInput = new BufferedReader(new InputStreamReader(in));
		PrintWriter outToUser = new PrintWriter(out);

		String line = "";
		List<String> input = new ArrayList<>();
		try {
			while (!(line = userInput.readLine()).equals("end"))
				input.add(line);
			input.add("end");
			input.add(userInput.readLine());
			input.add(userInput.readLine());

			MatrixSearchable ms = MatrixConverter.problemToMatrixSearchable(input);

			String answer = null;
			if ((answer = cm.getSolution(ms)) == null) {
				answer = solver.solve(ms);
				cm.saveSolution(ms, answer);
			}

			outToUser.println(answer);
			outToUser.flush();
		} catch (IOException e1) {
			
		}
	}

}
