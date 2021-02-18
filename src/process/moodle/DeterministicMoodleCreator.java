package process.moodle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import data.Automaton;
import process.builders.AutomatonBuilder;
import process.builders.DotBuilder;
import process.builders.RandomAutomatonBuilder;
import process.file.DataFilePaths;
import process.file.PrefsFileHelper;
import process.helpers.GraphvizHelper;
import process.util.FileUtility;

//TODO : plus des tests qu'autre chose pour l'instant, à rendre plus flexible
public class DeterministicMoodleCreator {
	public DeterministicMoodleCreator() {

	}

	public void createImage(Automaton automaton, String fileName) throws IOException {
		File f = new File(DataFilePaths.TEMP_PATH + "/" + fileName + ".dot");
		DotBuilder dotBuilder = new DotBuilder(automaton);
		// dotBuilder.setIsTriyingToGetStatesNames(false);
		dotBuilder.buildDotFile(f);

		PrefsFileHelper prefsFileHelper = new PrefsFileHelper();
		GraphvizHelper graphvizHelper = new GraphvizHelper(f.getAbsolutePath(), prefsFileHelper);
		graphvizHelper.setFileOutputPath(DataFilePaths.TEMP_PATH);
		graphvizHelper.setFileOutputName(fileName + ".jpg");
		graphvizHelper.runCommand();
	}

	public void createMoodleDataFile(String filePath, int numberOfQuestions) throws IOException {
		// create the xml tree
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();

			// root node
			Element rootNode = document.createElement("quiz");
			document.appendChild(rootNode);

			// 1 questionNode per question
			for (int i = 0; i < numberOfQuestions; i++) {

				// create the automaton and the deterministic one
				RandomAutomatonBuilder randomAutomatonBuilder = new RandomAutomatonBuilder(4, 2);
				randomAutomatonBuilder.setAlphabet("abc");
				Automaton automaton = randomAutomatonBuilder.build();

				AutomatonBuilder automatonBuilder = new AutomatonBuilder(automaton);
				Automaton synchronizedAutomaton = automatonBuilder.buildSynchronizedAutomaton();

				automatonBuilder.setAutomaton(synchronizedAutomaton);
				Automaton deterministicAutomaton = automatonBuilder.buildDeterministicAutomaton();

				// create the images for them
				String autoImageName = "Auto_" + i;
				createImage(automaton, autoImageName);
				String autoDeterImageName = String.format("Auto_%d_deter", i);
				createImage(deterministicAutomaton, autoDeterImageName);

				Element questionNode = document.createElement("question");
				Attr questionAttr = document.createAttribute("type");
				questionAttr.setValue("numerical");
				questionNode.setAttributeNode(questionAttr);
				rootNode.appendChild(questionNode);

				// name of the question
				String questionTitle = "Déterminisation";
				Element nameNode = document.createElement("name");
				questionNode.appendChild(nameNode);
				Element titleTextNode = document.createElement("text");
				titleTextNode.appendChild(document.createTextNode(questionTitle));
				nameNode.appendChild(titleTextNode);

				// question text
				Element questionTextNode = document.createElement("questiontext");
				questionNode.appendChild(questionTextNode);

				Element textNode = document.createElement("text");
				questionTextNode.appendChild(textNode);
				String startQuestion = "Soit l'automate suivant : ";
				String endQuestion = "Combien d'états aura-t-il suite à sa déterminisation ?";
				String imageQuestionName = autoImageName + ".jpg";

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

				// general feedback
				Element generalFeedbackNode = document.createElement("generalfeedback");
				generalFeedbackNode.setAttribute("format", "html");
				questionNode.appendChild(generalFeedbackNode);

				Element generalFeedbackTextNode = document.createElement("text");
				generalFeedbackNode.appendChild(generalFeedbackTextNode);
				String answerBegin = "Voici l'automate après déterminisation : ";
				String answerEnd = "";
				String imageAnswerName = autoDeterImageName + ".jpg";
				String generalFeedbackContent = "<p>"
						+ answerBegin
						+ "</p>"
						+ "<p><img src=\"@@PLUGINFILE@@/"
						+ imageAnswerName
						+ "\" alt=\"\" width=\"355\" height=\"109\" role=\"presentation\" class=\"img-responsive atto_image_button_text-bottom\"><br></p><p>"
						+ answerEnd
						+ "<br></p>";
				generalFeedbackTextNode.appendChild(document.createCDATASection(generalFeedbackContent));

				// image answer
				Element imageAnswerNode = createImageFileNode(imageAnswerName, DataFilePaths.TEMP_PATH + "/" + imageAnswerName, document);
				generalFeedbackNode.appendChild(imageAnswerNode);

				// data for the question
				int numberOfTotalStates = deterministicAutomaton.getNumberOfTotalStates();;
				String answer = String.valueOf(numberOfTotalStates);
				double questionGrade = 1.0;
				double penalty = 1.0 / 3.0; // 0.33333

				// answer node
				Element answerDataNode = document.createElement("answer");
				questionNode.appendChild(answerDataNode);
				answerDataNode.setAttribute("fraction", "100"); // Only 1 valid answer per question (determinist)
				answerDataNode.setAttribute("format", "moodle_auto_format");
				appendElementToNode("text", answer, answerDataNode, document);
				appendElementToNode("tolerance", "0", answerDataNode, document); // no tolerance

				// The others little nodes

				// In order to keep 7 digits after the decimal operator, format the double
				// Also, the "Locale.ROOT" property force the "." as the decimal separator
				// (else, we could have ',')

				String gradeString = String.format(Locale.ROOT, "%.7f", questionGrade);
				String penaltyString = String.format(Locale.ROOT, "%.7f", penalty);

				appendElementToNode("defaultgrade", gradeString, questionNode, document);
				appendElementToNode("penalty", penaltyString, questionNode, document);
				appendElementToNode("hidden", "0", questionNode, document);
				appendElementToNode("unitgradingtype", "0", questionNode, document);
				appendElementToNode("unitpenalty", "0.1000000", questionNode, document);
				appendElementToNode("showunits", "3", questionNode, document);
				appendElementToNode("unitsleft", "0", questionNode, document);

			}

			/* Finished ! Now, create the xml file */
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new File(DataFilePaths.XML_PATH + "/" + filePath));
			transformer.transform(domSource, streamResult);
			
			// And clear temp folder
			FileUtility.clearFolder("data/tmp");

		} catch (ParserConfigurationException e) {
			System.err.println("Error while creating the xml tree : " + e.getMessage());
		} catch (TransformerException e) {
			System.err.println("Error while creating the xml file : " + e.getMessage());
		}

	}

	public void appendElementToNode(String name, String value, Element node, Document document) {
		Element element = document.createElement(name);
		element.setTextContent(value);
		node.appendChild(element);
	}

	public Element createImageFileNode(String imageName, String imageFile, Document document) throws IOException {
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
}
