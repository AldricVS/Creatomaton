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
	
	public static List<Transition> getAllTransitionFromListStates(List<State> listStates) {
		State nextState;
		List<Transition> listTransition = new ArrayList<Transition>();
		for (Iterator<State> it = listStates.iterator(); it.hasNext(); ) {
			nextState = it.next();
			listTransition.addAll(nextState.getTransitions());
		}
		return listTransition;
	}
	
	/**
	 * Method returning the list of Destination from a list of Transition
	 * @param listTransitions List of all transition
	 * @return an ArrayList of all Destination
	 */
	public static List<State> getAllDestinationFromTransition(List<Transition> listTransitions) {
		List<State> listState = new ArrayList<State>();
		Transition nextTransition;
		for (Iterator<Transition> it = listTransitions.iterator(); it.hasNext(); ) {
			nextTransition = it.next();
			State nextState = nextTransition.getDestination();
			if (!listState.contains(nextState)) {
				listState.add(nextState);
			}
		}
		return listState;
	}
	
	/**
	 * Method returning the list of Destination with the correct char from a list of Transition, including epsilon
	 * @param listTransitions List of all transition
	 * @param letter the lettre used in the transition
	 * @return an ArrayList of all valid Destination
	 */
	public static List<State> getValidDestinationFromTransition(List<Transition> listTransitions, char letter) {
		List<State> listState = new ArrayList<State>();
		Transition nextTransition;
		for (Iterator<Transition> it = listTransitions.iterator(); it.hasNext(); ) {
			nextTransition = it.next();
			if ((nextTransition.isEpsilon()) ||
				(nextTransition.getLetter() == letter)) {
				//little check that the destination isn't already here
				State nextState = nextTransition.getDestination();
				if (!listState.contains(nextState)) {
					listState.add(nextState);
				}
			}
			//else, we dont have the right letter for the transition
		}
		return listState;
	}

}
