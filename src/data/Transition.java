package data;

/**
 * A Transition is a path from a State to another (it can be the exact same state).
 * Each Transition has a character indicating which letter to go through this transition
 * (if this character is empty i.e equals to '\0000' or {@code Character.MIN_VALUE}, the transition will be treated as an epsilon-transition),
 * and the State that Transition leads to.
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class Transition {
	/**
	 * If equals to '\0000', the transition is an Epsilon-transition
	 */
	private char letter;
	
	
	private State destination;
	
	public Transition(char letter, State destination) {
		super();
		this.letter = letter;
		this.destination = destination;
	}
	
	/**
	 * Checks if the letter of the transition is 'empty' (or equals to '\0000')
	 * @return if this transition is an Epsilon-transition or not 
	 */
	public boolean isEpsilon() {
		return letter == Character.MIN_VALUE;
	}
	
	/**
	 * change letter to 'Epsilon value'
	 */
	public void changeToEpsilon() {
		letter = Character.MIN_VALUE;
	}

	public char getLetter() {
		return letter;
	}

	public State getDestination() {
		return destination;
	}

	public void setLetter(char letter) {
		this.letter = letter;
	}

	public void setDestination(State destination) {
		this.destination = destination;
	}
}
