package process.moodle.questionGenerator;

import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import process.file.DataFilePaths;

/**
 * Implementation of the QuestionGenerator class, that allows to create a question 
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class NumericalQuestionGenerator extends QuestionGenerator{
	
	private int answer;
	
	public NumericalQuestionGenerator(Document document) {
		super(document);
	}
	
	public int getAnswer() {
		return answer;
	}

	public void setAnswer(int answer) {
		this.answer = answer;
	}

	@Override
	protected void defineMoodleCategory() {
		super.setMoodleCategory("numerical");
	}

	/**
	 * @param questionNode
	 * @param generalFeedbackNode
	 * @param imageAnswerName
	 * @throws IOException
	 */
	protected void appendAnswer(Element questionNode, Element generalFeedbackNode) throws IOException {
		// image answer
		Element imageAnswerNode = createImageFileNode(answerImageName + ".jpg", DataFilePaths.TEMP_PATH + "/" + answerImageName + ".jpg", document);
		generalFeedbackNode.appendChild(imageAnswerNode);

		// data for the question
		String answerString = String.valueOf(answer);

		// answer node
		Element answerDataNode = document.createElement("answer");
		questionNode.appendChild(answerDataNode);
		answerDataNode.setAttribute("fraction", "100"); // Only 1 valid answer per question (determinist)
		answerDataNode.setAttribute("format", "moodle_auto_format");
		appendElementToNode("text", answerString, answerDataNode, document);
		appendElementToNode("tolerance", "0", answerDataNode, document); // no tolerance
	}

	/**
	 * @param questionNode
	 * @param questionGrade
	 * @param penalty
	 */
	protected void appendRemainingNodes(Element questionNode) {
		appendElementToNode("hidden", "0", questionNode, document);
		appendElementToNode("unitgradingtype", "0", questionNode, document);
		appendElementToNode("unitpenalty", "0.1000000", questionNode, document);
		appendElementToNode("showunits", "3", questionNode, document);
		appendElementToNode("unitsleft", "0", questionNode, document);
	}
}
