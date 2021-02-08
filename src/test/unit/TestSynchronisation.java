package test.unit;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import data.State;
import data.Transition;
import process.builders.AutomatonBuilder;
import process.factory.AutomatonFactory;

public class TestSynchronisation {

	
	private static Automaton automaton;
	private static State state0;
	private static State state1;
	private static State state2;
	private static State state3;
	private static State state4;
	private static State state5;
	private static State state6;
	
	


	@BeforeClass
	public static void prepareAutomaton() {
		
		automaton = new Automaton("abc");
		//state0 = new State(0);
		state1 = new State(1);
		state2 = new State(2);
		state3 = new State(3); //state2 is final
		
		//automate -> a (a*) b (c*)
		//automaton.addState(state0, true, false);
		automaton.addState(state1, true, false);
		automaton.addState(state2, false, false);
		automaton.addState(state3, false, true);
		
		//automaton.addTransition(state0, state1, 'a');
		automaton.addTransition(state1, state1, 'a');
		automaton.addEpsilonTransition(state1, state2);
		automaton.addTransition(state2, state2, 'b');
		automaton.addEpsilonTransition(state2, state3);
		automaton.addTransition(state3, state3, 'c');
	
		
		
		
		/*automaton = new Automaton("xyz");
		//state0 = new State(0);
		state0 = new State(0);
		state1 = new State(1);
		state2 = new State(2); 
		state3 = new State(3); 
		state4 = new State(4); 
		state5 = new State(5); 
		state6 = new State(6); 

		//automate -> a (a*) b (c*)
		//automaton.addState(state0, true, false);
		automaton.addState(state0, true, true);
		automaton.addState(state1, false, false);
		automaton.addState(state2, false, false);
		automaton.addState(state3, false, true);
		automaton.addState(state4, false, false);
		automaton.addState(state5, false, false);
		automaton.addState(state6, false, true);

		//automaton.addTransition(state0, state1, 'a');
		automaton.addEpsilonTransition(state0, state1);
		automaton.addTransition(state1, state2, 'x');
		automaton.addEpsilonTransition(state1, state4);
		automaton.addTransition(state2, state3, 'z');
		automaton.addEpsilonTransition(state3, state1);
		automaton.addTransition(state4, state5, 'y');
		automaton.addTransition(state5, state6, 'y');
		automaton.addEpsilonTransition(state6, state1);
			*/

	}
	
	@Test
	public void synchronisation2() {
		AutomatonBuilder builder = new AutomatonBuilder(automaton);
		Automaton copie = builder.Synchronisation();
		List<State> listStates =  copie.getAllStates();
		State state;
		if (copie.getNumberOfTotalStates()!=3) {
			fail("Nombre d'etat ");
		}
		if (copie.getNumberOfFinalStates()!=3) {
			fail("Nombre d'etat final");
		}
		if (copie.getNumberOfInitialStates()!=1) {
			fail("Nombre d'etat initial ");
		}
		
		for (int i=0 ; i< copie.getNumberOfTotalStates() ; i++) {
			 state =listStates.get(i);
			 
			 if ( (state.getId()==1) && (state.getNumberOfTransition()!=3)){
				 fail("nombre de transition");
			 }
			 else if ((state.getId()==2)&&(state.getNumberOfTransition()!=2)) {
				 fail("nombre de transition");

			 }
			 else if ( (state.getId()==3) && (state.getNumberOfTransition()!=1)){
				 fail("nombre de transition");
			 }
		}
	}
	
}
