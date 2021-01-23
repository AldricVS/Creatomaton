package process.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

	/**
	 * Letters that can be used to generate the automaton
	 */
	private String alphabet;

	/**
	 * If set to true, epsilon transtions are allowed
	 */
	private boolean containsEpsilonTransitions;

	private Automaton automaton;
	private Random random = new Random();

	public RandomAutomatonBuilder(int numberOfStates, int numberOfTransitions, String alphabet, boolean containsEpsilonTransitions) {
		this.numberOfStates = numberOfStates;
		this.numberOfTransitions = numberOfTransitions;
		this.alphabet = alphabet;
		this.containsEpsilonTransitions = containsEpsilonTransitions;
	}

	public RandomAutomatonBuilder(int numberOfStates, int numberOfTransitions, boolean containsEpsilonTransitions) {
		this(numberOfStates, numberOfTransitions, DEFAULT_ALPHABET, containsEpsilonTransitions);
	}

	public RandomAutomatonBuilder(int numberOfStates, int numberOfTransitions) {
		this(numberOfStates, numberOfTransitions, DEFAULT_ALPHABET, false);
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
		this(DEFAULT_NUMBER_OF_STATES, DEFAULT_NUMBER_OF_TRANSITIONS, DEFAULT_ALPHABET, false);
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

	public String getAlphabet() {
		return alphabet;
	}

	public boolean containsEpsilonTransitions() {
		return containsEpsilonTransitions;
	}

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

	public void setAlphabet(String alphabet) {
		this.alphabet = alphabet;
	}

	public void setContainsEpsilonTransitions(boolean containsEpsilonTransitions) {
		this.containsEpsilonTransitions = containsEpsilonTransitions;
	}

	/**
	 * Build the random automaton with the help of all parameters set by user.
	 * 
	 * @return a new Random automaton
	 */
	public Automaton build() {
		automaton = new Automaton(alphabet);

		createAllStates();
		createAllTransitions();

		return automaton;
	}

	private void createAllTransitions() {
		int alphabetSize = alphabet.length();
		// if we must have epsilon transitions, add 1 to the number of characters so
		// that we add epsilon to the pool of possible characters
		if (containsEpsilonTransitions) {
			alphabetSize++;
		}
		
		int numberOfTransitionsAdded = 0;
		while(numberOfTransitionsAdded < numberOfTransitionsAdded) {
			State startingState = searchRandomState();
			State destinationState = searchRandomState();
			
			int characterIndex = random.nextInt(alphabetSize + 1);
			boolean isTransitionAdded;
			if(characterIndex == alphabetSize) {
				isTransitionAdded = automaton.addEpsilonTransition(startingState, destinationState);
			}else {
				isTransitionAdded = automaton.addTransition(startingState, destinationState, alphabet.charAt(characterIndex));
			}
			
			if(isTransitionAdded) {
				numberOfTransitionsAdded++;
			}
		}
	}

	private State searchRandomState() {
		int numberOfTotalStates = automaton.getNumberOfTotalStates();
		int stateId = random.nextInt(numberOfTotalStates);
		return automaton.getStateById(stateId);
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
