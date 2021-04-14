package hmi;

import java.util.HashMap;
import java.util.Map;

import data.Automaton;
import process.builders.AutomatonBuilder;

/**
 * Container class that permits to store an automaton and all of his modifications
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class AutomatonContainer {

	private static final String DETER = "det";
	private static final String MINI = "mini";
	private static final String MIRROR = "mir";
	private static final String SYNC = "sync";
	private static final String BASE_AUTOMATON = "base";

	private AutomatonBuilder automatonBuilder;

	private Map<String, Automaton> automatonsMap = new HashMap<>();

	public AutomatonContainer(Automaton baseAutomaton) {
		automatonBuilder = new AutomatonBuilder(baseAutomaton);
		automatonsMap.put(BASE_AUTOMATON, baseAutomaton);
	}

	public Map<String, Automaton> getAutomatons() {
		return automatonsMap;
	}
	
	public void appendSynchronizedAutomaton() {
		Automaton automaton = automatonBuilder.buildSynchronizedAutomaton();
		automatonsMap.put(SYNC, automaton);
	}

	public void appendDeterministicAutomaton() {
		Automaton automaton = automatonBuilder.buildDeterministicAutomaton();
		automatonsMap.put(DETER, automaton);
	}

	public void appendMirrorAutomaton() {
		Automaton automaton = automatonBuilder.buildMirrorAutomaton();
		automatonsMap.put(MIRROR, automaton);
	}

	public void appendMinimalAutomaton() {
		Automaton automaton = automatonBuilder.buildMinimalAutomaton();
		automatonsMap.put(MINI, automaton);
	}
}
