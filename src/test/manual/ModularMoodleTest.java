package test.manual;

import process.moodle.questionBankGenerator.QuestionBankFactory;
import process.moodle.questionBankGenerator.QuestionBankGenerator;

public class ModularMoodleTest {

	public static void main(String[] args) {
		// Change only the first line in order to change the question type
		QuestionBankGenerator questionBankGenerator = QuestionBankFactory.createWriteSynchronisedStatesGenerator("MoodleTest", 5);
		questionBankGenerator.setNumberOfStates(4);
		questionBankGenerator.setAlphabet("abc");
		questionBankGenerator.setQuestionPoints(1.0);
		questionBankGenerator.generateBankFile();
	}

}
