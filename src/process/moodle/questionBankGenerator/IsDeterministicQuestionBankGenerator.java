package process.moodle.questionBankGenerator;

import org.w3c.dom.Document;

import data.Automaton;
import process.AutomatonManager;
import process.builders.RandomAutomatonBuilder;
import process.moodle.questionGenerator.QuestionGenerator;
import process.moodle.questionGenerator.TrueOfFalseQuestionGenerator;

class IsDeterministicQuestionBankGenerator extends QuestionBankGenerator {

	public IsDeterministicQuestionBankGenerator(String title, int numberOfQuestons) {
		super(title, numberOfQuestons);
	}

	@Override
	protected QuestionGenerator defineQuestionGenerator(Document document) {
		return new TrueOfFalseQuestionGenerator(document);
	}

	@Override
	protected void initSpecificQuestionGenerator() {
		QuestionGenerator questionGenerator = getQuestionGenerator();
		questionGenerator.setQuestionTitle("Déterministe ?");
		questionGenerator.setQuestionTopText("Soit l'automate suivant :");
		questionGenerator.setQuestionBottomText("Est-il déterministe ?");
	}

	@Override
	protected void defineQuestion() {
		// Create a random automaton
		RandomAutomatonBuilder randomAutomatonBuilder = new RandomAutomatonBuilder();
		randomAutomatonBuilder.setAlphabet(getAlphabet());
		randomAutomatonBuilder.setNumberOfStates(getNumberOfStates());
		randomAutomatonBuilder.setNumberOfFinalStates(getNumberOfFinalStates());
		Automaton automaton = randomAutomatonBuilder.build();

		AutomatonManager manager = new AutomatonManager(automaton);

		TrueOfFalseQuestionGenerator questionGenerator = (TrueOfFalseQuestionGenerator) getQuestionGenerator();
		questionGenerator.setQuestionAutomaton(automaton);
		questionGenerator.setAnswer(manager.isDeterministic());
	}

}
