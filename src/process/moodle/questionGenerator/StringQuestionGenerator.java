package process.moodle.questionGenerator;

import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import process.file.DataFilePaths;

/**
 * Implementation of the QuestionGenerator class, that allows to create a
 * question that is answered with a string.
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class StringQuestionGenerator extends QuestionGenerator {

	private String answer;

	@Override
	public void setAnswer(Object obj) {
		if (obj.getClass() == String.class) {
			answer = (String) obj;
		}
	}

	@Override
	public Object getAnswer() {
		return answer;
	}

	public StringQuestionGenerator(Document document) {
		super(document);
	}

	@Override
	protected void defineMoodleCategory() {
		super.setMoodleCategory("shortanswer");
	}

	@Override
	protected void appendAnswer(Element questionNode, Element generalFeedbackNode) throws IOException {
		// image answer
		Element imageAnswerNode = createImageFileNode(answerImageName + ".jpg",
				DataFilePaths.TEMP_PATH + "/" + answerImageName + ".jpg", document);
		generalFeedbackNode.appendChild(imageAnswerNode);

		// answer node
		Element answerDataNode = document.createElement("answer");
		questionNode.appendChild(answerDataNode);
		answerDataNode.setAttribute("fraction", "100"); // Only 1 valid answer per question (determinist)
		answerDataNode.setAttribute("format", "moodle_auto_format");
		appendElementToNode("text", answer, answerDataNode, document);
	}

	@Override
	protected void appendRemainingNodes(Element questionNode) {
		appendElementToNode("hidden", "0", questionNode, document);
		appendElementToNode("usecase", "0", questionNode, document);
	}

}
