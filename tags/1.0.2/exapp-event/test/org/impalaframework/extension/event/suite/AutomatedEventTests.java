package org.impalaframework.extension.event.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.impalaframework.extension.event.EventIntegrationTest;

public class AutomatedEventTests extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(EventIntegrationTest.class);
		return suite;
	}
}