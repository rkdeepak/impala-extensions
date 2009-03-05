package org.impalaframework.extension.mvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Custom {@link WebArgumentResolver} which can use the
 * {@link RequestHeader} to inject a request header from the underlying request
 * as a method argument.
 * 
 * @author Phil Zoio
 */
public class RequestHeaderArgumentResolver extends BaseAttributeArgumentResolver {

	protected String getAttribute(Object paramAnn) {
		String attributeName = null;
		if (RequestHeader.class.isInstance(paramAnn)) {
			RequestHeader attribute = (RequestHeader) paramAnn;
			attributeName = attribute.value();
		}
		return attributeName;
	}

	protected Object getValue(NativeWebRequest webRequest, String headerName) {
		Object nativeRequest = webRequest.getNativeRequest();
		if (nativeRequest instanceof HttpServletRequest) {
			HttpServletRequest req = (HttpServletRequest) nativeRequest;
			return req.getHeader(headerName);
		}
		return null;
	}

}
