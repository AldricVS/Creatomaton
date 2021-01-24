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
		String defaultOutputFolderPath = prefsFileHelper.getPreference(PrefsFileHelper.DEFALUT_OUTPUT_FOLDER_KEY);
		String defaultInputFolderPath = prefsFileHelper.getPreference(PrefsFileHelper.DEFALUT_INPUT_FOLDER_KEY);
		
		for(int index = 0; index < 5; index++){
			RandomAutomatonBuilder randomAutomatonBuilder = new RandomAutomatonBuilder(3 + index, 2 + index, index);
			randomAutomatonBuilder.setAlphabet("abc");
			Automaton automaton = randomAutomatonBuilder.build();
			
			if(index == 2) {
				AutomatonBuilder automatonBuilder = new AutomatonBuilder(automaton);
				automaton = automatonBuilder.buildDeterminedAutomaton();
			}
			
			DotBuilder dotBuilder = new DotBuilder(automaton);
			
			File file = new File(defaultInputFolderPath + "result.dot");
			dotBuilder.buildDotFile(file);
			
			graphvizHelper = new GraphvizHelper(file.getAbsolutePath(), defaultOutputFolderPath);
			graphvizHelper.setFileOutputName("result" + index + ".jpg");
			graphvizHelper.runCommand();
		}
	}
}
