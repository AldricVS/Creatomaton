package test.manual;

import process.moodle.DeterministicTransitionsQuestionBankGenerator;
import process.moodle.QuestionBankGenerator;

public class ModularMoodleTest {

	public static void main(String[] args) {
		// Change only the first line in order to change the question type
		QuestionBankGenerator questionBankGenerator = new DeterministicTransitionsQuestionBankGenerator("Determinisation - Transitions", 20);
		questionBankGenerator.setNumberOfEpsilonTransitions(2);
		questionBankGenerator.setNumberOfStates(5);
		questionBankGenerator.setQuestionPoints(2.0);
		questionBankGenerator.generateBankFile();
	}

}
