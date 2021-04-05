package test.manual;

import java.io.IOException;

import data.Automaton;
import process.builders.RandomAutomatonBuilder;
import process.file.AutomatonFileHelper;
import process.file.ImageCreator;

public class RandomAutomatonTest {
	
	private static boolean mustOverwrite = false;

	public static void main(String[] args) throws IOException {
		for (int index = 0; index < 5; index++) {
			RandomAutomatonBuilder randomAutomatonBuilder = new RandomAutomatonBuilder(3 + index, index);
			randomAutomatonBuilder.setAlphabet("abc");
			randomAutomatonBuilder.setNumberOfFinalStates(index + 1);
			Automaton automaton = null;
			ImageCreator imageCreator = new ImageCreator(automaton, "test");
			imageCreator.setMustEraseDotFiles(mustOverwrite);
			AutomatonFileHelper automatonFileHelper = new AutomatonFileHelper();
			automatonFileHelper.setMustOverwriteFiles(mustOverwrite);
			try {
				automaton = randomAutomatonBuilder.build();
				imageCreator.setAutomaton(automaton);
				imageCreator.createImageFile();
				automatonFileHelper.saveAutomaton(automaton, "test");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}
}
