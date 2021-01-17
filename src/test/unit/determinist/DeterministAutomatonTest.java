package test.unit.determinist;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import data.AutomatonConstants;
import data.State;

/**
 * @author Maxence
 */
public class DeterministAutomatonTest {
	
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
		state0 = new State(3);
		state1 = new State(4);
		state2 = new State(5);
		
		//automate -> a(a*)b + bc
		automaton.addState(state0, true, false);
		automaton.addState(state1, false, false);
		automaton.addState(state2, false, false);
		automaton.addState(state3, false, false);
		automaton.addState(state4, false, false);
		automaton.addState(state5, false, true);
		
		automaton.addTransition(state0, state1, 'a');
		automaton.addTransition(state1, state1, 'a');
		automaton.addTransition(state1, state2, AutomatonConstants.EPSILON_CHAR);
		automaton.addTransition(state2, state1, AutomatonConstants.EPSILON_CHAR);
		automaton.addTransition(state2, state5, 'b');
		
		automaton.addTransition(state0, state3, 'b');
		automaton.addTransition(state3, state4, AutomatonConstants.EPSILON_CHAR);
		automaton.addTransition(state4, state4, AutomatonConstants.EPSILON_CHAR);
		automaton.addTransition(state4, state5, 'c');
	}
	
	@Test
	public void isDeterministic() {
		fail("Not yet implemented");
	}
	
	@Test
	public void derterminedAutomaton() {
		fail("Not yet implemented");
	}
	
	@Test
	public void isDeterministicAfter() {
		fail("Not yet implemented");
	}

}
