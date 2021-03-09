package process.moodle.questionGenerator;

import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Implementation of the QuestionGenerator class, that allows to create a
 * question that can be answered by true or false.
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class TrueOfFalseQuestionGenerator extends QuestionGenerator {

	boolean answerValue;

	public boolean isAnswerValue() {
		return answerValue;
	}

	public void setAnswerValue(boolean answerValue) {
		this.answerValue = answerValue;
	}

	public TrueOfFalseQuestionGenerator(Document document) {
		super(document);
	}

	@Override
	protected void defineMoodleCategory() {
		setMoodleCategory("truefalse");
		// We don't want to show an image as the answer
		setMustShowAnswerImage(false);
	}

	@Override
	protected void appendAnswer(Element questionNode, Element generalFeedbackNode) throws IOException {
		// two "answers" for the question : true or false
		
		// "true" node
		String trueValue = answerValue ? "100" : "0";
		String falseValue = answerValue ? "0" : "100";
		Element trueDataNode = document.createElement("answer");
		questionNode.appendChild(trueDataNode);
		trueDataNode.setAttribute("fraction", trueValue);
		trueDataNode.setAttribute("format", "moodle_auto_format");
		appendElementToNode("text", "true", trueDataNode, document);
		
		Element falseDataNode = document.createElement("answer");
		questionNode.appendChild(falseDataNode);
		falseDataNode.setAttribute("fraction", falseValue);
		falseDataNode.setAttribute("format", "moodle_auto_format");
		appendElementToNode("text", "false", falseDataNode, document);
	}

	@Override
	protected void appendRemainingNodes(Element questionNode) {
		appendElementToNode("hidden", "0", questionNode, document);
	}

}
