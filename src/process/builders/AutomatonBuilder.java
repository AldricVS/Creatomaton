/**
 * 
 */
package process.builders;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import data.Automaton;
import data.State;
import data.Transition;
import process.factory.AutomatonFactory;
import process.util.StateListUtility;
import process.util.TransitionListUtility;

/**
 * <p>
 * AutomatonBuilder is used to create new modified Automaton from a given
 * Automaton.
 * </p>
 * 
 * @author Maxence
 * @author Chloé Gateau
 */
public class AutomatonBuilder {

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
		// some variable
		List<State> listState, listInitialState, listFinalState;

		// our new automaton
		Automaton miroirAutomaton;

		miroirAutomaton = AutomatonFactory.createCopy(automaton);
		// miroirAutomaton = new Automaton(automaton.getAlphabet());

		// get the list of Initial States from Final States
		listInitialState = new ArrayList<State>(miroirAutomaton.getFinalStates());

		// get the list of Final States from Initial States
		listFinalState = new ArrayList<State>(miroirAutomaton.getInitialStates());

		// get the list of all States
		listState = new ArrayList<State>(miroirAutomaton.getAllStates());

		// clear old list of states
		miroirAutomaton.clearAllStates();

		// add all states
		for (State state : listState) {
			miroirAutomaton.addState(state, false, false);
		}

		List<Transition> allTransitionFromListStates = TransitionListUtility.getAllTransitionFromListStates(listState);
		for (Transition transition : allTransitionFromListStates) {
			State state;
			state = TransitionListUtility.getDepartureFromTransition(listState, transition);
			state.removeTransition(transition);
			miroirAutomaton.addTransition(transition.getDestination(), state, transition.getLetter());
		}

		// set all initial states
		for (State state : listInitialState) {
			miroirAutomaton.setStateInitial(state, true);
		}

		// set all final states
		for (State state : listFinalState) {
			miroirAutomaton.setStateFinal(state, true);
		}

		return miroirAutomaton;
	}

	/**
	 * Build a new Automaton with all his States as determined.
	 * 
	 * @return the new Determined Automaton
	 */
	public Automaton buildDeterministicAutomaton() {
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
		Automaton determinedAutomaton = new Automaton(alphabet);

		// id of the state and a boolean to determined if they are final
		int id = 0;
		boolean isFinal;

		// while we have new states to add
		while (!listDeterminedStates.isEmpty()) {
			// reset of isFinal
			isFinal = false;

			// listState will take the first list of listDeterminedState
			listState = listDeterminedStates.pop();

			// construct the name of our new determined state
			String nameDeparture = "";
			nameDeparture = StateListUtility.constructNameOfDeterminedStates(listState);

			// we add all transition from all the state we are coming from
			listTransitions = TransitionListUtility.getAllTransitionFromListStates(listState);

			// check if there is a final state
			if (StateListUtility.hasCommonStates(listState, automaton.getFinalStates())) {
				isFinal = true;
			}

			// we dont have any interest in our old list
			listState.clear();

			// search the state from where we come from in the automaton's list
			int stateStartingId = StateListUtility.getIdStateFromNameInList(determinedAutomaton.getAllStates(),
					nameDeparture);

			// verification if we find it
			State stateDeparture;
			if (stateStartingId < 0) {
				// create a new state in determinedAutomaton
				stateDeparture = new State(id, nameDeparture);
				id++;
				determinedAutomaton.addState(stateDeparture, true, isFinal);
			} else {
				// get the departure state
				stateDeparture = determinedAutomaton.getStateFromId(stateStartingId);
			}

			// for each of the alphabet letter
			for (char letter : alphabet.toCharArray()) {
				//reset of isFinal
				isFinal = false;
				// get all state from valid transition
				listNewState = TransitionListUtility.getValidDestinationFromTransition(listTransitions, letter);
				if (StateListUtility.hasCommonStates(listNewState, automaton.getFinalStates())) {
					isFinal = true;
				}

				// we have gone through all transition
				// check that we have found a destination
				if (!listNewState.isEmpty()) {

					// get a appropriate name for our new state
					String nameDestination = "";
					nameDestination = StateListUtility.constructNameOfDeterminedStates(listNewState);

					// check that it doesn't already exist
					// we will get the Id we need after otherwise
					int nameId = StateListUtility.getIdStateFromNameInList(determinedAutomaton.getAllStates(),
							nameDestination);

					// if an id hasn't been found, we can create a new State
					// creating a new state mean that it will be added to listDeterminedState
					if (nameId < 0) {
						// we can create our new state
						State newState = new State(id, nameDestination);
						id++;

						// create a new state in determinedAutomaton
						determinedAutomaton.addState(newState, false, isFinal);
						determinedAutomaton.addTransition(stateDeparture, newState, letter);
						// add the list of state to be search through
						listDeterminedStates.add(listNewState);
						listNewState = new ArrayList<State>();
					} else {
						// add a transition
						determinedAutomaton.addTransition(stateDeparture, determinedAutomaton.getStateById(nameId),
								letter);
					}

					// reset the list of added state
					listNewState.clear();
				}
			}
			listTransitions.clear();
		}

		return determinedAutomaton;
	}

	public Automaton buildMinimalAutomaton() {
		automaton = buildDeterministicAutomaton();
		automaton = buildMirrorAutomaton();
		automaton = buildDeterministicAutomaton();
		automaton = buildMirrorAutomaton();
		return automaton;
	}

	private void removeEpsilon(Automaton automaton , State departState , Transition transition) {
	//	Automaton resultAutomaton = AutomatonFactory.createCopy(automaton);
			State finalState = transition.getDestination();
			
		// verifie si l'etat d'arrive est final, si oui l'etat de départ devient final
		if (automaton.isStateFinal(finalState.getId())) {
			automaton.setStateFinal(departState, true);
		}
		
		// verifie si l'etat d'arrive est final, si oui l'etat de départ devient final
		if (automaton.isStateInitial(finalState.getId())) {
			automaton.setStateInitial(departState, true);
		}
		
		// prendre toute les transition qui parte de l'état final et les faire partir de l'état de départ
		for (int i=0 ;i< finalState.getNumberOfTransition(); i++ ) {
			 Transition transitionIeme = finalState.getTransitions().get(i);
			 departState.addTransition(transitionIeme);
		}
		departState.removeTransition(transition);
	}
	
	private Automaton removeInaccessibleState(Automaton automaton) {
		 List<State> listStates =  automaton.getAllStates();
		for (int i=0 ; i< automaton.getNumberOfTotalStates() ; i++) {
			State state =listStates.get(i);
			if (!(automaton.isStateAccessible(state)) && !(automaton.isStateFinal(state)) ) {
				automaton.removeState(state);
			}
		}
		return automaton;
	}
	
	public Automaton buildSynchronizedAutomaton() {
		Automaton resultAutomaton = AutomatonFactory.createCopy(automaton);
		 List<State> listStates =  resultAutomaton.getAllStates();
		 State state;
		 
		 //parcours des etats
		for (int i=0 ; i< resultAutomaton.getNumberOfTotalStates() ; i++) {
			 state =listStates.get(i);
			 
			//parcours des transitions de l'etat en court
			 for (int j=0 ; j < state.getNumberOfTransition() ; j++) {
				 Transition transition = state.getTransitions().get(j);
				
				 if (transition.isEpsilon()) {
					 removeEpsilon(resultAutomaton , state ,transition);
				 }
			 }
		}
		
		return removeInaccessibleState(resultAutomaton);
	}
}
