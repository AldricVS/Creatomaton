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

	private static final String AUTOMATON3_FILE_NAME = "src/test/unit/automatonBuilder/testFile/DeterministAutomatonFile3.crea";
	private static final String AUTOMATON4_FILE_NAME = "src/test/unit/automatonBuilder/testFile/DeterministAutomatonFile4.crea";
	private static Automaton automaton3;
	private static Automaton automaton4;

	private static Automaton deterAutomaton3;

	@BeforeClass
	public static void loadAutomaton() throws IllegalArgumentException, FileFormatException, IOException {
		AutomatonFileHelper automatonFileHelper = new AutomatonFileHelper();
		AutomatonBuilder automatonBuilder = new AutomatonBuilder(automaton1);
		
		// a (a*) (b*) (ca)* ba (c+cc)
		automaton1 = automatonFileHelper.loadAutomaton(AUTOMATON1_FILE_NAME);
		automaton2 = automatonFileHelper.loadAutomaton(AUTOMATON2_FILE_NAME);

		automatonBuilder.setAutomaton(automaton1);
		deterAutomaton1 = automatonBuilder.buildDeterministicAutomaton();
		automatonBuilder.setAutomaton(automaton2);
		deterAutomaton2 = automatonBuilder.buildDeterministicAutomaton();
		
		// (aa(b*)a)+(ab(bb)*c)
		automaton3 = automatonFileHelper.loadAutomaton(AUTOMATON3_FILE_NAME);
		automaton4 = automatonFileHelper.loadAutomaton(AUTOMATON4_FILE_NAME);

		automatonBuilder.setAutomaton(automaton3);
		deterAutomaton3 = automatonBuilder.buildDeterministicAutomaton();
	}

	@Test
	public void areDeterminisedAutomatonEquals() {
		AutomatonManager manager = new AutomatonManager(deterAutomaton1);
		assertTrue(manager.isEqualsByMinimalism(deterAutomaton2));
	}

	@Test
	public void areAutomatonEquals() {
		AutomatonManager manager = new AutomatonManager(automaton1);
		assertTrue(manager.isEqualsByMinimalism(automaton2));
	}

	@Test
	public void areNormalAndDeterminisedAutomatonEquals() {
		AutomatonManager manager = new AutomatonManager(automaton1);
		assertTrue(manager.isEqualsByMinimalism(automaton2));
		assertTrue(manager.isEqualsByMinimalism(deterAutomaton1));
		assertTrue(manager.isEqualsByMinimalism(deterAutomaton2));
	}

	@Test
	public void areAutomatonEquals2() {
		AutomatonManager manager = new AutomatonManager(automaton3);
		assertTrue(manager.isEqualsByMinimalism(automaton4));
	}
	
	@Test
	public void isNewDeterminisedAutomatonEquals() {
		assertTrue(automaton4.isEquals(deterAutomaton3));
	}
}
