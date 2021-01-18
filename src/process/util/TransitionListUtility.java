/**
 * 
 */
package process.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import data.State;
import data.Transition;

/**
 * @author Maxence
 */
public class TransitionListUtility {
	
	private static List<State>listState = new ArrayList<State>();

	public static List<Transition> getAllTransitionFromListStates(List<State> listStates) {
		State nextState;
		List<Transition> listTransition = new ArrayList<Transition>();
		for (Iterator<State> it = listStates.iterator(); it.hasNext();) {
			nextState = it.next();
			listTransition.addAll(nextState.getTransitions());
		}
		return listTransition;
	}

	/**
	 * Method returning the list of Destination from a list of Transition
	 * 
	 * @param listTransitions List of all transition
	 * @return an ArrayList of all Destination
	 */
	public static List<State> getAllDestinationFromTransition(List<Transition> listTransitions) {
		List<State> listState = new ArrayList<State>();
		Transition nextTransition;
		for (Iterator<Transition> it = listTransitions.iterator(); it.hasNext();) {
			nextTransition = it.next();
			State nextState = nextTransition.getDestination();
			if (!listState.contains(nextState)) {
				listState.add(nextState);
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
	 * @return the starting state of a transition, null otherwise
	 */
	public static State getDepartureFromTransition(List<State> listStates, Transition transition) {
		State state;
		for (Iterator<State> it = listStates.iterator(); it.hasNext();) {
			state = it.next();
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
		Transition nextTransition;
		for (Iterator<Transition> it = listTransitions.iterator(); it.hasNext();) {
			nextTransition = it.next();
			if (nextTransition.getLetter() == letter) {
				// little check that the destination isn't already here
				State nextState = nextTransition.getDestination();
				if (!listState.contains(nextState)) {
					listState.add(nextState);
				}
			}
			else if (nextTransition.isEpsilon()) {
				//as a epsilon transition, we add all new valid states from this destination
				State epsilonState = nextTransition.getDestination();
				if (!listState.contains(epsilonState)) {
					listState.add(epsilonState);
					List<State> newTransitionFromEpsilon = getValidDestinationFromTransition(epsilonState.getTransitions(), letter);
					for (Iterator<State> its = newTransitionFromEpsilon.iterator(); its.hasNext(); ) {
						State nextState = its.next();
						if (!listState.contains(nextState)) {
							listState.add(nextState);
						}
					}
					listState.remove(epsilonState);
				}
				//the state from epsilon has already been visited
			}
			// else, we dont have the right letter for the transition
		}
		List<State> validListState = new ArrayList<State>(listState);
		listState.clear();
		return validListState;
	}

}
