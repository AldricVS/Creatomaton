package process.factory;

import data.Automaton;
import data.State;
import data.Transition;
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
	 * This creates brand new automaton which have not any connections with the 2
	 * others.
	 * <p>
	 * We suppose that those automatons have only 1 inital state and one final state
	 * (they are normalized).
	 * 
	 * @param automaton1 the first automaton
	 * @param automaton2 the second automaton
	 * @return the concatenation between both automatons
	 */
	public static Automaton createConcatenationAutomaton(Automaton automaton1, Automaton automaton2){
		Automaton a1 = AutomatonFactory.createCopy(automaton1);
		Automaton a2 = AutomatonFactory.createCopy(automaton2);
		String alphabet = StringUtility.alphabetOf2Strings(a1.getAlphabet(), a2.getAlphabet());
//		Automaton automaton = new Automaton(alphabet);
//
//		/*
//		 * - Initial state is the automaton1's initial state - Final state is the
//		 * automatons2's final state - the automaton1's final state and the automaton2's
//		 * initial state are merged
//		 */
//		int cpt = 1;
//
//		State initialState = a1.getInitialStates().get(0);
//		State finalState = a2.getInitialStates().get(0);
//		// automaton.addState(initialState, true, false);
//		// automaton.addState(finalState, false, true);
//
//		// we must merge two states, keep them in mind for later
//		State F1 = a1.getFinalStates().get(0);
//		State I2 = a2.getInitialStates().get(0);
//
//		// add all remaining states (with id from 1 to n-1)
//		for (State state : a1.getAllStates()) {
//			if (state != initialState && state != finalState && state != F1 && state != I2) {
//				//change his id before
//				state.setId(cpt);
//				cpt++;
//			}
//		}
		
		a1.setAlphabet(alphabet);
		
		//merge the final state of a1 with the initial state of a2
		State f1 = a1.getFinalStates().get(0);
		State i2 = a2.getInitialStates().get(0);
		
		for(Transition transition : i2.getTransitions()) {
			f1.addTransition(transition);
		}
		
		//add all states from a2 to a1 (except for I2)
		//we have to change their state id in order to have unique ids in automaton
		int currentId = a1.getNumberOfTotalStates() + 1;
		for(State state : a2.getAllStates()) {
			if(state != i2) {
				state.setId(currentId);
				currentId++;
				a1.addState(state);
			}
		}
		//add the remaining state
		
		
		//one state is final in the automaton
		State f2 = a2.getFinalStates().get(0);
		a1.setStateFinal(f2, true);
		//and f1 is not final anymore
		a1.setStateFinal(f1, false);

		return a1;
	}
}
