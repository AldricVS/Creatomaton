package process.moodle;

import java.util.List;

import data.Automaton;
import data.State;
import data.Transition;
import process.builders.AutomatonBuilder;
import process.builders.RandomAutomatonBuilder;
import process.util.TransitionListUtility;

/**
 * Implémentation of the QuestionBankGenerator that permits to create question
 * on the number of transitions after modify an automaton to be deterministic
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class DeterministicTransitionsQuestionBankGenerator extends QuestionBankGenerator {

	public DeterministicTransitionsQuestionBankGenerator(String title, int numberOfQuestons) {
		super(title, numberOfQuestons);
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
		Automaton deterministicAutomaton = automatonBuilder.buildDeterministicAutomaton();
		
		//The answer is the number of transtions of the deterministic automaton
		List<State> allStates = deterministicAutomaton.getAllStates();
		List<Transition> allTransitionFromListStates = TransitionListUtility.getAllTransitionFromListStates(allStates);
		int answer = allTransitionFromListStates.size();
		
		// Define the question with all elements needed
		NumericalQuestionGenerator numericalQuestionGenerator = getNumericalQuestionGenerator();
		numericalQuestionGenerator.setAnswerValue(answer);
		numericalQuestionGenerator.setQuestionAutomaton(automaton);
		numericalQuestionGenerator.setAnswerAutomaton(deterministicAutomaton);
	}
	
	@Override
	protected void initSpecificQuestionGenerator() {
		NumericalQuestionGenerator numericalQuestionGenerator = getNumericalQuestionGenerator();
		numericalQuestionGenerator.setQuestionTopText("Soit l'automate suivant :");
		numericalQuestionGenerator.setQuestionBottomText("Après déterminisation, combien de transitions aura-t'il ?");
		numericalQuestionGenerator.setAnswerTopText("Voici l'automate après déterminisation : ");
	}

}
