package org.impalaframework.extension.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.springframework.web.context.request.NativeWebRequest;

public class BaseResolverTest extends TestCase {

	protected HttpServletRequest request;
	protected NativeWebRequest nativeRequest;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		request = createMock(HttpServletRequest.class);
		nativeRequest = createMock(NativeWebRequest.class);
	}

	protected void replayMocks() {
		replay(request, nativeRequest);
	}	

	protected void verifyMocks() {
		verify(request, nativeRequest);
	}	
	
}