package org.impalaframework.extension.mvc.annotation.collector;

import org.impalaframework.extension.mvc.annotation.handler.ArgumentCollector;
import org.springframework.beans.TypeConverter;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * {@link ArgumentCollector} based on {@link ModelAttribute} annotation.
 * @author Phil Zoio
 */
public class ModelAttributeArgumentCollector implements ArgumentCollector {
	
	private String attributeName;

	public ModelAttributeArgumentCollector(ModelAttribute annotation) {
		super();
		attributeName = annotation.value();
	}

	public Object getArgument(NativeWebRequest request, ExtendedModelMap implicitModel, TypeConverter typeConverter) {
		return implicitModel.get(attributeName);
	}
}
