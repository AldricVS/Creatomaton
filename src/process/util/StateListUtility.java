/**
 * 
 */
package process.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import data.State;

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
		Map<Integer, String> mapStateName = new HashMap<Integer, String>();
		
		// lets get a appropriate name for our new state
		StringBuilder nameBuilder = new StringBuilder(listStates.size());

		for (State state : listStates) {
			listStateId.add(state.getId());
			if (state.hasName()) {
				mapStateName.put(state.getId(), state.getName());
			}
		}

		// sort the id
		listStateId.sort(null);

		// construct the name
		for (int id : listStateId) {
			if (mapStateName.containsKey(id)) {
				nameBuilder.append(mapStateName.get(id));
			} else {
				nameBuilder.append(id);
			}
			nameBuilder.append(";");
		}
		nameBuilder.deleteCharAt(nameBuilder.length()-1);
		return "{" + nameBuilder.toString() + "}";
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
