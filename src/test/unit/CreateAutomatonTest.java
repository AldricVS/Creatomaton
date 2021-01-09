package test.unit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import data.State;
import data.Transition;

/**
 * Muliple tests that check if an automaton is correctly created.<p>
 * We have an alphabet of 3 letters : 'a', 'b' and 'c' 
 * The automaton will have 3 states "0, 1, 2", with 0 the initial state and 2 the final.<p>
 * There will be also those states :
 * <ul>
 * 	<li>0 --> 1 with letter 'a'</li> 
 * 	<li>1 --> 2 with letter 'b'</li> 
 * 	<li>1 --> 1 with letter 'a' (loop)</li> 
 * 	<li>2 --> 2 with letter 'c' (loop)</li> 
 * </ul>
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class CreateAutomatonTest {
	
	private static Automaton automaton;
	private static State state0;
	private static State state1;
	private static State state2;
	
	
	@BeforeClass
	public static void prepareAutomaton() {
		automaton = new Automaton("abc");
		state0 = new State(0);
		state1 = new State(1);
		state2 = new State(2); //state2 is final
		
		//automate -> a (a*) b (c*)
		automaton.addState(state0, true, false);
		automaton.addState(state1, false, false);
		automaton.addState(state2, false, true);
		
		automaton.addTransition(state0, state1, 'a');
		automaton.addTransition(state1, state1, 'a');
		automaton.addTransition(state1, state2, 'b');
		automaton.addTransition(state2, state2, 'c');
	}
	
	@Test
	public void assertBasicData() {
		//normally, we have only 1 initial state, 1 final state and 3 states in total
		assertEquals(1, automaton.getNumberOfInitialStates());
		assertEquals(1, automaton.getNumberOfFinalStates());
		assertEquals(3, automaton.getNumberOfTotalStates());
	}
	
	@Test
	public void testStartingState() {
		//check if starting state of automaton is state0
		//and if the following state is state 1
		State normallyState0 = automaton.getInitialStates().get(0);
		assertEquals(0, normallyState0.getId());
		
		Transition transition = normallyState0.getTransitions().get(0);
		assertEquals('a', transition.getLetter());
		
		State normallyState1 = transition.getDestination();
		assertEquals(1, normallyState1.getId());
	}
	
	@Test
	public void testState1() {
		//check if have 2 transitions : one loop and one going to state 2
		
		List<Transition> transitions = state1.getTransitions();
		assertEquals(2, transitions.size());
		
		for(Transition t : transitions) {
			if(t.getLetter() == 'a') {
				assertEquals(1, t.getDestination().getId());
			}else if(t.getLetter() == 'b') {
				assertEquals(2, t.getDestination().getId());
			}else {
				fail("The transition " + t.getLetter() + " is not supposed to be here");
			}
		}
	}
	
	@Test 
	public void checkTransitions() {
		//check if 0 --> 1 with letter a
		State s0 = automaton.getInitialStates().get(0);
		State s1 = s0.findNextState('a');
		assertEquals(1, s1.getId());
		
		//and if 1 --> 1 with letter a
		State s1Bis = s1.findNextState('a');
		assertEquals(1, s1Bis.getId());
	}
	
	@Test
	public void checkAutomaton() {
		String testAlphabet = "ab";
		boolean hasFinish = false;
		
		//our automaton recognize any alphabet a (a*) b (c*)
		State startingState = automaton.getInitialStates().get(0);
		State addingState;
		Transition addingTransition;
		
		List<State> listState = new ArrayList<State>();
		listState.add(startingState);
		
		List<Transition> listTransitions = new ArrayList<Transition>();
		
		//we search through our list of transition for any possible transition to add
		//then we do it again until we arrived at a final state
		while (!testAlphabet.trim().isEmpty()) {
			//first, get the next letter to search and reduced our word
			char nextLetter = testAlphabet.charAt(0);
			testAlphabet = testAlphabet.substring(1);
			
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
		}
		//if we arrived here, then our listState got all final state
		for (Iterator<State> it = listState.iterator(); it.hasNext(); ) {
			State finalState = it.next();
			if (automaton.isStateFinal(finalState)) {
				hasFinish = true;
			}
		}
		
		assertTrue(hasFinish);
	}
	
	
	@Test
	public void isDertemined() {
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
		
		//which test TODO to assure that our automaton is determined
		
	}
}
