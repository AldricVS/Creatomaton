package process.moodle.questionBankGenerator;

import org.w3c.dom.Document;

import data.Automaton;
import process.builders.AutomatonBuilder;
import process.builders.RandomAutomatonBuilder;
import process.moodle.questionGenerator.QuestionGenerator;
import process.moodle.questionGenerator.StringQuestionGenerator;
import process.util.StateListUtility;

/**
 * @author Maxence
 */
public class WriteSynchronisedStatesQuestionBankGenerator extends QuestionBankGenerator {

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
		questionGenerator.setQuestionTopText("Écrivez les états restants après synchronisation ? Écrivez les dans l'ordre croissant");
		questionGenerator.setQuestionBottomText("Exemple de notation avec e1 e2 et e3 : {e1;e2;e3}");
	}

	@Override
	protected void defineQuestion() {
		// Create a random automaton
		RandomAutomatonBuilder randomAutomatonBuilder = new RandomAutomatonBuilder();
		randomAutomatonBuilder.setAlphabet(getAlphabet());
		randomAutomatonBuilder.setNumberOfStates(getNumberOfStates());
		randomAutomatonBuilder.setNumberOfTransitions(3*getNumberOfStates()/5);
		randomAutomatonBuilder.setNumberOfEpsilonTransitions(2*getNumberOfStates()/5);
		Automaton automaton = randomAutomatonBuilder.build();

		AutomatonBuilder builder = new AutomatonBuilder(automaton);
		Automaton automatonSynch = builder.buildSynchronizedAutomaton();
		String nameListOfStates = StateListUtility.constructNameOfDeterminedStates(automatonSynch.getAllStates());

		StringQuestionGenerator questionGenerator = (StringQuestionGenerator) getQuestionGenerator();
		questionGenerator.setQuestionAutomaton(automaton);
		questionGenerator.setAnswerAutomaton(automatonSynch);
		questionGenerator.setAnswer(nameListOfStates);
	}

}