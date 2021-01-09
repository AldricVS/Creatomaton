package test.unit.thompson;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Global test suite of thompson automatons implementation.
 * Includes 1 tests cases :
 * <ul>
 * 		<li>creation of one automaton accepting one character</li>
 * </ul>
 * @author Aldric Vitali Silvestre
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	LetterAutomatonTest.class,
	ConcatenationAutomatonTest.class,
	UnionAutomatonTest.class
	})
public class ThompsonAutomatonTestSuite {
	
}
