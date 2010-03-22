package org.impalaframework.extension.mvc.annotation.collector;

import static org.easymock.EasyMock.expect;

import org.impalaframework.extension.mvc.annotation.RequestAttribute;
import org.impalaframework.extension.mvc.annotation.resolver.RequestAttributeArgumentResolver;
import org.springframework.core.MethodParameter;

public class CustomResolverArgumentCollectorTest extends BaseArgumentCollectorTest {

	public void testGetArgument() {
		MethodParameter methodParameter = getMethodParameter(CustomResolverTestClass.class, "method1", Long.class);
		CustomResolverArgumentCollector collector = new CustomResolverArgumentCollector(new RequestAttributeArgumentResolver(), methodParameter);
		
		expect(request.getAttribute("param1", 0)).andReturn(1L);
		
		replayMocks();
		assertEquals(1L, collector.getArgument(request, implicitModel, typeConverter));
		verifyMocks();
	}
	
}
class CustomResolverTestClass {
	public void method1(@RequestAttribute("param1") Long param1) {}
}