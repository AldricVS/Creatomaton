package process.factory;

import data.Automaton;
import data.State;
import process.util.StringUtility;

/**
 * Factory class that permits to create multiple Thompson's automatons from
 * already existing automatons
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class ThompsonAutomatonFactory {

	/**
	 * Create an automaton that accept one character. An empty word can be created
	 * with the character {@link data.AutomatonConstants#EPSILON_CHAR}
	 * 
	 * @param character the caracter to accept in the automaton
	 * @return the desired automaton
	 */
	public static Automaton createLetterAutomaton(char character) {
		Automaton automaton = new Automaton(String.valueOf(character));
		// 2 states for this automaton
		State initialState = new State(0);
		State finalState = new State(1);
		automaton.addState(initialState, true, false);
		automaton.addState(finalState, false, true);
		automaton.addTransition(initialState, finalState, character);
		return automaton;
	}

	/**
	 * Create the concatenation between two Thompson's automatons (accept "A1.A2").
	 * <p>
	 * We suppose that those automatons have only 1 inital state and one final state
	 * (they are normalized)
	 * 
	 * @param a1 the first automaton
	 * @param a2 the second automaton
	 * @return the concatenation between both automatons
	 */
	public static Automaton createConcatenationAutomaton(Automaton a1, Automaton a2) {
		String alphabet = StringUtility.alphabetOf2Strings(a1.getAlphabet(), a2.getAlphabet());
		Automaton automaton = new Automaton(alphabet);
		
		/*
		 * - Initial state is the automaton1's initial state
		 * - Final state is the automatons2's final state
		 * - the automaton1's final state and the automaton2's initial state are merged
		 */
		int cpt = 1;
		
		State initialState = a1.getInitialStates().get(0);
		State finalState = a2.getInitialStates().get(0);
		//automaton.addState(initialState, true, false);
		//automaton.addState(finalState, false, true);
		
		// we must merge two states, keep them in mind for later
		State F1 = a1.getFinalStates().get(0);
		State I2 = a2.getInitialStates().get(0);
		
		//add all remaining states
		for(State state : a1.getAllStates()) {
			if(state != initialState && state != finalState) {
				
			}
		}
		
		return automaton;
	}
}
