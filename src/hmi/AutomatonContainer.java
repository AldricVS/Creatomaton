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

	public static final String DETER_AUTOMATON = "det";
	public static final String MINI_AUTOMATON = "mini";
	public static final String MIRROR_AUTOMATON = "mir";
	public static final String SYNC_AUTOMATON = "sync";
	public static final String BASE_AUTOMATON = "base";

	private AutomatonBuilder automatonBuilder;

	private Map<String, Automaton> automatonsMap = new HashMap<>();

	public AutomatonContainer(Automaton baseAutomaton) {
		automatonBuilder = new AutomatonBuilder(baseAutomaton);
		automatonsMap.put(BASE_AUTOMATON, baseAutomaton);
	}
	
	public Automaton getBaseAutomaton() {
		return automatonsMap.get(BASE_AUTOMATON);
	}

	public Map<String, Automaton> getAutomatons() {
		return automatonsMap;
	}
	
	public void appendSynchronizedAutomaton() {
		Automaton automaton = automatonBuilder.buildSynchronizedAutomaton();
		automatonsMap.put(SYNC_AUTOMATON, automaton);
	}

	public void appendDeterministicAutomaton() {
		Automaton automaton = automatonBuilder.buildDeterministicAutomaton();
		automatonsMap.put(DETER_AUTOMATON, automaton);
	}

	public void appendMirrorAutomaton() {
		Automaton automaton = automatonBuilder.buildMirrorAutomaton();
		automatonsMap.put(MIRROR_AUTOMATON, automaton);
	}

	public void appendMinimalAutomaton() {
		Automaton automaton = automatonBuilder.buildMinimalAutomaton();
		automatonsMap.put(MINI_AUTOMATON, automaton);
	}
}
