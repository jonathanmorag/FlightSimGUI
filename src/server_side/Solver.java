package server_side;

public interface Solver<Problem, Solution> {
	Solution solve(Problem problem);
}