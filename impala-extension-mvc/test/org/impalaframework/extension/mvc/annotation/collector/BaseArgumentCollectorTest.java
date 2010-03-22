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

package org.impalaframework.extension.mvc.annotation.collector;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.impalaframework.extension.mvc.annotation.WebAnnotationUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.MethodParameter;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.NativeWebRequest;

public class BaseArgumentCollectorTest extends TestCase {
	
	protected NativeWebRequest request;

	protected BeanWrapper typeConverter = new BeanWrapperImpl();

	protected ExtendedModelMap implicitModel;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		request = createMock(NativeWebRequest.class);
		implicitModel = new ExtendedModelMap();
	}

	protected Annotation getParamAnnotation(Class<?> clazz, String name, Class<?> paramType) {
		MethodParameter parameter = getMethodParameter(clazz, name, paramType);
		Annotation annotation = WebAnnotationUtils.getAnnotations(parameter, "getParameterAnnotations")[0];
			//parameter.getParameterAnnotations()[0];
		return annotation;
	}

	protected MethodParameter getMethodParameter(Class<?> clazz, String name, Class<?> paramType) {
		Class<?>[] paramTypes = new Class[]{paramType};
		Method method = ReflectionUtils.findMethod(clazz, name, paramTypes);
		return new MethodParameter(method, 0);
	}

	public void replayMocks() {
		replay(request);
	}

	public void verifyMocks() {
		verify(request);
	}


}
