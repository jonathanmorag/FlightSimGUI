package algorithms_interface;

import java.util.List;
import java.util.Stack;

public class DFS<T> extends CommonSearcher<T> {

	Stack<State<T>> stack = new Stack<>();

	@Override
	public List<State<T>> search(Searchable<T> searchable) {

		stack.add(searchable.getInitialState());

		while (!stack.isEmpty()) {
			State<T> current = stack.pop();
			if (!closedList.contains(current)) {
				if (searchable.isGoalState(current)) {
					// System.out.println("DFS: GOAL!");
					return current.backtrace();
				} else {
					stack.addAll(searchable.getAllPossibleStates(current));
					closedList.add(current);
				}
			}
		}
		return null;
	}

}
