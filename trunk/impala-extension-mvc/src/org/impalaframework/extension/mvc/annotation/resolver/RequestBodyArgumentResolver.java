package org.impalaframework.extension.mvc.annotation.resolver;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.impalaframework.extension.mvc.annotation.RequestBody;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Custom {@link WebArgumentResolver} which can use the
 * {@link RequestBody} to inject the body of a request
 * as a method argument.
 * 
 * @author Phil Zoio
 */
public class RequestBodyArgumentResolver extends BaseAttributeArgumentResolver {

	protected String getAttribute(Object paramAnn) {
		if (RequestBody.class.isInstance(paramAnn)) {
			RequestBody attribute = (RequestBody) paramAnn;
			return attribute.value();
		}
		return null;
	}

	protected Object getValue(NativeWebRequest webRequest, String encoding) {
		Object nativeRequest = webRequest.getNativeRequest();
		if (nativeRequest instanceof HttpServletRequest) {
			HttpServletRequest req = (HttpServletRequest) nativeRequest;
			try {
				ServletInputStream inputStream = req.getInputStream();
				String body = FileCopyUtils.copyToString(new InputStreamReader(inputStream, encoding));
				return body;
			} catch (IOException e) {
				//FIXME log
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

}
