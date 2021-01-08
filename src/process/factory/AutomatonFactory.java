package process.factory;

import java.util.ArrayList;

import data.Automaton;
import data.State;
import data.Transition;

public class AutomatonFactory {

	/**
	 * Create a distinct copy of an automaton. Therefore, a change in an automaton will not change the other one
	 * @param automatonToCopy the automaton to copy
	 * @return the copy of the automaton
	 */
	public static Automaton createCopy(Automaton automatonToCopy) {
		Automaton automatonResult = new Automaton(automatonToCopy.getAlphabet());
		//create the copy of each state, and give info on initial / final attribute
		for(State stateToCopy : automatonToCopy.getAllStates()) {
			State newState = new State(stateToCopy.getId());
			automatonResult.addState(newState);
			if(automatonToCopy.isStateFinal(stateToCopy)) {
				automatonResult.setStateFinal(newState, true);
			}
			if(automatonToCopy.isStateInitial(stateToCopy)) {
				automatonResult.setStateInitial(newState, true);
			}
		}
		
		/*
		 * For now, all transitions are still leading to the automaton to copy,
		 * we need to change that. We will use the identical states ids in order to do that
		 */
		for(State state : automatonResult.getAllStates()) {
			//find the "associated" state in the original automaton
			State originalState = automatonToCopy.getStateById(state.getId());
			//change the destination of each transition of this state
			for(Transition transition : originalState.getTransitions()) {
				int stateToCopyId = transition.getDestination().getId();
				State newDestination = automatonResult.getStateById(stateToCopyId);
				Transition newTransition = new Transition(transition.getLetter(), newDestination);
				state.addTransition(newTransition);
			}
		}
		
		return automatonResult;
	}
}
