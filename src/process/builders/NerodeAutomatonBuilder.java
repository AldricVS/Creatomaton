package process.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import data.Automaton;
import data.State;
import data.Transition;

/**
 * A Builder made for the minimalism by Nerode (or something like that idk)
 * 
 * @author Maxence
 */
public class NerodeAutomatonBuilder {

	private Automaton automaton;

	private boolean isListReady = false;
	private LinkedList<ArrayList<State>> stepStatesList;
	private LinkedList<LinkedList<ArrayList<State>>> nerodeStatesList;

	/**
	 * Construct the {@link process.builders.NerodeAutomatonBuilder
	 * NerodeAutomatonBuilder}
	 */
	public NerodeAutomatonBuilder(Automaton automaton) {
		setAutomaton(automaton);
		//TODO init ailleurs
		initListStates();
	}

	/**
	 * @return the automaton
	 */
	public Automaton getAutomaton() {
		return automaton;
	}

	/**
	 * attach an automaton to the builder, making the list of previous automaton invalid
	 * @param automaton the automaton to set
	 */
	public void setAutomaton(Automaton automaton) {
		this.automaton = automaton;
		isListReady = false;
	}

	/**
	 * @return the automatonList
	 */
	public List<ArrayList<State>> getStatesStepList() {
		if (isListReady) {
			return stepStatesList;
		}
		return new LinkedList<ArrayList<State>>();
	}

	public Automaton buildNerodeAutomaton() {
		initListStates();
		while (!checkMinimalByNerode()) {
			buildNextNerodeState();
			isListReady = true;
		}
		return automaton;
	}

	/**
	 * Check that the automaton is minimal
	 * 
	 * @return true if the automaton is minimal, false otherwise
	 * TODO changer en private ?
	 */
	public boolean checkMinimalByNerode() {
		stepStatesList = nerodeStatesList.getLast();
		// for each List of State
		for (List<State> stateList : stepStatesList) {
			// create a map with the alphabet
			Map<Character, List<State>> mapLetter = new HashMap<Character, List<State>>();
			for (char letter : automaton.getAlphabet().toCharArray()) {
				mapLetter.put(letter, null);
			}
			// check that the destination of each State is only directed to one group
			for (State state : stateList) {
				// for that, we will see where each transition redirect to
				for (Transition transition : state.getTransitions()) {
					char transitionLetter = transition.getLetter();
					State destination = transition.getDestination();
					// check that the letter is here, otherwise there is an error
					if (mapLetter.containsKey(transitionLetter)) {
						// create destinationList, which consist of knowning where the transition link to
						List<State> destinationlist;
						if ((destinationlist = mapLetter.get(transitionLetter)) == null) {
							// here, a list hasn't been linked yet, so we search for the original one, and
							// add it to our map
							for (List<State> listStates : stepStatesList) {
								if (listStates.contains(destination)) {
									destinationlist = listStates;
								}
							}
							mapLetter.put(transitionLetter, destinationlist);
						} else {
							// the destination map isn't null, so we must have setup a group, check our
							// destination belong to the destination group
							if (!destinationlist.contains(destination)) {
								return false;
							}
						}
					} else {
						throw new IllegalArgumentException(
								"The contained letter inside a transition isn't part of the alphabet");
					}
				}
			}
			// we have finish with our group, get to the next one
		}
		return true;
	}


	/**
	 * 
	 */
	private void buildNextNerodeState() {
		
	}

	/**
	 * 
	 */
	private void initListStates() {
		// initialize the nerode table
		nerodeStatesList = new LinkedList<LinkedList<ArrayList<State>>>();
		// initialize the List of List
		stepStatesList = new LinkedList<ArrayList<State>>();
		// create the List
		ArrayList<State> finalStateList = new ArrayList<State>();
		finalStateList.addAll(automaton.getFinalStates());
		ArrayList<State> allStateList = new ArrayList<State>();
		allStateList.addAll(automaton.getAllStates());
		allStateList.removeAll(finalStateList);
		// add them to the upper List
		stepStatesList.add(allStateList);
		stepStatesList.add(finalStateList);
		//add to the nerode table as the first "row"
		nerodeStatesList.add(stepStatesList);
	}
}
