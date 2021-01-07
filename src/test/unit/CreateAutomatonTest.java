package test.unit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
		
		List<State> listState = new ArrayList<State>();
		listState.addAll(automaton.getInitialStates());
		
		List<Transition> listTransitions = new ArrayList<Transition>();
		
		String alphabet = automaton.getAlphabet();
		Automaton determinedAutomaton = new Automaton(alphabet);
		
		//for each of the alphabet letter
		if (!listState.isEmpty()) {
			//we add all transition from all the state we are searching
			for (Iterator<State> it = listState.iterator(); it.hasNext(); ) {
				addingState = it.next();
				listTransitions.addAll(addingState.getTransitions());
			}
			listState.clear();
			
			//we now have all transition to go through
			for (char letter : alphabet.toCharArray()) {	
				//we search all transition, and add as a new state
				for (Iterator<Transition> it = listTransitions.iterator(); it.hasNext(); ) {
					addingTransition = it.next();
					if (addingTransition.getLetter() == letter) {
						listState.add(addingTransition.getDestination());						
					}
				}
				//we have gone through all transition
				//check that we have found a destination
				if (!listState.isEmpty()) {
					//we can create our new state
						//create the State
					//check that it doesn't already exist
						//create a new state in determinedAutomaton
					//else
						//add a transition
				}
				listState.clear();
			}
			listTransitions.clear();
		}
		
	}
}
