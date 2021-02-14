package test.manual;

import java.io.File;
import java.io.IOException;

import process.moodle.DeterministicMoodleCreator;

/**
 * Create a simple xml file for deterministic questions
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class MoodleTest {
	public static void main(String[] args) {
		DeterministicMoodleCreator deterministicMoodleCreator = new DeterministicMoodleCreator();
		try {
			int numberOfQuestions = 10;
			deterministicMoodleCreator.createMoodleDataFile("deterministic_banque.xml", numberOfQuestions);
		} catch (IOException e) {
			System.err.println("Error while creating files : " + e.getMessage());
		}
		clearTmpFiles();
	}
	
	/**
	 * Delete all files in the data/tmp folder
	 */
	public static void clearTmpFiles() {
		File tmpDir = new File("data/tmp");
		for (File file : tmpDir.listFiles()) {
			file.delete();
		}
	}
}
