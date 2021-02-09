package test.demo;

import data.Automaton;
import data.State;
import process.builders.AutomatonBuilder;

public class MinimalAutomatonTest {

	public static void main(String[] args) {
		Automaton automaton = new Automaton("abc");
		State state0 = new State(0);
		automaton.addState(state0, true, false); 		// état 0 initial
		State state1 = new State(1);
		automaton.addState(state1, true, false); 		// état 1 initial
		State state2 = new State(2);
		automaton.addState(state2, false, true); 		// état 2 final
		
		automaton.addTransition(state0, state0, 'a');	// 0 --> 'a' --> 0
		automaton.addTransition(state0, state1, 'a');	// 0 --> 'a' --> 1 ==> non déterministe
		automaton.addEpsilonTransition(state0, state2);	// 0 --> epsilon --> 2 ==> non synchronisé
		automaton.addTransition(state1, state2, 'b');	// 1 --> 'b' --> 2
		automaton.addTransition(state2, state2, 'c');	// 2 --> 'c' --> 2
		
		BuilderTest.createImage(automaton, "min_1", false);
		
		AutomatonBuilder builder = new AutomatonBuilder(automaton);
		Automaton mirAutomaton = builder.buildMirrorAutomaton();
		BuilderTest.createImage(mirAutomaton, "min_2", true);
		
		builder.setAutomaton(mirAutomaton);
		Automaton detMirAutomaton = builder.buildDeterministicAutomaton();
		BuilderTest.createImage(detMirAutomaton, "min_3", true);
		
		builder.setAutomaton(detMirAutomaton);
		Automaton mirDetMirAutomaton = builder.buildMirrorAutomaton();
		BuilderTest.createImage(mirDetMirAutomaton, "min_4", true);
		
		builder.setAutomaton(mirDetMirAutomaton);
		Automaton minimalAutomaton = builder.buildDeterministicAutomaton();
		BuilderTest.createImage(minimalAutomaton, "min_5", true);
	}

}
