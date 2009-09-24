package org.impalaframework.extension.mvc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AutomatedMvcTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(FlashStateEnabledAnnotationHandlerAdapterTest.class);
		suite.addTestSuite(RequestAttributeTest.class);
		suite.addTestSuite(RequestBodyTest.class);
		suite.addTestSuite(RequestHeaderTest.class);
		suite.addTestSuite(RequestParameterMapTest.class);
		suite.addTestSuite(SessionAttributeTest.class);
		return suite;
	}
}
