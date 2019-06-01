package Models;

import server_side.Solver;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import matrix.*;
import algorithms_interface.BestFirstSearch;
import algorithms_interface.Searcher;
import algorithms_interface.State;

public class MatrixModel extends Observable implements Solver<List<String>, String> {
	
	Matrix resultMatrix;
	double startCoordinateX;
	double startCoordinateY;
	
	public Matrix getMatrix() {
		return resultMatrix;
	}
	
	public void buildMatrix(String[] csv) {
		int size = (int) Math.sqrt(csv.length);
		startCoordinateX = Double.parseDouble(csv[0]);
		startCoordinateY = Double.parseDouble(csv[1]);
		// int cellSize = Integer.parseInt(csv[2]);
		csv = Arrays.copyOfRange(csv, 3, csv.length);
		int[][] mat = new int[size][size];
		int c = 0;
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[i].length; j++) {
				mat[i][j] = Integer.parseInt(csv[c++]);
			}
		}
		resultMatrix = new Matrix(mat, new Position(0,0), null);
		setChanged();
		notifyObservers();
	}
	
	public double getStartCooX() {
		return startCoordinateX;
	}
	public double getStartCooY() {
		return startCoordinateY;
	}
    @Override
    public String solve(List<String> problem) {

        Searcher<Position> BestFSsearcher = new BestFirstSearch<>();
        List<State<Position>> bt = BestFSsearcher.search(MatrixConverter.problemToMatrixSearchable(problem));
        
        List<String> sol= MatrixConverter.backtraceToSolution(bt);
        StringBuilder sb = new StringBuilder();
        sol.forEach(s -> sb.append(s+","));
        return sb.substring(0, sb.length() - 1).toString();
    }
    
	public static void getSolutionAndSum(List<State<Position>> bt) {
		
		StringBuilder sb = new StringBuilder();
		bt.forEach(s -> sb.append(s + " -> "));
		System.out.println(sb.toString().substring(0, sb.length() - 3));
		//Collections.reverse(bt);
		List<String> solution = MatrixConverter.backtraceToSolution(bt);
		sb.delete(0, sb.length());
		solution.forEach(s -> sb.append(s + ", "));
		System.out.println(sb.toString().substring(0, sb.length() - 2));
		System.out.println("Total Sum is: " + bt.get(bt.size() - 1).getCost());
	}
	
}
