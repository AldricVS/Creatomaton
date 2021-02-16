package test.unit.automatonBuilder.equivalence;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import exceptions.FileFormatException;
import process.AutomatonManager;
import process.file.AutomatonFileHelper;

public class SameAutomatonFromFilesTest {
private static final String AUTOMATON1_FILE_NAME = "src/test/unit/automatonBuilder/equivalence/testFile/testFile1.crea";
private static final String AUTOMATON2_FILE_NAME = "src/test/unit/automatonBuilder/equivalence/testFile/testFile2.crea";
	
	private static Automaton automaton1;
	private static Automaton automaton2;
	
	@BeforeClass
	public static void loadAutomaton() throws IllegalArgumentException, FileFormatException, IOException {
		//this file should be loaded correctly
		AutomatonFileHelper automatonFileHelper = new AutomatonFileHelper();
		automaton1 = automatonFileHelper.loadAutomaton(AUTOMATON1_FILE_NAME);
		automaton2 = automatonFileHelper.loadAutomaton(AUTOMATON2_FILE_NAME);
	}
	
	@Test
	public void areAutomatonEquals() {
		AutomatonManager manager = new AutomatonManager(automaton1);
		assertTrue(manager.isEquals(automaton2));
	}
}
