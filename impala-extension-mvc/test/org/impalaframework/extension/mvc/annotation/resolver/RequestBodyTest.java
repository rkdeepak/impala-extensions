package org.impalaframework.extension.mvc.annotation.resolver;

import static org.easymock.EasyMock.expect;

import java.io.IOException;

import javax.servlet.ServletInputStream;

import org.impalaframework.extension.mvc.BaseResolverTest;
import org.impalaframework.extension.mvc.annotation.resolver.RequestBodyArgumentResolver;
import org.springframework.core.MethodParameter;
import org.springframework.util.ReflectionUtils;

public class RequestBodyTest extends BaseResolverTest {

	private RequestBodyArgumentResolver resolver;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		resolver = new RequestBodyArgumentResolver();
	}
	
	public void testAttribute() throws Exception {
		assertEquals("", doRequestAttribute("requestBody", "attribute", "value"));
	}

	private Object doRequestAttribute(String methodName, final String attributeName, final String value)
			throws Exception {
		
		MethodParameter methodParameter = new MethodParameter(ReflectionUtils.findMethod(AnnotatedClass.class, methodName, new Class[]{ String.class }), 0);

		expect(nativeRequest.getNativeRequest()).andReturn(request);
		expect(request.getInputStream()).andReturn(new ServletInputStream() {
			
			@Override
			public int read() throws IOException {
				return -1;
			}
		});
		
		replayMocks();
		
		Object arg = resolver.resolveArgument(methodParameter, nativeRequest);
		
		verifyMocks();
		return arg;
	}
	
}