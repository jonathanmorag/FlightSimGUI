package algorithms_interface;

import java.util.List;
import java.util.Stack;

public class HillClimbing<T> extends CommonSearcher<T> {

	@Override
	public List<State<T>> search(Searchable<T> searchable) {

		Stack<State<T>> closeList = new Stack<>();
		closeList.push(searchable.getInitialState());

		while (!closeList.isEmpty()) {
			State<T> currentState = closeList.pop();

			if (searchable.isGoalState(currentState)) {
				return currentState.backtrace();
			}
			List<State<T>> l = searchable.getAllPossibleStates(currentState);

			double minCost = Double.MAX_VALUE;

			for (State<T> s : l) {
				if (s.getCost() < minCost)
					minCost = s.getCost();
			}
			for (State<T> s : l) {
				if (s.getCost() == minCost) {
					closeList.add(s);
					if (s.getCost() > 9999) {
						System.out.println("Unfortunately Hill Climbing can't find a solution . . .");
						return null;
					}
				}

			}

		}
		return null;

	}

}
