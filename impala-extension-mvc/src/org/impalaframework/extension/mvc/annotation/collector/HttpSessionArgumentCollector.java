package org.impalaframework.extension.mvc.annotation.collector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.impalaframework.extension.mvc.annotation.handler.ArgumentCollector;
import org.springframework.beans.TypeConverter;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * {@link ArgumentCollector} which returns {@link HttpSession} instance
 * @author Phil Zoio
 */
public class HttpSessionArgumentCollector implements ArgumentCollector {
	
	public HttpSessionArgumentCollector() {
		super();
	}

	public Object getArgument(NativeWebRequest request, ExtendedModelMap implicitModel, TypeConverter typeConverter) {
		//FIXME test
		HttpServletRequest req = (HttpServletRequest)request.getNativeRequest();
		return req.getSession();
	}

}
