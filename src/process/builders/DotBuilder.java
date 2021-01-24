package process.builders;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import data.Automaton;
import data.State;
import data.Transition;
import process.util.ReversedList;
import process.util.TransitionListUtility;

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

	private boolean isInLandscapeMode = true;
	/**
	 * This value has to be changed to true in certain scenarios where Graphviz will
	 * struggle to draw nodes properly, like the moment where wehave a miror
	 * automaton.
	 */
	private boolean isInReverseMode = false;

	/**
	 * Check if the resulting graph will be displayed from left to right or top to
	 * bottom. By default, this value is set to {@code true}.
	 */
	public boolean isInLandscapeMode() {
		return isInLandscapeMode;
	}

	/**
	 * Set if the resulting graph will be displayed from left to right or top to
	 * bottom. By default, this value is set to {@code true}.
	 * 
	 * @param isInLandscapeMode
	 */
	public void setInLandscapeMode(boolean isInLandscapeMode) {
		this.isInLandscapeMode = isInLandscapeMode;
	}

	/**
	 * Check if the resulting graph will be displayed in the state id order or not.
	 * This value has to be changed to true in certain scenarios where Graphviz will
	 * struggle to draw nodes properly, like the moment where wehave a miror
	 * automaton. By default, this value is set to {@code false}.
	 */
	public boolean isInReverseMode() {
		return isInReverseMode;
	}

	/**
	 * Set if the resulting graph will be displayed from left to right or top to
	 * bottom. This value has to be changed to true in certain scenarios where
	 * Graphviz will struggle to draw nodes properly, like the moment where wehave a
	 * miror automaton. By default, this value is set to {@code false}.
	 */
	public void setInReverseMode(boolean isInReverseMode) {
		this.isInReverseMode = isInReverseMode;
	}

	public DotBuilder(Automaton automaton) {
		this.automaton = automaton;
	}

	public Automaton getAutomaton() {
		return automaton;
	}

	public void setAutomaton(Automaton automaton) {
		this.automaton = automaton;
	}

//	/**
//	 * Create the String that could be contained in a ".dot" file. This method will
//	 * not run properly if it tries to parse a very, very big automaton (a String
//	 * have a space limit of 2 147 483 647 characters)
//	 * 
//	 * @return a String containing all informations
//	 */
//	public String buildDotString() {
//		// Line separator is  different on windows and unix
//		StringBuilder stringBuilder = new StringBuilder(); 
//		if(isInLandscapeMode) {
//			stringBuilder.append("digraph G {rankdir=\"LR\";" + System.lineSeparator());
//		}else {
//			stringBuilder.append("digraph G {" + System.lineSeparator());
//		}
//		
//		//We want to go through all states
//		List<State> states = automaton.getAllStates();
//		for (State state : states) {
//			String stateData = extractDataFromState(state);
//			stringBuilder.append(stateData + System.lineSeparator());
//		}
//
//		// don't forget the ending curly brace '}'
//		stringBuilder.append('}');
//
//		return stringBuilder.toString();
//	}

	/**
	 * Exports the automaton to a specified file (that will be created if not
	 * exists) in the ".dot" format
	 * 
	 * @param file the file where to export automaton
	 */
	public void buildDotFile(File file) {
		// in case file didn't exist
		FileWriter fileWriter;
		try {
			file.createNewFile();
			fileWriter = new FileWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try (BufferedWriter bw = new BufferedWriter(fileWriter)) {
			if (isInLandscapeMode) {
				bw.write("digraph G {rankdir=\"LR\";");
			} else {
				bw.write("digraph G {");
			}
			bw.newLine();

			// We want to go through all states,
			List<State> states = automaton.getAllStates();
			Iterable<State> listToIterate;

			// The order of passage depends on what user choose
			if (isInReverseMode) {
				listToIterate = ReversedList.revertList(states);
			} else {
				listToIterate = states;
			}

			for (State state : listToIterate) {
				String stateData = extractDataFromState(state);
				bw.write(stateData);
				bw.newLine();
			}

			// don't forget the ending curly brace '}'
			bw.write('}');
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		// get a temp list of unused transitions to merge them if needed
		List<Transition> usedTransitions = new ArrayList<Transition>(transitions.size());
		// usedTransitions.addAll(transitions);

		// iterator needed to remove items during iteration
		for (Transition transition : transitions) {
			State destinationState = transition.getDestination();

//			List<Transition> transitionsWithSamePath = TransitionListUtility.getTransitionsWithSamePath(state, destinationState);
//			String label = "";
//			for(Transition t : transitionsWithSamePath) {
//				//if transition not already used before
//				if(!usedTransitions.contains(t)) {
//					label += t.isEpsilon() ? '\u03B5' : t.getLetter();
//					label += ",";
//					//remove it so that we cannot print same transition twice in file
//					usedTransitions.add(t);
//				}
//			}
//			//remove the last comma from the label 
//			if(label.endsWith(",")) {
//				label = label.substring(0, label.length() - 1);
//			}
			char transitionCharacter = transition.isEpsilon() ? '\u03B5' : transition.getLetter();
			String transitionString = stateValidName + "->" + destinationState.getValidName() + "[label=\"" + transitionCharacter + "\"];";
			// Exemple of resulted string : 1 -> 2 [label="a"]:
			sb.append(transitionString);
		}

		return sb.toString();
	}
}
