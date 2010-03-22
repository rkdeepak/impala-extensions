package org.impalaframework.extension.mvc.annotation.collector;

import javax.servlet.http.HttpServletResponse;

import org.impalaframework.extension.mvc.annotation.handler.ArgumentCollector;
import org.springframework.beans.TypeConverter;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * {@link ArgumentCollector} which returns {@link HttpServletResponse} instance
 * @author Phil Zoio
 */
public class HttpServletResponseArgumentCollector implements ArgumentCollector {
	
	public HttpServletResponseArgumentCollector() {
		super();
	}

	public Object getArgument(NativeWebRequest request, ExtendedModelMap implicitModel, TypeConverter typeConverter) {
		//FIXME test
		return (HttpServletResponse)request.getNativeResponse();
	}

}
