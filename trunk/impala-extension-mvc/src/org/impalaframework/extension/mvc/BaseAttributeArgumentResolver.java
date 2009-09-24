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
		
		Object paramAnn = null;
		Object[] paramAnns = methodParameter.getParameterAnnotations();
		for (int j = 0; j < paramAnns.length; j++) {
			paramAnn = paramAnns[j];
			attributeName = getAttribute(paramAnn);
			if (attributeName != null) {
				break;
			}
		}
		
		if (attributeName == null) {
			return UNRESOLVED;
		}
		
		Object value = getValue(webRequest, attributeName);
		
		if (paramAnn != null) {
			checkValue(value, paramAnn);
		}
		
		return value;
	}

	/**
	 * Can be used to validate value based on annotation contents. For example, to check if value is required
	 * @param value the value returned from the request
	 * @param paramAnn matched annotation
	 */
	protected void checkValue(Object value, Object paramAnn) {
	}

	protected abstract Object getValue(NativeWebRequest webRequest, String attributeName);

	protected abstract String getAttribute(Object paramAnn);

}
