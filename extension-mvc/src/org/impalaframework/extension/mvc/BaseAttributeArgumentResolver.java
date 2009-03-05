package org.impalaframework.extension.mvc;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Implements template method for attribute argument resolver implementations
 * 
 * @author Phil Zoio
 */
public abstract class BaseAttributeArgumentResolver implements WebArgumentResolver {

	public Object resolveArgument(MethodParameter methodParameter,
			NativeWebRequest webRequest) throws Exception {
		
		String attributeName = null;
		
		Object[] paramAnns = methodParameter.getParameterAnnotations();
		for (int j = 0; j < paramAnns.length; j++) {
			Object paramAnn = paramAnns[j];
			attributeName = getAttribute(paramAnn);
			if (attributeName != null) {
				break;
			}
		}
		
		if (attributeName == null) {
			return UNRESOLVED;
		}
		
		Object attribute = getValue(webRequest, attributeName);
		return attribute;
	}

	protected abstract Object getValue(NativeWebRequest webRequest, String attributeName);

	protected abstract String getAttribute(Object paramAnn);

}
