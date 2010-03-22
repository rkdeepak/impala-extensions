package org.impalaframework.extension.mvc.annotation.collector;

import org.impalaframework.extension.mvc.annotation.handler.ArgumentCollector;
import org.springframework.beans.TypeConverter;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Implementation of {@link ArgumentCollector} which simply returns passed in
 * {@link ExtendedModelMap} instance.
 * @author Phil Zoio
 */
public class ModelArgumentCollector implements ArgumentCollector {
	
	public ModelArgumentCollector() {
		super();
	}

	public Object getArgument(NativeWebRequest request, ExtendedModelMap implicitModel, TypeConverter typeConverter) {
		return implicitModel;
	}

}
