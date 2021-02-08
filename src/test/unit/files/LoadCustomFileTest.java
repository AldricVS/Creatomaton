package test.unit.files;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import data.AutomatonConstants;
import data.State;
import exceptions.FileFormatException;
import process.file.AutomatonFileHelper;

/**
 * Test that will check if an automaton was succesfully loaded from a file. 
 * We have normally 4 states and 6 transitions
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class LoadCustomFileTest {
	private static final String REFERENCE_FILE_NAME = "src/test/unit/files/samples/reference.crea";
	
	private static Automaton automaton;
	
	@BeforeClass
	public static void loadAutomaton() throws IllegalArgumentException, FileFormatException, IOException {
		//this file should be loaded correctly
		AutomatonFileHelper automatonFileHelper = new AutomatonFileHelper();
		automaton = automatonFileHelper.loadAutomaton(REFERENCE_FILE_NAME);
	}
	
	@Test
	public void testNumberOfStates() {
		assertEquals(4, automaton.getNumberOfTotalStates());
		assertEquals(2, automaton.getNumberOfInitialStates());
		assertEquals(1, automaton.getNumberOfFinalStates());
	}
	
	@Test
	public void testTransitions() {
		//check 3 transitions : 2 normal and 1 epsilon
		State state0 = automaton.getStateById(0);
		State nextState = state0.findNextState('a');
		assertEquals(1, nextState.getId());
		
		State state3 = automaton.getStateById(3);
		List<State> transitionList = state3.findNextStatesList('a');
		assertEquals(2, transitionList.size());
		assertEquals(3, transitionList.get(1).getId());
		
		State state1 = automaton.getStateById(1);
		nextState = state1.findNextState(AutomatonConstants.EPSILON_CHAR);
		assertEquals(2, nextState.getId());
	}
}
