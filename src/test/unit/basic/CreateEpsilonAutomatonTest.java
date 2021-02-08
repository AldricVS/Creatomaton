package test.unit.basic;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import data.AutomatonConstants;
import data.State;
import process.AutomatonManager;

/**
 * @author Maxence
 */
public class CreateEpsilonAutomatonTest {

	private static Automaton automaton;
	private static State state0;
	private static State state1;
	private static State state2;
	private static State state3;
	private static State state4;

	@BeforeClass
	public static void prepareAutomaton() {
		automaton = new Automaton("abc");
		state0 = new State(0);
		state1 = new State(1);
		state2 = new State(2);
		state3 = new State(3);
		state4 = new State(4);
		
		//automate -> a(e.c)+(b.c)
		//only valid word: ac & abc
		automaton.addState(state0, true, false);
		automaton.addState(state1, false, false);
		automaton.addState(state2, false, false);
		automaton.addState(state3, false, false);
		automaton.addState(state4, false, true);
		
		automaton.addTransition(state0, state1, 'a');
		automaton.addEpsilonTransition(state1, state2);
		automaton.addTransition(state2, state4, 'c');
		automaton.addTransition(state1, state3, 'b');
		automaton.addTransition(state3, state4, 'c');
	}
	
	@Test
	public void has5states() {
		assertEquals(5, automaton.getNumberOfTotalStates());
	}
	
	@Test
	public void recognize2Words() {
		AutomatonManager manager = new AutomatonManager(automaton);
		String word1 = "ac";
		String word2 = "abc";
		assertTrue(manager.validateAutomaton(word1));
		assertTrue(manager.validateAutomaton(word2));
	}
	
	@Test
	public void dontRecognizeOtherWord() {
		AutomatonManager manager = new AutomatonManager(automaton);
		assertFalse(manager.validateAutomaton("acc"));
	}
	
}
