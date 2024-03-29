package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An Automaton is the main class that will hold information of all the machine.
 * <p>
 * It contains the alphabet used for all transitions and a list of all states,
 * which can be grouped as initial or final states.
 * </p>
 * <p>
 * Any manipulation have to be done on the automaton directly.
 * </p>
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class Automaton {

	private String alphabet;
	private List<State> initialStates = new ArrayList<>();
	private List<State> finalStates = new ArrayList<>();
	private Map<Integer, State> states = new HashMap<Integer, State>();

	public Automaton(String alphabet) {
		super();
		this.alphabet = alphabet;
	}

	/* =======GETTERS & SETTERS======= */

	public String getAlphabet() {
		return alphabet;
	}

	/**
	 * @param stateId the Id of a state
	 * @return the state associated with the Id, or {@code null} if no state have
	 *         this id
	 */
	public State getStateById(int stateId) {
		return states.get(stateId);
	}

	/**
	 * @return All States of the Automaton
	 */
	public List<State> getAllStates() {
		return new ArrayList<State>(states.values());
	}

	public List<State> getNonFinalStateList() {
		int initialCapacity = getNumberOfTotalStates() - getNumberOfFinalStates();
		List<State> nonFinalStateList = new ArrayList<State>(initialCapacity);
		for (State state : states.values()) {
			if (!finalStates.contains(state)) {
				nonFinalStateList.add(state);
			}
		}
		return nonFinalStateList;
	}

	public List<State> getNonInitialStateList() {
		int initialCapacity = getNumberOfTotalStates() - getNumberOfInitialStates();
		List<State> nonInitialStateList = new ArrayList<State>(initialCapacity);
		for (State state : states.values()) {
			if (!initialStates.contains(state)) {
				nonInitialStateList.add(state);
			}
		}
		return nonInitialStateList;
	}

	/**
	 * @return All States that are listed as Initial
	 */
	public List<State> getInitialStates() {
		return initialStates;
	}

	/**
	 * @return All States that are listed as Final
	 */
	public List<State> getFinalStates() {
		return finalStates;
	}

	public void setAlphabet(String alphabet) {
		this.alphabet = alphabet;
	}

	public int getNumberOfInitialStates() {
		return initialStates.size();
	}

	public int getNumberOfFinalStates() {
		return finalStates.size();
	}

	public int getNumberOfTotalStates() {
		return states.size();
	}

	public void setStateFinal(State state, boolean isFinal) {
		if (isFinal) {
			// check if state not already final
			if (!finalStates.contains(state)) {
				finalStates.add(state);
			}
		} else {
			finalStates.remove(state);
		}
	}

	public void setStateInitial(State state, boolean isInitial) {
		if (isInitial) {
			// check if state not already initial
			if (!initialStates.contains(state)) {
				initialStates.add(state);
			}
		} else {
			initialStates.remove(state);
		}
	}

	/**
	 * @return true if the Automaton doesn't have any state
	 */
	public boolean isEmpty() {
		return states.isEmpty();
	}

	/* =======ADD & REMOVE======= */

	/**
	 * Add a state to the current list of states of the automaton. If the id of the
	 * state is already used, a new one will be assigned to it
	 * 
	 * @param state     the state to add to the automaton
	 * @param isInitial if the state is an initial state
	 * @param isFinal   if the state is a final state
	 * @return if the state was succefully added
	 */
	public boolean addState(State state, boolean isInitial, boolean isFinal) {

		// We don't want to insert a null state.
		if (state == null) {
			return false;
		}

		// Moreover, a state must have a unique id, else, we have to set to a unique one
		if (states.containsKey(state.getId())) {
			state.setId(getMaxIdValue() + 1);
		}

		// add the state in the hashMap, and in other lists if needeed
		states.put(state.getId(), state);
		// if state is initial and/or final, add it to the corresponding list
		if (isFinal) {
			finalStates.add(state);
		}
		if (isInitial) {
			initialStates.add(state);
		}
		return true;
	}

	public int getMaxIdValue() {
		return Collections.max(states.keySet());
	}

	/**
	 * Add a state that is not initial nor final to the current list of states of
	 * the automaton
	 * 
	 * @param state the state to add to the automaton
	 * @return true if the state was succefully added
	 */
	public boolean addState(State state) {
		return addState(state, false, false);
	}

	/**
	 * Remove a state from the automaton
	 * 
	 * @param stateId the id of the state to remove
	 * @return if the state was successfully removed
	 */
	public boolean removeState(int stateId) {
		State stateRemoved = states.remove(stateId);
		// if state successfully removed, try to removed it from the initial and final
		// list
		if (stateRemoved != null) {
			initialStates.remove(stateRemoved);
			finalStates.remove(stateRemoved);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Remove a state from the automaton
	 * 
	 * @param stateId the state to remove
	 * @return if the state was successfully removed
	 */
	public boolean removeState(State state) {
		return removeState(state.getId());
	}

	/**
	 * Add a transition between two states. The transition will not be added if
	 * aleready exists
	 * 
	 * @param startingState    the state where the transition starts
	 * @param destinationState the state where the transition starts
	 * @param letter           the letter that "holds" the transition
	 * @return if the transition was successfully added
	 */
	public boolean addTransition(State startingState, State destinationState, char letter) {
		return startingState.addTransition(letter, destinationState);
	}

	/**
	 * Add an espilon transition from a state to another.
	 * <p>
	 * Same as calling {@linkplain Automaton#addTransition(State, State, char)} with
	 * character '\0000'
	 * 
	 * @param startingState    the state where the transition starts
	 * @param destinationState the state where the transition starts
	 * @return if the transition was successfully added
	 */
	public boolean addEpsilonTransition(State startingState, State destinationState) {
		return startingState.addTransition(AutomatonConstants.EPSILON_CHAR, destinationState);
	}

	/* ======= CLEAR ======= */

	/**
	 * Remove all initial states in the Automaton
	 */
	public void clearInitialStates() {
		initialStates.clear();
	}

	/**
	 * Remove all final states in the Automaton
	 */
	public void clearFinalStates() {
		finalStates.clear();
	}

	/**
	 * Remove all states in the Automaton
	 */
	public void clearAllStates() {
		clearInitialStates();
		clearFinalStates();
		states.clear();
	}

	/* =======VERIFICATIONS======= */

	/**
	 * Check if the state is labeled as a final state
	 * 
	 * @param stateId the unique ID of the state
	 * @return if the state was succefully added
	 */
	public boolean isStateFinal(int stateId) {
		State state = states.get(stateId);
		return finalStates.contains(state);
	}

	/**
	 * Check if the state is labeled as a final state
	 * 
	 * @param state the state we want to know
	 * @return if the state was succefully added
	 */
	public boolean isStateFinal(State state) {
		return finalStates.contains(state);
	}

	/**
	 * Check if the state is labeled as a initial state
	 * 
	 * @param stateId the unique ID of the state
	 * @return if the state was succefully added
	 */
	public boolean isStateInitial(int stateId) {
		State state = states.get(stateId);
		return initialStates.contains(state);
	}

	/**
	 * Check if the state is labeled as a initial state
	 * 
	 * @param state the state we want to know
	 * @return if the state was succefully added
	 */
	public boolean isStateInitial(State state) {
		return initialStates.contains(state);
	}

	/**
	 * Check if a state can be accessed by another state by any transition
	 * 
	 * @param state the id of the state to check
	 * @return if the state is accessible or not
	 */
	public boolean isStateAccessible(int stateId) {
		return isStateAccessible(states.get(stateId));
	}

	/**
	 * Check if a state can be accessed by another state by any transition
	 * 
	 * @param state the state to check
	 * @return if the state is accessible or not
	 */
	public boolean isStateAccessible(State state) {
		// check if all other states have any transition leading to this state
		for (State currentState : states.values()) {
			if (!currentState.equals(state)) {
				List<Transition> transitions = currentState.getTransitions();
				for (Transition transition : transitions) {
					State destination = transition.getDestination();
					if (destination == state) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Compare this Automaton with another to determined if they are equals.
	 * </p>
	 * <p>
	 * This method compare by check if there is the all state of the Automaton have
	 * the same destination.
	 * </p>
	 * 
	 * @param automaton the Automaton to Compare to
	 * @return true if two Automaton are equals, false otherwise
	 */
	public boolean isEquals(Automaton automaton) {
		// start comparation
		if ((this.getNumberOfTotalStates() != automaton.getNumberOfTotalStates())
				|| (this.getNumberOfInitialStates() != automaton.getNumberOfInitialStates())
				|| (this.getNumberOfFinalStates() != automaton.getNumberOfFinalStates())) {
			return false;
		}

		// get the list of all states
		List<State> listStatesFirstAutomaton = this.getAllStates();
		List<State> listStatesSecondAutomaton = automaton.getAllStates();

		// Map all state that are equals
		Map<State, State> equalsStatesMap = new HashMap<State, State>();
		// Map all transition that are equals
		Map<Transition, Transition> equalsTransitionsMap = new HashMap<Transition, Transition>();

		for (State state : listStatesFirstAutomaton) {
			List<Transition> listTransitions = state.getTransitions();

			for (State state2 : listStatesSecondAutomaton) {
				// check if the state hasn't been added to the map
				if (!(equalsStatesMap.containsKey(state) || (equalsStatesMap.containsValue(state2)))) {
					List<Transition> listTransitions2 = state2.getTransitions();

					// compare the transition of the two states
					for (Transition transition : listTransitions) {
						for (Transition transition2 : listTransitions2) {

							if ((transition.getLetter() == transition2.getLetter())
									&& (!equalsTransitionsMap.containsKey(transition))
									&& (!equalsTransitionsMap.containsValue(transition2))) {
								equalsTransitionsMap.put(transition, transition2);
							}
						}
					}

					if ((equalsTransitionsMap.keySet().containsAll(listTransitions))
							&& (equalsTransitionsMap.values().containsAll(listTransitions2))) {
						equalsStatesMap.put(state, state2);
					}
					equalsTransitionsMap.clear();
				}
			}
		}

		if ((equalsStatesMap.keySet().containsAll(listStatesFirstAutomaton))
				&& (equalsStatesMap.values().containsAll(listStatesSecondAutomaton))) {
			return true;
		}

		return false;
	}

	@Override
	public String toString() {
		return "Automaton [alphabet=" + alphabet + ", states=" + states + "]";
	}

}
