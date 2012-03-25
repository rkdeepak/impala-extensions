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

package org.impalaframework.extension.mvc.annotation;

import java.lang.annotation.Annotation;

import org.springframework.core.MethodParameter;
import org.springframework.util.ReflectionUtils;

public class WebAnnotationUtils {

    public static final String ARGUMENT_PENDING = ".ARGUMENT_PENDING";
    
    public static Annotation[] getAnnotations(MethodParameter methodParameter, String methodName) {
        try {
            return (Annotation[]) 
                ReflectionUtils.invokeMethod(methodParameter.getClass().getMethod(methodName, new Class[0]), methodParameter);
        }
        catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("No method found: " + methodName, e);
        }
    }

}
