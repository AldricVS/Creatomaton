package test.unit.files;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite in order to check that the file save / load system is functionnal
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	SaveCustomFileTest.class,
	LoadCustomFileTest.class
	})
public class FileManagementTestSuite {
	
}
