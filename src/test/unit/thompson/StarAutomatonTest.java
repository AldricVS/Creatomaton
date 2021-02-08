package test.unit.thompson;

import static org.junit.Assert.*;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import data.AutomatonConstants;
import data.State;
import data.Transition;
import process.factory.ThompsonAutomatonFactory;

/**
 * Tests to check the "star Thompson" automaton. Accept {@code a*}
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class StarAutomatonTest {
	
	private static Automaton letterAutomaton;
	private static Automaton automaton;
	
	@BeforeClass
	public static void createLetterAutomaton() {
		letterAutomaton = ThompsonAutomatonFactory.createLetterAutomaton('a');
		automaton = ThompsonAutomatonFactory.createStarAutomaton(letterAutomaton);
	}
	
	@Test
	public void testNumberOfStates() {
		//there is normally 1 initial, 1 final, and 4 total states
		assertEquals(1, automaton.getNumberOfInitialStates());
		assertEquals(1, automaton.getNumberOfFinalStates());
		assertEquals(4, automaton.getNumberOfTotalStates());
	}
	
	@Test
	public void testTransitions() {
		State initialState = automaton.getInitialStates().get(0);
		State finalState = automaton.getFinalStates().get(0);
		
		//initial state have 2 epsilon transitions
		List<State> epsilonTransitionsStates = initialState.findNextStatesList(AutomatonConstants.EPSILON_CHAR);
		assertEquals(2, epsilonTransitionsStates.size());
		
		//check the transitions in the letter automaton
		for(State state : epsilonTransitionsStates) {
			if(state != finalState) {
				//path is a --> epsilon (2 paths here)
				State letterFinalState = state.findNextState('a');
				assertNotNull(letterFinalState);
				List<State> finalTransitionStates = letterFinalState.findNextStatesList(AutomatonConstants.EPSILON_CHAR);
				for(State s : finalTransitionStates) {
					if(s != finalState) {
						//we must have been at the begining of the letter automaton
						assertTrue(s == state);
					}
				}
			}
		}
	}
}
