package org.impalaframework.extension.mvc;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.springframework.web.servlet.view.RedirectView;

@SuppressWarnings("unchecked")
public class RequestPreservingAnnotationHandlerAdapter extends AnnotationMethodHandlerAdapter {
	
	private static Log logger = LogFactory.getLog(RequestPreservingAnnotationHandlerAdapter.class);
	
	public RequestPreservingAnnotationHandlerAdapter() {
		super();
	}

	@Override
	public ModelAndView handle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		maybeDebug(request);
		
		//unpack flash state from session into request
		unpackFlashState(request);
		
		ModelAndView modelAndView = super.handle(request, response, handler);

		final ModelMap modelMap = modelAndView.getModelMap();
		
		//merge in existing flash state from request
		mergeFlashState(request, modelMap);
		
		//outgoing flash parameters should be added to the session
		setFlashState(request, modelMap);
		
		boolean isRedirect = isRedirect(modelAndView);
		
		if (!isRedirect)
			setParameters(request, modelMap);
		
		return modelAndView;
	}

	private void maybeDebug(HttpServletRequest request) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("#####################################################################################");
			logger.debug("---------------------------- Request details ---------------------------------------");
			logger.debug("Request context path: " + request.getContextPath());
			logger.debug("Request path info: " + request.getPathInfo());
			logger.debug("Request path translated: " + request.getPathTranslated());
			logger.debug("Request query string: " + request.getQueryString());
			logger.debug("Request servlet path: " + request.getServletPath());
			logger.debug("Request request URI: " + request.getRequestURI());
			logger.debug("Request request URL: " + request.getRequestURL());
			logger.debug("Request session ID: " + request.getRequestedSessionId());

			logger.debug("------------------------------------------------ ");
			logger.debug("Parameters ------------------------------------- ");
			final Enumeration<String> parameterNames = request.getParameterNames();
			
			Map<String,String> parameters = new TreeMap<String, String>();
			
			while (parameterNames.hasMoreElements()) {
				String name = parameterNames.nextElement();
				String value = request.getParameter(name);
				final String lowerCase = name.toLowerCase();
				if (lowerCase.contains("password") || lowerCase.contains("cardnumber")) {
					value = "HIDDEN";
				}
				parameters.put(name, value);
			}
			
			//now output			
			final Set<String> parameterKeys = parameters.keySet();
			for (String key : parameterKeys) {
				logger.debug(key + ": " + parameters.get(key));
			}

			logger.debug("------------------------------------------------ ");
			
			Map<String,Object> attributes = new TreeMap<String, Object>();
			
			logger.debug("Attributes ------------------------------------- ");
			final Enumeration<String> attributeNames = request.getAttributeNames();
			while (attributeNames.hasMoreElements()) {
				String name = attributeNames.nextElement();
				Object value = request.getAttribute(name);
				final String lowerCase = name.toLowerCase();
				if (lowerCase.contains("password") || lowerCase.contains("cardnumber")) {
					value = "HIDDEN";
				}
				attributes.put(name, value);
			}
			
			//now output
			final Set<String> keys = attributes.keySet();
			for (String name : keys) {
				Object value = attributes.get(name);
				logger.debug(name + ": " + (value != null ? value.toString() : value));
			}

			logger.debug("------------------------------------------------ ");
			logger.debug("#####################################################################################");
		} else {
			if (logger.isInfoEnabled()) {
				logger.info("Request query string: " + request.getQueryString());
				logger.info("Request request URI: " + request.getRequestURI());
			}
		}
	}

	boolean isRedirect(ModelAndView modelAndView) {
		boolean isRedirect = false;
		if (modelAndView.getView() instanceof RedirectView
				|| (modelAndView.getViewName() != null && 
						modelAndView.getViewName().startsWith("redirect:"))) {
			isRedirect = true;
		}
		return isRedirect;
	}

	void setParameters(HttpServletRequest request,
			final ModelMap modelMap) {
		
		Enumeration parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String parameterName = (String) parameterNames.nextElement();
			if (!modelMap.containsKey(parameterName)) {
				String value = request.getParameter(parameterName);
				modelMap.put(parameterName, value);
			
				if (logger.isDebugEnabled()) {
					logger.debug("Adding parameter " + parameterName + ": " + value );
				}
			}
		}
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
