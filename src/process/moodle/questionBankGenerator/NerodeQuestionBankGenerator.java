package process.moodle.questionBankGenerator;

import java.util.ArrayList;
import java.util.LinkedList;

import org.w3c.dom.Document;

import data.Automaton;
import data.State;
import process.builders.NerodeAutomatonBuilder;
import process.builders.RandomAutomatonBuilder;
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
		int nbState = getNumberOfStates();
		randomAutomatonBuilder.setNumberOfStates(nbState);
		randomAutomatonBuilder.setNumberOfFinalStates(getNumberOfFinalStates());
		randomAutomatonBuilder.setNumberOfEpsilonTransitions(getNumberOfEpsilonTransitions());
		Automaton automaton = randomAutomatonBuilder.build();

		System.out.println("Création de l'automate de Nerode");
		
		NerodeAutomatonBuilder nerodeBuilder = new NerodeAutomatonBuilder(automaton);
		Automaton minimalAutomaton = nerodeBuilder.buildNerodeAutomaton();

		System.out.println("Fin de la génération de Nerode, début de création de la réponse");
		
		String[][] answer = new String[nbState][nbState];
		LinkedList<LinkedList<ArrayList<State>>> tableState = nerodeBuilder.getNerodeStatesList();
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
		if (tableState.size() < nbState) {
			for (int lastRowIndex = tableState.size(); lastRowIndex < nbState; lastRowIndex++) {
				for (int caseIndex = 0; caseIndex < nbState; caseIndex++) {
					answer[lastRowIndex][caseIndex] = "i";
				}
			}
		}

		TableQuestionGenerator tableQuestionGenerator = (TableQuestionGenerator) getQuestionGenerator();
		tableQuestionGenerator.setAnswer(answer);
		tableQuestionGenerator.setQuestionAutomaton(automaton);
		tableQuestionGenerator.setAnswerAutomaton(minimalAutomaton);
	}

}
