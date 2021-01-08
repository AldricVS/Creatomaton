package process.factory;

import java.util.List;

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
	
	/**
	 * Create the union between two Thompson's automatons (accept "A1+A2").
	 * This creates a brand new automaton which doesn't have any connections with the 2
	 * others.
	 * <p>
	 * We suppose that those automatons have only 1 inital state and one final state
	 * (they are normalized), else, unexpected behavior will occur.
	 * 
	 * @param automaton1 the first automaton
	 * @param automaton2 the second automaton
	 * @return the concatenation between both automatons
	 */
	public static Automaton createUnionAutomaton(Automaton automaton1, Automaton automaton2) {
		//create copy af automatons
		Automaton a1 = AutomatonFactory.createCopy(automaton1);
		Automaton a2 = AutomatonFactory.createCopy(automaton2);
		String alphabet = StringUtility.alphabetOf2Strings(a1.getAlphabet(), a2.getAlphabet());
		
		//Get the initial & final state of both automatons
		State i1 = a1.getInitialStates().get(0);
		State i2 = a2.getInitialStates().get(0);
		State f1 = a1.getFinalStates().get(0);
		State f2 = a2.getFinalStates().get(0);
		
		Automaton automaton = new Automaton(alphabet);
		
		/*Add all states in the automaton*/
		List<State> states1 = a1.getAllStates();
		List<State> states2 = a2.getAllStates();
		//add the initial state
		State initialState = new State(0);
		automaton.addState(initialState, true, false);
		//add all the others states from both automatons
		for(State s : states1) {
			automaton.addState(s);
		}
		for(State s : states2) {
			automaton.addState(s);
		}
		//add the final state
		State finalState = new State(0); 
		automaton.addState(finalState, false, false); //a right id will be set automatically
		
		/*Link all states in the automaton*/
		//link initialState and the initial states of sub automatons
		automaton.addEpsilonTransition(initialState, i1);
		automaton.addEpsilonTransition(initialState, i2);
		
		//link finalState and the final states of sub automatons
		automaton.addEpsilonTransition(f1, finalState);
		automaton.addEpsilonTransition(f2, finalState);
		
		return automaton;
	}
}
