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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.impalaframework.extension.mvc.annotation.WebAnnotationUtils;
import org.impalaframework.extension.mvc.annotation.collector.CustomResolverArgumentCollector;
import org.impalaframework.extension.mvc.annotation.collector.HttpServletRequestArgumentCollector;
import org.impalaframework.extension.mvc.annotation.collector.HttpServletResponseArgumentCollector;
import org.impalaframework.extension.mvc.annotation.collector.HttpSessionArgumentCollector;
import org.impalaframework.extension.mvc.annotation.collector.ModelArgumentCollector;
import org.impalaframework.extension.mvc.annotation.collector.ModelAttributeArgumentCollector;
import org.impalaframework.extension.mvc.annotation.collector.RequestParameterArgumentCollector;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Class with responsiblility for returning argument collector for a particular handler method
 * index.
 * @author Phil Zoio
 */
public class ArgumentCollectorHelper {
	
	private final WebArgumentResolver[] webArgumentResolvers;

	public ArgumentCollectorHelper(WebArgumentResolver[] webArgumentResolvers) {
		this.webArgumentResolvers = webArgumentResolvers;
	}

	public ArgumentCollector getArgumentCollector(Method handlerMethod, NativeWebRequest webRequest, int index) {
		
		//FIXME test
		
		ArgumentCollector collector = null;
		MethodParameter parameter = new MethodParameter(handlerMethod, index);
		Annotation[] annotations = WebAnnotationUtils.getAnnotations(parameter, "getParameterAnnotations");
			//parameter.getParameterAnnotations();
		
		Class<?> parameterType = parameter.getParameterType();
		
		Annotation annotation = null;
		
		if (annotations.length > 2) {
			throw new IllegalStateException("Parameter with index " + index + " from method " + handlerMethod + " has more than one annotation");
		}
		
		if (annotations.length == 1) {
			annotation = annotations[0];
		}
		
		if (annotation != null) {
			
			//FIXME test
			
			if (RequestParam.class.isInstance(annotation)) {
				collector = new RequestParameterArgumentCollector((RequestParam)annotation, parameterType);
			} else if (ModelAttribute.class.isInstance(annotation)) {
				collector = new ModelAttributeArgumentCollector((ModelAttribute)annotation);
			} else {
				
				if (webArgumentResolvers != null) {
					for (WebArgumentResolver webArgumentResolver : webArgumentResolvers) {
						
						try {
							Object resolveArgument = webArgumentResolver.resolveArgument(parameter, webRequest);
							if (!WebArgumentResolver.UNRESOLVED.equals(resolveArgument)) {
								collector = new CustomResolverArgumentCollector(webArgumentResolver, parameter);
							}
						}
						catch (Exception e) {
						}
					}
				}
			}
		} 
		
		if (collector == null) {
			
			//FIXME only handling from point of view of map
			if (Map.class.isAssignableFrom(parameterType)) {
				collector = new ModelArgumentCollector();
			} else if (HttpServletRequest.class.isAssignableFrom(parameterType)) {
				collector = new HttpServletRequestArgumentCollector();
			} else if (HttpServletResponse.class.isAssignableFrom(parameterType)) {
				collector = new HttpServletResponseArgumentCollector();
			} else if (HttpSession.class.isAssignableFrom(parameterType)) {
				collector = new HttpSessionArgumentCollector();
			}
		}
		
		if (collector == null) {
			throw new IllegalStateException("Unable to determine parameter type for arameter with index " + index + " from method " + handlerMethod);
		}
		return collector;
	}

}
