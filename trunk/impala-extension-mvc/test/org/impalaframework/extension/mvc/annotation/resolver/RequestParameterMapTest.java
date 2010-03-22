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

package org.impalaframework.extension.mvc.annotation.resolver;

import static org.easymock.EasyMock.expect;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.util.ReflectionUtils;

public class RequestParameterMapTest extends BaseResolverTest {

	private RequestParameterMapArgumentResolver resolver;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		resolver = new RequestParameterMapArgumentResolver();
	}
	
	@SuppressWarnings("unchecked")
	public void testAttribute() throws Exception {
		final Map<String,String> map = (Map<String, String>) doRequestAttribute("requestParameterMap", "attribute", "value");
		assertEquals(2, map.size());
	}

	private Object doRequestAttribute(String methodName, final String attributeName, final String value)
			throws Exception {
		
		MethodParameter methodParameter = new MethodParameter(ReflectionUtils.findMethod(AnnotatedClass.class, methodName, new Class[]{ String.class }), 0);

		expect(nativeRequest.getNativeRequest()).andReturn(request);
		expect(request.getParameterNames()).andReturn(Collections.enumeration(Arrays.asList("one", "two")));
		expect(request.getParameter("one")).andReturn("1");
		expect(request.getParameter("two")).andReturn("2");
		
		replayMocks();
		
		Object arg = resolver.resolveArgument(methodParameter, nativeRequest);
		
		verifyMocks();
		return arg;
	}
	
}