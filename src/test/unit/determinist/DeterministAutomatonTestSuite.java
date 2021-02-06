package test.unit.determinist;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Maxence
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	DeterministAutomatonTest.class,
	SimpleDeterministAutomatontest.class,
	MinimalAutomatonTest.class,
	})
public class DeterministAutomatonTestSuite {
	
}
