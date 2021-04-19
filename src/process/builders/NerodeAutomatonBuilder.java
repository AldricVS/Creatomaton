package process.builders;

import data.Automaton;

/**
 * A Builder made for the minimalism by Nerode (or something like that idk)
 * @author Maxence
 */
public class NerodeAutomatonBuilder {
	
	private Automaton automaton;

	/**
	 * Construct the {@link process.builders.NerodeAutomatonBuilder NerodeAutomatonBuilder}
	 */
	public NerodeAutomatonBuilder(Automaton automaton) {
		this.setAutomaton(automaton);
	}

	/**
	 * @return the automaton
	 */
	public Automaton getAutomaton() {
		return automaton;
	}

	/**
	 * @param automaton the automaton to set
	 */
	public void setAutomaton(Automaton automaton) {
		this.automaton = automaton;
	}

}
