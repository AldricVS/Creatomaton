package test.manual;

import process.moodle.questionBankGenerator.QuestionBankFactory;
import process.moodle.questionBankGenerator.QuestionBankGenerator;

public class ModularMoodleTest {

	public static void main(String[] args) {
		QuestionBankGenerator questionBankGenerator = QuestionBankFactory.createDeterministicStatesGenerator("nb états deterministe", 50);
		questionBankGenerator.setNumberOfStates(5);
		questionBankGenerator.setNumberOfFinalStates(2);
		questionBankGenerator.setNumberOfEpsilonTransitions(2);
		questionBankGenerator.setQuestionPoints(2.0);
		questionBankGenerator.generateBankFile();
	}

}
