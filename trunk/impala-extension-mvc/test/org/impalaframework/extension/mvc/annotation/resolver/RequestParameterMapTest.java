package org.impalaframework.extension.mvc.annotation.resolver;

import static org.easymock.EasyMock.expect;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.util.ReflectionUtils;

public class RequestParameterMapTest extends BaseResolverTest {

	private RequestParameterMapArgumentResolver resolver;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		resolver = new RequestParameterMapArgumentResolver();
	}
	
	@SuppressWarnings("unchecked")
	public void testAttribute() throws Exception {
		final Map<String,String> map = (Map<String, String>) doRequestAttribute("requestParameterMap", "attribute", "value");
		assertEquals(2, map.size());
	}

	private Object doRequestAttribute(String methodName, final String attributeName, final String value)
			throws Exception {
		
		MethodParameter methodParameter = new MethodParameter(ReflectionUtils.findMethod(AnnotatedClass.class, methodName, new Class[]{ String.class }), 0);

		expect(nativeRequest.getNativeRequest()).andReturn(request);
		expect(request.getParameterNames()).andReturn(Collections.enumeration(Arrays.asList("one", "two")));
		expect(request.getParameter("one")).andReturn("1");
		expect(request.getParameter("two")).andReturn("2");
		
		replayMocks();
		
		Object arg = resolver.resolveArgument(methodParameter, nativeRequest);
		
		verifyMocks();
		return arg;
	}
	
}