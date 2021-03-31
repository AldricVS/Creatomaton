package test.unit.automatonBuilder;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import data.State;
import process.builders.AutomatonBuilder;
import process.file.ImageCreator;

/**
 * @author Maxence
 */
public class SynchronisedAutomatonTest {

	private static Automaton automaton;
	private static Automaton automatonSynch;
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

		// automate -> ( (a*) (cc(b*)) )*
		automaton.addState(state0, true, true);
		automaton.addState(state1, false, false);
		automaton.addState(state2, false, false);
		automaton.addState(state3, false, false);
		automaton.addState(state4, false, false);

		automaton.addTransition(state0, state0, 'a');
		automaton.addTransition(state0, state1, 'c');
		automaton.addTransition(state1, state2, 'c');
		automaton.addEpsilonTransition(state2, state3);
		automaton.addEpsilonTransition(state3, state0);
		automaton.addTransition(state3, state3, 'b');
		automaton.addTransition(state3, state4, 'a');
		automaton.addEpsilonTransition(state4, state0);

		AutomatonBuilder builder = new AutomatonBuilder(automaton);
		automatonSynch = builder.buildSynchronizedAutomaton();
		ImageCreator imageCreator;
		try {
			imageCreator = new ImageCreator(automaton, "test");
			imageCreator.createImageFile();
			imageCreator = new ImageCreator(automatonSynch, "testSynch");
			imageCreator.createImageFile();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test() {
		assertEquals(3, automatonSynch.getNumberOfTotalStates());
	}

}
