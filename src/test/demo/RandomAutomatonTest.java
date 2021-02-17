package test.demo;

import data.Automaton;
import process.builders.AutomatonBuilder;
import process.builders.RandomAutomatonBuilder;

public class RandomAutomatonTest {

	public static void main(String[] args) {
		// create a random automatons
		int numberState = 5;
		RandomAutomatonBuilder randomAutomatonBuilder = new RandomAutomatonBuilder(numberState);
		randomAutomatonBuilder.setAlphabet("abc");
		randomAutomatonBuilder.setNumberOfEpsilonTransitions(numberState - 2);
		Automaton randomAutomaton = randomAutomatonBuilder.build();
		BuilderTest.createImage(randomAutomaton, "Random_" + numberState, false);
		
		AutomatonBuilder automatonBuilder = new AutomatonBuilder(randomAutomaton);
		Automaton randomSynchAutomaton = automatonBuilder.buildSynchronizedAutomaton();
		BuilderTest.createImage(randomSynchAutomaton, "RandomSynch_" + numberState, false);
		
		automatonBuilder.setAutomaton(randomAutomaton);
		Automaton randomDeterminedAutomaton = automatonBuilder.buildDeterministicAutomaton();
		BuilderTest.createImage(randomDeterminedAutomaton, "RandomDeter_" + numberState, false);
		
		automatonBuilder.setAutomaton(randomAutomaton);
		Automaton randomMinimalAutomaton = automatonBuilder.buildMinimalAutomaton();
		BuilderTest.createImage(randomMinimalAutomaton, "RandomMini_" + numberState, false);
	}

}
