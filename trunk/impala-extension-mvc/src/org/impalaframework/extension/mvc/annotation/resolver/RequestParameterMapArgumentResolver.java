package org.impalaframework.extension.mvc.annotation.resolver;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.impalaframework.extension.mvc.annotation.RequestBody;
import org.impalaframework.extension.mvc.annotation.RequestParameterMap;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Custom {@link WebArgumentResolver} which can use the
 * {@link RequestBody} to inject the body of a request
 * as a method argument.
 * 
 * @author Phil Zoio
 */
public class RequestParameterMapArgumentResolver extends BaseAttributeArgumentResolver {

	protected String getAttribute(Object paramAnn) {
		if (RequestParameterMap.class.isInstance(paramAnn)) {
			return "found";
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected Object getValue(NativeWebRequest webRequest, String encoding) {
		
		Object nativeRequest = webRequest.getNativeRequest();
		if (nativeRequest instanceof HttpServletRequest) {
			HttpServletRequest req = (HttpServletRequest) nativeRequest;

			Map<String,String> parameters = new HashMap<String,String>();
			final Enumeration<String> parameterNames = req.getParameterNames();
			while(parameterNames.hasMoreElements()) {
				String parameterName = parameterNames.nextElement();
				String parameterValue = req.getParameter(parameterName);
				parameters.put(parameterName, parameterValue);
			}
			return parameters;
		}
		return null;
	}

}
