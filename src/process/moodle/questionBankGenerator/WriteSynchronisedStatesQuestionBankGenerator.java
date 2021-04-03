package process.moodle.questionBankGenerator;

import org.w3c.dom.Document;

import data.Automaton;
import process.builders.AutomatonBuilder;
import process.builders.RandomAutomatonBuilder;
import process.moodle.questionGenerator.QuestionGenerator;
import process.moodle.questionGenerator.StringQuestionGenerator;
import process.util.StateListUtility;

/**
 * <p>
 * Exercice about finding which states will stay after a synchronisation.
 * </p>
 * <p>
 * Meaning all states that won't become inaccessible.
 * </p>
 * <p>
 * This Question is preferable with a low number of states and a higher number
 * of epsilon transition
 * </p>
 * 
 * @author Maxence
 */
class WriteSynchronisedStatesQuestionBankGenerator extends QuestionBankGenerator {

	public WriteSynchronisedStatesQuestionBankGenerator(String title, int numberOfQuestions) {
		super(title, numberOfQuestions);
	}

	@Override
	protected QuestionGenerator defineQuestionGenerator(Document document) {
		return new StringQuestionGenerator(document);
	}

	@Override
	protected void initSpecificQuestionGenerator() {
		QuestionGenerator questionGenerator = getQuestionGenerator();
		questionGenerator.setQuestionTitle("Trouver les états après synchronisation");
		questionGenerator.setQuestionTopText("Écrivez les états restants après synchronisation ?");
		questionGenerator.setQuestionBottomText(" Écrivez les dans l'ordre croissant. Exemple de notation avec 1, 2 et 3 => {1;2;3}");
	}

	@Override
	protected void defineQuestion() {
		// Create a random automaton
		RandomAutomatonBuilder randomAutomatonBuilder = new RandomAutomatonBuilder();
		randomAutomatonBuilder.setAlphabet(getAlphabet());
		randomAutomatonBuilder.setNumberOfStates(getNumberOfStates());
		randomAutomatonBuilder.setNumberOfTransitions(4 * getNumberOfStates() / 4);
		randomAutomatonBuilder.setNumberOfEpsilonTransitions(3 * getNumberOfStates() / 4);
		Automaton automaton = randomAutomatonBuilder.build();

		// build the synchronised version
		AutomatonBuilder builder = new AutomatonBuilder(automaton);
		Automaton automatonSynch = builder.buildSynchronizedAutomaton();
		String nameListOfStates = StateListUtility.constructNameOfDeterminedStates(automatonSynch.getAllStates());

		// generate the question
		StringQuestionGenerator questionGenerator = (StringQuestionGenerator) getQuestionGenerator();
		questionGenerator.setQuestionAutomaton(automaton);
		questionGenerator.setAnswerAutomaton(automatonSynch);
		questionGenerator.setAnswer(nameListOfStates);
	}

}
