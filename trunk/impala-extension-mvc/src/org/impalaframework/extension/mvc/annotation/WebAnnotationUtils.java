package org.impalaframework.extension.mvc.annotation;

import java.lang.annotation.Annotation;

import org.springframework.core.MethodParameter;
import org.springframework.util.ReflectionUtils;

public class WebAnnotationUtils {

	public static Annotation[] getAnnotations(MethodParameter methodParameter, String methodName) {
		try {
			return (Annotation[]) 
				ReflectionUtils.invokeMethod(methodParameter.getClass().getMethod(methodName, new Class[0]), methodParameter);
		}
		catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("No method found: " + methodName, e);
		}
	}

}
