package org.impalaframework.extension.mvc.annotation.collector;

import org.impalaframework.extension.mvc.annotation.handler.ArgumentCollector;
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
