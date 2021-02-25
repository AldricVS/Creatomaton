package test.manual;

import process.moodle.questionBankGenerator.QuestionBankGenerator;
import process.moodle.questionBankGenerator.deterministic.DeterministicTransitionsQuestionBankGenerator;
import process.moodle.questionBankGenerator.deterministic.IsDeterministicQuestionBankGenerator;

public class ModularMoodleTest {

	public static void main(String[] args) {
		// Change only the first line in order to change the question type
		QuestionBankGenerator questionBankGenerator = new IsDeterministicQuestionBankGenerator("Est déterministe", 20);
		questionBankGenerator.setNumberOfStates(5);
		questionBankGenerator.setAlphabet("abcd");
		questionBankGenerator.setQuestionPoints(1.0);
		questionBankGenerator.generateBankFile();
	}

}
