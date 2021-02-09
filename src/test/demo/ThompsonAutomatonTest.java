package test.demo;

import java.text.ParseException;

import data.Automaton;
import process.builders.AutomatonBuilder;
import process.builders.ThompsonAutomatonBuilder;

public class ThompsonAutomatonTest {

	public static void main(String[] args) throws ParseException {
		String expression = "((a*)+(bc))";
		ThompsonAutomatonBuilder thompsonAutomatonBuilder = new ThompsonAutomatonBuilder(expression);
		Automaton automaton = thompsonAutomatonBuilder.build();
		BuilderTest.createImage(automaton, "ThompsonAutomaton", false);
		AutomatonBuilder automatonBuilder = new AutomatonBuilder(automaton);
		Automaton synchronizedAutomaton = automatonBuilder.buildSynchronizedAutomaton();
		BuilderTest.createImage(synchronizedAutomaton, "ThompsonAutomatonSync", false);
	}

}
