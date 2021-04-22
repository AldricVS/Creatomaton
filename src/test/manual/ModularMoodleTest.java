package test.manual;

import process.moodle.questionBankGenerator.QuestionBankFactory;
import process.moodle.questionBankGenerator.QuestionBankGenerator;

public class ModularMoodleTest {

	public static void main(String[] args) {
		QuestionBankGenerator questionBankGenerator = QuestionBankFactory.createNerodeGenerator("nerode", 5);
		questionBankGenerator.setNumberOfStates(5);
		questionBankGenerator.setNumberOfFinalStates(2);
		questionBankGenerator.setNumberOfEpsilonTransitions(0);
		questionBankGenerator.setQuestionPoints(2.0);
		questionBankGenerator.generateBankFile();
	}

}
