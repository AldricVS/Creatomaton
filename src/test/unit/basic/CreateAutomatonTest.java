package test.unit.basic;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import data.State;
import data.Transition;
import process.AutomatonManager;
import process.builders.AutomatonBuilder;
import process.util.StateListUtility;

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
		boolean test = false;
		String word = "ab";
		
		AutomatonManager automatonManager = new AutomatonManager(automaton);
		test = automatonManager.validateAutomaton(word);
		
		assertTrue(test);
	}
	
	
	@Test
	public void isMiror() {
		AutomatonBuilder automatonBuilder = new AutomatonBuilder(automaton);
		Automaton automatonMiror = automatonBuilder.buildMirrorAutomaton();
		
		assertTrue(automatonMiror.isStateInitial(2));
		assertTrue(automatonMiror.isStateFinal(0));
		
		int test = automatonMiror.getStateById(1).getTransitions().get(0).getDestination().getId();
		assertEquals(0, test);
		
	}
	
	@Test
	public void constructStateName() {
		String stateName = "";
		
		//test that the name is correctly generated
		stateName = StateListUtility.constructNameOfDeterminedStates(automaton.getAllStates());
		assertEquals("0;1;2", stateName);
		
		//test that the list is sorted, so that the order of adding dont matter
		List<State> listState = new ArrayList<State>();
		listState.add(state2);
		listState.add(state0);
		listState.add(state1);
		listState.add(state0);
		listState.add(state2);
		stateName = StateListUtility.constructNameOfDeterminedStates(listState);
		assertEquals("0;0;1;2;2", stateName);
	}
}
