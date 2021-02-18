package process.moodle;

import org.w3c.dom.Element;

/**
 * Implémentation of the QuestionBankGenerator that permits to create question
 * on the number of states after modify an automaton to be deterministic
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class DeterministicQuestionBankGenerator extends QuestionBankGenerator {

	public DeterministicQuestionBankGenerator(int numberOfQuestons) {
		super(numberOfQuestons);
	}

	@Override
	public Element generateQuestion() {
		//TODO : random automaton ==> deterministic one ==> create NumericalQuestionGenerator(fill content)
		return null;
	}

}
