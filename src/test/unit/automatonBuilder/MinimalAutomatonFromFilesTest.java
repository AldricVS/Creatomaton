package test.unit.automatonBuilder;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import exceptions.FileFormatException;
import process.AutomatonManager;
import process.builders.AutomatonBuilder;
import process.file.AutomatonFileHelper;

/**
 * @author Maxence
 */
public class MinimalAutomatonFromFilesTest {
	private static final String AUTOMATON_EX1_1_FILENAME = "src/test/unit/automatonBuilder/testFile/MinimalAutomaton1.crea";
	private static final String AUTOMATON_EX1_2_FILENAME = "src/test/unit/automatonBuilder/testFile/MinimalAutomaton2.crea";
	
	private static final String AUTOMATON_EX2_1_FILENAME = "src/test/unit/automatonBuilder/testFile/MinimalAutomaton3.crea";
	private static final String AUTOMATON_EX2_2_FILENAME = "src/test/unit/automatonBuilder/testFile/MinimalAutomaton4.crea";
	
	private static final String AUTOMATON_EX3_FILENAME = "src/test/unit/automatonBuilder/testFile/MinimalAutomaton5.crea";

	private static Automaton automaton1;
	private static Automaton automaton2;
	private static Automaton automaton3;
	private static Automaton automaton4;
	private static Automaton automaton5;

	@BeforeClass
	public static void loadAutomaton() throws IllegalArgumentException, FileFormatException, IOException {
		AutomatonFileHelper automatonFileHelper = new AutomatonFileHelper();
		automaton1 = automatonFileHelper.loadAutomaton(AUTOMATON_EX1_1_FILENAME);
		automaton2 = automatonFileHelper.loadAutomaton(AUTOMATON_EX1_2_FILENAME);
		
		automaton3 = automatonFileHelper.loadAutomaton(AUTOMATON_EX2_1_FILENAME);
		automaton4 = automatonFileHelper.loadAutomaton(AUTOMATON_EX2_2_FILENAME);
		
		automaton5 = automatonFileHelper.loadAutomaton(AUTOMATON_EX3_FILENAME);
	}

	@Test
	public void areAutomatonEquals() {
		AutomatonManager manager = new AutomatonManager(automaton1);
		assertTrue(manager.isEquals(automaton2));
	}
	
	@Test
	public void areAutomatonEquals2() {
		AutomatonManager manager = new AutomatonManager(automaton3);
		assertTrue(manager.isEquals(automaton4));
	}

	@Test
	public void areAutomatonEquals3() {
		//from the given exercice, the minimal automaton is the same
		AutomatonBuilder builder = new AutomatonBuilder(automaton5);
		Automaton miniAutomaton5 = builder.buildMinimalAutomaton();
		
		assertEquals(automaton5.getNumberOfTotalStates(), miniAutomaton5.getNumberOfTotalStates());
		assertEquals(automaton5.getNumberOfFinalStates(), miniAutomaton5.getNumberOfFinalStates());
	}
	
}
