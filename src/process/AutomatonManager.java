/**
 * 
 */
package process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import data.Automaton;
import data.State;
import data.Transition;

import process.util.StateListUtility;
import process.util.TransitionListUtility;

/**
 * <p>Main class to manage Automaton</p>
 * <p>Can validate and determined an Automaton</p>
 * @author Maxence
 */
public class AutomatonManager {
	
	private Automaton automaton;
	public AutomatonManager(Automaton automaton) {
		this.automaton = automaton;
	}
	
	/**
	 * Validate the given Automaton
	 * @param word the word to be tested
	 * @return true if the word is Final, false otherwise
	 */
	public boolean validateAutomaton (String word) {
		List<State> listState = new ArrayList<State>();
		listState.addAll(automaton.getInitialStates());
		
		List<Transition> listTransitions = new ArrayList<Transition>();
		
		//we search through our list of transition for any possible transition to add
		//then we do it again until we arrived at a final state
		while (!word.isEmpty()) {
			//first, get the next letter to search and reduced our word
			char nextLetter = word.charAt(0);
			word = word.substring(1);
			
			//we have some initial transition to go through
			if (!listState.isEmpty()) {
				
				//we add all transition from all the state we are searching
				listTransitions = TransitionListUtility.getAllTransitionFromListStates(listState);
				listState.clear();
				
				//we added all transition, add all next state that we are looking for
				listState = TransitionListUtility.getValidDestinationFromTransition(listTransitions, nextLetter);
				listTransitions.clear();
			}
			//if its empty, then we can stop our research
			else {
				return false;
			}
		}
		//if we arrived here, then our listState got all final state
		for (Iterator<State> it = listState.iterator(); it.hasNext(); ) {
			State finalState = it.next();
			if (automaton.isStateFinal(finalState)) {
				return true;
			}
		}
		
		return false;
	}
	
	
}
