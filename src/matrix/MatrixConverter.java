package matrix;

import java.util.ArrayList;
import java.util.List;
import algorithms_interface.State;

public class MatrixConverter { 									// Parser // BI Directional Parser
	
	public static MatrixSearchable problemToMatrixSearchable(List<String> problem) {
		int numOfRows = problem.size() - 3;
		int numofCols = problem.get(0).split(",").length;
		int[][] data = new int[numOfRows][numofCols];

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numofCols; j++) {
				data[i][j] = Integer.valueOf(problem.get(i).split(",")[j]);
			}
		}
		String[] tmpValues = problem.get(problem.size() - 2).split(","); // "0,12" => ["0","12"] for example;
		Position enter = new Position(Integer.valueOf(tmpValues[0]), Integer.valueOf(tmpValues[1]));

		tmpValues = problem.get(problem.size() - 1).split(","); // "0,12" => ["0","12"] for example;
		Position exit = new Position(Integer.valueOf(tmpValues[0]), Integer.valueOf(tmpValues[1]));
		return new MatrixSearchable(new Matrix(data, enter, exit));
	}

	public static List<String> backtraceToSolution(List<State<Position>> bt) {

		List<String> solution = new ArrayList<String>();
		for (int i = 0; i < bt.size() - 1; i++) {
			if (bt.get(i).getState().row == bt.get(i + 1).getState().row) {
				// so to moving horizontal
				if (bt.get(i).getState().col > bt.get(i + 1).getState().col) {
					solution.add("Left");
				} else {
					solution.add("Right");
				}
			}
			if (bt.get(i).getState().col == bt.get(i + 1).getState().col) {
				// moving is vertical
				if (bt.get(i).getState().row > bt.get(i + 1).getState().row) {
					solution.add("Up");
				} else {
					solution.add("Down");
				}
			}
		}
		return solution;
	}
}
