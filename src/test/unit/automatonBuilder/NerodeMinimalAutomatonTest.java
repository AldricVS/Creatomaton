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
	private static Automaton nerodeAutomaton;
	private static Automaton minimalAutomaton;
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

		automaton.addState(state0, false, false);
		automaton.addState(state1, false, false);
		automaton.addState(state2, false, false);
		automaton.addState(state3, false, true);
		automaton.addState(state4, false, true);
		automaton.addState(state5, true, false);

		automaton.addTransition(state0, state2, 'a');
		automaton.addTransition(state0, state3, 'b');
		automaton.addTransition(state1, state3, 'a');
		automaton.addTransition(state1, state3, 'b');
		automaton.addTransition(state2, state0, 'a');
		automaton.addTransition(state2, state3, 'b');
		automaton.addTransition(state3, state1, 'a');
		automaton.addTransition(state3, state3, 'b');
		automaton.addTransition(state4, state2, 'a');
		automaton.addTransition(state4, state5, 'b');
		automaton.addTransition(state5, state2, 'b');
		automaton.addTransition(state5, state4, 'a');
	}

	@Test
	public void checkNerodeMinimalAutomaton() {
		NerodeAutomatonBuilder nerodeBuilder = new NerodeAutomatonBuilder(automaton);
		nerodeAutomaton = nerodeBuilder.buildNerodeAutomaton();
		AutomatonBuilder builder = new AutomatonBuilder(automaton);
		minimalAutomaton = builder.buildMinimalAutomaton();

		try {
			ImageCreator image = new ImageCreator(automaton, "NerodeTestBase");
			image.createImageFile();
			image = new ImageCreator(nerodeAutomaton, "NerodeTestMini");
			image.createImageFile();
			image = new ImageCreator(minimalAutomaton, "NerodeTestMinimal");
			image.createImageFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertTrue(nerodeAutomaton.isEquals(minimalAutomaton));
	}

}
