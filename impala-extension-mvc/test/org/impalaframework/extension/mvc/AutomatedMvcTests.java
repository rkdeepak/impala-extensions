package org.impalaframework.extension.mvc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AutomatedMvcTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(FlashStateEnabledAnnotationHandlerAdapterTest.class);
		return suite;
	}
}
