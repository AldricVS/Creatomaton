package process.moodle.questionGenerator;

import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Maxence
 */
public class TableQuestionGenerator extends QuestionGenerator {

	private String[][] answer;
	
	@Override
	public void setAnswer(Object obj) {
		if (obj.getClass() == answer.getClass()) {
			answer = (String[][]) obj;
		}
	}

	@Override
	public Object getAnswer() {
		return answer;
	}

	/**
	 * @param document
	 */
	public TableQuestionGenerator(Document document) {
		super(document);
		mustShowAnswerImage = false;
	}
	
	@Override
	protected void defineMoodleCategory() {
		super.setMoodleCategory("cloze");
	}

	@Override
	protected void appendAnswer(Element questionNode, Element generalFeedbackNode) throws IOException {
		
	}

	@Override
	protected void appendRemainingNodes(Element questionNode) {
		
	}

}
