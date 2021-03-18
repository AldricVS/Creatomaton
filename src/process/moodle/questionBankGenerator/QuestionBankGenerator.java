package process.moodle.questionBankGenerator;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import process.file.DataFilePaths;
import process.moodle.questionGenerator.QuestionGenerator;
import process.util.FileUtility;

/**
 * The abstract class of all bank generator's questions. This permit to create
 * an xml file containing a list of questions of the same type.
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public abstract class QuestionBankGenerator {
	private int numberOfQuestions;
	private String title;
	private QuestionGenerator questionGenerator;

	// Attributes relative to the grading of the question
	private double questionPoints = 1.0;
	/**
	 * In order to have a decent value, this is better to set this value with a
	 * fraction. For exemple, 1.0/3.0 will produce 0.333333..., more prcise than
	 * just 0.33.
	 */
	private double penaltyPerTry = 1.0 / 3.0;

	// Attributes relative to the automaton and its random creation
	private String alphabet = "abc";
	private int numberOfStates = 4;
	private int numberOfEpsilonTransitions = 0;
	private int numberOfFinalStates = 1;

	public QuestionBankGenerator(String title, int numberOfQuestions) {
		this.title = title;
		this.numberOfQuestions = numberOfQuestions;
	}

	/**
	 * Generate all the questions and put them in a xml file in the "data/xml"
	 * folder
	 */
	public void generateBankFile() {
		// Create the root node
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		try {
			// root node
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			initGlobalQuestionGenerator(document);
			initSpecificQuestionGenerator();
			Element rootNode = document.createElement("quiz");
			document.appendChild(rootNode);

			for (int index = 0; index < numberOfQuestions; index++) {
				defineQuestion();
				Element generatedQuestion = questionGenerator.generateQuestion(index);
				rootNode.appendChild(generatedQuestion);
			}

			/* Finished ! Now, create the xml file */
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new File(DataFilePaths.XML_PATH + "/" + title + ".xml"));
			transformer.transform(domSource, streamResult);

			// And clear temp folder
			FileUtility.clearFolder(DataFilePaths.TEMP_PATH);
			FileUtility.clearFolder(DataFilePaths.INPUT_PATH);

		} catch (ParserConfigurationException e) {
			System.err.println("Error while creating the xml tree : " + e.getMessage());
			e.printStackTrace();
		} catch (TransformerException e) {
			System.err.println("Error while creating the xml file : " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error while working with images and dot files : " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Choose which implementation of the question generator the question need to
	 * use
	 * 
	 * @return a new instance of the QuestionGenerator implementation choosen.
	 */
	protected abstract QuestionGenerator defineQuestionGenerator(Document document);

	/**
	 * Set all values that are common to all question in the question generator
	 * 
	 * @param document
	 */
	private void initGlobalQuestionGenerator(Document document) {
		questionGenerator = defineQuestionGenerator(document);
		questionGenerator.setQuestionPoints(questionPoints);
		questionGenerator.setPenaltyPerTry(penaltyPerTry);
	}

	/**
	 * This method must init the QuestionGenerator with all values that depends on
	 * the question, so the text (question sentence, answer sentence,...), and the
	 * question Generator Type
	 */
	protected abstract void initSpecificQuestionGenerator();

	/**
	 * Define the question (the automaton created and the answer) depending on the
	 * implementation of this class and the parameter values
	 */
	protected abstract void defineQuestion();

	public int getNumberOfQuestions() {
		return numberOfQuestions;
	}

	public String getTitle() {
		return title;
	}

	public QuestionGenerator getNumericalQuestionGenerator() {
		return questionGenerator;
	}

	public double getQuestionPoints() {
		return questionPoints;
	}

	public double getPenaltyPerTry() {
		return penaltyPerTry;
	}

	public String getAlphabet() {
		return alphabet;
	}

	public QuestionGenerator getQuestionGenerator() {
		return questionGenerator;
	}

	public int getNumberOfStates() {
		return numberOfStates;
	}

	public int getNumberOfEpsilonTransitions() {
		return numberOfEpsilonTransitions;
	}

	public int getNumberOfFinalStates() {
		return numberOfFinalStates;
	}

	public void setNumberOfQuestions(int numberOfQuestions) {
		this.numberOfQuestions = numberOfQuestions;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setQuestionGenerator(QuestionGenerator questionGenerator) {
		this.questionGenerator = questionGenerator;
	}

	public void setQuestionPoints(double questionPoints) {
		this.questionPoints = questionPoints;
	}

	public void setPenaltyPerTry(double penaltyPerTry) {
		this.penaltyPerTry = penaltyPerTry;
	}

	public void setAlphabet(String alphabet) {
		this.alphabet = alphabet;
	}

	public void setNumberOfStates(int numberOfStates) {
		this.numberOfStates = numberOfStates;
	}

	public void setNumberOfEpsilonTransitions(int numberOfEpsilonTransitions) {
		this.numberOfEpsilonTransitions = numberOfEpsilonTransitions;
	}

	public void setNumberOfFinalStates(int numberOfFinalStates) {
		this.numberOfFinalStates = numberOfFinalStates;
	}
}
