package test.unit.files;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import data.AutomatonConstants;
import data.State;
import process.AutomatonManager;
import process.builders.AutomatonBuilder;

/**
 * @author Maxence
 */
public class LoadCustomFileTest {
	
	private static Automaton automaton;
	private static State state0;
	private static State state1;
	private static State state2;
	private static State state3;
	private static State state4;
	private static State state5;

	@BeforeClass
	public static void prepareAutomaton() {
		automaton = new Automaton("abc");
		state0 = new State(0);
		state1 = new State(1);
		state2 = new State(2);
		state3 = new State(3);
		state4 = new State(4);
		state5 = new State(5);
		
		//automate -> a(a*)b + bc
		automaton.addState(state0, true, false);
		automaton.addState(state1, false, false);
		automaton.addState(state2, false, false);
		automaton.addState(state3, false, false);
		automaton.addState(state4, false, false);
		automaton.addState(state5, false, true);
		
		automaton.addTransition(state0, state1, 'a');
		automaton.addTransition(state1, state1, 'a');
		automaton.addEpsilonTransition(state1, state2);
		automaton.addEpsilonTransition(state2, state1);
		automaton.addTransition(state2, state5, 'b');
		
		automaton.addTransition(state0, state3, 'b');
		automaton.addEpsilonTransition(state3, state4);
		automaton.addEpsilonTransition(state4, state4);
		automaton.addTransition(state4, state5, 'c');
	}
	
	@Before
	public void isValid() {
		AutomatonManager manager = new AutomatonManager(automaton);
		String word;
		word = "aab";
		assertTrue(manager.validateAutomaton(word));
		 word = "bc";
		assertTrue(manager.validateAutomaton(word));
	}
	
	@After
	public void isDeterministic() {
		AutomatonManager manager = new AutomatonManager(automaton);
		assertTrue(manager.isDeterminist());
	}
	
	@Test
	public void derterminedAutomaton() {
		AutomatonBuilder builder = new AutomatonBuilder(automaton);
		automaton = builder.buildDeterminedAutomaton();
	}
}
