package test.unit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Global test suite of unit tests.
 * Includes 1 tests cases :
 * <ul>
 * 		<li>One for testing the creation of an automaton</li>
 * </ul>
 * @author Aldric Vitali Silvestre
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	CreateAutomatonTest.class,
	})
public class AutomatonTestSuite {
	
}
