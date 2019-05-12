package algorithms_interface;

import java.util.List;
import java.util.PriorityQueue;


public class BestFirstSearch<T> extends CommonSearcher<T> {

	@Override
	public List<State<T>> search(Searchable<T> searchable) {

		PriorityQueue<State<T>> openList = new PriorityQueue<>((s1, s2) -> Double.compare(s1.getCost(), s2.getCost()));

		openList.add(searchable.getInitialState());

		while (!openList.isEmpty()) {
			State<T> currentState = openList.remove();
			closedList.add(currentState);
			if (searchable.isGoalState(currentState)) {
				return currentState.backtrace();
			}
			List<State<T>> possibleStates = searchable.getAllPossibleStates(currentState);
			
			for(State<T> state : possibleStates) {
				if (!openList.contains(state))
					openList.add(state);
			}

		}
		return null;
	}
}
