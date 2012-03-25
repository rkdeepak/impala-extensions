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

import org.impalaframework.extension.mvc.annotation.CookieValue;
import org.impalaframework.extension.mvc.annotation.RequestAttribute;
import org.impalaframework.extension.mvc.annotation.RequestBody;
import org.impalaframework.extension.mvc.annotation.RequestHeader;
import org.impalaframework.extension.mvc.annotation.RequestParameterMap;
import org.impalaframework.extension.mvc.annotation.SessionAttribute;

public class AnnotatedClass {
    
    public void requestAttribute(@RequestAttribute("attribute") String name){}
    public void sessionAttribute(@SessionAttribute("attribute") String name){}
    public void requestHeader(@RequestHeader("attribute") String name){}
    public void requestBody(@RequestBody("UTF8") String name){}
    public void requestParameterMap(@RequestParameterMap String name){}
    public void cookieValue(@CookieValue("cookie") String name){}

}
