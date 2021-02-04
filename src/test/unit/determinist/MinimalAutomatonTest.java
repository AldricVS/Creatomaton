package test.unit.determinist;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import data.State;


/**
 * @author Maxence
 */
public class MinimalAutomatonTest {
	
	private static Automaton automaton;
	private static State state0;
	private static State state1;
	private static State state2;
	private static State state3;
	private static State state4;
	private static State state5;
	private static State state6;
	private static State state7;
	private static State state8;
	private static State state9;

	@BeforeClass
	public static void prepareAutomaton() {
		automaton = new Automaton("abc");
		state0 = new State(0);
		state1 = new State(1);
		state2 = new State(2);
		state3 = new State(3);
		state4 = new State(4);
		state5 = new State(5);
		state6 = new State(6);
		state7 = new State(7);
		state8 = new State(8);
		state9 = new State(9);
		
		//automate -> (ab*) + (a+c).(c*).b 
		
		automaton.addState(state0, true, true);
		automaton.addState(state1, false, false);
		automaton.addState(state2, false, true);
		
		automaton.addState(state3, true, false);
		automaton.addState(state4, false, false);
		automaton.addState(state5, false, false);
		automaton.addState(state6, false, false);
		automaton.addState(state7, false, false);
		automaton.addState(state8, false, false);
		automaton.addState(state9, false, false);
		
		automaton.addTransition(state0, state1, 'a');
		automaton.addTransition(state1, state2, 'b');
		automaton.addEpsilonTransition(state2, state0);
		
		automaton.addTransition(state3, state4, 'a');
		automaton.addEpsilonTransition(state4, state4);
		automaton.addTransition(state3, state5, 'a');
		automaton.addTransition(state3, state6, 'a');
		automaton.addEpsilonTransition(state6, state7);
		automaton.addTransition(state3, state7, 'c');
	}

}
