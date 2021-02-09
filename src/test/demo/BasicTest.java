package test.demo;

import java.io.File;
import java.io.IOException;

import data.Automaton;
import data.State;
import process.builders.DotBuilder;
import process.helpers.GraphvizHelper;

public class BasicTest {

	public static void main(String[] args) {
		
		Automaton automaton = new Automaton("abc");
		State state0 = new State(0);
		automaton.addState(state0, true, false); 		// état 0 initial
		State state1 = new State(1);
		automaton.addState(state1); 					// état 1 ni initial, ni final
		State state2 = new State(2);
		automaton.addState(state2, false, true); 		// état 2 final
		
		automaton.addTransition(state0, state1, 'a');	// 0 --> 'a' --> 1
		automaton.addEpsilonTransition(state0, state2);	// 0 --> epsilon --> 2
		automaton.addTransition(state1, state2, 'b');	// 1 --> 'b' --> 2
		automaton.addTransition(state2, state2, 'c');	// 2 --> 'c' --> 2
		
		File f = new File("test.dot");
		DotBuilder dotBuilder = new DotBuilder(automaton);
		dotBuilder.buildDotFile(f);
		
		try {
			GraphvizHelper graphvizHelper = new GraphvizHelper(f.getAbsolutePath());
			graphvizHelper.setFileOutputName("basicAutomaton.jpg");
			graphvizHelper.runCommand();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
