import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.impalaframework.extension.application.suite.AutomatedModuleTests;
import org.impalaframework.extension.event.suite.AutomatedEventTests;
import org.impalaframework.extension.web.suite.AutomatedWebTests;
import org.impalaframework.extension.mvc.suite.AutomatedMvcTests;
import org.impalaframework.extension.root.suite.AutomatedRootTests;

public class AllTests extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(AutomatedEventTests.suite());
		suite.addTest(AutomatedMvcTests.suite());
		suite.addTest(AutomatedRootTests.suite());
		suite.addTest(AutomatedModuleTests.suite());
		suite.addTest(AutomatedWebTests.suite());
		return suite;
	}
}
