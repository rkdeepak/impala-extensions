package org.impalaframework.extension.mvc.annotation.argument;

import static org.easymock.EasyMock.expect;

import org.impalaframework.extension.mvc.BaseResolverTest;
import org.impalaframework.extension.mvc.annotation.argument.SessionAttributeArgumentResolver;
import org.springframework.core.MethodParameter;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.WebRequest;

public class SessionAttributeTest extends BaseResolverTest {

	private SessionAttributeArgumentResolver resolver;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		resolver = new SessionAttributeArgumentResolver();
	}
	
	public void testAttribute() throws Exception {
		assertEquals("value", doRequestAttribute("sessionAttribute", "attribute", "value"));
	}

	private Object doRequestAttribute(String methodName, final String attributeName, final String value)
			throws Exception {
		
		MethodParameter methodParameter = new MethodParameter(ReflectionUtils.findMethod(AnnotatedClass.class, methodName, new Class[]{ String.class }), 0);

		expect(nativeRequest.getAttribute(attributeName, WebRequest.SCOPE_SESSION)).andReturn(value);
		
		replayMocks();
		
		Object arg = resolver.resolveArgument(methodParameter, nativeRequest);
		
		verifyMocks();
		return arg;
	}
	
}