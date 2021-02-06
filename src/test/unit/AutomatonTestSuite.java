package test.unit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.unit.determinist.DeterministAutomatonTestSuite;
import test.unit.thompson.ThompsonAutomatonTestSuite;

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
	CreateEpsilonAutomatonTest.class,
	ThompsonAutomatonTestSuite.class,
	DeterministAutomatonTestSuite.class,
	})
public class AutomatonTestSuite {
	
}
