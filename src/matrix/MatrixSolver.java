package matrix;

import server_side.Solver;

import java.util.List;

import algorithms_interface.BFS;
import algorithms_interface.BestFirstSearch;
import algorithms_interface.DFS;
import algorithms_interface.HillClimbing;
import algorithms_interface.Searcher;
import algorithms_interface.State;

public class MatrixSolver implements Solver<List<String>, String> {
	
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

    public static void main(String[] args) {
    	/*
    	List<String> problem = new ArrayList<String>();
        problem.add("5,11,132,198");
        problem.add("140,5,5,185");
        problem.add("167,190,5,5");   
        problem.add("123,74,140,5");
        problem.add("end");
        problem.add("0,0");
        problem.add("3,3");

        System.out.println(new MatrixSolver().solve(problem)); */
     
		int[][] mat = { { 90, 2, 50, 40 },       
					    { 100, 70, 90, 120 },
					    { 60, 60, 43, 27 },
					    { 130, 15, 200, 3 }
			};
    	
    	System.out.println("BestFS Solution:");       
    	getSolutionAndSum(new BestFirstSearch<Position>().search(
    			new MatrixSearchable(new Matrix(mat, new Position(0,0), new Position(3,3)))));
//    	System.out.println("\nPrim Solution:"); 
//    	Searcher<Position> prim = new Prim<Position>();
//    	if(prim.search(
//    			new MatrixSearchable(new Matrix(mat, new Position(0,0), new Position(3,3))))
//    			!= null)
//    		getSolutionAndSum(prim.search(
//    				new MatrixSearchable(new Matrix(mat, new Position(0,0), new Position(3,3)))));
		
    	System.out.println("\nBFS Solution:");
    	getSolutionAndSum(new BFS<Position>().search(
    			new MatrixSearchable(new Matrix(mat, new Position(0,0), new Position(3,3)))));
    	System.out.println("\nDFS Solution:");
    	getSolutionAndSum(new DFS<Position>().search(
    			new MatrixSearchable(new Matrix(mat, new Position(0,0), new Position(3,3)))));
    	System.out.println("\nHill Climbing Solution:");
    	Searcher<Position> hillClimbing = new HillClimbing<Position>();
    	if(hillClimbing.search(
    			new MatrixSearchable(new Matrix(mat, new Position(0,0), new Position(3,3))))
    			!= null)
    		getSolutionAndSum(hillClimbing.search(
    				new MatrixSearchable(new Matrix(mat, new Position(0,0), new Position(3,3)))));
		
    }

}
