package server_side;

public interface CacheManager<Problem, Solution> {
	boolean isSolutionExists(Problem problem);
	Solution getSolution(Problem request);
	void saveSolution(Problem problem, Solution solution);
}
