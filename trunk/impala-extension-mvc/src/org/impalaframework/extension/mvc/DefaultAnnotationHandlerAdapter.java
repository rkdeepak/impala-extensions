package org.impalaframework.extension.mvc;

import org.springframework.web.bind.support.WebArgumentResolver;

public class DefaultAnnotationHandlerAdapter extends FlashStateEnabledAnnotationHandlerAdapter {

	public void init() {
		setCustomArgumentResolvers(new WebArgumentResolver[]{
				new SessionAttributeArgumentResolver(),
				new RequestAttributeArgumentResolver()
		});
	}
	
}
