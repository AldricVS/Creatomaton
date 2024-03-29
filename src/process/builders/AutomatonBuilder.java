package process.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import data.Automaton;
import data.State;
import data.Transition;
import process.AutomatonManager;
import process.factory.AutomatonFactory;
import process.util.StateListUtility;
import process.util.TransitionListUtility;

/**
 * <p>
 * AutomatonBuilder is used to create new modified {@link data.Automaton
 * Automaton} from a given Automaton.
 * </p>
 * <ul>
 * List of possible modification :
 * <li>build a {@link #buildMirrorAutomaton() mirror} Automaton</li>
 * <li>build a {@link #buildSynchronizedAutomaton() synchronized} Automaton</li>
 * <li>build a {@link #buildDeterministicAutomaton() determinist} Automaton</li>
 * <li>build a {@link #buildMinimalAutomaton() minimal} Automaton</li>
 * </ul>
 * 
 * @author Maxence
 * @author Chloé Gateau
 */
public class AutomatonBuilder {

	/**
	 * 
	 */
	private Automaton automaton;

	public AutomatonBuilder(Automaton automaton) {
		this.automaton = automaton;
	}

	public Automaton getAutomaton() {
		return automaton;
	}

	public void setAutomaton(Automaton automaton) {
		this.automaton = automaton;
	}

	/**
	 * Build a new {@link data.Automaton Automaton} as a Mirror, meaning all Final
	 * States are Initial, and Initial are Final. Transition also work in reverse,
	 * going from their starting point to their destination.
	 * <p>
	 * Use the {@link process.factory.AutomatonFactory#createCopy(Automaton)
	 * createCopy()} to create the Automaton
	 * </p>
	 * 
	 * @return the new Mirror Automaton
	 */
	public Automaton buildMirrorAutomaton() {
		// create a new automaton
		Automaton miroirAutomaton = AutomatonFactory.createCopy(automaton);

		// get the list of all States
		List<State> listState;
		listState = miroirAutomaton.getAllStates();

		// get the list of Initial States and Final States
		List<State> listInitialState, listFinalState;
		listInitialState = new ArrayList<State>(miroirAutomaton.getInitialStates());
		listFinalState = new ArrayList<State>(miroirAutomaton.getFinalStates());

		// Map all reverse transition
		Map<State, List<Transition>> reverseTransitionMap;
		reverseTransitionMap = new HashMap<State, List<Transition>>();

		// set all initial states
		for (State state : listState) {
			// check if the state is initial or final
			boolean isStateInitial = listFinalState.contains(state);
			boolean isStateFinal = listInitialState.contains(state);

			// set state as initial and final
			miroirAutomaton.setStateInitial(state, isStateInitial);
			miroirAutomaton.setStateFinal(state, isStateFinal);

			// get the list of transitions to reverse it after
			List<Transition> listTransitions = state.getTransitions();
			reverseTransitionMap.put(state, new ArrayList<Transition>(listTransitions));
			listTransitions.clear();
		}
		// now, reverse all transitions
		for (State state : reverseTransitionMap.keySet()) {
			List<Transition> listTransitions = reverseTransitionMap.get(state);
			for (Transition transition : listTransitions) {
				miroirAutomaton.addTransition(transition.getDestination(), state, transition.getLetter());
			}
		}

		return miroirAutomaton;
	}

	/**
	 * Build a new Automaton with all his States as determined. Calling this method
	 * will change the given automaton and {@link #buildSynchronizedAutomaton()
	 * synchronized} it (if it isn't already synchronized)
	 * 
	 * To create our new determinist Automaton, we will using a method for the
	 * {@link #createDeterminisedState(Automaton, List) the creation of State} and
	 * another for
	 * {@link #addDeterminisedDestinationFromTransitionLetter(Automaton, List, State, char)
	 * the linking with their destination}
	 * 
	 * @return the new Determined Automaton
	 */
	public Automaton buildDeterministicAutomaton() {
		AutomatonManager manager = new AutomatonManager(automaton);
		// check if automaton is synchronized
		if (!manager.isSynchronized()) {
			// if not, synchronize it
			automaton = buildSynchronizedAutomaton();
		}

		// listState have all state of the first determined state
		List<State> listState = new ArrayList<State>();
		listState.addAll(automaton.getInitialStates());

		// listTransition will get all transition of listState
		List<Transition> listTransitions = new ArrayList<Transition>();

		// listNewState will get all new State from the destination of listTransition
		List<State> listNewState = new ArrayList<State>();

		// listDeterminedState is a list of all listState we need to go through
		LinkedList<List<State>> listDeterminedStates = new LinkedList<List<State>>();
		listDeterminedStates.add(listState);

		// get the alphabet and create the new automaton
		String alphabet = automaton.getAlphabet();
		Automaton determinedAutomaton = AutomatonFactory.createAutomaton(alphabet);

		// id of the next state that will be added
		int nextStateId = 0;

		// while we have new states to add
		while (!listDeterminedStates.isEmpty()) {

			// listState will take the first list of listDeterminedState
			listState = listDeterminedStates.pop();

			// we add all transition from all the state we are coming from
			listTransitions = TransitionListUtility.getAllTransitionFromListStates(listState);

			State stateDeparture = createDeterminisedState(determinedAutomaton, listState);
			if (stateDeparture.getId() >= nextStateId) {
				nextStateId++;
			}

			// for each of the alphabet letter
			for (char letter : alphabet.toCharArray()) {
				listNewState = addDeterminisedDestinationFromTransitionLetter(determinedAutomaton, listTransitions,
						stateDeparture, letter);

				if (determinedAutomaton.getStateById(nextStateId) != null) {
					// add the list of state to be search through
					listDeterminedStates.add(listNewState);
					nextStateId++;
				}
			}
		}
		return determinedAutomaton;
	}

	/**
	 * Take all destination states from the list of Transition with the given
	 * letter, and if there is a destination, use it to create a new determined
	 * state, in the determined automaton with the specified id, then add a
	 * transition with the given letter from the given state to the new automaton.
	 * 
	 * @param determinedAutomaton the Automaton to add the new determinised state
	 * @param listTransitions     the list of transition to form the new state
	 * @param stateDeparture      the determinised state we come from
	 * @param letter              the letter of the transition we will create
	 * @return
	 */
	private List<State> addDeterminisedDestinationFromTransitionLetter(Automaton determinedAutomaton,
			List<Transition> listTransitions, State stateDeparture, char letter) {

		// get all state from valid transition
		List<State> listNewState;
		listNewState = TransitionListUtility.getValidDestinationFromTransition(listTransitions, letter);

		// we have gone through all transition
		// check that we have found a destination
		if (!listNewState.isEmpty()) {
			State newDeterminedState = createDeterminisedState(determinedAutomaton, listNewState);
			determinedAutomaton.addTransition(stateDeparture, newDeterminedState, letter);
		}
		return listNewState;
	}

	/**
	 * Construct a new Determined State, with his name based on a list of State
	 * 
	 * @param determinedAutomaton the determined automaton where the state will be
	 *                            added
	 * @param listState           the list of State which will be based on to create
	 *                            the new determined State
	 * @return the new determinised State
	 */
	private State createDeterminisedState(Automaton determinedAutomaton, List<State> listState) {
		// check if the state is initial or not
		boolean isInitial = false;
		if (determinedAutomaton.isEmpty()) {
			isInitial = true;
		}
		// check if the state is final or not
		boolean isFinal = false;
		if (StateListUtility.hasCommonStates(listState, automaton.getFinalStates())) {
			isFinal = true;
		}

		// construct the name of our new determined state
		String nameDeparture = "";
		nameDeparture = StateListUtility.constructNameOfDeterminedStates(listState);

		// search the state from where we come from in the automaton's list
		int stateStartingId = StateListUtility.getIdStateFromNameInList(determinedAutomaton.getAllStates(),
				nameDeparture);

		// verification if we find it
		State stateDeparture;
		if (stateStartingId < 0) {
			// create a new state in determinedAutomaton
			stateDeparture = new State(0, nameDeparture);
			determinedAutomaton.addState(stateDeparture, isInitial, isFinal);
		} else {
			// get the departure state
			stateDeparture = determinedAutomaton.getStateById(stateStartingId);
		}
		return stateDeparture;
	}

	private static final String WELL_STATE_NAME = "Well";
	
	/**
	 * Add a new state that will be redirect to anytime a state doesn't have a
	 * transition for each character in the alphabet of the automaton
	 * 
	 * @return the newly modified Automaton
	 */
	public Automaton addWellState() {
		if (StateListUtility.getIdStateFromNameInList(automaton.getAllStates(), WELL_STATE_NAME) > 0) {
			return automaton;
		}
		Automaton wellAutomaton = buildSynchronizedAutomaton();
		// create the new state and add it to the automaton
		// (dont worry about the id, it will be modified accordingly
		State wellState = new State(0, WELL_STATE_NAME);
		wellAutomaton.addState(wellState);
		// get the alphabet
		String alphabet = wellAutomaton.getAlphabet();
		// search for all state
		for (State state : wellAutomaton.getAllStates()) {
			// if they have all letters
			for (char letter : alphabet.toCharArray()) {
				boolean hasLetter = false;
				// check all transition of the state
				for (Transition transition : state.getTransitions()) {
					if (transition.getLetter() == letter) {
						hasLetter = true;
					}
				}
				// if the letter has been found
				if (!hasLetter) {
					wellAutomaton.addTransition(state, wellState, letter);
				}
			}
		}
		return wellAutomaton;
	}

	/**
	 * Build the minimal automaton, with the least state and transition. This method
	 * use the {@link #buildDeterministicAutomaton() buildDeterminism} and the
	 * {@link #buildMirrorAutomaton() buildMirror}, and will work as follow :
	 * det(mir(det(mit(automaton)))).
	 * 
	 * @return the minimal automaton
	 */
	public Automaton buildMinimalAutomaton() {
		Automaton oldAutomaton = automaton;
		automaton = buildMirrorAutomaton();
		automaton = buildDeterministicAutomaton();
		automaton = buildMirrorAutomaton();
		automaton = buildDeterministicAutomaton();
		Automaton newAutomaton = automaton;
		automaton = oldAutomaton;
		return newAutomaton;
	}

	/**
	 * Transform the automaton by synchronising it, meaning that there will no
	 * longer have epsilon transition
	 * 
	 * @return the synchronized automaton
	 */
	public Automaton buildSynchronizedAutomaton() {
		Automaton resultAutomaton = AutomatonFactory.createCopy(automaton);
		List<State> listStates = resultAutomaton.getAllStates();

		// parcourir les etats tant qu'il y a des epsilon-transition
		while (TransitionListUtility.isThereAnyEpsilonTransition(listStates)) {
			for (State state : listStates) {
				// recupere les transitions epsilon a supprimer
				List<Transition> listTransitionsEpsilon = new ArrayList<Transition>();
				// parcours des transitions de l'etat en court
				for (Transition transition : state.getTransitions()) {
					if (transition.isEpsilon()) {
						listTransitionsEpsilon.add(transition);
					}
				}
				// suppression des transitions
				for (Transition transition : listTransitionsEpsilon) {
					removeEpsilon(resultAutomaton, state, transition);
				}
			}
		}
		resultAutomaton = removeInaccessibleState(resultAutomaton);
		return resultAutomaton;
	}

	/**
	 * Remove a transition epsilon and give all his transition & status to the
	 * departure state
	 * 
	 * @param automaton         the Automaton where the state is
	 * @param departState       the state where the epsilon-transition start from
	 * @param epsilonTransition the transition to remove
	 */
	private void removeEpsilon(Automaton automaton, State departState, Transition epsilonTransition) {
		if (epsilonTransition.isEpsilon()) {
			State destinationState = epsilonTransition.getDestination();

			// si la destination est final, l'etat de départ devient final
			if (automaton.isStateFinal(destinationState)) {
				automaton.setStateFinal(departState, true);
			}

			// recupere toutes les transition qui partent de l'état final et les faire
			// partir de l'état de départ
			for (Transition transition : destinationState.getTransitions()) {
				departState.addTransition(transition);
			}
			departState.removeTransition(epsilonTransition);
		}
	}

	/**
	 * Remove all inaccessible state from an automaton
	 * 
	 * @param automaton the Automaton where we want to remove inaccessible state
	 * @return
	 */
	private Automaton removeInaccessibleState(Automaton automaton) {
		List<State> listStates = automaton.getAllStates();
		for (State state : listStates) {
			if (!(automaton.isStateAccessible(state)) && !(automaton.isStateInitial(state))) {
				automaton.removeState(state);
			}
		}
		return automaton;
	}

}
