/**
 * 
 */
package process.util;

import java.util.ArrayList;
import java.util.List;

import data.State;
import data.Transition;

/**
 * Class containing multiple method to help working around
 * {@link data.Transition Transition}
 * 
 * @author Maxence
 */
public class TransitionListUtility {

	// A static list of State used when using a recursive method, to avoid double
	private static List<State> listState = new ArrayList<State>();

	public static List<Transition> getAllTransitionFromListStates(List<State> listStates) {
		List<Transition> listTransition = new ArrayList<Transition>();
		for (State state : listStates) {
			listTransition.addAll(state.getTransitions());
		}
		return listTransition;
	}

	/**
	 * Search and retrieve all transitions that have te same starting state and
	 * destination state.
	 * 
	 * @param startingState
	 * @param destinationState
	 * @return a list containing all matching transitions (this one can be empty if
	 *         no transition found)
	 */
	public static List<Transition> getTransitionsWithSamePath(State startingState, State destinationState) {
		List<Transition> matchingTranstionList = new ArrayList<Transition>();
		for (Transition transition : startingState.getTransitions()) {
			if (transition.getDestination() == destinationState) {
				matchingTranstionList.add(transition);
			}
		}
		return matchingTranstionList;
	}

	/**
	 * Method returning the list of Destination from a list of Transition
	 * 
	 * @param listTransitions List of all transition
	 * @return an ArrayList of all Destination
	 */
	public static List<State> getAllDestinationFromTransition(List<Transition> listTransitions) {
		List<State> listState = new ArrayList<State>();
		for (Transition transition : listTransitions) {
			State destinationState = transition.getDestination();
			if (!listState.contains(destinationState)) {
				listState.add(destinationState);
			}
		}
		return listState;
	}

	/**
	 * Search the Departure of a Transition from a List of States, return null if
	 * not found
	 * 
	 * @param listStates a list of State to search in
	 * @param transition the transition to look for the starting point
	 * @return the first starting state found from the transition, null otherwise
	 */
	public static State getDepartureFromTransition(List<State> listStates, Transition transition) {
		for (State state : listStates) {
			if (state.getTransitions().contains(transition)) {
				return state;
			}
		}
		return null;
	}

	/**
	 * Method returning the list of Destination with the correct char from a list of
	 * Transition, including all new destination from an epsilon transition
	 * 
	 * @param listTransitions List of all transition
	 * @param letter          the lettre used in the transition
	 * @return an ArrayList of all valid Destination
	 */
	public static List<State> getValidDestinationFromTransition(List<Transition> listTransitions, char letter) {
		for (Transition transition : listTransitions) {
			if (transition.getLetter() == letter) {
				// little check that the destination isn't already here
				State destinationState = transition.getDestination();
				if (!listState.contains(destinationState)) {
					listState.add(destinationState);
				}
			} else if (transition.isEpsilon()) {
				// as a epsilon transition, we add all new valid states from this destination
				State epsilonState = transition.getDestination();
				if (!listState.contains(epsilonState)) {
					listState.add(epsilonState);
					List<State> listStatesFromEpsilon = getValidDestinationFromTransition(epsilonState.getTransitions(), letter);
					for (State state : listStatesFromEpsilon) {
						if (!listState.contains(state)) {
							listState.add(state);
						}
					}
					listState.remove(epsilonState);
				}
				// the state from epsilon has already been visited
			}
			// else, we dont have the right letter for the transition
		}
		List<State> validListState = new ArrayList<State>(listState);
		listState.clear();
		return validListState;
	}

}
