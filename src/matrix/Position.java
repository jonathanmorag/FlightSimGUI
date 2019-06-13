package matrix;

public class Position {
	
	public int row;
	public int col;

	public Position(int row, int col) {
		this.row = row;
		this.col = col;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Position position = (Position) o;
		return row == position.row && col == position.col;
	}

	@Override
	public int hashCode() {
		return (row + "," + col).hashCode();
	}

	@Override
	public String toString() {
		return "("+row + "," + col+")";
	}
}