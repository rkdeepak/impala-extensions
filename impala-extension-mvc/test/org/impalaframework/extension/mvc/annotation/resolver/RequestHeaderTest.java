package org.impalaframework.extension.mvc.annotation.resolver;

import static org.easymock.EasyMock.expect;

import org.impalaframework.extension.mvc.BaseResolverTest;
import org.impalaframework.extension.mvc.annotation.resolver.RequestHeaderArgumentResolver;
import org.springframework.core.MethodParameter;
import org.springframework.util.ReflectionUtils;

public class RequestHeaderTest extends BaseResolverTest {

	private RequestHeaderArgumentResolver resolver;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		resolver = new RequestHeaderArgumentResolver();
	}
	
	public void testAttribute() throws Exception {
		assertEquals("value", doRequestAttribute("requestHeader", "attribute", "value"));
	}

	private Object doRequestAttribute(String methodName, final String attributeName, final String value)
			throws Exception {
		
		MethodParameter methodParameter = new MethodParameter(ReflectionUtils.findMethod(AnnotatedClass.class, methodName, new Class[]{ String.class }), 0);

		expect(nativeRequest.getNativeRequest()).andReturn(request);
		expect(request.getHeader(attributeName)).andReturn(value);
		
		replayMocks();
		
		Object arg = resolver.resolveArgument(methodParameter, nativeRequest);
		
		verifyMocks();
		return arg;
	}
	
}