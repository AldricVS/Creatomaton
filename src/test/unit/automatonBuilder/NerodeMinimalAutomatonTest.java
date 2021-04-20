package test.unit.automatonBuilder;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import data.State;
import process.builders.AutomatonBuilder;
import process.builders.NerodeAutomatonBuilder;
import process.file.ImageCreator;;

/**
 * @author Maxence
 */
public class NerodeMinimalAutomatonTest {

	private static Automaton automaton;
	private static State state0;
	private static State state1;
	private static State state2;
	private static State state3;
	private static State state4;
	private static State state5;

	@BeforeClass
	public static void prepareAutomaton() {
		automaton = new Automaton("ab");
		state0 = new State(0, "0");
		state1 = new State(1, "1");
		state2 = new State(2, "2");
		state3 = new State(3, "3");
		state4 = new State(4, "4");
		state5 = new State(5, "5");
		
		// (e+b+ab) (ab+aab)* bb (a+b)*

		automaton.addState(state0, true, false);
		automaton.addState(state1, true, false);
		automaton.addState(state2, false, false);
		automaton.addState(state3, false, false);
		automaton.addState(state4, false, true);
		automaton.addState(state5, false, true);

		automaton.addTransition(state0, state1, 'a');
		automaton.addTransition(state0, state3, 'b');
		automaton.addTransition(state1, state0, 'b');
		automaton.addTransition(state1, state2, 'a');
		automaton.addTransition(state2, state0, 'b');
		automaton.addTransition(state3, state4, 'b');
		automaton.addTransition(state4, state5, 'a');
		automaton.addTransition(state4, state5, 'b');
		automaton.addTransition(state5, state4, 'b');
		automaton.addTransition(state5, state5, 'a');
	}

	@Test
	public void checkNerodeMinimalAutomaton() {
		AutomatonBuilder builder = new AutomatonBuilder(automaton);
		Automaton minimalAutomaton = builder.buildMinimalAutomaton();

		try {
			ImageCreator image = new ImageCreator(automaton, "NerodeTest");
			image.setDoesTryToGetNames(true);
			image.createImageFile();
			image = new ImageCreator(minimalAutomaton, "NerodeTest_Mini");
			image.createImageFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		NerodeAutomatonBuilder nerodeBuilder = new NerodeAutomatonBuilder(minimalAutomaton);
		assertTrue(nerodeBuilder.checkMinimalByNerode());
		//TODO make it true
	}

}
