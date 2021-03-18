package test.unit.thompson;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import process.AutomatonManager;
import process.factory.ThompsonAutomatonFactory;

/**
 * Tests to check a union of 2 "letter Thompson" automatons. Accept letter 'a'
 * or letter 'b'
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class UnionAutomatonTest {

	private static Automaton letterAutomaton1;
	private static Automaton letterAutomaton2;
	private static Automaton automaton;

	@BeforeClass
	public static void createLetterAutomaton() {
		letterAutomaton1 = ThompsonAutomatonFactory.createLetterAutomaton('a');
		letterAutomaton2 = ThompsonAutomatonFactory.createLetterAutomaton('b');
		automaton = ThompsonAutomatonFactory.createUnionAutomaton(letterAutomaton1, letterAutomaton2);
	}

	@Test
	public void testNumberOfStates() {
		// there is normally 1 initial, 1 final, and 6 total states
		assertEquals(1, automaton.getNumberOfInitialStates());
		assertEquals(1, automaton.getNumberOfFinalStates());
		assertEquals(6, automaton.getNumberOfTotalStates());
	}

	@Test
	public void testTransitions() {
		// check both paths :
		// one go from epsilon -> a -> epsilon
		// the other epsilon -> b -> epsilon
		AutomatonManager manager = new AutomatonManager(automaton);
		String word = "a";
		assertTrue(manager.validateAutomaton(word));
		word = "b";
		assertTrue(manager.validateAutomaton(word));
	}
}
