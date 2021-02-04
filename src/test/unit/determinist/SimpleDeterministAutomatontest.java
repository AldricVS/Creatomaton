package test.unit.determinist;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import data.State;
import process.AutomatonManager;
import process.builders.AutomatonBuilder;

/**
 * @author Maxence
 */
public class SimpleDeterministAutomatontest {

	private static Automaton automaton;
	private static State state0;
	private static State state1;
	private static State state2;

	@BeforeClass
	public static void prepareAutomaton() {
		automaton = new Automaton("ab");
		state0 = new State(0);
		state1 = new State(1);
		state2 = new State(2);

		// automate -> e + a + ba
		automaton.addState(state0, true, true);
		automaton.addState(state1, false, false);
		automaton.addState(state2, false, true);

		automaton.addTransition(state0, state1, 'b');
		automaton.addTransition(state1, state2, 'a');
		automaton.addTransition(state0, state2, 'a');
	}

	@After
	public void isValid() {
		AutomatonManager manager = new AutomatonManager(automaton);
		String word = "";
		assertTrue(manager.validateAutomaton(word));
		word = "a";
		assertTrue(manager.validateAutomaton(word));
		word = "ba";
		assertTrue(manager.validateAutomaton(word));
	}

	@After
	public void isInvalid() {
		AutomatonManager manager = new AutomatonManager(automaton);
		String word;
		word = "b";
		assertFalse(manager.validateAutomaton(word));
		word = "ab";
		assertFalse(manager.validateAutomaton(word));
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
