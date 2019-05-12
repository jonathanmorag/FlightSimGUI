package server_side;


public interface CacheManager<Problem, Solution> {
	
	public boolean isSolutionExists(Problem problem);
	
	public Solution getSolution(Problem request) throws Exception;
	
	public void saveSolution(Problem problem,Solution solution);
}
