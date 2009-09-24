package org.impalaframework.extension.mvc;

import static org.easymock.EasyMock.expect;

import org.springframework.core.MethodParameter;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.WebRequest;

public class RequestAttributeTest extends BaseResolverTest {

	private RequestAttributeArgumentResolver resolver;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		resolver = new RequestAttributeArgumentResolver();
	}
	
	public void testAttribute() throws Exception {
		assertEquals("value", doRequestAttribute("requestAttribute", "attribute", "value"));
	}

	private Object doRequestAttribute(String methodName, final String attributeName, final String value)
			throws Exception {
		
		MethodParameter methodParameter = new MethodParameter(ReflectionUtils.findMethod(AnnotatedClass.class, methodName, new Class[]{ String.class }), 0);

		expect(nativeRequest.getAttribute(attributeName, WebRequest.SCOPE_REQUEST)).andReturn(value);
		
		replayMocks();
		
		Object arg = resolver.resolveArgument(methodParameter, nativeRequest);
		
		verifyMocks();
		return arg;
	}
	
}