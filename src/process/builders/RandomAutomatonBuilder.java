package process.builders;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import data.Automaton;
import data.State;

/**
 * Builder that allows to create a random automaton with parameters to specify.
 * Default automaton generated creates an automaton with one initial state and one final state
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class RandomAutomatonBuilder {
	public static final int DEFAULT_NUMBER_OF_STATES = 3;
	public static final int DEFAULT_NUMBER_OF_TRANSITIONS = 5;
	public static final String DEFAULT_ALPHABET = "abc";
	
	private int numberOfStates;
	private int numberOfTransitions;
	
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
	 * 	<li>3 states</li>
	 * 	<li>5 transitons</li>
	 * 	<li>Can have transitions with characters 'a', 'b', 'c'</li>
	 * 	<li>no epsilon-transiton</li>
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
		
		//create all states 
		for(int index = 0; index < numberOfStates; index++) {
			//random.nextFloat()
		}
		
		return automaton;
	}
}
