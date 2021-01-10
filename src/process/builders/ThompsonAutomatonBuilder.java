package process.builders;

import java.text.ParseException;
import java.util.EmptyStackException;
import java.util.Stack;

import data.Automaton;
import process.factory.ThompsonAutomatonFactory;

/**
 * Builder class that takes an expression (String) and creates an automaton with
 * it.
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class ThompsonAutomatonBuilder {
	private String expression;

	/**
	 * Create an instance of the builder, that will do a fast check on the
	 * expression given.
	 * 
	 * @param expression the expression to parse
	 * @throws IllegalArgumentException if the expression is not valid
	 */
	public ThompsonAutomatonBuilder(String expression) throws IllegalArgumentException {
		this.expression = expression;
		checkExpression();
	}

	private void checkExpression() throws IllegalArgumentException {
		/*
		 * We will check if expression contains the same number of '(' and ')'
		 */
		int parenthesisWeight = 0;
		char previousCharacter = '\u0000';
		for (char c : expression.toCharArray()) {
			if (c == '(') {
				parenthesisWeight++;
			} else if (c == ')') {
				parenthesisWeight--;
			} else {
				// check if next character is the same (we don't accept "aa" or "a++b" for
				// example)
				if (previousCharacter == c) {
					throw new IllegalArgumentException("The character " + c + " is duplicated");
				}
			}
		}
		if (parenthesisWeight != 0) {
			throw new IllegalArgumentException("The number of parenthesis in the operation is not valid");
		}
	}

	/**
	 * Create the thompson automaton following the String expression. We will assume
	 * that the expression is valid
	 * 
	 * @return the automaton that corresponds to the expression.
	 * @throws ParseException if any problem during parsing the expression occurs
	 */
	public Automaton build() throws ParseException{
		Stack<Automaton> automatonStack = new Stack<>();
		Stack<Character> operatorStack = new Stack<>(); 

		int i;
		for (i = 0; i < expression.length(); i++) {
			char currentChar = expression.charAt(i);
			
			if(currentChar == ' ' || currentChar == '(') {
				continue;
			}
			
			if(isOperator(currentChar)) {
				operatorStack.push(currentChar);
			}else if(currentChar == ')') {
				// Get the needed operator and work depending on it
				char operator;
				try {
					operator = operatorStack.pop();
				}catch (EmptyStackException e) {
					// By default, we want to make a concatenation
					operator = Character.MIN_VALUE;
				}
				
				try {
					switch (operator) {
					case '*':
						// Get only 1 automaton from the stack and apply the star automaton
						Automaton pop = automatonStack.pop();
						Automaton starAutomaton = ThompsonAutomatonFactory.createStarAutomaton(pop);
						automatonStack.push(starAutomaton);
						break;
					case '+':
						//we have the second automaton before the first one because of the stack insertion order
						Automaton unionRight = automatonStack.pop();
						Automaton unionLeft = automatonStack.pop();
						Automaton unionAutomaton = ThompsonAutomatonFactory.createUnionAutomaton(unionLeft, unionRight);
						automatonStack.push(unionAutomaton);
						break;
					case '.':
					case Character.MIN_VALUE:
						// We have either operator '.' or not any operator, we will do a concatenation
						Automaton concatRight = automatonStack.pop();
						Automaton concatLeft = automatonStack.pop();
						Automaton concatAutomaton = ThompsonAutomatonFactory.createConcatenationAutomaton(concatLeft, concatRight);
						automatonStack.push(concatAutomaton);
						break;
					default:
						throw new ParseException("Unrecognized operator in stack", i);	
					}
				}catch (EmptyStackException e) {
					// Here, we don't have enough automatons to use with the operator
					throw new ParseException("Could not get enough automatons to work with operator " + operator, i);
				}
			}else {
				//we have a normal character, create the letter automaton and add it to the stack
				Automaton letterAutomaton = ThompsonAutomatonFactory.createLetterAutomaton(currentChar);
				automatonStack.push(letterAutomaton);
			}
		}
		
		// Normally, we have only one automaton remaining in the stack
		if(automatonStack.size() != 1) {
			throw new ParseException("Too many automatons left in the stack, operation probably not finished.", i);
		}else {
			return automatonStack.pop();
		}
	}
	
	private boolean isOperator(char character) {
		return character == '+' || character == '.' || character == '*';
	}
}
