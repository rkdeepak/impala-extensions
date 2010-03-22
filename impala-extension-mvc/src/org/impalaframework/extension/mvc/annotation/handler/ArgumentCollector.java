package org.impalaframework.extension.mvc.annotation.handler;

import org.springframework.beans.TypeConverter;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Defines mechanism for extracting handler method parameter value from request
 * @author Phil Zoio
 */
public interface ArgumentCollector {

	public Object getArgument(NativeWebRequest request, ExtendedModelMap implicitModel, TypeConverter typeConverter);
	
}
