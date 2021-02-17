/**
 * 
 */
package process.util;

import java.util.List;
import java.util.Stack;

import data.State;
import data.Transition;

/**
 * @author Maxence
 */
public class StateListUtility {

	/**
	 * <p>
	 * Method to compare if an element of a State List is in another list.
	 * </p>
	 * <p>
	 * Check If any State of the second list is in the first
	 * </p>
	 * 
	 * @param listStates  the first list to compare with
	 * @param listStates2 the second list to compare to
	 * @return true if any element of the second list is in the first one.
	 */
	public static boolean hasCommonStates(List<State> listStates, List<State> listStates2) {
		for (State state : listStates2) {
			if (listStates.contains(state)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Create a appropriate name for all determined State
	 * 
	 * @param listStates a list of States
	 * @return the name of a state, as a determined new state
	 */
	public static String constructNameOfDeterminedStates(List<State> listStates) {
		List<Integer> listStateId = new Stack<Integer>();
		// get a appropriate name for our new state
		String nameDestination = "";

		for (State state : listStates) {
			listStateId.add(state.getId());
		}

		// sort the id
		listStateId.sort(null);

		// construct the name
		for (int id : listStateId) {
			if (nameDestination.isEmpty()) {
				nameDestination = String.valueOf(id);
			} else {
				nameDestination = nameDestination + ";" + id;
			}
		}

		return "{" + nameDestination + "}";
	}

	/**
	 * Search in a List of States to found the Id of a State with the given name
	 * 
	 * @param listStates the list of State to search in
	 * @param name       the name of the state we are looking for
	 * @return the stateId found in listStates, -1 if not found
	 */
	public static int getIdStateFromNameInList(List<State> listStates, String name) {
		for (State state : listStates) {
			String nameCheck = state.getName();
			// compare with name
			if (name.equals(nameCheck)) {
				return state.getId();
			}
		}
		return -1;
	}

}
