package test.unit.automatonBuilder;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import process.builders.AutomatonBuilder;
import process.builders.NerodeAutomatonBuilder;
import process.builders.RandomAutomatonBuilder;
import process.file.ImageCreator;;

/**
 * @author Maxence
 */
public class NerodeMinimalRandomAutomatonTest {

	private static Automaton automaton;
	private static Automaton nerodeAutomaton;
	private static Automaton minimalAutomaton;

	@BeforeClass
	public static void prepareAutomaton() {
		RandomAutomatonBuilder randomAutomatonBuilder = new RandomAutomatonBuilder();
		randomAutomatonBuilder.setAlphabet("ab");
		randomAutomatonBuilder.setNumberOfStates(6);
		randomAutomatonBuilder.setNumberOfTransitions(8);
		randomAutomatonBuilder.setNumberOfEpsilonTransitions(0);
		automaton = randomAutomatonBuilder.build();
	}

	@Test
	public void checkNerodeMinimalAutomaton() {
		try {
			ImageCreator image = new ImageCreator(automaton, "NerodeRandTestBase");
			image.createImageFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		AutomatonBuilder builder = new AutomatonBuilder(automaton);
		minimalAutomaton = builder.buildMinimalAutomaton();

		NerodeAutomatonBuilder nerodeBuilder = new NerodeAutomatonBuilder(automaton);
		nerodeAutomaton = nerodeBuilder.buildNerodeAutomaton();

		try {
			ImageCreator image = new ImageCreator(nerodeAutomaton, "NerodeRandTestMini");
			image.createImageFile();
			image = new ImageCreator(minimalAutomaton, "NerodeRandTestMinimal");
			image.createImageFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertTrue(nerodeAutomaton.isEquals(minimalAutomaton));
	}

}
