package test.unit.automatonBuilder;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Maxence
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	DeterministicAutomatonTest.class,
	MinimalAutomatonFromFilesTest.class,
	MinimalAutomatonTest.class,
	SameAutomatonFromFilesTest.class,
	SameDeterministAutomatonTest.class,
	SimpleDeterministicAutomatonTest.class,
	SynchronizedEpsilonAutomatonTest.class,
	SynchronisedAutomatonTest.class,
	})
public class AutomatonBuilderTestSuite {
	
}
