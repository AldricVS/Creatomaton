package process.moodle.questionGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Locale;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import data.Automaton;
import process.file.DataFilePaths;
import process.file.ImageCreator;

/**
 * Abstract class for creating a question. Any child of this class can have a
 * different answer type (numerical, string, ...). Used by the
 * QuestionBankGenerator in order to generate questions of the same type
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public abstract class QuestionGenerator {
	// Common to all questions
	protected ImageCreator imageCreator;
	protected String questionTitle = "";
	protected String questionTopText = "";
	protected String questionBottomText = "";
	protected String answerTopText = "";
	protected String answerBottomText = "";

	protected Automaton questionAutomaton;
	protected Automaton answerAutomaton;

	protected double questionPoints;
	protected double penaltyPerTry;

	protected Document document;

	protected String questionImageName;
	protected String answerImageName;

	/**
	 * By default, set to true. If for some reason a question generator doesn't have
	 * to show an image as the answer, then this variable must be set to false.
	 */
	protected boolean mustShowAnswerImage = true;

	/**
	 * The category of the question (moodle format). It must be explicitly defined
	 * by the child class.
	 */
	protected String moodleCategory;

	public QuestionGenerator(Document document) {
		this.document = document;
	}

	/**
	 * Define the answer to the Question.
	 * @param obj the Object to define as an answer. Each question should have is own type, so beware
	 */
	public abstract void setAnswer(Object obj);
	
	/**
	 * Get the answer of the question.
	 * @return object which is the answer. Can differ depending on the question.
	 */
	public abstract Object getAnswer();
	
	/**
	 * Create the question xml tree with all data gathered
	 * 
	 * @param questionNumber the number of the question, useful when want to create
	 *                       multiple questions
	 * @return the xml root node of the question (XML moodle format)
	 * @throws IOException
	 */
	public Element generateQuestion(int questionNumber) throws IOException {
		defineMoodleCategory();
		// create the images for them
		questionImageName = "Auto_" + questionNumber;
		createImage(questionAutomaton, questionImageName);
		if(mustShowAnswerImage) {
			answerImageName = String.format("Auto_%d_answer", questionNumber);
			createImage(answerAutomaton, answerImageName);
		}
		
		Element questionNode = document.createElement("question");
		initQuestionNode(questionNode);
		appendQuestionContent(questionNode);
		Element generalFeedbackNode = appendGeneralFeedBackNode(questionNode);
		appendAnswer(questionNode, generalFeedbackNode);
		appendGradeAndPenalty(questionNode);
		// The others little nodes
		appendRemainingNodes(questionNode);
		return questionNode;
	}

	/**
	 * @param questionNode
	 */
	private void appendGradeAndPenalty(Element questionNode) {
		// In order to keep 7 digits after the decimal operator, format the double
		// Also, the "Locale.ROOT" property force the "." as the decimal separator
		// (else, we could have ',')
		String gradeString = String.format(Locale.ROOT, "%.7f", questionPoints);
		String penaltyString = String.format(Locale.ROOT, "%.7f", penaltyPerTry);

		appendElementToNode("defaultgrade", gradeString, questionNode, document);
		appendElementToNode("penalty", penaltyString, questionNode, document);
	}

	/**
	 * @param questionNode
	 */
	private void initQuestionNode(Element questionNode) {
		// only numerical answers for now
		Attr questionAttr = document.createAttribute("type");
		questionAttr.setValue(moodleCategory);
		questionNode.setAttributeNode(questionAttr);

		// name of the question
		Element nameNode = document.createElement("name");
		questionNode.appendChild(nameNode);
		Element titleTextNode = document.createElement("text");
		titleTextNode.appendChild(document.createTextNode(questionTitle));
		nameNode.appendChild(titleTextNode);
	}

	/**
	 * @param questionNode
	 * @throws IOException
	 */
	private void appendQuestionContent(Element questionNode) throws IOException {
		// question text
		Element questionTextNode = document.createElement("questiontext");
		questionNode.appendChild(questionTextNode);

		Element textNode = document.createElement("text");
		questionTextNode.appendChild(textNode);
		String imageQuestionName = questionImageName + ".jpg";

		// No need for StringBuilder, as he will be automatically be used when compiling
		String questionContent = "<p>"
				+ questionTopText
				+ "</p>"
				+ "<p><img src=\"@@PLUGINFILE@@/"
				+ imageQuestionName
				+ "\" alt=\"\" role=\"presentation\" class=\"img-responsive atto_image_button_text-bottom\"><br></p>"
				+ "<p>"
				+ questionBottomText
				+ "</p>";

		textNode.appendChild(document.createCDATASection(questionContent));

		// image question
		Element imageQuestionNode = createImageFileNode(imageQuestionName, DataFilePaths.TEMP_PATH + "/" + imageQuestionName, document);
		questionTextNode.appendChild(imageQuestionNode);
	}

	/**
	 * @param questionNode
	 * @return
	 */
	private Element appendGeneralFeedBackNode(Element questionNode) {
		// general feedback
		Element generalFeedbackNode = document.createElement("generalfeedback");
		generalFeedbackNode.setAttribute("format", "html");
		questionNode.appendChild(generalFeedbackNode);

		Element generalFeedbackTextNode = document.createElement("text");
		if(mustShowAnswerImage) {
			generalFeedbackNode.appendChild(generalFeedbackTextNode);
			// String answerBegin = "Voici l'automate après déterminisation : ";
			String imageAnswerName = answerImageName + ".jpg";
			String generalFeedbackContent = "<p>"
					+ answerTopText
					+ "</p>"
					+ "<p><img src=\"@@PLUGINFILE@@/"
					+ imageAnswerName
					+ "\" alt=\"\" width=\"355\" height=\"109\" role=\"presentation\" class=\"img-responsive atto_image_button_text-bottom\"><br></p><p>"
					+ answerBottomText
					+ "<br></p>";
			generalFeedbackTextNode.appendChild(document.createCDATASection(generalFeedbackContent));
		}
		return generalFeedbackNode;
	}

	private void createImage(Automaton automaton, String autoImageName) throws IOException {
		if (imageCreator == null) {
			imageCreator = new ImageCreator(automaton, DataFilePaths.TEMP_PATH + "/" + autoImageName);
		} else {
			imageCreator.setAutomaton(automaton);
			imageCreator.setFilename(DataFilePaths.TEMP_PATH + "/" + autoImageName);
		}
		imageCreator.createImageFile();
	}

	/**
	 * Here, the child class must set te name of the moodle category question (ex:
	 * for a question with 1 number as an answer, the category is "numerical"). This
	 * category name can be found as the attribute "type" in the "question" node.
	 */
	protected abstract void defineMoodleCategory();

	/**
	 * @param questionNode        the node where the answer data must be set
	 * @param generalFeedbackNode the node where the image of the answer have to be
	 *                            appended to
	 * @throws IOException
	 */
	protected abstract void appendAnswer(Element questionNode, Element generalFeedbackNode) throws IOException;

	/**
	 * Here, the child class must append all tiny nodes that is contained in this
	 * question type ("hidden", "showunits", ...)
	 * 
	 * @param questionNode where those new nodes must be appended
	 */
	protected abstract void appendRemainingNodes(Element questionNode);

	protected void appendElementToNode(String name, String value, Element node, Document document) {
		Element element = document.createElement(name);
		element.setTextContent(value);
		node.appendChild(element);
	}

	protected Element createImageFileNode(String imageName, String imageFile, Document document) throws IOException {
		Element imageNode = document.createElement("file");
		imageNode.setAttribute("name", imageName);
		imageNode.setAttribute("path", "/");
		imageNode.setAttribute("encoding", "base64");
		// get the base 64 encoding from the image
		String base64 = imageToBase64(new File(imageFile));
		imageNode.setTextContent(base64);
		return imageNode;
	}

	/**
	 * Encode the image file into a base64 representation
	 * 
	 * @param imageFile the image to encode. If the size of the file is too big
	 *                  (arround 1gb), unpredictible behavior can occur (depending
	 *                  on the computer that runs the program)
	 * @return the base64 String encoding of the image
	 * @throws IOException if image File is not found or cannot be opened
	 */
	private String imageToBase64(File imageFile) throws IOException {
		byte[] imagesBytes = Files.readAllBytes(imageFile.toPath());
		String base64String = Base64.getEncoder().encodeToString(imagesBytes);
		return base64String;
	}

	public String getQuestionTitle() {
		return questionTitle;
	}

	public String getQuestionTopText() {
		return questionTopText;
	}

	public String getQuestionBottomText() {
		return questionBottomText;
	}

	public String getAnswerTopText() {
		return answerTopText;
	}

	public String getAnswerBottomText() {
		return answerBottomText;
	}

	public Automaton getQuestionAutomaton() {
		return questionAutomaton;
	}

	public Automaton getAnswerAutomaton() {
		return answerAutomaton;
	}

	public double getQuestionPoints() {
		return questionPoints;
	}

	public double getPenaltyPerTry() {
		return penaltyPerTry;
	}

	public Document getDocument() {
		return document;
	}

	public String getQuestionImageName() {
		return questionImageName;
	}

	public String getAnswerImageName() {
		return answerImageName;
	}

	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	public void setQuestionTopText(String questionTopText) {
		this.questionTopText = questionTopText;
	}

	public void setQuestionBottomText(String questionBottomText) {
		this.questionBottomText = questionBottomText;
	}

	public void setAnswerTopText(String answerTopText) {
		this.answerTopText = answerTopText;
	}

	public void setAnswerBottomText(String answerBottomText) {
		this.answerBottomText = answerBottomText;
	}

	public void setQuestionAutomaton(Automaton questionAutomaton) {
		this.questionAutomaton = questionAutomaton;
	}

	public void setAnswerAutomaton(Automaton answerAutomaton) {
		this.answerAutomaton = answerAutomaton;
	}

	public void setQuestionPoints(double questionPoints) {
		this.questionPoints = questionPoints;
	}

	public void setPenaltyPerTry(double penaltyPerTry) {
		this.penaltyPerTry = penaltyPerTry;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public void setQuestionImageName(String questionImageName) {
		this.questionImageName = questionImageName;
	}

	public void setAnswerImageName(String answerImageName) {
		this.answerImageName = answerImageName;
	}

	public String getMoodleCategory() {
		return moodleCategory;
	}

	public void setMoodleCategory(String moodleCategory) {
		this.moodleCategory = moodleCategory;
	}

	public boolean isMustShowAnswerImage() {
		return mustShowAnswerImage;
	}

	public void setMustShowAnswerImage(boolean mustShowAnswerImage) {
		this.mustShowAnswerImage = mustShowAnswerImage;
	}
}
