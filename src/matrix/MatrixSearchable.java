package matrix;

import java.util.ArrayList;
import java.util.List;

import algorithms_interface.Searchable;
import algorithms_interface.State;

public class MatrixSearchable implements Searchable<Position> {
	
    Matrix matrix;

    public MatrixSearchable(Matrix matrix) {
        this.matrix = matrix;
    }


	@Override
	public State<Position> getInitialState() {
		return new State<Position>(matrix.getEntrance(),
				matrix.data[matrix.getEntrance().row][matrix.getEntrance().col], null);
	}
   
	@Override
	public boolean isGoalState(State<Position> state) {
		 return matrix.getExit().equals(state.getState());
	}

	@Override
	public List<State<Position>> getAllPossibleStates(State<Position> state) {
	       List<State<Position>> result = new ArrayList<>();

	        matrix.getPossibleMoves(state.getState())
	        	.forEach(child -> result.add(
	        			new State<Position>(child, state.getCost()+matrix.data[child.row][child.col], state)));
	        
	        return result;
	}
}
