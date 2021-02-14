package test.unit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.unit.automatonBuilder.AutomatonBuilderTestSuite;
import test.unit.automatonBuilder.SynchronizedAutomatonTest;
import test.unit.basic.CreateAutomatonTest;
import test.unit.basic.CreateEpsilonAutomatonTest;
import test.unit.basic.OnlyEpsilonTest;
import test.unit.files.FileManagementTestSuite;
import test.unit.thompson.ThompsonAutomatonTestSuite;

/**
 * Global test suite of unit tests.
 * Runs all Tests suites:
 * <ul>
 * 		<li>Automaton creation</li>
 * 		<li>Epsilon transitions</li>
 * 		<li>Thompson automaton implementations</li>
 * 		<li>Automaton builder</li>
 * 		<li>Load and save automaton from custom file</li>
 * </ul>
 * @author Aldric Vitali Silvestre
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	CreateAutomatonTest.class,
	CreateEpsilonAutomatonTest.class,
	OnlyEpsilonTest.class,
	ThompsonAutomatonTestSuite.class,
	AutomatonBuilderTestSuite.class,
	FileManagementTestSuite.class
	})
public class FullAutomatonTestSuite {
	
}
