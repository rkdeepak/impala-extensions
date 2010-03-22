package org.impalaframework.extension.mvc.annotation.collector;

import org.impalaframework.extension.mvc.annotation.handler.ArgumentCollector;
import org.springframework.beans.TypeConverter;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * {@link ArgumentCollector} which uses {@link RequestParam} annotation as data source
 * @author Phil Zoio
 */
public class RequestParameterArgumentCollector implements ArgumentCollector {

	private RequestParam annotation;
	
	private Class<?> type;

	public RequestParameterArgumentCollector(RequestParam annotation, Class<?> type) {
		super();
		this.annotation = annotation;
		this.type = type;
	}

	public Object getArgument(NativeWebRequest request, ExtendedModelMap implicitModel, TypeConverter typeConverter) {
		String value = annotation.value();
		boolean required = annotation.required();
		
		String parameter = request.getParameter(value);
		
		/*
		if (!StringUtils.hasText(parameter)) {
			parameter = annotation.defaultValue();
		}*/

		if (!StringUtils.hasText(parameter)) {
			if (required) {
				throw new IllegalArgumentException("Parameter '" + value + "' is required.");
			}
		}
		
		return typeConverter.convertIfNecessary(parameter, type);
	}
}
