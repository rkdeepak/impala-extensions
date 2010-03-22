package org.impalaframework.extension.mvc.annotation.collector;

import javax.servlet.http.HttpServletRequest;

import org.impalaframework.extension.mvc.annotation.handler.ArgumentCollector;
import org.springframework.beans.TypeConverter;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * {@link ArgumentCollector} which returns {@link HttpServletRequest} instance
 * @author Phil Zoio
 */
public class HttpServletRequestArgumentCollector implements ArgumentCollector {
	
	public HttpServletRequestArgumentCollector() {
		super();
	}

	public Object getArgument(NativeWebRequest request, ExtendedModelMap implicitModel, TypeConverter typeConverter) {
		//FIXME test
		return (HttpServletRequest)request.getNativeRequest();
	}

}
