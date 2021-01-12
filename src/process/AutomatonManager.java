/**
 * 
 */
package process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import data.Automaton;
import data.State;
import data.Transition;

/**
 * <p>Main class to manage Automaton</p>
 * <p>Can validate and determined an Automaton</p>
 * @author Maxence
 */
public class AutomatonManager {
	
	private AutomatonManager() {};
	
	private static AutomatonManager instance = new AutomatonManager();
	/**
	 * @return the static instance of AutomatonManager
	 */
	public static AutomatonManager getInstance() {
		return instance;
	}
	
	/**
	 * <p>Method to compare if an element of a State List is in another list.</p>
	 * <p>Useful when comparing with Initial or Final List of State.</p>
	 * @param listStates the first list to compare with
	 * @param listStates2 the second list to compare to
	 * @return true if any element of the second list is in the first one.
	 */
	public boolean hasCommonStates(List<State> listStates, List<State> listStates2) {
		State nextState;
		for (Iterator<State> it = listStates2.iterator(); it.hasNext(); ) {
			nextState = it.next();
			if (listStates.contains(nextState)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Create a appropriate name for all determined State
	 * @param listStates a list of States
	 * @return the name of a state, as a determined new state
	 */
	public String constructNameOfDeterminedStates(List<State> listStates) {
		//get a appropriate name for our new state
		State nextState;
		List<Integer> listStateId = new Stack<Integer>();
		String nameDestination = "";
		for (Iterator<State> it = listStates.iterator(); it.hasNext(); ) {
			nextState = it.next();
			listStateId.add(nextState.getId());
		}
		
		//sort the id
		listStateId.sort(null);
		
		int id;
		//construct the name
		for (Iterator<Integer> it = listStateId.iterator(); it.hasNext(); ) {
			id = it.next();
			if (nameDestination.isEmpty()) {
				nameDestination = String.valueOf(id);
			}
			else {
				nameDestination = nameDestination + ";" + id;
			}
		}
		
		return nameDestination;
	}
	
	/**
	 * Search in a List of States to found the Id of a State with the given name
	 * @param listStates the list of State to search in
	 * @param name the name of the state we are looking for
	 * @return the stateId found in listStates, -1 if not found
	 */
	public int getIdStateFromNameList(List<State> listStates, String name) {
		State nextState;
		for (Iterator<State> it = listStates.iterator(); it.hasNext(); ) {
			nextState = it.next();
			String nameCheck = nextState.getName();
			//compare with name
			if (name.equals(nameCheck)) {
				return nextState.getId();
			}
		}
		return -1;
	}
	
	public List<Transition> getAllTransitionFromListStates(List<State> listStates) {
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
	public List<State> getAllDestinationFromTransition(List<Transition> listTransitions) {
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
	public List<State> getValidDestinationFromTransition(List<Transition> listTransitions, char letter) {
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
	
	public boolean validateAutomaton (String word, Automaton automaton) {
		List<State> listState = new ArrayList<State>();
		listState.addAll(automaton.getInitialStates());
		
		List<Transition> listTransitions = new ArrayList<Transition>();
		
		//we search through our list of transition for any possible transition to add
		//then we do it again until we arrived at a final state
		while (!word.isEmpty()) {
			//first, get the next letter to search and reduced our word
			char nextLetter = word.charAt(0);
			word = word.substring(1);
			
			//we have some initial transition to go through
			if (!listState.isEmpty()) {
				
				//we add all transition from all the state we are searching
				listTransitions = getAllTransitionFromListStates(listState);
				listState.clear();
				
				//we added all transition, add all next state that we are looking for
				listState = getValidDestinationFromTransition(listTransitions, nextLetter);
				listTransitions.clear();
			}
			//if its empty, then we can stop our research
			else {
				return false;
			}
		}
		//if we arrived here, then our listState got all final state
		for (Iterator<State> it = listState.iterator(); it.hasNext(); ) {
			State finalState = it.next();
			if (automaton.isStateFinal(finalState)) {
				return true;
			}
		}
		
		return false;
	}
	
	public Automaton determinedAutomaton(Automaton automaton) {
		//listState have all state of the first determined state
		List<State> listState = new ArrayList<State>();
		listState.addAll(automaton.getInitialStates());
		
		//listTransition will get all transition of listState
		List<Transition> listTransitions = new ArrayList<Transition>();
		
		//listNewState will get all new State from the destination of listTransition
		List<State> listNewState = new ArrayList<State>();
		
		//listDeterminedState is a list of all listState we need to go through
		LinkedList<List<State>> listDeterminedStates = new LinkedList<List<State>>();
		listDeterminedStates.add(listState);
		
		//get the alphabet and create the new automaton
		String alphabet = automaton.getAlphabet();
		Automaton determinedAutomaton = new Automaton(alphabet);
		
		//id of the state and a boolean to determined if they are final
		int id = 0;
		boolean isFinal;
		
		//while we have new states to add
		while (!listDeterminedStates.isEmpty()) {
			//reset of isFinal
			isFinal = false;
			
			//listState will take the first list of listDeterminedState
			listState = listDeterminedStates.pop();
			
			//we add all transition from all the state we are coming from
			listTransitions = getAllTransitionFromListStates(listState);
			
			String nameDeparture = "";
			nameDeparture = constructNameOfDeterminedStates(listState);
			
			//we dont have any interest in our old list
			listState.clear();
			
			//get the state's name from which we go from
			//and search the state from the automaton's list
			int stateStartingId = getIdStateFromNameList(determinedAutomaton.getAllStates(), nameDeparture);
			
			//verification if we find it
			State stateDeparture;
			if (stateStartingId < 0) {
				//create a new state in determinedAutomaton
				stateDeparture = new State(id, nameDeparture);
				id++;
				determinedAutomaton.addState(stateDeparture, true, false);
			}
			else {
				//get the departure state
				stateDeparture = determinedAutomaton.getStateFromId(stateStartingId);
			}
			
			
			//for each of the alphabet letter
			for (char letter : alphabet.toCharArray()) {

				listNewState = getValidDestinationFromTransition(listTransitions, letter);
				if (hasCommonStates(listNewState, automaton.getFinalStates())) {
					isFinal = true;
				}
				
				//we have gone through all transition
				//check that we have found a destination
				if (!listNewState.isEmpty()) {
					
					//get a appropriate name for our new state
					String nameDestination = "";
					nameDestination = constructNameOfDeterminedStates(listNewState);
					
					
					//check that it doesn't already exist
					//we will get the Id we need after otherwise
					int nameId = getIdStateFromNameList(determinedAutomaton.getAllStates(), nameDestination);
					
					//if an id hasn't been found, we can create a new State
					//creating a new state mean that it will be added to listDeterminedState
					if (nameId < 0) {
						//we can create our new state
						State newState = new State(id, nameDestination);
						id++;
						
						//create a new state in determinedAutomaton
						determinedAutomaton.addState(newState, false, isFinal);
						determinedAutomaton.addTransition(stateDeparture, newState, letter);
						
						//add the list of state to be search through
						listDeterminedStates.add(listNewState);
						listNewState = new ArrayList<State>();
					}
					else {
						//add a transition
						determinedAutomaton.addTransition(stateDeparture, determinedAutomaton.getStateById(nameId), letter);
					}
					
					//reset the list of added state
					listNewState.clear();
				}
			}
			listTransitions.clear();
		}
		
		return determinedAutomaton;
	}
	
}
