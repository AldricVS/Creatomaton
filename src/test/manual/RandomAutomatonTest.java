package test.manual;

import java.io.File;
import java.io.IOException;

import data.Automaton;
import process.builders.DotBuilder;
import process.builders.RandomAutomatonBuilder;
import process.file.PrefsFileHelper;
import process.helpers.GraphvizHelper;

public class RandomAutomatonTest {
	public static void main(String[] args) throws IOException {
		for(int index = 0; index < 5; index++){
			RandomAutomatonBuilder randomAutomatonBuilder = new RandomAutomatonBuilder(3 + index, index, index);
			randomAutomatonBuilder.setAlphabet("abcdefg");
			Automaton automaton = randomAutomatonBuilder.build();
			
			DotBuilder dotBuilder = new DotBuilder(automaton);
			PrefsFileHelper prefsFileHelper = new PrefsFileHelper();
			String defaultOutputFolderPath = prefsFileHelper.getPreference(PrefsFileHelper.DEFALUT_OUTPUT_FOLDER_KEY);
			String defaultInputFolderPath = prefsFileHelper.getPreference(PrefsFileHelper.DEFALUT_INPUT_FOLDER_KEY);
			
			File file = new File(defaultInputFolderPath + "result.dot");
			dotBuilder.buildDotFile(file);
			
			GraphvizHelper graphvizHelper = new GraphvizHelper(file.getAbsolutePath(), defaultOutputFolderPath);
			graphvizHelper.setFileOutputName("result.jpg");
			graphvizHelper.runCommand();
		}
	}
}
