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

import org.springframework.beans.TypeConverter;
import org.springframework.core.MethodParameter;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * {@link ArgumentCollector} which wraps custom {@link WebArgumentResolver} instance, for custom
 * resolvers.
 * @author Phil Zoio
 */
public class CustomResolverArgumentCollector implements ArgumentCollector {
	
	private WebArgumentResolver customResolver;
	
	private MethodParameter methodParameter;
	
	public CustomResolverArgumentCollector(WebArgumentResolver customResolver, MethodParameter methodParameter) {
		super();
		this.customResolver = customResolver;
		this.methodParameter = methodParameter;
	}

	public Object getArgument(NativeWebRequest webRequest, ExtendedModelMap implicitModel, TypeConverter typeConverter) {
		//FIXME test
		try {
			return customResolver.resolveArgument(methodParameter, webRequest);
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
