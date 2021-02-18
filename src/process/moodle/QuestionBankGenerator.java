package process.moodle;

import org.w3c.dom.Element;

/**
 * The abstract class of all bank generator's questions
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public abstract class QuestionBankGenerator {
	private int numberOfQuestons;

	public QuestionBankGenerator(int numberOfQuestons) {
		this.numberOfQuestons = numberOfQuestons;
	}
	
	public void generateBankFile() {
		
	}
	
	public abstract Element generateQuestion();

	public int getNumberOfQuestons() {
		return numberOfQuestons;
	}

	public void setNumberOfQuestons(int numberOfQuestons) {
		this.numberOfQuestons = numberOfQuestons;
	}
}
