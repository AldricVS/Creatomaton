package test.unit.basic;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Maxence
 */
@RunWith(Suite.class)
@SuiteClasses({
	CreateAutomatonTest.class,
	CreateEpsilonAutomatonTest.class,
	OnlyEpsilonTest.class
	})
public class BasicAutomatonTestSuite {

}
