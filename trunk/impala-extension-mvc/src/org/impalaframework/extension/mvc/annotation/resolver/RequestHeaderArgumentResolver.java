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

import javax.servlet.http.HttpServletRequest;

import org.impalaframework.extension.mvc.annotation.RequestHeader;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Custom {@link WebArgumentResolver} which can use the
 * {@link RequestHeader} to inject a request header from the underlying request
 * as a method argument.
 * 
 * @author Phil Zoio
 */
public class RequestHeaderArgumentResolver extends BaseAttributeArgumentResolver {

    protected String getAttribute(Object paramAnn) {
        String attributeName = null;
        if (RequestHeader.class.isInstance(paramAnn)) {
            RequestHeader attribute = (RequestHeader) paramAnn;
            attributeName = attribute.value();
        }
        return attributeName;
    }

    protected Object getValue(NativeWebRequest webRequest, String headerName) {
        Object nativeRequest = webRequest.getNativeRequest();
        if (nativeRequest instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) nativeRequest;
            return req.getHeader(headerName);
        }
        return null;
    }

}
