package data;

import java.util.ArrayList;
import java.util.List;

/**
 * A State is a part of an automaton, and can lead to other ones with Transitions.
 * Each State can be final / initial or not.
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
	
	private boolean isInitial;
	
	private boolean isFinal;
	
	/**
	 * All paths from this state to others with specific letters
	 */
	private List<Transition> transitions = new ArrayList<>();

	/**
	 * Create a new State with all informations
	 * @param id
	 * @param name
	 * @param isInitial
	 * @param isFinal
	 */
	public State(int id, String name, boolean isInitial, boolean isFinal) {
		super();
		this.id = id;
		this.name = name;
		this.isInitial = isInitial;
		this.isFinal = isFinal;
	}

	public State(int id, boolean isInitial, boolean isFinal) {
		super();
		this.isInitial = isInitial;
		this.isFinal = isFinal;
	}

	public State(int id) {
		super();
		this.id = id;
	}
}
