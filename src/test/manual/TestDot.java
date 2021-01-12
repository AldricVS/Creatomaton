package test.manual;

import java.text.ParseException;

import data.Automaton;
import process.builders.DotBuilder;
import process.builders.ThompsonAutomatonBuilder;

public class TestDot {
	
	public static void main(String[] args) {
		try {
			Automaton automaton = new ThompsonAutomatonBuilder("((a*)+b)").build();
			DotBuilder builder = new DotBuilder(automaton);
			String res = builder.buildDotString();
			System.out.println(res);
		}catch (ParseException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		
	}
}
