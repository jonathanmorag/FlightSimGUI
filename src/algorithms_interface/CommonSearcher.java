package algorithms_interface;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class CommonSearcher<T> implements Searcher<T> {
	
	Set<State<T>> closedList;
	
	public CommonSearcher() {
		closedList = new HashSet<>();
	}
	
	@Override
	public abstract List<State<T>> search(Searchable<T> searchable);
}
