package process.moodle;

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
 * An helper class that will allows to quickly create a xml tree for one
 * question. Before creating the xml tree, each field must be set. Else,
 * unpredictible elements can occur
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class NumericalQuestionGenerator {
	private ImageCreator imageCreator;

	private String questionTitle;
	private String questionTopText;
	private String questionBottomText;
	private String answerTopText;
	private String answerBottomText;

	private Automaton questionAutomaton;
	private Automaton answerAutomaton;

	private double questionPoints;
	private double penaltyPerTry;

	private int answerValue;

	private Document document;
	
	private String questionImageName;
	private String answerImageName;

	public NumericalQuestionGenerator(Document document) {
		this.document = document;
	}

	/**
	 * Create the question xml tree with all data gathered
	 * @param questionNumber the number of the question, useful when want to create multiple questions
	 * @return the xml root node of the question (XML moodle format)
	 * @throws IOException
	 */
	public Element generateQuestion(int questionNumber) throws IOException {
		// create the images for them
		questionImageName = "Auto_" + questionNumber;
		createImage(questionAutomaton, questionImageName);
		answerImageName = String.format("Auto_%d_answer", questionNumber);
		createImage(answerAutomaton, answerImageName);

		Element questionNode = document.createElement("question");
		initQuestionNode(questionNode);
		appendQuestionContent(questionNode);
		Element generalFeedbackNode = appendGeneralFeedBackNode(questionNode);
		appendAnswer(questionNode, generalFeedbackNode);
		// The others little nodes
		appendRemainingNodes(questionNode);
		return questionNode;
	}

	/**
	 * @param questionNode
	 */
	private void initQuestionNode(Element questionNode) {
		//only numerical answers for now
		Attr questionAttr = document.createAttribute("type");
		questionAttr.setValue("numerical");
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
		String startQuestion = "Soit l'automate suivant : ";
		String endQuestion = "Combien d'états aura-t-il suite à sa déterminisation ?";
		String imageQuestionName = questionImageName + ".jpg";

		// No need for StringBuilder, as he will be automatically be used when compiling
		String questionContent = "<p>"
				+ startQuestion
				+ "</p>"
				+ "<p><img src=\"@@PLUGINFILE@@/"
				+ imageQuestionName
				+ "\" alt=\"\" role=\"presentation\" class=\"img-responsive atto_image_button_text-bottom\"><br></p>"
				+ "<p>"
				+ endQuestion
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
		generalFeedbackNode.appendChild(generalFeedbackTextNode);
		//String answerBegin = "Voici l'automate après déterminisation : ";
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
		return generalFeedbackNode;
	}

	private void createImage(Automaton automaton, String autoImageName) throws IOException {
		if(imageCreator == null) {
			imageCreator = new ImageCreator(automaton, autoImageName);
		}else {
			imageCreator.setAutomaton(automaton);
			imageCreator.setFilename(autoImageName);
		}
		imageCreator.setOutputFolder(DataFilePaths.TEMP_PATH);
		imageCreator.createImageFile();
	}

	/**
	 * @param questionNode
	 * @param generalFeedbackNode
	 * @param imageAnswerName
	 * @throws IOException
	 */
	private void appendAnswer(Element questionNode, Element generalFeedbackNode) throws IOException {
		// image answer
		Element imageAnswerNode = createImageFileNode(answerImageName, DataFilePaths.TEMP_PATH + "/" + answerImageName, document);
		generalFeedbackNode.appendChild(imageAnswerNode);

		// data for the question
		String answerString = String.valueOf(answerValue);

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
	private void appendRemainingNodes(Element questionNode) {
		// In order to keep 7 digits after the decimal operator, format the double
		// Also, the "Locale.ROOT" property force the "." as the decimal separator
		// (else, we could have ',')
		String gradeString = String.format(Locale.ROOT, "%.7f", questionPoints);
		String penaltyString = String.format(Locale.ROOT, "%.7f", penaltyPerTry);

		appendElementToNode("defaultgrade", gradeString, questionNode, document);
		appendElementToNode("penalty", penaltyString, questionNode, document);
		appendElementToNode("hidden", "0", questionNode, document);
		appendElementToNode("unitgradingtype", "0", questionNode, document);
		appendElementToNode("unitpenalty", "0.1000000", questionNode, document);
		appendElementToNode("showunits", "3", questionNode, document);
		appendElementToNode("unitsleft", "0", questionNode, document);
	}

	private void appendElementToNode(String name, String value, Element node, Document document) {
		Element element = document.createElement(name);
		element.setTextContent(value);
		node.appendChild(element);
	}

	private Element createImageFileNode(String imageName, String imageFile, Document document) throws IOException {
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

	public int getAnswerValue() {
		return answerValue;
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

	public void setAnswerValue(int answerValue) {
		this.answerValue = answerValue;
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
}
