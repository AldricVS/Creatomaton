/**
 * 
 */
package process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import data.Automaton;
import data.State;
import data.Transition;

/**
 * Main class to manage Automaton
 * Can validate and determined an Automaton
 * @author Maxence
 */
public class AutomatonManager {
	
	
	public boolean validateAutomaton (String word, Automaton automaton) {
		boolean hasFinish = false;
		State addingState;
		Transition addingTransition;
		
		State startingState = automaton.getInitialStates().get(0);
		
		List<State> listState = new ArrayList<State>();
		listState.add(startingState);
		
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
				for (Iterator<State> it = listState.iterator(); it.hasNext(); ) {
					addingState = it.next();
					listTransitions.addAll(addingState.getTransitions());
				}
				listState.clear();
				
				//we added all transition, add all next state that we are looking for
				for (Iterator<Transition> it = listTransitions.iterator(); it.hasNext(); ) {
					addingTransition = it.next();
					if ((addingTransition.isEpsilon()) ||
						(addingTransition.getLetter() == nextLetter)) {
						//little check that the destination isn't already here
						State nextState = addingTransition.getDestination();
						if (!listState.contains(nextState)) {
							listState.add(nextState);
						}
					}
					//else, we dont have the right letter for the transition
				}
				listTransitions.clear();
			}
			//if its empty, then we can stop our research
			else {
				word = "";
			}
		}
		//if we arrived here, then our listState got all final state
		for (Iterator<State> it = listState.iterator(); it.hasNext(); ) {
			State finalState = it.next();
			if (automaton.isStateFinal(finalState)) {
				hasFinish = true;
			}
		}
		
		return hasFinish;
	}
	
	public Automaton determinedAutomaton(Automaton automaton) {
		State addingState;
		Transition addingTransition;
		
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
			String nameDeparture = "";
			for (Iterator<State> it = listState.iterator(); it.hasNext(); ) {
				addingState = it.next();
				if (nameDeparture.isEmpty()) {
					nameDeparture = String.valueOf(addingState.getId());
				}
				else {
					nameDeparture = nameDeparture + ";" + addingState.getId();
				}
				listTransitions.addAll(addingState.getTransitions());
			}
			//we dont have any interest in our old list
			//TODO maybe later, use this list to get the name of the starting state
			listState.clear();
			
			//get the state's name from which we go from
			//and search the state from the automaton's list
			int stateStartingId = -1;
			for (Iterator<State> it = determinedAutomaton.getAllStates().iterator(); it.hasNext(); ) {
				//get the key/value set
				addingState = it.next();
				String nameCheck = addingState.getName();
				//compare with all name
				if (nameDeparture.equals(nameCheck)) {
					stateStartingId = addingState.getId();
				}
			}
			
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

				//we search all transition, and add as a new state
				for (Iterator<Transition> it = listTransitions.iterator(); it.hasNext(); ) {
					addingTransition = it.next();
					if ((addingTransition.isEpsilon()) || (addingTransition.getLetter() == letter)) {
						addingState = addingTransition.getDestination();
						listNewState.add(addingState);
						if (automaton.isStateFinal(addingState)) {
							isFinal = true;
						}
					}
				}
				
				//we have gone through all transition
				//check that we have found a destination
				if (!listNewState.isEmpty()) {
					
					//get a appropriate name for our new state
					String nameDestination = "";
					for (Iterator<State> it = listNewState.iterator(); it.hasNext(); ) {
						addingState = it.next();
						if (nameDestination.isEmpty()) {
							nameDestination = String.valueOf(addingState.getId());
						}
						else {
							nameDestination = nameDestination + ";" + addingState.getId();
						}
						//TODO erreur avec par exemple 2;0 & 0;2
					}
					
					
					//check that it doesn't already exist
					//we will get the Id we need after otherwise
					int nameId = -1;
					for (Iterator<State> it = determinedAutomaton.getAllStates().iterator(); it.hasNext(); ) {
						//get the key/value set
						addingState = it.next();
						String nameCheck = addingState.getName();
						//compare with all name
						if (nameDestination.equals(nameCheck)) {
							nameId = addingState.getId();
						}
					}
					
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
						determinedAutomaton.addTransition(stateDeparture, determinedAutomaton.getAllStates().get(nameId), letter);
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
