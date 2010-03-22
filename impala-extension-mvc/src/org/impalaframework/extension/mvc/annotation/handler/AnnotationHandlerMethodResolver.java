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

package org.impalaframework.extension.mvc.annotation.handler;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.support.HandlerMethodResolver;
import org.springframework.web.util.UrlPathHelper;

public class AnnotationHandlerMethodResolver {
		
	private Map<String,Method> pathMethodCache = new ConcurrentHashMap<String, Method>();	

	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	
	private HandlerMethodResolver handlerMethodResolver;
	
	/**
	 * Passes in initialized {@link HandlerMethodResolver} instance
	 * @param handlerClass
	 * @param handlerMethodResolver
	 */
	public AnnotationHandlerMethodResolver(Class<?> handlerClass, HandlerMethodResolver handlerMethodResolver) {
		this.handlerMethodResolver = handlerMethodResolver;
	}

	public Method resolveHandlerMethod(HttpServletRequest request) throws ServletException {
		
		String lookupPath = urlPathHelper.getLookupPathForRequest(request);
		String methodLookupPath = lookupPath+"." + request.getMethod();
		
		Method methodToReturn = pathMethodCache.get(methodLookupPath);
		
		if (methodToReturn == null) {
			methodToReturn = getHandlerMethod(lookupPath, methodLookupPath, request);
		}
		
		return methodToReturn;
	}

	private Method getHandlerMethod(String lookupPath, String methodLookupPath, HttpServletRequest request) {

		
		for (Method handlerMethod : getHandlerMethods()) {
			RequestMapping mapping = AnnotationUtils.findAnnotation(handlerMethod, RequestMapping.class);
			String[] values = mapping.value();
			
			if (values[0].equals(lookupPath)) {
				
				RequestMethod[] method = mapping.method();
				if (method != null && method.length > 0) {
					for (RequestMethod requestMethod : method) {
						if (request.getMethod().equals(requestMethod.toString())) {
							pathMethodCache.put(methodLookupPath, handlerMethod);
							return handlerMethod;
						}
					}
				} else {
					pathMethodCache.put(methodLookupPath, handlerMethod);
					return handlerMethod;
				}
			}				
		}
		
		return null;
	}

	public Set<Method> getHandlerMethods() {
		return handlerMethodResolver.getHandlerMethods();
	}

	public Set<Method> getModelAttributeMethods() {
		return handlerMethodResolver.getModelAttributeMethods();
	}
	
	public final boolean hasHandlerMethods() {
		return handlerMethodResolver.hasHandlerMethods();
	}
	
	
	
}
