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
public class SameDeterministAutomatonTest {
	private static final String AUTOMATON1_FILE_NAME = "src/test/unit/automatonBuilder/testFile/DeterministAutomatonFile1.crea";
	private static final String AUTOMATON2_FILE_NAME = "src/test/unit/automatonBuilder/testFile/DeterministAutomatonFile2.crea";

	private static Automaton automaton1;
	private static Automaton automaton2;

	private static Automaton deterAutomaton1;
	private static Automaton deterAutomaton2;

	@BeforeClass
	public static void loadAutomaton() throws IllegalArgumentException, FileFormatException, IOException {
		// a (a*) (b*) (ca)* ba (c+cc)
		AutomatonFileHelper automatonFileHelper = new AutomatonFileHelper();
		automaton1 = automatonFileHelper.loadAutomaton(AUTOMATON1_FILE_NAME);
		automaton2 = automatonFileHelper.loadAutomaton(AUTOMATON2_FILE_NAME);

		AutomatonBuilder builderAutomaton1 = new AutomatonBuilder(automaton1);
		deterAutomaton1 = builderAutomaton1.buildDeterministicAutomaton();
		AutomatonBuilder builderAutomaton2 = new AutomatonBuilder(automaton2);
		deterAutomaton2 = builderAutomaton2.buildDeterministicAutomaton();
	}

	@Test
	public void areDeterminisedAutomatonEquals() {
		AutomatonManager manager = new AutomatonManager(deterAutomaton1);
		assertTrue(manager.isEquals(deterAutomaton2));
	}

	@Test
	public void areAutomatonEquals() {
		AutomatonManager manager = new AutomatonManager(automaton1);
		assertTrue(manager.isEquals(automaton2));
	}

	@Test
	public void areNormalAndDeterminisedAutomatonEquals() {
		AutomatonManager manager = new AutomatonManager(automaton1);
		assertTrue(manager.isEquals(automaton2));
		assertTrue(manager.isEquals(deterAutomaton1));
		assertTrue(manager.isEquals(deterAutomaton2));
	}
}
