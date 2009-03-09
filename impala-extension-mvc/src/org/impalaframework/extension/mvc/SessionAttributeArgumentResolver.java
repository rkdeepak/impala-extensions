package org.impalaframework.extension.mvc;

import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * Custom {@link WebArgumentResolver} which can use the
 * {@link SessionAttribute} to inject an {@link javax.servlet.http.HttpSession}
 * as a method argument.
 * 
 * @author Phil Zoio
 */
public class SessionAttributeArgumentResolver extends BaseAttributeArgumentResolver {

	protected String getAttribute(Object paramAnn) {
		String sessionAttribute = null;
		if (SessionAttribute.class.isInstance(paramAnn)) {
			SessionAttribute attribute = (SessionAttribute) paramAnn;
			sessionAttribute = attribute.value();
		}
		return sessionAttribute;
	}

	protected Object getValue(NativeWebRequest webRequest, String attributeName) {
		Object attribute = webRequest.getAttribute(attributeName, WebRequest.SCOPE_SESSION);
		return attribute;
	}

}
