package test.unit.thompson;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import data.State;
import process.AutomatonManager;
import process.factory.ThompsonAutomatonFactory;

/**
 * Tests to check a "concatenation Thompson" automaton. we test the automaton
 * that accepts "ab"
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class ConcatenationAutomatonTest {

	private static Automaton letterAutomaton1;
	private static Automaton letterAutomaton2;
	private static Automaton concatAutomaton;

	@BeforeClass
	public static void createLetterAutomaton() {
		letterAutomaton1 = ThompsonAutomatonFactory.createLetterAutomaton('a');
		letterAutomaton2 = ThompsonAutomatonFactory.createLetterAutomaton('b');
		concatAutomaton = ThompsonAutomatonFactory.createConcatenationAutomaton(letterAutomaton1, letterAutomaton2);
	}

	@Test
	public void testNumberOfStates() {
		// we must have 1 initial state, 1 final, and 3 in total
		assertEquals(1, concatAutomaton.getNumberOfInitialStates());
		assertEquals(1, concatAutomaton.getNumberOfFinalStates());
		assertEquals(3, concatAutomaton.getNumberOfTotalStates());
	}

	@Test
	public void testIsRealCopy() {
		// just check if letterAutomaton1 and concatAutomaton references are not the same
		State originalInitialState = letterAutomaton1.getInitialStates().get(0);
		State concantInitialState = concatAutomaton.getInitialStates().get(0);
		assertEquals(originalInitialState, concantInitialState);
		assertNotEquals(originalInitialState.findNextState('a'), concantInitialState.findNextState('a'));
	}

	@Test
	public void testTransitions() {
		State initialState = concatAutomaton.getInitialStates().get(0);
		// check if the word "ab" is accepted
		State finalState = concatAutomaton.getFinalStates().get(0);
		assertEquals(initialState.findNextState('a').findNextState('b'), finalState);
	}

	@Test
	public void acceptAutomaton() {
		AutomatonManager manager = new AutomatonManager(concatAutomaton);
		assertTrue(manager.validateAutomaton("ab"));
		assertFalse(manager.validateAutomaton("a"));
	}
}
