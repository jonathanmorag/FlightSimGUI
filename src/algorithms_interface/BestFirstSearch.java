package algorithms_interface;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;


public class BestFirstSearch<T> extends CommonSearcher<T> {

	@Override
	public List<State<T>> search(Searchable<T> searchable) {

		PriorityQueue<State<T>> openList = new PriorityQueue<>(Comparator.comparingDouble(State::getCost));

		openList.add(searchable.getInitialState());

		while (!openList.isEmpty()) {
			State<T> currentState = openList.remove();
			closedList.add(currentState);
			if (searchable.isGoalState(currentState)) {
				return currentState.backtrace();
			}
			List<State<T>> possibleStates = searchable.getAllPossibleStates(currentState);
			
			possibleStates.forEach(s -> {
				if(!closedList.contains(s) && !openList.contains(s))
					openList.add(s);
				else {
					State<T> temp = null;
					for(State<T> st : openList) {
						if(s.equals(st) && s.cost < st.cost) {
							temp = st;
							break;
						}
					}
					if(temp != null) {
						openList.remove(temp);
						openList.add(s);
					}
				}
			});

		}
		return null;
	}
}
