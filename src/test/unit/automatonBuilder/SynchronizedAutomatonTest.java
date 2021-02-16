package test.unit.automatonBuilder;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import data.State;
import data.Transition;
import process.builders.AutomatonBuilder;
import process.factory.AutomatonFactory;

public class SynchronizedAutomatonTest {

	private static Automaton automaton;
	private static AutomatonBuilder builder;
	private static State state0;
	private static State state1;
	private static State state2;
	private static State state3;

	@BeforeClass
	public static void prepareAutomaton() {

		automaton = new Automaton("abc");
		state0 = new State(0);
		state1 = new State(1);
		state2 = new State(2);
		state3 = new State(3); // state3 is final

		// automate -> (a*) (a*) (b*) (c*)
		automaton.addState(state0, true, false);
		automaton.addState(state1, false, false);
		automaton.addState(state2, false, false);
		automaton.addState(state3, false, true);

		automaton.addTransition(state0, state0, 'a');
		automaton.addEpsilonTransition(state0, state1);
		automaton.addTransition(state1, state1, 'a');
		automaton.addEpsilonTransition(state1, state2);
		automaton.addTransition(state2, state2, 'b');
		automaton.addEpsilonTransition(state2, state3);
		automaton.addTransition(state3, state3, 'c');

		builder = new AutomatonBuilder(automaton);
		automaton = builder.buildSynchronizedAutomaton();

	}

	@Test
	public void assertSynchronizedAutomaton() {
		assertEquals(4, automaton.getNumberOfTotalStates());
		assertEquals(4, automaton.getNumberOfFinalStates());
		assertEquals(1, automaton.getNumberOfInitialStates());

		List<State> listStates = automaton.getAllStates();
		for (State state : listStates) {
			switch (state.getId()) {
			case 1:
				assertEquals(3, state.getNumberOfTransition());
				break;
			case 2:
				assertEquals(2, state.getNumberOfTransition());
				break;
			case 3:
				assertEquals(1, state.getNumberOfTransition());
				break;
			default:
				break;
			}
		}
	}

	@Test
	public void synchronisationOfSynchronized() {
		builder.setAutomaton(automaton);
		Automaton automatonCopy = builder.buildSynchronizedAutomaton();
		//TODO comparer les deux, ils doivent être identique (methode en creation...)
	}

}
