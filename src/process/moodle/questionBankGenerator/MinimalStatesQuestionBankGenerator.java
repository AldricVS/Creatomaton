package process.moodle.questionBankGenerator;

import org.w3c.dom.Document;

import data.Automaton;
import process.builders.AutomatonBuilder;
import process.builders.RandomAutomatonBuilder;
import process.moodle.questionGenerator.NumericalQuestionGenerator;
import process.moodle.questionGenerator.QuestionGenerator;

/**
 * Implémentation of the QuestionBankGenerator that permits to create question
 * on the number of states after modify an automaton to be minimal
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
class MinimalStatesQuestionBankGenerator extends QuestionBankGenerator {

	public MinimalStatesQuestionBankGenerator(String title, int numberOfQuestons) {
		super(title, numberOfQuestons);
	}

	@Override
	protected QuestionGenerator defineQuestionGenerator(Document document) {
		return new NumericalQuestionGenerator(document);
	}

	@Override
	protected void defineQuestion() {
		// Create a random automaton
		RandomAutomatonBuilder randomAutomatonBuilder = new RandomAutomatonBuilder();
		randomAutomatonBuilder.setAlphabet(getAlphabet());
		randomAutomatonBuilder.setNumberOfStates(getNumberOfStates());
		randomAutomatonBuilder.setNumberOfFinalStates(getNumberOfFinalStates());
		randomAutomatonBuilder.setNumberOfEpsilonTransitions(getNumberOfEpsilonTransitions());
		Automaton automaton = randomAutomatonBuilder.build();

		// And the deterministic one
		AutomatonBuilder automatonBuilder = new AutomatonBuilder(automaton);
		Automaton minimalAutomaton = automatonBuilder.buildMinimalAutomaton();

		// The answer is the number of states of the deterministic automaton
		int answer = minimalAutomaton.getNumberOfTotalStates();

		// Define the question with all elements needed
		NumericalQuestionGenerator numericalQuestionGenerator = (NumericalQuestionGenerator) getQuestionGenerator();
		numericalQuestionGenerator.setAnswer(answer);
		numericalQuestionGenerator.setQuestionAutomaton(automaton);
		numericalQuestionGenerator.setAnswerAutomaton(minimalAutomaton);
	}

	@Override
	protected void initSpecificQuestionGenerator() {
		QuestionGenerator questionGenerator = getQuestionGenerator();
		questionGenerator.setQuestionTopText("Soit l'automate suivant :");
		questionGenerator.setQuestionBottomText("Après minimalisation, combien d'états y aura-t-il ?");
		questionGenerator.setAnswerTopText("Voici l'automate après minimalisation : ");
	}
}
