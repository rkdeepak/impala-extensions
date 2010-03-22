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
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.util.ReflectionUtils;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.util.ClassUtils;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.bind.annotation.support.HandlerMethodResolver;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;

/**
 * Lightweight version of Spring's annotation adapter handler
 * @see org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter
 * @author Phil Zoio
 */
public class LightweightAnnotationHandlerAdapter implements HandlerAdapter, InitializingBean {

	private static final Log logger = LogFactory.getLog(LightweightAnnotationHandlerAdapter.class);
	
	private WebArgumentResolver[] customArgumentResolvers;
	
	private final Map<Class<?>, AnnotationHandlerMethodResolver> methodResolverCache =
			new ConcurrentHashMap<Class<?>, AnnotationHandlerMethodResolver>();
	
	private ServletHandlerMethodInvoker methodInvoker;
	
	public void afterPropertiesSet() throws Exception {
		methodInvoker = new ServletHandlerMethodInvoker(customArgumentResolvers);
	}
	
	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {
		return invokeHandlerMethod(request, response, handler);
	}
	
	public long getLastModified(HttpServletRequest request, Object handler) {
		return -1;
	}
	
	public boolean supports(Object handler) {
		return hasHandlerAnnotation(handler, LightweightAdaptable.class);
	}
	
	/* ****************** Implementation methods ************** */
	
	protected boolean hasHandlerAnnotation(
			Object handler,
			final Class<? extends Annotation> annotationClass) {
		final AnnotationHandlerMethodResolver methodResolver = getMethodResolver(handler);
		final Collection<Annotation> handlerAnnotations = methodResolver.getHandlerAnnotations();
		for (Annotation annotation : handlerAnnotations) {
			if (annotationClass.isInstance(annotation)) return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	protected ModelAndView invokeHandlerMethod(
			HttpServletRequest request,
			HttpServletResponse response, 
			Object handler) throws Exception {
		
		AnnotationHandlerMethodResolver methodResolver = getMethodResolver(handler);
		Method handlerMethod = methodResolver.resolveHandlerMethod(request);
		ServletWebRequest webRequest = new ServletWebRequest(request, response);
		ExtendedModelMap implicitModel = new BindingAwareModelMap();
		
		TypeConverter typeConverter = methodInvoker.getTypeConverter();

		Set<Method> modelAttributeMethods = methodResolver.getModelAttributeMethods();
		for (Method method : modelAttributeMethods) {
			methodInvoker.invokeModelAttributeMethod(method, handler, webRequest, implicitModel, typeConverter);
		}
		
		Object result = methodInvoker.invokeHandlerMethod(handlerMethod, handler, webRequest, implicitModel, typeConverter);
		ModelAndView mav = methodInvoker.getModelAndView(handlerMethod, handler.getClass(), result, implicitModel, webRequest);
		methodInvoker.updateModelAttributes(handler, (mav != null ? mav.getModel() : null), implicitModel, webRequest);
		
		return mav;
	}
	
	protected AnnotationHandlerMethodResolver getMethodResolver(Object handler) {
		Class<?> handlerClass = ClassUtils.getUserClass(handler);
		AnnotationHandlerMethodResolver resolver = this.methodResolverCache.get(handlerClass);
		if (resolver == null) {
			
			HandlerMethodResolver handlerMethodResolver = newHandlerMethodResolver(handlerClass);	
			
			resolver = new AnnotationHandlerMethodResolver(handlerClass, handlerMethodResolver);
			resolver.init();
			this.methodResolverCache.put(handlerClass, resolver);
		}
		return resolver;
	}

	HandlerMethodResolver newHandlerMethodResolver(Class<?> handlerClass) {
		HandlerMethodResolver handlerMethodResolver = null;
		Constructor<?> spring3Constructor = ReflectionUtils.findConstructor(HandlerMethodResolver.class, new Class[]{});
		if (spring3Constructor != null) {
			handlerMethodResolver = (HandlerMethodResolver) ReflectionUtils.invokeConstructor(spring3Constructor, new Object[0], false);
			ReflectionUtils.invokeMethod(handlerMethodResolver, "init", handlerClass);
		} else {
			Constructor<?> spring25Constructor = ReflectionUtils.findConstructor(HandlerMethodResolver.class, new Class[]{ Class.class });
			if (spring25Constructor != null) {
				handlerMethodResolver = (HandlerMethodResolver) ReflectionUtils.invokeConstructor(spring25Constructor, new Object[] { handlerClass }, false);
			}
		}
		return handlerMethodResolver;
	}
	
	public void setCustomArgumentResolvers(WebArgumentResolver[] webArgumentResolvers) {
		this.customArgumentResolvers = webArgumentResolvers;
	}

}
