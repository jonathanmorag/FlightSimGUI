package algorithms_interface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class State<T> {
	
	T state;
	double cost;
	State<T> cameFrom;
	
	public State(T state) {
		this.state = state;
		this.cost = 0;
	}
	
	public State(T state, double cost, State<T> cameFrom) {
		this.state = state;
		this.cost = cost;
		this.cameFrom = cameFrom;
	}

	public boolean hasCameFrom() {
		return cameFrom != null;
	}
	
	public T getState() {
		return state;
	}

	public void setState(T state) {
		this.state = state;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public State<T> getCameFrom() {
		return cameFrom;
	}

	public void setCameFrom(State<T> cameFrom) {
		this.cameFrom = cameFrom;
	}
	
    @SuppressWarnings("unchecked")
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
		State<T> state1 = (State<T>) o;
        // TODO: Ask the big question -  return state.equals(state1.state);
        return this.cost == state1.cost && state.equals(state1.state);
    }

    @Override
    public int hashCode() {
    	return state.hashCode();
    }

    @Override
    public String toString() {
    	return state.toString();  
    }
    
    public List<State<T>> backtrace() {
    	ArrayList<State<T>> backtrace = new ArrayList<>();
        State<T> current = this;
        backtrace.add(current);

        while(current.hasCameFrom()) {
            backtrace.add(current.cameFrom);
            current = current.cameFrom;
        }
        Collections.reverse(backtrace);
        return backtrace;
    }
	
		 
}