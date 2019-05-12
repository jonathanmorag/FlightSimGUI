package algorithms_interface;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class BFS<T> extends CommonSearcher<T> {

	@Override
	public List<State<T>> search(Searchable<T> searchable) {
		Queue<State<T>> openList = new LinkedList<>();
		openList.add(searchable.getInitialState());
		closedList.add(searchable.getInitialState());

		while (!openList.isEmpty()) {
			State<T> currentState = openList.remove();
			if (searchable.isGoalState(currentState)) {
				return currentState.backtrace();
			}
			List<State<T>> possibleStates = searchable.getAllPossibleStates(currentState);

			for (State<T> child : possibleStates) {
				if (!closedList.contains(child)) {
					openList.add(child);
					closedList.add(child);
				}
			}
		}
		return null;
	}
}
