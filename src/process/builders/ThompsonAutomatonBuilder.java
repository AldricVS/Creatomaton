package process.builders;

import java.text.ParseException;
import java.util.EmptyStackException;
import java.util.Stack;

import data.Automaton;
import data.AutomatonConstants;
import process.factory.ThompsonAutomatonFactory;

/**
 * Builder class that takes an expression (String) and creates an automaton with
 * it.
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class ThompsonAutomatonBuilder {
	private String expression;

	private static final char CONCATENATION_CHARACTER = '.';
	private static final char UNION_CHARACTER = '+';
	private static final char REPETITION_CHARACTER = '*';
	private static final char OPEN_PARENTHESIS_CHARACTER = '(';
	private static final char CLOSE_PARENTHESIS_CHARACTER = ')';

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
	 * that the expression is valid.
	 * 
	 * @return the automaton that corresponds to the expression.
	 * @throws ParseException if any problem during parsing the expression occurs
	 */
	public Automaton build() throws ParseException {
		Stack<Automaton> automatonStack = new Stack<>();
		Stack<Character> operatorStack = new Stack<>();

		for (int i = 0; i < expression.length(); i++) {
			char character = expression.charAt(i);

			if (character == ' ') {
				continue;
			}

			// Check if it is an operator
			if (character == UNION_CHARACTER || character == CONCATENATION_CHARACTER || character == REPETITION_CHARACTER
					|| character == OPEN_PARENTHESIS_CHARACTER) {

				boolean end = false;
				// Check if the operators before have higher priority
				while (!operatorStack.isEmpty() && !end) {
					char prec = operatorStack.peek();
					if (hasHigherPriority(character, prec)) {
						treatOperation(i, operatorStack.pop(), automatonStack);
					}else {
						end = true;
					}
				}

				operatorStack.push(character);

			} else if (character == CLOSE_PARENTHESIS_CHARACTER) {
				// Pop all operands until encoutering an opening parenthesis
				char operator = operatorStack.pop();
				while (operator != OPEN_PARENTHESIS_CHARACTER) {
					treatOperation(i, operator, automatonStack);
					operator = operatorStack.pop();
				}
			} else {
				Automaton letterAutomaton;
				// Create a letter automaton with the epsilon character if we can read "epsilon"
				if(expression.indexOf("epsilon", i) == i) {
					letterAutomaton = ThompsonAutomatonFactory.createLetterAutomaton(AutomatonConstants.EPSILON_CHAR);
					i += 6;
				}else {
					// Treat the character as a simple letter automaton
					letterAutomaton = ThompsonAutomatonFactory.createLetterAutomaton(character);
				}
				automatonStack.push(letterAutomaton);
			}
		}

		// Last thing, treat all other operands in the stack
		while (!operatorStack.isEmpty()) {
			treatOperation(0, operatorStack.pop(), automatonStack);
		}

		// Normally, we have only one automaton remaining in the stack
		if (automatonStack.size() != 1) {
			throw new ParseException("Too many automatons left in the stack, operation probably not finished.", expression.length());
		} else {
			return automatonStack.pop();
		}
	}

	/**
	 * Check if operator2 has higher priority than operator1
	 * 
	 * @param operator1
	 * @param operator2
	 * @return
	 */
	private boolean hasHigherPriority(char operator1, char operator2) {
		return (operator1 != REPETITION_CHARACTER && operator2 == REPETITION_CHARACTER)
				|| (operator1 == UNION_CHARACTER && operator2 == CONCATENATION_CHARACTER);
	}

	/**
	 * @param automatonStack
	 * @param currentIndex
	 * @param operator
	 * @throws ParseException
	 */
	private void treatOperation(int currentIndex, char operator, Stack<Automaton> automatonStack) throws ParseException {
		switch (operator) {
		case REPETITION_CHARACTER:
			// Get only 1 automaton from the stack and apply the star automaton
			pushStarAutomaton(automatonStack);
			break;
		case UNION_CHARACTER:
			// we have the second automaton before the first one because of the stack
			// insertion order
			pushUnionAutomaton(automatonStack);
			break;
		case CONCATENATION_CHARACTER:
		case AutomatonConstants.EPSILON_CHAR:
			// We have either operator '.' or not any operator, we will do a concatenation
			pushConcatenationAutomaton(automatonStack);
			break;
		default:
			throw new ParseException("Unrecognized operator in stack", currentIndex);
		}
	}

	/**
	 * @param automatonStack
	 */
	private void pushConcatenationAutomaton(Stack<Automaton> automatonStack) {
		Automaton concatRight = automatonStack.pop();
		Automaton concatLeft = automatonStack.pop();
		Automaton concatAutomaton = ThompsonAutomatonFactory.createConcatenationAutomaton(concatLeft, concatRight);
		automatonStack.push(concatAutomaton);
	}

	/**
	 * @param automatonStack
	 */
	private void pushUnionAutomaton(Stack<Automaton> automatonStack) {
		Automaton unionRight = automatonStack.pop();
		Automaton unionLeft = automatonStack.pop();
		Automaton unionAutomaton = ThompsonAutomatonFactory.createUnionAutomaton(unionLeft, unionRight);
		automatonStack.push(unionAutomaton);
	}

	/**
	 * @param automatonStack
	 */
	private void pushStarAutomaton(Stack<Automaton> automatonStack) {
		Automaton pop = automatonStack.pop();
		Automaton starAutomaton = ThompsonAutomatonFactory.createStarAutomaton(pop);
		automatonStack.push(starAutomaton);
	}

	/**
	 * @deprecated better use {@link ThompsonAutomatonBuilder#build()}
	 * Create the thompson automaton following the String expression. We will assume
	 * that the expression is valid
	 * 
	 * @return the automaton that corresponds to the expression.
	 * @throws ParseException if any problem during parsing the expression occurs
	 */
	public Automaton buildOld() throws ParseException {
		Stack<Automaton> automatonStack = new Stack<>();
		Stack<Character> operatorStack = new Stack<>();

		int i;
		for (i = 0; i < expression.length(); i++) {
			char currentChar = expression.charAt(i);
			;

			if (currentChar == ' ') {
				continue;
			} else if (currentChar == '(') {
				// Check the previous char, if we have a character or a closing parenthesis,
				// we know that there is an implicit '.'
				if (i != 0) {
					char previousCharacter = expression.charAt(i - 1);
					if (!isOperator(previousCharacter) && previousCharacter != ' ' && previousCharacter != '(') {
						operatorStack.push(CONCATENATION_CHARACTER);
					}
				}
				continue;
			}

			if (isOperator(currentChar)) {
				operatorStack.push(currentChar);
			} else if (currentChar == ')') {

				// Get the needed operator and work depending on it
				char operator;
				try {
					operator = operatorStack.pop();

				} catch (EmptyStackException e) {
					// By default, we want to make a concatenation
					operator = CONCATENATION_CHARACTER;
				}

				try {
					treatOperation(i, operator, automatonStack);
				} catch (EmptyStackException e) {
					// Here, we don't have enough automatons to use with the operator
					throw new ParseException("Could not get enough automatons to work with operator " + operator, i);
				}
			} else {
				// we have a normal character, create the letter automaton and add it to the
				// stack
				Automaton letterAutomaton = ThompsonAutomatonFactory.createLetterAutomaton(currentChar);
				automatonStack.push(letterAutomaton);
			}
		}

		// Normally, we have only one automaton remaining in the stack
		if (automatonStack.size() != 1) {
			throw new ParseException("Too many automatons left in the stack, operation probably not finished.", i);
		} else {
			return automatonStack.pop();
		}
	}

	private boolean isOperator(char character) {
		return character == UNION_CHARACTER || character == CONCATENATION_CHARACTER || character == REPETITION_CHARACTER;
	}

	
}
