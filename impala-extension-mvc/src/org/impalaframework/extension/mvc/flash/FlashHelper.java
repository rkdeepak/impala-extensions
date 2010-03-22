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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.ModelMap;

/**
 * Class with responsibilities for flash-scope specific request and model management operations.
 * @author Phil Zoio
 */
@SuppressWarnings("unchecked")
public class FlashHelper {

	public void beforeHandleRequest(HttpServletRequest request) {
		//unpack flash state from session into request
		unpackFlashState(request);
	}

	public void afterHandleRequest(HttpServletRequest request, final ModelMap modelMap) {
		//merge in existing flash state from request
		mergeFlashState(request, modelMap);
		
		//outgoing flash parameters should be added to the session
		setFlashState(request, modelMap);
	}
	
	void unpackFlashState(HttpServletRequest request) {
		
		final HttpSession session = request.getSession(false);
		if (session != null) {
			final Map<String,Object> flashState = (Map<String, Object>) session.getAttribute("flashState");
			if (flashState != null) {
				request.setAttribute("flashState", flashState);
				session.removeAttribute("flashState");
				
				Set<String> flashKeys = flashState.keySet();
				for (String flashKey : flashKeys) {
					Object currentRequestAttribute = request.getAttribute(flashKey);
					if (currentRequestAttribute == null) {
						request.setAttribute(flashKey, flashState.get(flashKey));
					}
				}
			}
		}
	}

	void mergeFlashState(HttpServletRequest request, ModelMap modelMap) {
		final Map<String,Object> flashState = (Map<String, Object>) request.getAttribute("flashState");
		if (flashState != null) {
			modelMap.mergeAttributes(flashState);
			request.removeAttribute("flashState");
		}
	}

	void setFlashState(HttpServletRequest request,
			ModelMap modelMap) {
		
		final Set keys = modelMap.keySet();
		Map<String,Object> flashState = null;
		
		for (Object object : keys) {
			String key = object.toString();
			if (key.startsWith("flash:")) {
				String realKey = key.substring("flash:".length());
				if (flashState == null) flashState = new HashMap<String, Object>();
				flashState.put(realKey, modelMap.get(key));
			}
		}
		
		if (flashState != null) {
			request.getSession().setAttribute("flashState", flashState);
		}
	}

	
}
