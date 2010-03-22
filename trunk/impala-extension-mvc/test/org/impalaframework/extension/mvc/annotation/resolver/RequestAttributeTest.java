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

import org.springframework.core.MethodParameter;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.WebRequest;

public class RequestAttributeTest extends BaseResolverTest {

	private RequestAttributeArgumentResolver resolver;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		resolver = new RequestAttributeArgumentResolver();
	}
	
	public void testAttribute() throws Exception {
		assertEquals("value", doRequestAttribute("requestAttribute", "attribute", "value"));
	}

	private Object doRequestAttribute(String methodName, final String attributeName, final String value)
			throws Exception {
		
		MethodParameter methodParameter = new MethodParameter(ReflectionUtils.findMethod(AnnotatedClass.class, methodName, new Class[]{ String.class }), 0);

		expect(nativeRequest.getAttribute(attributeName, WebRequest.SCOPE_REQUEST)).andReturn(value);
		
		replayMocks();
		
		Object arg = resolver.resolveArgument(methodParameter, nativeRequest);
		
		verifyMocks();
		return arg;
	}
	
}