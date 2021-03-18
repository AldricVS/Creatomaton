package process.moodle.questionBankGenerator;

import java.util.List;

import org.w3c.dom.Document;

import data.Automaton;
import data.State;
import data.Transition;
import process.builders.AutomatonBuilder;
import process.builders.RandomAutomatonBuilder;
import process.moodle.questionGenerator.NumericalQuestionGenerator;
import process.moodle.questionGenerator.QuestionGenerator;
import process.util.TransitionListUtility;

/**
 * Implémentation of the QuestionBankGenerator that permits to create question
 * on the number of transitions after modify an automaton to be minimal
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
class MinimalTransitionsQuestionBankGenerator extends QuestionBankGenerator {

	public MinimalTransitionsQuestionBankGenerator(String title, int numberOfQuestons) {
		super(title, numberOfQuestons);
	}
	
	@Override
	protected QuestionGenerator defineQuestionGenerator(Document document) {
		return new NumericalQuestionGenerator(document);
	}

	@Override
	protected void defineQuestion(){
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
		
		//The answer is the number of transtions of the deterministic automaton
		List<State> allStates = minimalAutomaton.getAllStates();
		List<Transition> allTransitionFromListStates = TransitionListUtility.getAllTransitionFromListStates(allStates);
		int answer = allTransitionFromListStates.size();
		
		// Define the question with all elements needed
		NumericalQuestionGenerator numericalQuestionGenerator = (NumericalQuestionGenerator) getQuestionGenerator();
		numericalQuestionGenerator.setAnswer(answer);
		numericalQuestionGenerator.setQuestionAutomaton(automaton);
		numericalQuestionGenerator.setAnswerAutomaton(minimalAutomaton);
	}
	
	@Override
	protected void initSpecificQuestionGenerator() {
		QuestionGenerator questionGenerator = getQuestionGenerator();
		questionGenerator.setQuestionTitle("Automate minimal - Transitions");
		questionGenerator.setQuestionTopText("Soit l'automate suivant :");
		questionGenerator.setQuestionBottomText("Après minimalisation, combien de transitions aura-t'il ?");
		questionGenerator.setAnswerTopText("Voici l'automate après minimalisation : ");
	}

	

}
