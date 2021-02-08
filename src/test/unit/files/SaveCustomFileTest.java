package test.unit.files;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import data.Automaton;
import data.State;
import process.file.AutomatonFileHelper;
import process.util.FileUtility;

/**
 * Tests that create a custom file and compares with a reference
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class SaveCustomFileTest {
	private static final String DIRECTORY_PATH = "src/test/unit/files/samples/";
	private static final String REFERENCE_FILE_NAME = "reference.crea";
	private static final String TEST_FILE_NAME = "test.crea";

	private static Automaton automaton;

	@BeforeClass
	public static void prepareAutomaton() {
		automaton = new Automaton("ab");

		State state0 = new State(0, "EI");
		State state1 = new State(1);
		State state2 = new State(2);
		State state3 = new State(3, "normal");

		automaton.addState(state0, true, false);
		automaton.addState(state1, false, true);
		automaton.addState(state2, true, false);
		automaton.addState(state3);

		automaton.addTransition(state0, state1, 'a');
		automaton.addTransition(state0, state3, 'b');
		automaton.addEpsilonTransition(state1, state2);
		automaton.addTransition(state2, state3, 'b');
		automaton.addTransition(state3, state1, 'a');
		automaton.addTransition(state3, state3, 'a');
	}

	@BeforeClass
	public static void cleanFiles() {
		// delete the test file name if already exists from a previous test
		File testFile = new File(DIRECTORY_PATH + TEST_FILE_NAME);
		if (testFile.exists()) {
			testFile.delete();
		}
	}

	@Test
	public void createTestFile() throws IOException {
		// if IOException occurs, test is failed
		AutomatonFileHelper automatonFileHelper = new AutomatonFileHelper();
		automatonFileHelper.saveAutomaton(automaton, DIRECTORY_PATH + TEST_FILE_NAME);
	}

	@AfterClass
	public static void compareFiles() throws IOException {
		File testFile = new File(DIRECTORY_PATH + TEST_FILE_NAME);
		File referenceFile = new File(DIRECTORY_PATH + REFERENCE_FILE_NAME);
		assertTrue(testFile.exists());
		assertTrue(referenceFile.exists());

		assertTrue(FileUtility.areFilesHaveSameContent(testFile, referenceFile));
	}
}
