package test.demo;

import data.Automaton;
import process.builders.RandomAutomatonBuilder;

public class RandomAutomatonTest {

	public static void main(String[] args) {
		//create 5 automatons with more and more states and more and more epsilon transitions
		for(int index = 0; index < 5; index++){
			RandomAutomatonBuilder randomAutomatonBuilder = new RandomAutomatonBuilder(3 + index);
			randomAutomatonBuilder.setAlphabet("abc");
			randomAutomatonBuilder.setNumberOfEpsilonTransitions(index);
			Automaton randomAutomaton = randomAutomatonBuilder.build();
			BuilderTest.createImage(randomAutomaton, "Random_" + index, false);
		}
	}
	
}
