package test.unit;

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
	//private static State state0;
	private static State state1;
	private static State state2;
	private static State state3;

	
	
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
	}
	
	@Test
	public void synchronisation2() {
		
		
		/* State state =automaton.getAllStates().get(0);
		 Transition transition = state.getTransitions().get(1);
		 
		 System.out.println("------- l'etat est "+state.getId());
		 
		 if (transition.isEpsilon()) {
			 System.out.println("------- la transition e"+transition.getLetter());
		 }
		*/	
		 
		/* 	Automaton resultAutomaton = AutomatonFactory.createCopy(automaton);
			AutomatonBuilder builder = new AutomatonBuilder(resultAutomaton);
			State state2 =resultAutomaton.getAllStates().get(1);
			Transition transition2 = state2.getTransitions().get(1);

			 System.out.println("avant ");
			 System.out.println("------- l'etat  "+state2.getId());
			 System.out.println("------- le nombre de transition  "+state2.getNumberOfTransition());

		 builder.removeEpsilon(resultAutomaton , state2 ,transition2);
		 System.out.println("apres ");
		 System.out.println("-- l'etat  "+state2.getId());
		 System.out.println("-- le nombre de transition  "+state2.getNumberOfTransition());
		 
		/* if (transition2.isEpsilon()) {
			 System.out.println("------- la transition e "+transition2.getLetter());
		 }*/
		/* System.out.println("-------- la transition 1 "+state2.getTransitions().get(0).getLetter());
		 System.out.println("---------- arrivé de le transition "+state2.getTransitions().get(0).getDestination().getId());

		 System.out.println("-------- la transition 2 "+state2.getTransitions().get(1).getLetter());
		 System.out.println("---------- arrivé de le transition "+state2.getTransitions().get(1).getDestination().getId());

		 System.out.println("-------- la transition 3 "+state2.getTransitions().get(2).getLetter());
		 System.out.println("---------- arrivé de le transition "+state2.getTransitions().get(2).getDestination().getId());



		

		
		 List<State> listStates =  automaton.getAllStates();
		 State state;
		for (int i=0 ; i< automaton.getNumberOfTotalStates() ; i++) {
			 state =listStates.get(i);
			 
			// System.out.println("------- nb d'etat ="+automaton.getNumberOfTotalStates());
			 System.out.println("------- l'etat est "+listStates.get(i).getId());
			 System.out.println("-------- le nombre de transition "+state.getNumberOfTransition());
		}
		
		
		*/
		State state1;
		List<State> listStates2 =  automaton.getAllStates();

		for (int i=0 ; i< automaton.getNumberOfTotalStates() ; i++) {
			 state1 =listStates2.get(i);
			 System.out.print("-------- l'etat "+i);
			 System.out.println(" de numero "+state1.getId());


			// List<Transition> listTransition =state.getTransitions();
			//parcours des transitions de l'etat en court
			 System.out.println(" le nombre de tansition "+state1.getNumberOfTransition());

			 for (int j=0 ; j < state1.getNumberOfTransition() ; j++) {
				  System.out.print("---------- la transition "+state1.getTransitions().get(j).getLetter());
				 System.out.println(" arrive a l'etat  "+state1.getTransitions().get(j).getDestination().getId());

			 }
			}
		
		
		System.out.println("-------depart");

		System.out.println("le nb d'etat final "+automaton.getNumberOfFinalStates());
		System.out.println("le nb d'etat intital "+automaton.getNumberOfInitialStates());
		System.out.println("le nb d'etat "+automaton.getNumberOfTotalStates());
	AutomatonBuilder builder = new AutomatonBuilder(automaton);
	Automaton copie = builder.Synchronisation();
		int nbFinal = copie.getNumberOfFinalStates();
		int nbInitial = copie.getNumberOfInitialStates();
		int nbetat = copie.getNumberOfTotalStates();
		State state;
		List<State> listStates =  copie.getAllStates();

		for (int i=0 ; i< copie.getNumberOfTotalStates() ; i++) {
			 state =listStates.get(i);
			 System.out.print("-------- l'etat "+i);
			 System.out.println(" de numero "+state.getId());


			// List<Transition> listTransition =state.getTransitions();
			//parcours des transitions de l'etat en court
			 System.out.println(" le nombre de tansition "+state.getNumberOfTransition());

			 for (int j=0 ; j < state.getNumberOfTransition() ; j++) {
				 System.out.print(" la transition "+state.getTransitions().get(j).getLetter());
				 System.out.println(" arrive a l'etat "+state.getTransitions().get(j).getDestination().getId());

			 }
			}	
		
		
		
		
		System.out.println("-------arrive");

		System.out.println("le nb d'etat final "+nbFinal);
		System.out.println("le nb d'etat intital "+nbInitial);
		System.out.println("le nb d'etat "+nbetat);
		

	}
	
}
