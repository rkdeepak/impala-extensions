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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.impalaframework.extension.mvc.annotation.CookieValue;
import org.impalaframework.util.ObjectUtils;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Custom {@link WebArgumentResolver} which retrieves a cookie value as an argument.
 * 
 * @author Phil Zoio
 */
public class CookieValueArgumentResolver extends BaseAttributeArgumentResolver {

	protected String getAttribute(Object paramAnn) {
		String attributeName = null;
		if (CookieValue.class.isInstance(paramAnn)) {
			CookieValue attribute = (CookieValue) paramAnn;
			attributeName = attribute.value();
		}
		return attributeName;
	}

	protected Object getValue(NativeWebRequest webRequest, String attributeName) {
		HttpServletRequest request = ObjectUtils.cast(webRequest.getNativeRequest(), HttpServletRequest.class);
		final Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		for (Cookie cookie : cookies) {
			final String name = cookie.getName();
			if (name.equals(attributeName)) {
				return cookie.getValue();
			}
		}
		return null;
	}

}
