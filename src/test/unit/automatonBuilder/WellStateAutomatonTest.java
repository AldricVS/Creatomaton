package test.unit.automatonBuilder;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import data.State;
import data.Transition;
import process.builders.AutomatonBuilder;
import process.builders.RandomAutomatonBuilder;
import process.file.ImageCreator;

/**
 * Unit test of the implementation of the well state
 * @author Maxence
 */
public class WellStateAutomatonTest {
	
	private static Automaton automaton;
	
	@BeforeClass
	public static void initAutomaton() {
		RandomAutomatonBuilder builderRandom = new RandomAutomatonBuilder();
		Automaton randomAutomaton = builderRandom.build();
		AutomatonBuilder builder = new AutomatonBuilder(randomAutomaton);
		automaton = builder.addWellState();
		
		ImageCreator image;
		try {
			image = new ImageCreator(automaton, "wellAutomatonWell");
			image.createImageFile();
			image = new ImageCreator(randomAutomaton, "wellAutomatonRand");
			image.createImageFile();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void allStateHasCompleteTransitionAlphabet() {
		String alphabet = automaton.getAlphabet();
		for (State state : automaton.getAllStates()) {
			for (char letter : alphabet.toCharArray()) {
				boolean hasLetter = false;
				for (Transition transition : state.getTransitions()) {
					if (transition.getLetter() == letter) {
						hasLetter = true;
					}
				}
				if (!hasLetter) {
					fail("State "+state.toString()+" does not contains the transtion letter "+letter);
				}
			}
		}
	}

}
