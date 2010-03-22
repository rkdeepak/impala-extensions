package org.impalaframework.extension.mvc.flash;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.ModelMap;

/**
 * Class with responsibilities for flash-scope specific request operations
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
