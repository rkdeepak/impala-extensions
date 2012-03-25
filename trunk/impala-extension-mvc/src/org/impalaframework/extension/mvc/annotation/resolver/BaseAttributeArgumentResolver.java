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

import java.lang.annotation.Annotation;

import org.impalaframework.extension.mvc.annotation.WebAnnotationUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Implements template method for attribute argument resolver implementations
 * @author Phil Zoio
 */
public abstract class BaseAttributeArgumentResolver implements WebArgumentResolver {
    
    public Object resolveArgument(MethodParameter methodParameter,
            NativeWebRequest webRequest) throws Exception {
        
        String attributeName = null;
        
        Object paramAnn = null;
        
        Annotation[] paramAnns = null;

        try {
            // using reflection since Spring 3.0 returns Annotation[] yet Spring 2.5 returns Object[]
            String methodName = "getParameterAnnotations";
            paramAnns = WebAnnotationUtils.getAnnotations(methodParameter, methodName);
        }
        catch (Exception e) {
            throw new IllegalStateException("Failed to introspect method parameter annotations", e);
        }
        
        for (int j = 0; j < paramAnns.length; j++) {
            paramAnn = paramAnns[j];
            attributeName = getAttribute(paramAnn);
            if (attributeName != null) {
                break;
            }
        }
        
        if (attributeName == null) {
            return UNRESOLVED;
        }
        
        Object value = getValue(webRequest, attributeName);
        
        if (paramAnn != null) {
            checkValue(value, paramAnn);
        }
        
        return value;
    }

    /**
     * Can be used to validate value based on annotation contents. For example, to check if value is required
     * @param value the value returned from the request
     * @param paramAnn matched annotation
     */
    protected void checkValue(Object value, Object paramAnn) {
    }

    protected abstract Object getValue(NativeWebRequest webRequest, String attributeName);

    protected abstract String getAttribute(Object paramAnn);

}
