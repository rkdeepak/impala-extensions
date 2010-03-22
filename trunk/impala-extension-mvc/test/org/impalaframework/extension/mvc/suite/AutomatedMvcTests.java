/*
 * Copyright 2009-2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.extension.mvc.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.impalaframework.extension.mvc.annotation.collector.CustomResolverArgumentCollectorTest;
import org.impalaframework.extension.mvc.annotation.collector.ModelArgumentCollectorTest;
import org.impalaframework.extension.mvc.annotation.collector.RequestParameterArgumentCollectorTest;
import org.impalaframework.extension.mvc.annotation.handler.AnnotationHandlerMethodResolverTest;
import org.impalaframework.extension.mvc.annotation.handler.LightweightAnnotationHandlerAdapterTest;
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

		suite.addTestSuite(AnnotationHandlerMethodResolverTest.class);		
		suite.addTestSuite(LightweightAnnotationHandlerAdapterTest.class);		

		suite.addTestSuite(RequestModelHelperTest.class);
		return suite;
	}
}
