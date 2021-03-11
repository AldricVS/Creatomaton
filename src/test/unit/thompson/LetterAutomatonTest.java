package test.unit.thompson;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import data.Transition;
import process.AutomatonManager;
import process.factory.ThompsonAutomatonFactory;

/**
 * Tests to check a "letter Thompson" automaton
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class LetterAutomatonTest {
	
	private static Automaton automaton;
	
	@BeforeClass
	public static void createLetterAutomaton() {
		automaton = ThompsonAutomatonFactory.createLetterAutomaton('a');
	}
	
	@Test
	public void testWholeAutomaton() {
		/* We must have only one initial state and this one must have one transition with letter 'a'
		 * Moreover, the transition must go to the final state*/
		assertEquals(1, automaton.getNumberOfInitialStates());
		
		List<Transition> transitions = automaton.getInitialStates().get(0).getTransitions();
		assertEquals(1, transitions.size());
		
		Transition aTransition = transitions.get(0);
		assertEquals('a', aTransition.getLetter());
		
		assertEquals(automaton.getFinalStates().get(0), aTransition.getDestination());
	}
	
	@Test
	public void acceptAutomaton() {
		AutomatonManager manager = new AutomatonManager(automaton);
		assertTrue(manager.validateAutomaton("a"));
		assertFalse(manager.validateAutomaton(""));
	}
}
