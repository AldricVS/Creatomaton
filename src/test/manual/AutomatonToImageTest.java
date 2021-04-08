package test.manual;

import java.io.IOException;
import java.text.ParseException;

import data.Automaton;
import process.builders.AutomatonBuilder;
import process.builders.ThompsonAutomatonBuilder;
import process.file.ImageCreator;

/**
 * @author Maxence
 */
public class AutomatonToImageTest {
	public static void main(String[] args) {
		ThompsonAutomatonBuilder AutomatonBuilder = new ThompsonAutomatonBuilder("a.(a+b).(b*)");
		Automaton automaton = null;
		try {
			automaton = AutomatonBuilder.build();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		try {
			ImageCreator imageCreator = new ImageCreator(automaton, "wellThompsonBefore");
			imageCreator.createImageFile();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		AutomatonBuilder builder = new AutomatonBuilder(automaton);
//		automaton = builder.buildDeterministicAutomaton();
//		builder.setAutomaton(automaton);
		automaton = builder.addWellState();
		
		try {
			ImageCreator imageCreator = new ImageCreator(automaton, "wellThompsonAfter");
			imageCreator.createImageFile();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
