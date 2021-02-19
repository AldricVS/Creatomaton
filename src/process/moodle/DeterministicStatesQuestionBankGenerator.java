package process.moodle;

import data.Automaton;
import process.builders.AutomatonBuilder;
import process.builders.RandomAutomatonBuilder;

/**
 * Implémentation of the QuestionBankGenerator that permits to create question
 * on the number of states after modify an automaton to be deterministic
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class DeterministicStatesQuestionBankGenerator extends QuestionBankGenerator {

	public DeterministicStatesQuestionBankGenerator(String title, int numberOfQuestons) {
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
		
		//The answer is the number of states of the deterministic automaton
		int answer = deterministicAutomaton.getNumberOfTotalStates();
		
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
		numericalQuestionGenerator.setQuestionBottomText("Après déterminisation, combien d'états aura-t'il ?");
		numericalQuestionGenerator.setAnswerTopText("Voici l'automate après déterminisation : ");
	}

}
