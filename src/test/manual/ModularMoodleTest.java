package test.manual;

import process.moodle.DeterministicQuestionBankGenerator;
import process.moodle.QuestionBankGenerator;

public class ModularMoodleTest {

	public static void main(String[] args) {
		// Change only the first line in order to change the question type
		QuestionBankGenerator questionBankGenerator = new DeterministicQuestionBankGenerator("Determinisation", 5);
		questionBankGenerator.setNumberOfEpsilonTransitions(2);
		questionBankGenerator.setNumberOfStates(5);
		questionBankGenerator.setQuestionPoints(2.0);
		questionBankGenerator.generateBankFile();
	}

}
