/*
 * Copyright 2009-2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.extension.mvc.flash;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.extension.mvc.annotation.handler.LightweightAnnotationHandlerAdapter;
import org.impalaframework.extension.mvc.util.RequestModelHelper;
import org.impalaframework.util.MemoryUtils;
import org.springframework.ui.ModelMap;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.ModelAndView;

public class FlashStateEnabledLightweightHandlerAdapter extends LightweightAnnotationHandlerAdapter {
	
	public static Log logger = LogFactory.getLog(FlashStateEnabledLightweightHandlerAdapter.class);
	
	private FlashHelper flashHelper = new FlashHelper();
	
	@Override
	public boolean supports(Object handler) {
		return super.hasHandlerAnnotation(handler, FlashEnabledAdaptable.class);
	}
	
	public ModelAndView handle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		RequestModelHelper.maybeDebugRequest(logger, request);
		
		StopWatch watch = null;
		
		final boolean debugEnabled;
		
		if (logger.isDebugEnabled()) {
			debugEnabled = true;
		} else {
			debugEnabled = false;
		}
		try {
		
			if (debugEnabled) {
				watch = new StopWatch();
				watch.start();
				logger.debug(MemoryUtils.getMemoryInfo().toString());
			}
			
			beforeHandle(request);
			
			ModelAndView modelAndView = super.handle(request, response, handler);
	
			if (modelAndView != null) {
			
				afterHandle(request, modelAndView);
				
				return modelAndView;
			
			}
		}
		finally {
		
			if (debugEnabled) {
				watch.stop();
				logger.debug("Request executed in " + watch.getTotalTimeMillis() + " milliseconds");
			}
		}
		
		return null;
	}

	protected void beforeHandle(HttpServletRequest request) {
		flashHelper.beforeHandleRequest(request);
	}

	protected void afterHandle(
			HttpServletRequest request,
			ModelAndView modelAndView) {
		
		final ModelMap modelMap = modelAndView.getModelMap();

		flashHelper.afterHandleRequest(request, modelMap);

		boolean isRedirect = RequestModelHelper.isRedirect(modelAndView);

		if (!isRedirect)
			RequestModelHelper.setParameters(request, modelMap);
	}
	
}
