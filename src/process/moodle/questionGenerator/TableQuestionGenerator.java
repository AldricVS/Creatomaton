package process.moodle.questionGenerator;

import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Maxence
 */
public class TableQuestionGenerator extends QuestionGenerator {

	private String[][] answer;
	private String rowName;
	private String columnName;
	private String questionText;
	
	@Override
	public void setAnswer(Object obj) {
		if (obj.getClass() == String[][].class) {
			answer = (String[][]) obj;
		}
	}

	@Override
	public Object getAnswer() {
		return answer;
	}
	
	/**
	 * Set a name appropriate for 
	 * @param rowName
	 * @param columnName
	 */
	public void setTableName(String rowName, String columnName) {
		this.rowName = rowName;
		this.columnName = columnName;
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
		// answer is set within the question
	}

	@Override
	/**
	 * Generate the table based on the given answer
	 */
	public Element generateQuestion(int questionNumber) throws IOException {
		if (answer == null) {
			throw new IOException("no answer defined");
		}
		if (questionText == null) {
			questionText = getQuestionTopText();
		}
		StringBuilder tableBuilder = new StringBuilder(255);
		tableBuilder.append("</p><table>");
		if (columnName != null) {
			tableBuilder.append("<thead><tr><th scope=\"col\"></th>");
			for (int j = 0; j < answer[answer.length-1].length; j++) {
				tableBuilder.append("<th scope=\"col\">"+columnName+j+"</th>");
			}
			tableBuilder.append("</tr></thead>");
		}
		tableBuilder.append("<tbody>");
		for (int i = 0; i < answer.length; i++) {
			tableBuilder.append("<tr>");
			if (rowName != null) {
				tableBuilder.append("<th scope=\"row\">"+rowName+i+"</th>");
			}
			String[] rowString = answer[i];
			for (int j = 0; j < rowString.length; j++) {
				String caseString = rowString[j];
				tableBuilder.append("<td>{:SA:="+caseString+"#BG~*#Invalid Answer}</td>");
			}
			tableBuilder.append("</tr>");
		}
		tableBuilder.append("</table><p>");
		String tableString = tableBuilder.toString();
		setQuestionTopText(questionText+tableString);
		return super.generateQuestion(questionNumber);
	}
	
	@Override
	protected void appendRemainingNodes(Element questionNode) {
		appendElementToNode("hidden", "0", questionNode, document);
	}

}
