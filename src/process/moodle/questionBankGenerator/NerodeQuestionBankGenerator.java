package process.moodle.questionBankGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.w3c.dom.Document;

import data.Automaton;
import data.State;
import process.builders.NerodeAutomatonBuilder;
import process.builders.RandomAutomatonBuilder;
import process.file.ImageCreator;
import process.moodle.questionGenerator.QuestionGenerator;
import process.moodle.questionGenerator.TableQuestionGenerator;

/**
 * @author Maxence
 */
class NerodeQuestionBankGenerator extends QuestionBankGenerator {

	/**
	 * @param title
	 * @param numberOfQuestions
	 */
	public NerodeQuestionBankGenerator(String title, int numberOfQuestions) {
		super(title, numberOfQuestions);
	}

	@Override
	protected QuestionGenerator defineQuestionGenerator(Document document) {
		return new TableQuestionGenerator(document);
	}

	@Override
	protected void initSpecificQuestionGenerator() {
		QuestionGenerator questionGenerator = getQuestionGenerator();
		questionGenerator.setQuestionTitle("Automate minimal - Nerode");
		questionGenerator.setQuestionTopText(
				"Considérant l'automate suivant, remplissez le tableau pour trouver l'automate minimale par Nerode");
		questionGenerator.setQuestionBottomText("Répondez dans chaque case du tableau");
	}

	@Override
	protected void defineQuestion() {
		RandomAutomatonBuilder randomAutomatonBuilder = new RandomAutomatonBuilder();
		randomAutomatonBuilder.setAlphabet(getAlphabet());
		randomAutomatonBuilder.setNumberOfStates(getNumberOfStates());
		randomAutomatonBuilder.setNumberOfFinalStates(getNumberOfFinalStates());
		Automaton automaton = randomAutomatonBuilder.build();

		NerodeAutomatonBuilder nerodeBuilder = new NerodeAutomatonBuilder(automaton);
		Automaton minimalAutomaton = nerodeBuilder.buildNerodeAutomaton();
		int nbState = minimalAutomaton.getNumberOfTotalStates();
		LinkedList<LinkedList<ArrayList<State>>> tableState = nerodeBuilder.getNerodeStatesList();
		int nbRow = tableState.size();
		
		try {
			ImageCreator image = new ImageCreator(automaton, "nerode_testGenerator");
			image.createImageFile();
			image = new ImageCreator(minimalAutomaton, "nerode_testGeneratorMini");
			image.createImageFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String[][] answer = new String[nbRow][nbState];
		for (LinkedList<ArrayList<State>> rowList : tableState) {
			int rowIndex = tableState.indexOf(rowList);
			for (ArrayList<State> caseTable : rowList) {
				int caseIndex = rowList.indexOf(caseTable);
				StringBuilder caseContentBuilder = new StringBuilder(nbState);
				for (State state : caseTable) {
					caseContentBuilder.append(state.getId());
				}
				answer[rowIndex][caseIndex] = caseContentBuilder.toString();
			}
			if (rowList.size() < nbState) {
				for (int index = rowList.size(); index < nbState; index++) {
					answer[rowIndex][index] = "i";
				}
			}
		}

		for (int rowIndex = 0; rowIndex < nbRow; rowIndex++) {
			for (int caseIndex = 0; caseIndex < nbState; caseIndex++) {
				if (answer[rowIndex][caseIndex] == null) {
					answer[rowIndex][caseIndex] = "i";
				}
			}
		}

		TableQuestionGenerator tableQuestionGenerator = (TableQuestionGenerator) getQuestionGenerator();
		tableQuestionGenerator.setTableName("equi", "etat");;
		tableQuestionGenerator.setAnswer(answer);
		tableQuestionGenerator.setQuestionAutomaton(minimalAutomaton);
		tableQuestionGenerator.setShowStateNameImage(false);
	}

}
