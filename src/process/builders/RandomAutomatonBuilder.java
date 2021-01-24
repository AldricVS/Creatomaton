package process.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import data.Automaton;
import data.State;
import data.Transition;

/**
 * Builder that allows to create a random automaton with parameters to specify.
 * Default automaton generated creates an automaton with one initial state and
 * one final state
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class RandomAutomatonBuilder {
	public static final int DEFAULT_NUMBER_OF_STATES = 3;
	public static final int DEFAULT_NUMBER_OF_TRANSITIONS = 5;
	public static final String DEFAULT_ALPHABET = "abc";

	private int numberOfStates;
	private int numberOfTransitions;
	private int numberOfInitialStates = 1;
	private int numberOfFinalStates = 1;
	private int numberOfEpsilonTransitions = 0;

	/**
	 * Letters that can be used to generate the automaton
	 */
	private String alphabet;

//	/**
//	 * If set to true, epsilon transtions are allowed
//	 */
//	private boolean containsEpsilonTransitions;

	private Automaton automaton;
	private Random random = new Random();

	public RandomAutomatonBuilder(int numberOfStates, int numberOfTransitions, String alphabet, int numberOfEpsilonTransitions) {
		this.numberOfStates = numberOfStates;
		this.numberOfTransitions = numberOfTransitions;
		this.alphabet = alphabet;
		this.numberOfEpsilonTransitions = numberOfEpsilonTransitions;
	}

	public RandomAutomatonBuilder(int numberOfStates, int numberOfTransitions, int numberOfEpsilonTransitions) {
		this(numberOfStates, numberOfTransitions, DEFAULT_ALPHABET, numberOfEpsilonTransitions);
	}

	public RandomAutomatonBuilder(int numberOfStates, int numberOfTransitions) {
		this(numberOfStates, numberOfTransitions, DEFAULT_ALPHABET, 0);
	}

	/**
	 * Create the builder with all parameters set to default :
	 * <ul>
	 * <li>3 states</li>
	 * <li>5 transitons</li>
	 * <li>Can have transitions with characters 'a', 'b', 'c'</li>
	 * <li>no epsilon-transiton</li>
	 * </ul>
	 */
	public RandomAutomatonBuilder() {
		this(DEFAULT_NUMBER_OF_STATES, DEFAULT_NUMBER_OF_TRANSITIONS, DEFAULT_ALPHABET, 0);
	}

	public int getNumberOfStates() {
		return numberOfStates;
	}

	public int getNumberOfTransitions() {
		return numberOfTransitions;
	}

	public int getNumberOfInitialStates() {
		return numberOfInitialStates;
	}

	public int getNumberOfFinalStates() {
		return numberOfFinalStates;
	}

	public int getNumberOfEpsilonTransitions() {
		return numberOfEpsilonTransitions;
	}

	public String getAlphabet() {
		return alphabet;
	}

//	public boolean containsEpsilonTransitions() {
//		return containsEpsilonTransitions;
//	}

	public void setNumberOfStates(int numberOfStates) {
		this.numberOfStates = numberOfStates;
	}

	public void setNumberOfTransitions(int numberOfTransitions) {
		this.numberOfTransitions = numberOfTransitions;
	}

	public void setNumberOfInitialStates(int numberOfInitialStates) {
		this.numberOfInitialStates = numberOfInitialStates;
	}

	public void setNumberOfFinalStates(int numberOfFinalStates) {
		this.numberOfFinalStates = numberOfFinalStates;
	}

	public void setNumberOfEpsilonTransitions(int numberOfEpsilonTransitions) {
		this.numberOfEpsilonTransitions = numberOfEpsilonTransitions;
	}

	public void setAlphabet(String alphabet) {
		this.alphabet = alphabet;
	}

//	public void setContainsEpsilonTransitions(boolean containsEpsilonTransitions) {
//		this.containsEpsilonTransitions = containsEpsilonTransitions;
//	}

	/**
	 * Build the random automaton with the help of all parameters set by user.
	 * WARNING ! This method cannot produce automatons with multiple initial states.
	 * 
	 * @return
	 */
	public Automaton build() {
		//if not enough states, go with the silly implementation
		if(numberOfStates <= 3) {
			return buildSilly();
		}
		
		automaton = new Automaton(alphabet);
		numberOfTransitions = numberOfStates + random.nextInt(numberOfStates / 2);

		for (int index = 0; index < numberOfStates; index++) {
			State state = new State(index);
			automaton.addState(state);
		}

		// state 0 IS initial
		State initialState = automaton.getStateById(0);
		automaton.setStateInitial(initialState, true);

		List<State> statesEncountered = new LinkedList<State>();
		int currentNumberOfTransitions = 0;

		while (currentNumberOfTransitions < numberOfTransitions) {
			statesEncountered.clear();
			// create a path going from initial state that will end only if state was
			// already encountered in this path
			statesEncountered.add(initialState);
			int transitionsPathCount = random.nextInt(automaton.getNumberOfTotalStates());
			int currentTransitonCount = 0;
			State destinationState = null;
			State lastEncounteredState = initialState;
			boolean isPathEnded = false;
			while (!isPathEnded && currentTransitonCount < transitionsPathCount) {
				char character = randomCharFromAlphabet();
				destinationState = searchDifferentState(initialState);
				if (statesEncountered.contains(destinationState)) {
					isPathEnded = true;
				}
				statesEncountered.add(destinationState);
				if (automaton.addTransition(lastEncounteredState, destinationState, character)) {
					currentNumberOfTransitions++;
				}
				lastEncounteredState = destinationState;
			}
			//last state encountered must go to another state

			State endingPathState;
			endingPathState = searchStateNotEncountered(lastEncounteredState);
			automaton.addTransition(lastEncounteredState, endingPathState, randomCharFromAlphabet());
		}
		
		for (State state : automaton.getAllStates()) {
			if (state.getTransitions().isEmpty()) {
				char character = randomCharFromAlphabet();
				// search a state this is not
				State destinationState = searchDifferentState(state);
				automaton.addTransition(state, destinationState, character);
			}
		}

		return automaton;
	}

	private State searchStateNotEncountered(State state) {
		State resultingState;
		boolean isValid;
		do {
			isValid = true;
			int numberOfTotalStates = automaton.getNumberOfTotalStates();
			int stateId = random.nextInt(numberOfTotalStates);
			resultingState = automaton.getStateById(stateId);
			
			//search in both state's transitions if already encountered another
			for(Transition transition : resultingState.getTransitions()) {
				if(transition.getDestination() == state) {
					isValid = false;
					break;
				}
			}
			if(isValid) {
				for(Transition transition : state.getTransitions()) {
					if(transition.getDestination() == resultingState) {
						isValid = false;
						break;
					}
				}
			}
			
		}while(!isValid);
		
		return resultingState;
	}

	/**
	 * This build method is unlikely to produce always good results, so be careful
	 * using it. Build the random automaton with the help of all parameters set by
	 * user.
	 * 
	 * @return a new Random automaton
	 */
	public Automaton buildSilly() {
		automaton = new Automaton(alphabet);

		createAllStates();
		createAllTransitions();

		return automaton;
	}

	private void createAllTransitions() {
		int alphabetSize = alphabet.length();

		int numberOfTransitionsAdded = 0;
		int currentEpsilonTransitionsCount = 0;

		List<State> statesWithoutTransition = new ArrayList<State>();
		List<State> automatonStateList = automaton.getAllStates();
		statesWithoutTransition.addAll(automatonStateList);

		// add one transition from each of the states
		while (!statesWithoutTransition.isEmpty()) {
			int size = statesWithoutTransition.size();
			State startingState;
			State destinationState;

			int statePos = random.nextInt(size);
			startingState = statesWithoutTransition.get(statePos);
			statesWithoutTransition.remove(statePos);

			size = statesWithoutTransition.size();

			destinationState = searchDifferentState(startingState);

			if (currentEpsilonTransitionsCount < numberOfEpsilonTransitions) {
				automaton.addEpsilonTransition(startingState, destinationState);
				currentEpsilonTransitionsCount++;
			} else {
				int characterIndex = random.nextInt(alphabetSize);
				automaton.addTransition(startingState, destinationState, alphabet.charAt(characterIndex));
			}

			numberOfTransitionsAdded++;
		}

		// add remaining transitions if needed
		while (numberOfTransitionsAdded < numberOfTransitions) {
			State startingState = searchRandomState();
			State destinationState = searchRandomState();

			int characterIndex = random.nextInt(alphabetSize);
			boolean isTransitionAdded;
			if (currentEpsilonTransitionsCount < numberOfEpsilonTransitions) {
				isTransitionAdded = automaton.addEpsilonTransition(startingState, destinationState);
				if (isTransitionAdded) {
					currentEpsilonTransitionsCount++;
				}
			} else {
				isTransitionAdded = automaton.addTransition(startingState, destinationState, alphabet.charAt(characterIndex));
			}

			if (isTransitionAdded) {
				numberOfTransitionsAdded++;
			}
		}

		// search for all states that don't have incomming transitions
		// (can't be accessed), and create new transitions for them
		for (State state : automatonStateList) {
			if (!automaton.isStateAccessible(state)) {
				char character = randomCharFromAlphabet();
				// search a state this is not
				State startingState = searchDifferentState(state);
				automaton.addTransition(startingState, state, character);
			}
		}

	}

	private char randomCharFromAlphabet() {
		int characterIndex = random.nextInt(alphabet.length());
		return alphabet.charAt(characterIndex);
	}

	private State searchRandomState() {
		int numberOfTotalStates = automaton.getNumberOfTotalStates();
		int stateId = random.nextInt(numberOfTotalStates);
		return automaton.getStateById(stateId);
	}

	private State searchDifferentState(State state) {
		State differentState;
		int numberOfTotalStates = automaton.getNumberOfTotalStates();
		do {
			int stateId = random.nextInt(numberOfTotalStates);
			differentState = automaton.getStateById(stateId);
		} while (differentState == state);
		return differentState;
	}

	private void createAllStates() {
		boolean areAllStatesInitial = numberOfInitialStates >= numberOfStates;
		boolean areAllStatesFinal = numberOfFinalStates >= numberOfStates;
		for (int index = 0; index < numberOfStates; index++) {
			State state = new State(index);
			automaton.addState(state, areAllStatesInitial, areAllStatesFinal);
		}
		// define inital and final states randomly
		if (!areAllStatesInitial) {
			defineInitialStates();
		}
		if (!areAllStatesFinal) {
			defineFinalStates();
		}
	}

	private void defineFinalStates() {
		List<State> statesNonTreated = new ArrayList<State>();
		statesNonTreated.addAll(automaton.getAllStates());
		for (int index = 0; index < numberOfFinalStates; index++) {
			// get one state that was not taken before
			int statesCount = statesNonTreated.size();
			int stateChoosenIndex = random.nextInt(statesCount);
			State stateChoosen = statesNonTreated.get(stateChoosenIndex);
			automaton.setStateFinal(stateChoosen, true);
			// remove it from the temp list
			statesNonTreated.remove(stateChoosenIndex);
		}
	}

	private void defineInitialStates() {
		List<State> statesNonTreated = new ArrayList<State>();
		statesNonTreated.addAll(automaton.getAllStates());
		for (int index = 0; index < numberOfInitialStates; index++) {
			// get one state that was not taken before
			int statesCount = statesNonTreated.size();
			int stateChoosenIndex = random.nextInt(statesCount);
			State stateChoosen = statesNonTreated.get(stateChoosenIndex);
			automaton.setStateInitial(stateChoosen, true);
			// remove it from the temp list
			statesNonTreated.remove(stateChoosenIndex);
		}
	}
}
