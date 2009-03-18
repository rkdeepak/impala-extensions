package org.impalaframework.extension.dataaccess.jdbc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AutomatedDataAccessTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(JdbcArgumentsConverterTest.class);
		return suite;
	}
}
