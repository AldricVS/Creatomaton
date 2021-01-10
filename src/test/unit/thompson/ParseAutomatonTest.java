package test.unit.thompson;

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import process.builders.ThompsonAutomatonBuilder;

/**
 * Tests to check the automaton resulting from multiple inputs.
 * For now, only the number of states od each resulting automatons will be tested
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class ParseAutomatonTest {
	
	@Test
	public void testAutomaton1() throws ParseException {
		final String INPUT_EXPRESSION = "(((a*)(b*))+(c(a+b)))";
		Automaton automaton = automatonFromExpression(INPUT_EXPRESSION);
		int numberOfStates = automaton.getNumberOfTotalStates();
		assertEquals(16, numberOfStates);
	}
	
	@Test
	public void testAutomaton2() throws ParseException {
		final String INPUT_EXPRESSION = "(((a*).(b*))+(c(d*)))";
		Automaton automaton = automatonFromExpression(INPUT_EXPRESSION);
		int numberOfStates = automaton.getNumberOfTotalStates();
		assertEquals(14, numberOfStates);
	}
	
	private Automaton automatonFromExpression(String expression) throws ParseException {
		ThompsonAutomatonBuilder builder = new ThompsonAutomatonBuilder(expression);
		return builder.build();
	}
}
