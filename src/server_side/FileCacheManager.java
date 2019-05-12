package server_side;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileCacheManager<Problem, Solution> implements CacheManager<Problem, Solution> {
	
	Map<Integer, Solution> myCache = new HashMap<>();
	
	static String root = "./db/";
	
	static {
		new File(root).mkdirs();
	}
	
	static String typeName = ".txt";

	public FileCacheManager() {}

	@Override
	public boolean isSolutionExists(Problem problem) {
		return 
			myCache.containsKey(problem.hashCode()) ? true :
				Files.exists(Paths.get(root+problem.hashCode() + typeName));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Solution getSolution(Problem request) {
		if(myCache.containsKey(request.hashCode())) {
			return myCache.get(request.hashCode());
		}
		try {
			return (Solution)new String(Files.readAllBytes(Paths.get(root+request.hashCode()+typeName)),"utf8");
		} catch (IOException e) {}
		return null;
	}

	@Override
	public void saveSolution(Problem problem, Solution solution) {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(root + problem.hashCode() + typeName));
			pw.println(solution);
			pw.flush();
			pw.close();
		} catch (IOException e) { e.printStackTrace(); }
		myCache.put(problem.hashCode(), solution);
	}
	
	
	/*
	static String typeName = ".txt";

	@Override public boolean isSolutionExists(Problem problem) { // try { //
	  ObjectInputStream in = new ObjectInputStream(new FileInputStream(problem + //
	  typeName)); // in.close(); // return true; // } catch (IOException e) {
	  return false; }

	return Files.exists(Paths.get(problem+typeName));

	}

	@SuppressWarnings("unchecked")
	  
	  @Override public Solution getSolution(Problem request) throws Exception {
	  Solution line; ObjectInputStream in = new ObjectInputStream(new
	  FileInputStream(request + typeName)); line = (Solution) in.readObject();
	  in.close(); return line; }

	@Override
	public void saveSolution(Problem problem, Solution solution) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(problem + typeName));
			out.writeObject(solution);
			out.flush();
			out.close();
		} catch (IOException e) { e.printStackTrace(); }
	}
	 */
	
}
