package process.builders;

import java.util.List;

import data.Automaton;
import data.State;
import data.Transition;

/**
 * DotBuilder is a builder class that allows to create a ".dot" file, that can
 * be read by <a href="https://graphviz.org">Graphviz</a>.
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class DotBuilder {

	/**
	 * The automaton to parse.
	 */
	private Automaton automaton;

	public DotBuilder(Automaton automaton) {
		this.automaton = automaton;
	}

	public Automaton getAutomaton() {
		return automaton;
	}

	public void setAutomaton(Automaton automaton) {
		this.automaton = automaton;
	}

	/**
	 * Create the String that could be contained in a ".dot" file. This method will
	 * not run properly if it tries to parse a very, very big automaton (a String
	 * have a space limit of 2 147 483 647 characters)
	 * 
	 * @return a String containing all informations
	 */
	public String buildDotString() {
		StringBuilder stringBuilder = new StringBuilder("diagraph G {" + System.lineSeparator()); // Line separator is
																									// different on
																									// windows and unix
		// We want to go through all states
		List<State> states = automaton.getAllStates();
		for (State state : states) {
			String stateData = extractDataFromState(state);
			stringBuilder.append(stateData + System.lineSeparator());
		}
		
		//don't forget the ending curly brace '}'
		stringBuilder.append('}');
		
		return stringBuilder.toString();
	}

	private String extractDataFromState(State state) {
		// Each state data will be displayed on a unique line
		StringBuilder sb = new StringBuilder();
		int stateId = state.getId();
		String stateValidName = state.getValidName();

		// If state is initial, we want to create a substate in order to meake the
		// illusion that the arrow come from nowhere
		if (automaton.isStateInitial(state)) {
			String initialNode = "INITIAL_NODE_" + stateId;
			sb.append(initialNode + "[style=\"invis\",shape=\"point\"];"); /* DONT FORGET SEMICOLON !!! */
			sb.append(initialNode + "->" + stateValidName + ";");
			// Exemple of resulted string : INITIAL_NODE_0 -> 0; INITIAL_NODE_0
			// [style="invis",shape="point"]
		}

		// If state is final, we will want to add a little bit of style on this edge
		if (automaton.isStateFinal(state)) {
			sb.append(stateValidName + "[peripheries=2];");
		}

		// Now, we will want to iterate in each transition in order to display it
		List<Transition> transitions = state.getTransitions();
		for (Transition transition : transitions) {
			State destinationState = transition.getDestination();
			char transitionChar = transition.isEpsilon() ? '\u03B5' : transition.getLetter();
			String transitionString = stateValidName + "->" + destinationState.getValidName() + "[label=\""
					+ transitionChar + "\"];";
			// Exemple of resulted string : 1 -> 2 [label="a"]:
			sb.append(transitionString);
		}

		return sb.toString();
	}
}
