package test.unit.automatonBuilder;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Maxence
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	DeterministicAutomatonTest.class,
	SimpleDeterministicAutomatonTest.class,
	MinimalAutomatonTest.class,
	SynchronizedAutomatonTest.class
	})
public class AutomatonBuilderTestSuite {
	
}
