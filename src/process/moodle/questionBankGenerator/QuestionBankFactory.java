/**
 * 
 */
package process.moodle.questionBankGenerator;

import process.moodle.questionGenerator.QuestionGenerator;

/**
 * Factory pattern that allows user to create questions banks generator's
 * implementations without letting them modifying anything.
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class QuestionBankFactory {

	public static QuestionBankGenerator createDeterministicStatesGenerator(String title, int numberOfQuestions) {
		return new DeterministicStatesQuestionBankGenerator(title, numberOfQuestions);
	}
	
	public static QuestionBankGenerator createDeterministicTransitionsGenerator(String title, int numberOfQuestions) {
		return new DeterministicTransitionsQuestionBankGenerator(title, numberOfQuestions);
	}
	
	public static QuestionBankGenerator createIsDeterministicGenerator(String title, int numberOfQuestions) {
		return new IsDeterministicQuestionBankGenerator(title, numberOfQuestions);
	}
}
