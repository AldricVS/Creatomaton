package test.unit.thompson;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import data.State;
import data.Transition;
import process.factory.ThompsonAutomatonFactory;

/**
 * Tests to check a "letter Thompson" automaton
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class UnionAutomatonTest {
	
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
		//TODO : Ajouter des tests d'acceptation une fois que les algos seront implémentés 
	}
}
