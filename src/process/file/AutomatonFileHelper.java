package process.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import data.Automaton;
import data.State;
import data.Transition;
import process.util.FileUtility;

/**
 * Class used in order to save an automaton in file or load one.
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class AutomatonFileHelper {
	public static final String AUTOMATON_FILE_EXTENSION = "crea";

	private Automaton automaton;

	public AutomatonFileHelper(Automaton automaton) {
		// if automaton is null, a NullPointerException is directly thrown
		this.automaton = Objects.requireNonNull(automaton, "Automaton must not be null");
	}

	public Automaton getAutomaton() {
		return automaton;
	}

	public void setAutomaton(Automaton automaton) {
		this.automaton = automaton;
	}

	/**
	 * Save the currently stored automaton in a custom file (with extension .crea).
	 * This method does not overwrite any file, instead, the name of the save file
	 * will be slightly changed.
	 * <p>
	 * <u>ex</u> : if "file.crea" already exists, it will be replaced by
	 * "file(1).crea"
	 * 
	 * @param filePath the path of the file where automaton will be saved. The name
	 *                 can change if one already exists. Moreover, the file will
	 *                 have the extension ".crea" no matter what
	 * @throws IOException If any IO error occurs (such as security error)
	 */
	public void saveAutomaton(String filePath) throws IOException {
		String realFilepath = FileUtility.getRightFilenameExtension(filePath, AUTOMATON_FILE_EXTENSION);
		realFilepath = FileUtility.searchFileOutputName(realFilepath);
		File outputFile = new File(realFilepath);
		outputFile.createNewFile();

		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));

		List<State> initialStates = automaton.getInitialStates();
		List<State> finalStates = automaton.getFinalStates();
		List<State> allStates = automaton.getAllStates();
		List<State> remainingStates = new ArrayList<State>();

		for (State state : allStates) {
			if (!initialStates.contains(state) && !finalStates.contains(state)) {
				remainingStates.add(state);
			}
		}
		writeStateList("#Initial States", initialStates, bufferedWriter);
		writeStateList("#Final States", finalStates, bufferedWriter);
		writeStateList("#Remaining States", remainingStates, bufferedWriter);

		writeTransitions(allStates, bufferedWriter);
		bufferedWriter.write("#End");
		bufferedWriter.close();
	}

	private void writeStateList(String sectionName, List<State> states, BufferedWriter bufferedWriter) throws IOException {
		bufferedWriter.write(sectionName);
		bufferedWriter.newLine();
		for (State state : states) {
			String descritpionString = getStateDescription(state);
			bufferedWriter.write(descritpionString);
			bufferedWriter.newLine();
		}
	}

	private void writeTransitions(List<State> allStates, BufferedWriter bufferedWriter) throws IOException {
		bufferedWriter.write("#Transitions");
		bufferedWriter.newLine();
		for (State state : allStates) {
			for (Transition transition : state.getTransitions()) {
				String transitionDescription = getTransitionDescription(state, transition);
				bufferedWriter.write(transitionDescription);
				bufferedWriter.newLine();
			}
		}
	}

	private String getStateDescription(State state) {
		if (state.hasName()) {
			return state.getId() + ";" + state.getName();
		} else {
			return state.getId() + ";";
		}
	}
	
	private String getTransitionDescription(State sourceState, Transition transition) {
		int destinationStateId = transition.getDestination().getId();
		if(transition.isEpsilon()) {
			return sourceState.getId() + ";" + destinationStateId + ";epsilon";
		}else {
			char letter = transition.getLetter();
			return sourceState.getId() + ";" + destinationStateId + ";" + letter;
		}
	}
}
