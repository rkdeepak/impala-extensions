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

package org.impalaframework.extension.mvc.util;

import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Class with web and model related static helper methods
 * @author Phil Zoio
 */
@SuppressWarnings("unchecked")
public abstract class RequestModelHelper {

    private static Log logger = LogFactory.getLog(RequestModelHelper.class);

    /**
     * Adds to model all parameters in request which are not in the model
     */
    public static void setParameters(HttpServletRequest request,
            final Map modelMap) {
        
        Enumeration<String> parameterNames = request.getParameterNames();
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
    
    /**
     * Returns whether a particular {@link ModelAndView} represents a redirect
     */
    public static boolean isRedirect(ModelAndView modelAndView) {
        boolean isRedirect = false;
        if (modelAndView.getView() instanceof RedirectView
                || (modelAndView.getViewName() != null && 
                        modelAndView.getViewName().startsWith("redirect:"))) {
            isRedirect = true;
        }
        return isRedirect;
    }

    /**
     * 
     * @param logger
     * @param request
     */
    public static void maybeDebugRequest(Log logger, HttpServletRequest request) {
        
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
                logger.info("#####################################################################################");
                logger.info("Request query string: " + request.getQueryString());
                logger.info("Request request URI: " + request.getRequestURI());
                logger.info("#####################################################################################");
            }
        }
    }
    
}
