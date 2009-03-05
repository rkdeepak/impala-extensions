package org.impalaframework.extension.mvc;

import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * Custom {@link WebArgumentResolver} which can use the
 * {@link RequestAttribute} to inject an {@link javax.servlet.http.HttpSession}
 * as a method argument.
 * 
 * @author Phil Zoio
 */
public class RequestAttributeArgumentResolver extends BaseAttributeArgumentResolver {

	protected String getAttribute(Object paramAnn) {
		String attributeName = null;
		if (RequestAttribute.class.isInstance(paramAnn)) {
			RequestAttribute attribute = (RequestAttribute) paramAnn;
			attributeName = attribute.value();
		}
		return attributeName;
	}

	protected Object getValue(NativeWebRequest webRequest, String attributeName) {
		Object attribute = webRequest.getAttribute(attributeName, WebRequest.SCOPE_REQUEST);
		return attribute;
	}

}
