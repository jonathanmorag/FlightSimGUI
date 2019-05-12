package algorithms_interface;

import java.util.List;

public interface Searcher<T> {
	List<State<T>> search(Searchable<T> searchable);
}
