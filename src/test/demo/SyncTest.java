package test.demo;

import data.Automaton;
import data.State;
import process.builders.AutomatonBuilder;

public class SyncTest {

	public static void main(String[] args) {
		Automaton automaton = new Automaton("abc");
		State state0 = new State(1);
		automaton.addState(state0, true, false); 		// état 0 initial
		State state1 = new State(2);
		automaton.addState(state1, false, false); 		// état 1 initial
		State state2 = new State(3);
		automaton.addState(state2, false, true); 		// état 2 final
		
		automaton.addTransition(state0, state0, 'a');	// 0 --> 'a' --> 0
		automaton.addEpsilonTransition(state0, state1);	// 0 --> epsilon --> 2 ==> non synchronisé
		automaton.addTransition(state1, state1, 'b');	// 1 --> 'b' --> 2
		automaton.addEpsilonTransition(state1, state2);	// 0 --> epsilon --> 2 ==> non synchronisé
		automaton.addTransition(state2, state2, 'c');	// 2 --> 'c' --> 2
		
		BuilderTest.createImage(automaton, "sync_avant", false);
		
		AutomatonBuilder builder = new AutomatonBuilder(automaton);
		Automaton synchronizedAutomaton = builder.buildSynchronizedAutomaton();
		BuilderTest.createImage(synchronizedAutomaton, "sync_apres", false);
	}

}
