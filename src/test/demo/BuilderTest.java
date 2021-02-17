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
		
		Automaton automaton = new Automaton("ab");
		
		State state0 = new State(0);
		automaton.addState(state0, true, false);
		State state1 = new State(1);
		automaton.addState(state1, false, true);
		State state2 = new State(2);
		automaton.addState(state2, false, false);
		State state3 = new State(3);
		automaton.addState(state3, false, false);
		
		automaton.addTransition(state0, state2, 'a');
		automaton.addTransition(state2, state0, 'a');
		automaton.addTransition(state0, state3, 'b');
		
		automaton.addTransition(state2, state1, 'b');
		automaton.addTransition(state2, state2, 'b');
		automaton.addTransition(state2, state3, 'b');
		
		automaton.addTransition(state1, state1, 'b');
		automaton.addTransition(state1, state3, 'b');
		
		automaton.addTransition(state3, state3, 'a');
		automaton.addTransition(state3, state1, 'b');
		automaton.addEpsilonTransition(state3, state1);
		
		createImage(automaton, "Automate_BASE", false);
		
		AutomatonBuilder automatonBuilder = new AutomatonBuilder(automaton);
		
		Automaton synchronizedAutomaton = automatonBuilder.buildSynchronizedAutomaton();
		createImage(synchronizedAutomaton, "Automate_SYNCHRO", false);
		
		automatonBuilder.setAutomaton(automaton);
		Automaton deterministicAutomaton = automatonBuilder.buildDeterministicAutomaton();
		createImage(deterministicAutomaton, "Automate_DETER", false);
		
		automatonBuilder.setAutomaton(automaton);
		Automaton mirrorAutomaton = automatonBuilder.buildMirrorAutomaton();
		createImage(mirrorAutomaton, "Automate_MIROIR", true);
		
		automatonBuilder.setAutomaton(automaton);
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
