package org.impalaframework.extension.mvc.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.impalaframework.extension.mvc.FlashStateEnabledAnnotationHandlerAdapterTest;
import org.impalaframework.extension.mvc.annotation.resolver.RequestAttributeTest;
import org.impalaframework.extension.mvc.annotation.resolver.RequestBodyTest;
import org.impalaframework.extension.mvc.annotation.resolver.RequestHeaderTest;
import org.impalaframework.extension.mvc.annotation.resolver.RequestParameterMapTest;
import org.impalaframework.extension.mvc.annotation.resolver.SessionAttributeTest;

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
