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
import java.util.concurrent.ConcurrentHashMap;

import org.impalaframework.extension.mvc.annotation.collector.ArgumentCollector;
import org.impalaframework.extension.mvc.annotation.collector.ArgumentCollectorHelper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.TypeConverter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

/**
 * Simplified analog to ServletHandlerMethodInvoker
 * @author Phil Zoio
 */
public class ServletHandlerMethodInvoker {
	
	private static final String UNRESOLVED = ".UNRESOLVED";
	
	private final ArgumentCollectorHelper argumentCollectorHelper;
	
	public ServletHandlerMethodInvoker(WebArgumentResolver[] customArgumentResolvers) {
		this.argumentCollectorHelper = new ArgumentCollectorHelper(customArgumentResolvers);
	}
	
	private ConcurrentHashMap<Method, ArgumentCollector[]> argumentCollectors = new ConcurrentHashMap<Method, ArgumentCollector[]>();

	private ConcurrentHashMap<Method, String> modelAttributeMap = new ConcurrentHashMap<Method, String>();
	
	public TypeConverter getTypeConverter() {
		TypeConverter typeConverter = new BeanWrapperImpl();
		return typeConverter;
	}

	@SuppressWarnings("unchecked")
	public void invokeModelAttributeMethod(
			Method attributeMethod,
			Object handler,
			ServletWebRequest webRequest, 
			ExtendedModelMap implicitModel, 
			TypeConverter typeConverter) {

		//FIXME test
		String attributeName = getModelAttributeName(attributeMethod);
		
		if (!UNRESOLVED.equals(attributeName)) {
			Object result = invokeMethod(handler, attributeMethod, webRequest, implicitModel, typeConverter);
			implicitModel.put(attributeName, result);
		}
	}
	
	public Object invokeHandlerMethod(
			Method handlerMethod, 
			Object handler,
			NativeWebRequest webRequest, 
			ExtendedModelMap implicitModel, TypeConverter typeConverter) throws Exception {

		return invokeMethod(handler, handlerMethod, webRequest, implicitModel, typeConverter);
	}

	private String getModelAttributeName(Method attributeMethod) {
		String attributeName = modelAttributeMap.get(attributeMethod);
		if (attributeName == null) {
			attributeName = AnnotationUtils.findAnnotation(attributeMethod, ModelAttribute.class).value();
			if (!StringUtils.hasText(attributeName)) {
				modelAttributeMap.put(attributeMethod, UNRESOLVED);
			}
			modelAttributeMap.put(attributeMethod, attributeName);
		}
		return attributeName;
	}

	private Object invokeMethod(
			Object handler, 
			Method handlerMethod, 
			NativeWebRequest webRequest,
			ExtendedModelMap implicitModel, 
			TypeConverter typeConverter) {
		
		ArgumentCollector[] collection = argumentCollectors.get(handlerMethod);
		if (collection == null) {
			collection = assembleArgumentCollectors(handlerMethod, handler, webRequest, implicitModel);
		}
		
		Class<?>[] parameterTypes = handlerMethod.getParameterTypes();
		Object[] arguments = new Object[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			arguments[i] = collection[i].getArgument(webRequest, implicitModel, typeConverter);
		}
		
		return ReflectionUtils.invokeMethod(handlerMethod, handler, arguments);
	}

	/**
	 * Supports {@link ModelAndView} resolution from:
	 * <ul>
	 * <li> null - return value is null and return type is Void, uses implicit view
	 * <li> null - return value is null and return type is not Void, returns null
	 * <li> {@link Model}
	 * <li> {@link View}
	 * <li> {@link ModelAndView}
	 * </ul>
	 */
	public ModelAndView getModelAndView(Method handlerMethod, Class<? extends Object> handlerType,
			Object returnValue, ExtendedModelMap implicitModel, ServletWebRequest webRequest) {
		
		//FIXME as with ArgumentCollectors, ideally, these should have a similar return result resolving 
		//interface whose implementation instance could be cached against the method
		
		if (returnValue instanceof String) {
			return new ModelAndView((String) returnValue).addAllObjects(implicitModel);
		}
		else if (returnValue == null) {
			if (Void.TYPE.equals(handlerMethod.getReturnType())) {
				return new ModelAndView().addAllObjects(implicitModel);
			} else {
				return null;
			}
		}
		else if (returnValue instanceof ModelAndView) {
			ModelAndView mav = (ModelAndView) returnValue;
			mav.getModelMap().mergeAttributes(implicitModel);
			return mav;
		}
		else if (returnValue instanceof Model) {
			return new ModelAndView().addAllObjects(implicitModel).addAllObjects(((Model) returnValue).asMap());
		}
		else if (returnValue instanceof View) {
			return new ModelAndView((View) returnValue).addAllObjects(implicitModel);
		}
		else {
			throw new IllegalArgumentException("Invalid handler method return value: " + returnValue);
		}
	}

	/**
	 * Does nothing. Spring MVC equivalent will update session status
	 */
	public void updateModelAttributes(Object handler, java.util.Map<String, Object> map,
			ExtendedModelMap implicitModel, ServletWebRequest webRequest) {
	}
	
	private ArgumentCollector[] assembleArgumentCollectors(
			Method handlerMethod, 
			Object handler,
			NativeWebRequest webRequest, 
			ExtendedModelMap implicitModel) {
		
		Class<?>[] parameterTypes = handlerMethod.getParameterTypes();
		
		ArgumentCollector[] collection = new ArgumentCollector[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			
			collection[i] = argumentCollectorHelper.getArgumentCollector(handlerMethod, webRequest, i);
		}

		argumentCollectors.put(handlerMethod, collection);
		return collection;
	}

	
}
