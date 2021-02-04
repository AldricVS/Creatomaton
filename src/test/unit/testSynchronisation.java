package test.unit;

import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import data.State;
import process.builders.AutomatonBuilder;

public class testSynchronisation {

	
	private static Automaton automaton;
	//private static State state0;
	private static State state1;
	private static State state2;
	
	
	@BeforeClass
	public static void prepareAutomaton() {
		automaton = new Automaton("abc");
		//state0 = new State(0);
		state1 = new State(1);
		state2 = new State(2); //state2 is final
		
		//automate -> a (a*) b (c*)
		//automaton.addState(state0, true, false);
		automaton.addState(state1, false, false);
		automaton.addState(state2, false, true);
		
		//automaton.addTransition(state0, state1, 'a');
		automaton.addTransition(state1, state1, 'a');
		automaton.addEpsilonTransition(state1, state2);
		automaton.addTransition(state2, state2, 'b');
	}
	
	@Test
	public static void synchronisation2() {
	AutomatonBuilder builder = new AutomatonBuilder(automaton);
	Automaton copie = builder.Synchronisation();
		/*int nbFinal = copie.getNumberOfFinalStates();
		int nbInitial = copie.getNumberOfInitialStates();
		int nbetat = copie.getNumberOfTotalStates();*/
		
		/*System.out.println("le nb d'etat final"+nbFinal);
		System.out.println("le nb d'etat intital"+nbInitial);
		System.out.println("le nb d'etat "+nbetat);*/
		

	}
	
}
