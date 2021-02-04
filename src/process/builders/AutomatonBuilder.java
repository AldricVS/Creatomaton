/**
 * 
 */
package process.builders;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import data.Automaton;
import data.State;
import data.Transition;
import process.factory.AutomatonFactory;
import process.util.StateListUtility;
import process.util.TransitionListUtility;

/**
 * <p>AutomatonBuilder is used to create new modified Automaton from a given Automaton.</p>
 * 
 * @author Maxence
 */
public class AutomatonBuilder {

	private Automaton automaton;
	public AutomatonBuilder(Automaton automaton) {
		this.automaton = automaton;
	}
	
	/**
	 * Build a new {@link data.Automaton Automaton} as a Mirror, meaning all Final States are Initial, and Initial are Final.
	 * Transition also work in reverse, going from their starting point to their destination.
	 * <p>Use the {@link process.factory.AutomatonFactory#createCopy(Automaton) createCopy()} to create the Automaton</p>
	 * @return the new Mirror Automaton
	 */
	public Automaton buildMiroirAutomaton() {
		//some variable
		List<State> listState, listInitialState, listFinalState;
		
		//our new automaton
		Automaton miroirAutomaton;
		
		miroirAutomaton = AutomatonFactory.createCopy(automaton);
		//miroirAutomaton = new Automaton(automaton.getAlphabet());
		
		//get the list of Initial States from Final States
		listInitialState = new ArrayList<State>(miroirAutomaton.getFinalStates());
		
		//get the list of Final States from Initial States
		listFinalState = new ArrayList<State>(miroirAutomaton.getInitialStates());
		
		//get the list of all States
		listState = new ArrayList<State>(miroirAutomaton.getAllStates());
		
		//clear old list of states
		miroirAutomaton.clearAllStates();
		
		//add all states
		for (State state : listState) {
			miroirAutomaton.addState(state, false, false);
		}
		
		List<Transition> allTransitionFromListStates = TransitionListUtility.getAllTransitionFromListStates(listState);
		for (Transition transition : allTransitionFromListStates) {
			State state;
			state = TransitionListUtility.getDepartureFromTransition(listState, transition);
			state.removeTransition(transition);
			miroirAutomaton.addTransition(transition.getDestination(), state, transition.getLetter());
		}
		
		//set all initial states
		for (State state : listInitialState) {
			miroirAutomaton.setStateInitial(state, true);
		}
		
		//set all final states
		for (State state : listFinalState) {
			miroirAutomaton.setStateFinal(state, true);
		}
		
		return miroirAutomaton;
	}
	
	/**
	 * Build a new Automaton with all his States as determined.
	 * @return the new Determined Automaton
	 */
	public Automaton buildDeterminedAutomaton() {
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
			listTransitions = TransitionListUtility.getAllTransitionFromListStates(listState);
			
			String nameDeparture = "";
			nameDeparture = StateListUtility.constructNameOfDeterminedStates(listState);
			
			//we dont have any interest in our old list
			listState.clear();
			
			//get the state's name from which we go from
			//and search the state from the automaton's list
			int stateStartingId = StateListUtility.getIdStateFromNameInList(determinedAutomaton.getAllStates(), nameDeparture);
			
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

				listNewState = TransitionListUtility.getValidDestinationFromTransition(listTransitions, letter);
				if (StateListUtility.hasCommonStates(listNewState, automaton.getFinalStates())) {
					isFinal = true;
				}
				
				//we have gone through all transition
				//check that we have found a destination
				if (!listNewState.isEmpty()) {
					
					//get a appropriate name for our new state
					String nameDestination = "";
					nameDestination = StateListUtility.constructNameOfDeterminedStates(listNewState);
					
					
					//check that it doesn't already exist
					//we will get the Id we need after otherwise
					int nameId = StateListUtility.getIdStateFromNameInList(determinedAutomaton.getAllStates(), nameDestination);
					
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
	
	public Automaton buildMinimalAutomaton() {
		
		return null;
	}
	
}
