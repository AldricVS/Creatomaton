package test.demo;

import java.io.File;
import java.io.IOException;

import data.Automaton;
import data.State;
import process.builders.AutomatonBuilder;
import process.builders.DotBuilder;
import process.helpers.GraphvizHelper;

public class BuilderTest {

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
		
		createImage(automaton, "Automate_BASE", false);
		
		AutomatonBuilder automatonBuilder = new AutomatonBuilder(automaton);
		
		Automaton synchronizedAutomaton = automatonBuilder.buildSynchronizedAutomaton();
		createImage(synchronizedAutomaton, "Automate_SYNCHRO", false);
		
		automatonBuilder.setAutomaton(synchronizedAutomaton);
		Automaton deterministicAutomaton = automatonBuilder.buildDeterministicAutomaton();
		createImage(deterministicAutomaton, "Automate_DETER", false);
		
		automatonBuilder.setAutomaton(automaton);
		Automaton mirrorAutomaton = automatonBuilder.buildMirrorAutomaton();
		createImage(mirrorAutomaton, "Automate_MIROIR", true);
		
		automatonBuilder.setAutomaton(synchronizedAutomaton);
		Automaton minimalAutomaton = automatonBuilder.buildMinimalAutomaton();
		createImage(minimalAutomaton, "Automate_MINI", false);
	}

	
	public static void createImage(Automaton automaton, String fileName, boolean isMirror) {
		File f = new File(fileName + ".dot");
		DotBuilder dotBuilder = new DotBuilder(automaton);
		dotBuilder.setInReverseMode(isMirror);
		//dotBuilder.setIsTriyingToGetStatesNames(false);
		dotBuilder.buildDotFile(f);
		
		try {
			GraphvizHelper graphvizHelper = new GraphvizHelper(f.getAbsolutePath());
			graphvizHelper.setFileOutputName(fileName + ".jpg");
			graphvizHelper.runCommand();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
