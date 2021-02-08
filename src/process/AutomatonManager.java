/**
 * 
 */
package process;

import java.util.ArrayList;
import java.util.List;

import data.Automaton;
import data.State;
import data.Transition;

import process.util.StateListUtility;
import process.util.TransitionListUtility;

/**
 * <p>
 * Main class to manage Automaton
 * </p>
 * <p>
 * Can validate and determined an Automaton
 * </p>
 * 
 * @author Maxence
 */
public class AutomatonManager {

	private Automaton automaton;

	public AutomatonManager(Automaton automaton) {
		this.automaton = automaton;
	}

	public Automaton getAutomaton() {
		return automaton;
	}

	public void setAutomaton(Automaton automaton) {
		this.automaton = automaton;
	}

	/**
	 * Validate the given Automaton
	 * 
	 * @param word the word to be tested
	 * @return true if the word is Final, false otherwise
	 */
	public boolean validateAutomaton(String word) {
		List<State> listState = new ArrayList<State>();
		listState.addAll(automaton.getInitialStates());

		List<Transition> listTransitions = new ArrayList<Transition>();

		// we search through our list of transition for any possible transition to add
		// then we do it again until we arrived at a final state
		while (!word.isEmpty()) {
			// first, get the next letter to search and reduced our word
			char nextLetter = word.charAt(0);
			word = word.substring(1);

			// we have some initial transition to go through
			if (!listState.isEmpty()) {

				// we add all transition from all the state we are searching
				listTransitions = TransitionListUtility.getAllTransitionFromListStates(listState);
				listState.clear();

				// we added all transition, add all next state that we are looking for
				listState = TransitionListUtility.getValidDestinationFromTransition(listTransitions, nextLetter);
				listTransitions.clear();
			}
			// if its empty, then we can stop our research
			else {
				return false;
			}
		}

		// if we arrived here, then our listState got all state which we have gone last
		for (State state : listState) {
			if (automaton.isStateFinal(state)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Check if the given automaton is Determinist :
	 * <ul>
	 * <li>Has a unique Initial State</li>
	 * <li>No Epsilon transition</li>
	 * <li>Only one transition for each letter</li>
	 * </ul>
	 */
	public boolean isDeterministic() {
		// check if there is multiple initial states
		if (automaton.getNumberOfInitialStates() != 1) {
			return false;
		}

		// check if there is any epsilon transition or any multiple
		// transition of thesame letter
		List<State> listStates = automaton.getAllStates();
		for (State state : listStates) {
			// the list of transition
			List<Transition> listTransitions = new ArrayList<Transition>(state.getTransitions());
			// a list of letter of the transition
			List<Character> listLetter = new ArrayList<Character>();

			for (Transition transition : listTransitions) {
				if (transition.isEpsilon()) {
					return false;
				} else if (listLetter.contains(transition.getLetter())) {
					return false;
				}
				listLetter.add(transition.getLetter());
			}

			listTransitions.clear();
			listLetter.clear();
		}

		return true;
	}

}
