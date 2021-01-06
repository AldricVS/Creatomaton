package data;

import java.util.ArrayList;
import java.util.List;

/**
 * An Automaton is the main class that will hold information of all the machine.<p>
 * It contains the alphabet and a list of all the starting states 
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
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
