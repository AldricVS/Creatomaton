package data;

import java.util.ArrayList;
import java.util.List;

public class Automaton {

	private String alphabet;
	private List<State> startingStates = new ArrayList<>();
	
	public Automaton(String alphabet) {
		super();
		this.alphabet = alphabet;
	}

	public String getAlphabet() {
		return alphabet;
	}

	public List<State> getStartingStates() {
		return startingStates;
	}

	public void setAlphabet(String alphabet) {
		this.alphabet = alphabet;
	}

	public void setStartingStates(List<State> startingStates) {
		this.startingStates = startingStates;
	}
	
	public void addStartingState(State state) {
		startingStates.add(state);
	}

}
