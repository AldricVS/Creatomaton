package test.manual;

import java.io.File;
import java.io.IOException;

import data.Automaton;
import process.builders.AutomatonBuilder;
import process.builders.DotBuilder;
import process.builders.RandomAutomatonBuilder;
import process.file.PrefsFileHelper;
import process.helpers.GraphvizHelper;

public class RandomAutomatonTest {

	private static PrefsFileHelper prefsFileHelper;
	private static GraphvizHelper graphvizHelper;

	public static void main(String[] args) throws IOException {
		prefsFileHelper = new PrefsFileHelper();
		String defaultOutputFolderPath = prefsFileHelper.getPreference(PrefsFileHelper.DEFAULT_OUTPUT_FOLDER_KEY);
		String defaultInputFolderPath = prefsFileHelper.getPreference(PrefsFileHelper.DEFAULT_INPUT_FOLDER_KEY);

		for (int i = 0; i < 5; i++) {
			for (int index = 0; index < 5; index++) {
				RandomAutomatonBuilder randomAutomatonBuilder = new RandomAutomatonBuilder(3 + index, index);
				randomAutomatonBuilder.setAlphabet("abc");
				randomAutomatonBuilder.setNumberOfFinalStates(index + 1);
				Automaton automaton;
				try {
					automaton = randomAutomatonBuilder.build();
					DotBuilder dotBuilder = new DotBuilder(automaton);

					File file = new File(defaultInputFolderPath + "result.dot");
					dotBuilder.buildDotFile(file);

					PrefsFileHelper prefsFileHelper = new PrefsFileHelper();
					graphvizHelper = new GraphvizHelper(file.getAbsolutePath(), defaultOutputFolderPath, prefsFileHelper);
					graphvizHelper.setFileOutputName("result" + index + ".jpg");
					graphvizHelper.runCommand();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}

			}
		}

	}
}
