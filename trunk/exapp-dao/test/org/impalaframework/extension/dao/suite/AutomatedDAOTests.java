package org.impalaframework.extension.dao.suite;

import org.impalaframework.extension.dao.DAOIntegrationTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AutomatedDAOTests extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(DAOIntegrationTest.class);
		return suite;
	}
}