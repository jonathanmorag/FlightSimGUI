package models;

import server_side.Solver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
	PrintWriter outToSolver;
	BufferedReader in;
	String shortestPath;

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
		resultMatrix = new Matrix(mat, new Position(0, 0), null);
		setChanged();
		notifyObservers("matrix");
	}

	public double getStartCooX() {
		return startCoordinateX;
	}

	public double getStartCooY() {
		return startCoordinateY;
	}

	public void connect(String ip, int port) {
		Socket server = null;
		try {
			server = new Socket(ip, port);
			in = new BufferedReader(new InputStreamReader(server.getInputStream()));
			outToSolver = new PrintWriter(server.getOutputStream());
		} catch (IOException e) {}
		System.out.println("Connected to a solver server.");
	}
	public void setStartPosition(Position start) {
		resultMatrix.setEntrance(start);
	}
	public void setExitPosition(Position exit) {
		resultMatrix.setExit(exit);
	}
	public String getShortestPath() {
		return shortestPath;
	}
	
	public void calculatePath() {
		if (outToSolver == null) {
			System.out.println("you are not connected to a solver.");
			setChanged();
			notifyObservers("not connected");
			return;
		}
		String[] problem = convertToProblem(resultMatrix);
		for (String s : problem) {
			outToSolver.println(s);
			outToSolver.flush();
		}
		outToSolver.println("end");
		outToSolver.flush();
		outToSolver.println(resultMatrix.getEntrance().row + "," + resultMatrix.getEntrance().col);
		outToSolver.flush();
		outToSolver.println(resultMatrix.getExit().row + "," + resultMatrix.getExit().col);
		outToSolver.flush();
		try {
			shortestPath = in.readLine();
			setChanged();
			notifyObservers("shortest path");
		} catch (IOException e) {}
	}

	public String[] convertToProblem(Matrix matrix) {
		String[] problem = new String[matrix.getData().length];
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < matrix.getData().length; i++) {
			for (int j = 0; j < matrix.getData()[0].length; j++) {
				sb.append(matrix.getData()[i][j] + ",");
			}
			problem[i] = sb.substring(0, sb.length() - 1).toString();
		}
		return problem;
	}

	@Override
	public String solve(List<String> problem) {

		Searcher<Position> BestFSsearcher = new BestFirstSearch<>();
		List<State<Position>> bt = BestFSsearcher.search(MatrixConverter.problemToMatrixSearchable(problem));

		List<String> sol = MatrixConverter.backtraceToSolution(bt);
		StringBuilder sb = new StringBuilder();
		sol.forEach(s -> sb.append(s + ","));
		return sb.substring(0, sb.length() - 1).toString();
	}

	public static void getSolutionAndSum(List<State<Position>> bt) {

		StringBuilder sb = new StringBuilder();
		bt.forEach(s -> sb.append(s + " -> "));
		System.out.println(sb.toString().substring(0, sb.length() - 3));
		// Collections.reverse(bt);
		List<String> solution = MatrixConverter.backtraceToSolution(bt);
		sb.delete(0, sb.length());
		solution.forEach(s -> sb.append(s + ", "));
		System.out.println(sb.toString().substring(0, sb.length() - 2));
		System.out.println("Total Sum is: " + bt.get(bt.size() - 1).getCost());
	}

}
