package test.unit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.unit.automatonBuilder.AutomatonBuilderTestSuite;
import test.unit.basic.BasicAutomatonTestSuite;
import test.unit.files.FileManagementTestSuite;
import test.unit.thompson.ThompsonAutomatonTestSuite;

/**
 * Global test suite of unit tests.
 * Runs all Tests suites:
 * <ul>
 * 		<li>Creation of some basic Automaton, some with epsilon transition</li>
 * 		<li>Thompson automaton implementations</li>
 * 		<li>Automaton builder, with determinism, synchronisation and minimalism</li>
 * 		<li>Load and save automaton from custom file</li>
 * </ul>
 * @author Aldric Vitali Silvestre
 * @author Maxence
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	BasicAutomatonTestSuite.class,
	ThompsonAutomatonTestSuite.class,
	AutomatonBuilderTestSuite.class,
	FileManagementTestSuite.class,
	})
public class FullAutomatonTestSuite {
	
}
