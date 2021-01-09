package process.builders;

import java.util.Stack;

import data.Automaton;

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
	 */
	public Automaton build() {
		Stack<Automaton> stack = new Stack<>();

		for (int i = 0; i < expression.length(); i++) {
			char currentChar = expression.charAt(i);
			if (isSpecialCharacter(currentChar)) {

			}
		}
		
		return null;
	}

	private boolean isSpecialCharacter(char character) {
		// we have 5 characters that cannot be used in those expressions
		return character == '(' || character == ')' || character == '+' || character == '.' || character == '*';
	}
}
