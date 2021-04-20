package process.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import data.Automaton;
import data.State;
import data.Transition;
import process.util.StateListUtility;

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
	}

	/**
	 * @return the automaton attach to the builder
	 */
	public Automaton getAutomaton() {
		return automaton;
	}

	/**
	 * attach an automaton to the builder, making the list of previous automaton
	 * invalid
	 * 
	 * @param automaton the automaton to set
	 */
	public void setAutomaton(Automaton automaton) {
		this.automaton = automaton;
		isListReady = false;
	}

	/**
	 * @return the automatonList
	 */
	public LinkedList<LinkedList<ArrayList<State>>> getNerodeStatesList() {
		if (isListReady) {
			return nerodeStatesList;
		}
		return new LinkedList<LinkedList<ArrayList<State>>>();
	}

	public Automaton buildNerodeAutomaton() {
		initListStates();
		while (!checkMinimalByNerode()) {
			buildNextNerodeState();
		}
		isListReady = true;
		Automaton nerodeAutomaton = buildAutomaton();
		return nerodeAutomaton;
	}

	/**
	 * @return the minimal Automaton
	 */
	private Automaton buildAutomaton() {
		Automaton minimalAutomaton = new Automaton(getAutomaton().getAlphabet());
		if (isListReady) {
			stepStatesList = nerodeStatesList.getLast();
			Map<State, List<Transition>> transitionDestinationMap = new HashMap<State, List<Transition>>();
			Map<List<State>, State> groupListStatesMap = new HashMap< List<State>, State>();

			for (ArrayList<State> groupList : stepStatesList) {
				// a new appropriate name for our grouped state
				String name = StateListUtility.constructNameOfDeterminedStates(groupList);
				State groupState = new State(0, name);
				List<Transition> transitionList = new LinkedList<Transition>();
				// check if initial or final
				boolean isInitial = false, isFinal = false;
				for (State state : groupList) {
					if (getAutomaton().isStateInitial(state)) {
						isInitial = true;
					}
					if (getAutomaton().isStateFinal(state)) {
						isFinal = true;
					}
					transitionList.addAll(state.getTransitions());
				}
				minimalAutomaton.addState(groupState, isInitial, isFinal);
				transitionDestinationMap.put(groupState, transitionList);
				groupListStatesMap.put(groupList, groupState);
			}
			// now that we have added all state, let's add those transition
			for (State state : transitionDestinationMap.keySet()) {
				// we have our list of transition based for our new state
				List<Transition> transitionList = transitionDestinationMap.get(state);
				// lets search for our new destination
				for (Transition transition : transitionList) {
					// search among existing group if the state can be found
					for (List<State> groupState : groupListStatesMap.keySet()) {
						if (groupState.contains(transition.getDestination())) {
							State destination = groupListStatesMap.get(groupState);
							minimalAutomaton.addTransition(state, destination, transition.getLetter());
						}
					}
				}
			}
		}
		return minimalAutomaton;
	}

	/**
	 * Check that the automaton is minimal
	 * 
	 * @return true if the automaton is minimal, false otherwise
	 */
	private boolean checkMinimalByNerode() {
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
						// create destinationList, which consist of knowning where the transition link
						// to
						List<State> destinationlist;
						if ((destinationlist = mapLetter.get(transitionLetter)) == null) {
							// here, a list hasn't been linked yet, so we search for the original one, and
							// add it to our map
							for (List<State> listStates : stepStatesList) {
								if (listStates.contains(destination)) {
									destinationlist = listStates;
									break;
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
		// all group has been visited, the minimal automaton has been made
		return true;
	}

	/**
	 * En se basant sur la dernière List de nerodeStatesList, on va diviser en
	 * sous-groupe pour qu'une seule lettre part vers un autre groupe
	 */
	private void buildNextNerodeState() {
		stepStatesList = new LinkedList<ArrayList<State>>(nerodeStatesList.getLast());
		List<ArrayList<State>> newSubGroupStatesList = new LinkedList<ArrayList<State>>();

		for (List<State> groupList : stepStatesList) {
			// create a map with the alphabet
			Map<Character, List<State>> mapLetter = new HashMap<Character, List<State>>();
			for (char letter : automaton.getAlphabet().toCharArray()) {
				mapLetter.put(letter, null);
			}
			// check that the destination of each State is only directed to one group
			List<State> toRemoveInList = new LinkedList<State>();
			for (State state : groupList) {
				// for that, we will see where each transition redirect to
				for (Transition transition : state.getTransitions()) {
					char transitionLetter = transition.getLetter();
					State destination = transition.getDestination();
					// create destinationList, which consist of knowning where the transition link
					List<State> destinationlist;
					if ((destinationlist = mapLetter.get(transitionLetter)) == null) {
						// here, a list hasn't been linked yet, so we search for the original one, and
						// add it to our map
						for (List<State> listStates : stepStatesList) {
							if (listStates.contains(destination)) {
								destinationlist = listStates;
								break;
							}
						}
						mapLetter.put(transitionLetter, destinationlist);
					} else {
						// the destination map isn't null, so we must have setup a group, check our
						// destination belong to the destination group
						if (!destinationlist.contains(destination)) {
							// the destination lead to another group, so we must separe our state from his
							// group in the next step
							toRemoveInList.add(state);
						}
					}
				}
			}
			for (State state : toRemoveInList) {
				ArrayList<State> newStateGroup = new ArrayList<State>();
				newStateGroup.add(state);
				newSubGroupStatesList.add(newStateGroup);
				groupList.remove(state);
			}
		}
		stepStatesList.addAll(newSubGroupStatesList);
		nerodeStatesList.addLast(stepStatesList);
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
		ArrayList<State> finalStateList = new ArrayList<State>(automaton.getFinalStates());
		ArrayList<State> allStateList = new ArrayList<State>(automaton.getAllStates());
		allStateList.removeAll(finalStateList);
		// add them to the upper List
		stepStatesList.add(allStateList);
		stepStatesList.add(finalStateList);
		// add to the nerode table as the first "row"
		nerodeStatesList.add(stepStatesList);
	}
}
