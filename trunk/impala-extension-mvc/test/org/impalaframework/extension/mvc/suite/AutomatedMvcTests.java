package org.impalaframework.extension.mvc.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.impalaframework.extension.mvc.annotation.collector.CustomResolverArgumentCollectorTest;
import org.impalaframework.extension.mvc.annotation.collector.ModelArgumentCollectorTest;
import org.impalaframework.extension.mvc.annotation.collector.RequestParameterArgumentCollectorTest;
import org.impalaframework.extension.mvc.annotation.resolver.RequestAttributeTest;
import org.impalaframework.extension.mvc.annotation.resolver.RequestBodyTest;
import org.impalaframework.extension.mvc.annotation.resolver.RequestHeaderTest;
import org.impalaframework.extension.mvc.annotation.resolver.RequestParameterMapTest;
import org.impalaframework.extension.mvc.annotation.resolver.SessionAttributeTest;
import org.impalaframework.extension.mvc.flash.FlashHelperTest;
import org.impalaframework.extension.mvc.util.RequestModelHelperTest;

public class AutomatedMvcTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		
		suite.addTestSuite(FlashHelperTest.class);
		suite.addTestSuite(RequestAttributeTest.class);
		suite.addTestSuite(RequestBodyTest.class);
		suite.addTestSuite(RequestHeaderTest.class);
		suite.addTestSuite(RequestParameterMapTest.class);
		suite.addTestSuite(SessionAttributeTest.class);		

		suite.addTestSuite(CustomResolverArgumentCollectorTest.class);
		suite.addTestSuite(ModelArgumentCollectorTest.class);
		suite.addTestSuite(RequestParameterArgumentCollectorTest.class);
		
		suite.addTestSuite(RequestModelHelperTest.class);
		return suite;
	}
}
