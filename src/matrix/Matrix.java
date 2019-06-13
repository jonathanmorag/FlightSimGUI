package matrix;

import java.util.ArrayList;
import java.util.List;

public class Matrix {

	int[][] data;
	Position entrance;
	Position exit;
	
	
	public Matrix(int[][] data, Position entrance, Position exit) {
		this.data = data;
		this.entrance = entrance;
		this.exit = exit;
	}
	
	public void setEntrance(Position entrance) {
		this.entrance = entrance;
	}
	public Position getEntrance() {
		return entrance;
	}
	
	public void setExit(Position exit) {
		this.exit = exit;
	}
	public Position getExit() {
		return exit;
	}

	public int[][] getData() {
		return data;
	}

	// helper
	private boolean isInBound(int i, int j) {
		return (i >= 0 && i < data.length && j >= 0 && j < data[i].length);
	}

	// returns a list of possible positions

	public List<Position> getPossibleMoves(Position p) {
		int i = p.row, j = p.col;
		if (isInBound(i, j)) {
			List<Position> neighbors = new ArrayList<>();
			if (isInBound(i, j - 1))
				neighbors.add(new Position(i, j - 1));
			if (isInBound(i - 1, j))
				neighbors.add(new Position(i - 1, j));
			if (isInBound(i, j + 1))
				neighbors.add(new Position(i, j + 1));
			if (isInBound(i + 1, j))
				neighbors.add(new Position(i + 1, j));
			return neighbors;
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int[] rows : data) {
			for (int cols : rows) 
				sb.append(Integer.toString(cols));
			sb.append(" ");
		}
		return sb.toString();
	}

}
