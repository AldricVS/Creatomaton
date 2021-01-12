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
// je suis passé par la 
/**
 * Tests to check a "concatenation Thompson" automaton.
 * we test the automaton that accepts "ab"
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
		//we must have 1 initial state, 1 final, and 3 in total
		assertEquals(1, concatAutomaton.getNumberOfInitialStates());
		assertEquals(1, concatAutomaton.getNumberOfFinalStates());
		assertEquals(3, concatAutomaton.getNumberOfTotalStates());
	}
	
	@Test
	public void testIsRealCopy() {
		//just check if letterAutomaton1 and concatAutomaton references are not the same
		State originalInitialState = letterAutomaton1.getInitialStates().get(0);
		assertFalse(originalInitialState == concatAutomaton.getInitialStates().get(0));
		assertTrue(originalInitialState.findNextState('a') != concatAutomaton.getInitialStates().get(0).findNextState('a'));
	}
	
	@Test
	public void testTransitions() {
		State initialState = concatAutomaton.getInitialStates().get(0);
		//check if the word "ab" is accepted
		State finalState = concatAutomaton.getFinalStates().get(0);
		assertTrue(initialState.findNextState('a').findNextState('b') == finalState);
	}
	
	@Test
	public void acceptAutomaton() {
		//TODO : Ajouter des tests d'acceptation une fois que les algos seront implémentés 
	}
}
