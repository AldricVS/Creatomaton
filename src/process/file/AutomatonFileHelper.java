package process.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import data.Automaton;
import data.State;
import data.Transition;
import exceptions.FileFormatException;
import process.util.FileUtility;

/**
 * Class used in order to save an automaton in file or load one.
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class AutomatonFileHelper {
	public static final String AUTOMATON_FILE_EXTENSION = "crea";
	public static final String COMMENTARY_CHARACTER = "\"";
	public static final String COMMAND_CHARACTER = "#";

	public static final String SEPARATOR_CHARACTER = ";";
	public static final String EPSILON_STRING = "epsilon";

	public static final String INITIAL_STATES_COMMAND = "#Initial States";
	public static final String FINAL_STATES_COMMAND = "#Final States";
	public static final String REMAINING_STATES_COMMAND = "#Remaining States";
	public static final String TRANSITIONS_COMMAND = "#Transitions";
	public static final String END_COMMAND = "#End";

	public AutomatonFileHelper() {
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
	public void saveAutomaton(Automaton automaton, String filePath) throws IOException {
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
		writeStateList(INITIAL_STATES_COMMAND, initialStates, bufferedWriter);
		writeStateList(FINAL_STATES_COMMAND, finalStates, bufferedWriter);
		writeStateList(REMAINING_STATES_COMMAND, remainingStates, bufferedWriter);

		writeTransitions(allStates, bufferedWriter);
		bufferedWriter.close();
	}

	public Automaton loadAutomaton(String filename) throws FileFormatException, IOException, IllegalArgumentException {
		File file = new File(filename);
		if (!FileUtility.isFileWithGoodExtension(filename, AUTOMATON_FILE_EXTENSION)) {
			throw new IllegalArgumentException("The filename doesn't have the correct extension (." + AUTOMATON_FILE_EXTENSION + ")");
		}

		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		Automaton automaton = new Automaton("");

		String line;
		while ((line = bufferedReader.readLine()) != null) {
			if (line.isEmpty() || line.startsWith(COMMENTARY_CHARACTER)) {
				continue;
			}

			// else, we must have a command, or there is a problem
			if (!line.startsWith(COMMAND_CHARACTER)) {
				throw new FileFormatException("Invalid starting character at the beginning of the line \n\t " + line);
			}

			// we have 3 differents starts
			switch (line) {
			case INITIAL_STATES_COMMAND:
				readStates(bufferedReader, automaton, true, false);
				break;
			case FINAL_STATES_COMMAND:
				readStates(bufferedReader, automaton, false, true);
				break;
			case REMAINING_STATES_COMMAND:
				readStates(bufferedReader, automaton, false, false);
				break;
			case TRANSITIONS_COMMAND:
				readTransitions(bufferedReader, automaton);
				break;
			default:
				throw new FileFormatException("Command non recognized : " + line);
			}
		}

		bufferedReader.close();
		return automaton;
	}

	private void readTransitions(BufferedReader bufferedReader, Automaton automaton) throws FileFormatException, IOException {
		String line;
		boolean isEndEncountered = false;
		while (!isEndEncountered && (line = bufferedReader.readLine()) != null) {
			if (line.isEmpty() || line.startsWith(COMMENTARY_CHARACTER)) {
				continue;
			}
			if (line.equals(END_COMMAND)) {
				isEndEncountered = true;
			} else {
				String split[] = line.split(SEPARATOR_CHARACTER, 3); //limit in case of the last character IS also the separator
				if(split.length != 3) {
					throw new FileFormatException("A transition must be defined with 3 distinct fields");
				}
				
				//read states ids
				int startingStateId, destinationStateId;
				try {
					startingStateId = Integer.parseInt(split[0]);
					destinationStateId = Integer.parseInt(split[1]);
				}catch (NumberFormatException e) {
					throw new FileFormatException("Invalid state Id at line : " + line);
				}
				
				//read transition character : except from epsilon, they must have only 1 character
				boolean isEpsilonTransition = false;
				char transitionCharacter = 'a';
				if(split[2].length() > 1) {
					if(EPSILON_STRING.equals(split[2])) {
						isEpsilonTransition = true;
					}else {
						throw new FileFormatException("Transition character is not valid at line : " + line);
					}
				}else if(split[2].length() == 0){
					throw new FileFormatException("No transition character found at line :" + line);
				}else {
					transitionCharacter = split[2].charAt(0);
				}
				
				//chack if states id really exists
				State startingState = automaton.getStateById(startingStateId);
				State destinationState = automaton.getStateById(destinationStateId);
				
				if(startingState == null || destinationState == null) {
					throw new FileFormatException("No state found for this transition. Be sure to define all states before transitions : " + line);
				}
				
				//finally, add this transition
				if(isEpsilonTransition) {
					automaton.addEpsilonTransition(startingState, destinationState);
				}else {
					automaton.addTransition(startingState, destinationState, transitionCharacter);
				}
			}
		}
		if (!isEndEncountered) {
			// we are at the end of the file, that's anormal, we must have a "#End" before
			throw new FileFormatException("Unespected end of file. Has not the \"" + END_COMMAND + "\" been forgotten ? ");
		}
	}

	private void readStates(BufferedReader bufferedReader, Automaton automaton, boolean isInitial, boolean isFinal) throws FileFormatException, IOException {
		String line;
		boolean isEndEncountered = false;
		while (!isEndEncountered && (line = bufferedReader.readLine()) != null) {
			if (line.isEmpty() || line.startsWith(COMMENTARY_CHARACTER)) {
				continue;
			}
			if (line.equals(END_COMMAND)) {
				isEndEncountered = true;
			} else {
				// split the string in 2 parts : id and name
				int splitCharacterIndex = line.indexOf(SEPARATOR_CHARACTER);
				if (splitCharacterIndex == -1) {
					throw new FileFormatException("Separator index (\"" + SEPARATOR_CHARACTER + "\") not found at the line : " + line);
				}
				//try to read Id
				int id;
				String name = null;
				if(splitCharacterIndex > 0) {
					String idString = line.substring(0, splitCharacterIndex);
					try {
						id = Integer.parseInt(idString);
					}catch (NumberFormatException e) {
						throw new FileFormatException("Id \"" + idString + "\" is not a valid Id");
					}
				}else {
					throw new FileFormatException("No id found for state at : " + line);
				}
				
				//try to read name (optionnal)
				int lineLength = line.length();
				if(splitCharacterIndex < lineLength) {
					name = line.substring(splitCharacterIndex + 1, lineLength);
				}
				
				//create the state and put it in the right place, don't put it if already exists
				if(automaton.getStateById(id) == null) {
					automaton.addState(new State(id, name));
				}
				
				State state = automaton.getStateById(id);
				//include if the state must be initial or final
				automaton.setStateInitial(state, isInitial);
				automaton.setStateFinal(state, isFinal);
			}
		}
		if (!isEndEncountered) {
			// we are at the end of the file, that's anormal, we must have a "#End" before
			throw new FileFormatException("Unespected end of file. Has not the \"" + END_COMMAND + "\" been forgotten ? ");
		}

	}

	private void writeStateList(String sectionName, List<State> states, BufferedWriter bufferedWriter) throws IOException {
		bufferedWriter.write(sectionName);
		bufferedWriter.newLine();
		for (State state : states) {
			String descritpionString = getStateDescription(state);
			bufferedWriter.write(descritpionString);
			bufferedWriter.newLine();
		}
		bufferedWriter.write(END_COMMAND);
		bufferedWriter.newLine();
	}

	private void writeTransitions(List<State> allStates, BufferedWriter bufferedWriter) throws IOException {
		bufferedWriter.write(TRANSITIONS_COMMAND);
		bufferedWriter.newLine();
		for (State state : allStates) {
			for (Transition transition : state.getTransitions()) {
				String transitionDescription = getTransitionDescription(state, transition);
				bufferedWriter.write(transitionDescription);
				bufferedWriter.newLine();
			}
		}
		bufferedWriter.write(END_COMMAND);
		bufferedWriter.newLine();
	}

	private String getStateDescription(State state) {
		if (state.hasName()) {
			return state.getId() + SEPARATOR_CHARACTER + state.getName();
		} else {
			return state.getId() + SEPARATOR_CHARACTER;
		}
	}

	private String getTransitionDescription(State sourceState, Transition transition) {
		int destinationStateId = transition.getDestination().getId();
		if (transition.isEpsilon()) {
			return sourceState.getId() + SEPARATOR_CHARACTER + destinationStateId + SEPARATOR_CHARACTER + EPSILON_STRING;
		} else {
			char letter = transition.getLetter();
			return sourceState.getId() + SEPARATOR_CHARACTER + destinationStateId + SEPARATOR_CHARACTER + letter;
		}
	}
}
