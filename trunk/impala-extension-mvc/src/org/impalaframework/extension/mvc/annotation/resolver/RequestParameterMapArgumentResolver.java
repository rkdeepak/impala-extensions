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

package org.impalaframework.extension.mvc.annotation.resolver;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.impalaframework.extension.mvc.annotation.RequestBody;
import org.impalaframework.extension.mvc.annotation.RequestParameterMap;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Custom {@link WebArgumentResolver} which can use the
 * {@link RequestBody} to inject the body of a request
 * as a method argument.
 * 
 * @author Phil Zoio
 */
public class RequestParameterMapArgumentResolver extends BaseAttributeArgumentResolver {

    protected String getAttribute(Object paramAnn) {
        if (RequestParameterMap.class.isInstance(paramAnn)) {
            return "found";
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    protected Object getValue(NativeWebRequest webRequest, String encoding) {
        
        Object nativeRequest = webRequest.getNativeRequest();
        if (nativeRequest instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) nativeRequest;

            Map<String,String> parameters = new HashMap<String,String>();
            final Enumeration<String> parameterNames = req.getParameterNames();
            while(parameterNames.hasMoreElements()) {
                String parameterName = parameterNames.nextElement();
                String parameterValue = req.getParameter(parameterName);
                parameters.put(parameterName, parameterValue);
            }
            return parameters;
        }
        return null;
    }

}
