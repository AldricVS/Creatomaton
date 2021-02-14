package test.unit.basic;

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
public class OnlyEpsilonTest {

	private static Automaton automaton;
	private static State state0;
	private static State state1;
	private static State state2;

	@BeforeClass
	public static void prepareAutomaton() {
		automaton = new Automaton("a");
		state0 = new State(0);
		state1 = new State(1);
		state2 = new State(2);

		// automate -> (a*)e
		automaton.addState(state0, true, false);
		automaton.addState(state1, false, false);
		automaton.addState(state2, false, true);

		automaton.addEpsilonTransition(state0, state1);
		automaton.addTransition(state1, state1, 'a');
		automaton.addEpsilonTransition(state1, state2);
	}

	@After
	public void isValid() {
		AutomatonManager manager = new AutomatonManager(automaton);
		String word = "";
		assertTrue(manager.validateAutomaton(word));
	}

	@After
	public void isInvalid() {
		AutomatonManager manager = new AutomatonManager(automaton);
		String word;
		word = "a";
		assertFalse(manager.validateAutomaton(word));
	}

	@Test
	public void derterminedAutomaton() {
		AutomatonBuilder builder = new AutomatonBuilder(automaton);
		automaton = builder.buildDeterministicAutomaton();
	}
}
