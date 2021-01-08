package process.factory;

import data.Automaton;
import data.State;

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

	
}
