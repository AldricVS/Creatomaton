package data;

import java.util.ArrayList;
import java.util.List;

/**
 * A State is a part of an automaton, and can lead to other ones with
 * Transitions. Each State can be final / initial or not.
 * <p>
 * By default, a State is non-final
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class State {
	/**
	 * Id must be distinc from all others States's id.
	 */
	private int id;

	/**
	 * The name of the state is not required, as it can be named by his id
	 */
	private String name;

	// private boolean isInitial;

	private boolean isFinal;

	/**
	 * All paths from this state to others with specific letters
	 */
	private List<Transition> transitions = new ArrayList<>();

	/**
	 * Create a new State with all informations
	 * 
	 * @param id
	 * @param name
	 * @param isInitial
	 * @param isFinal
	 */
	public State(int id, String name, boolean isFinal) {
		super();
		this.id = id;
		this.name = name;
		// this.isInitial = isInitial;
		this.isFinal = isFinal;
	}

	public State(int id, String name) {
		this(id, name, false);
	}
	
	public State(int id, boolean isFinal) {
		super();
		this.id = id;
		// this.isInitial = isInitial;
		this.isFinal = isFinal;
	}
	
	public State(int id) {
		this(id, false);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	public List<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(List<Transition> transitions) {
		this.transitions = transitions;
	}

	/**
	 * Get the next state that goes from this state with a specified letter. This
	 * one can only return one State (the first one found in all transitions).
	 * 
	 * @param letter the letter of the transition
	 * @return the next state or {@code null} if no one can be found
	 * @see {@linkplain State#findNextStatesList(char)}
	 */
	public State findNextState(char letter) {
		// iteration time
		for (Transition t : transitions) {
			if (t.getLetter() == letter) {
				return t.getDestination();
			}
		}
		// no one is found
		return null;
	}

	/**
	 * Get ALL states that goes from this state with a specified letter.
	 * 
	 * @param letter the letter of the transition
	 * @return a list containing all valid states. This one can be empty if no state
	 *         is found
	 */
	public List<State> findNextStatesList(char letter) {
		List<State> states = new ArrayList<>();
		for (Transition t : transitions) {
			if (t.getLetter() == letter) {
				states.add(t.getDestination());
			}
		}
		return states;
	}

	/**
	 * Tries to add a specified transition to the state's list. This will not happen
	 * if the transtion already exists.
	 * 
	 * @param transition the transition to add
	 * @return if transistion is added to the list or not
	 */
	public boolean addTransition(Transition transition) {
		// check if any transition like this exists, and return false if it is the case
		for (Transition t : transitions) {
			if (transition.equals(t)) {
				return false;
			}
		}
		// we can add the transition
		return transitions.add(transition); // will always return true
	}

	/**
	 * Tries to add a new transition towards a state. This will not happen if the
	 * transtion already exists.
	 * 
	 * @param letter
	 * @param state  the state to go to with this transition
	 * @return if a new transistion is added or not
	 */
	public boolean addTransition(char letter, State state) {
		Transition transition = new Transition(letter, state);
		return addTransition(transition);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * Two States are equals if they have the same name and id (normally, only the
	 * id can be enough)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
