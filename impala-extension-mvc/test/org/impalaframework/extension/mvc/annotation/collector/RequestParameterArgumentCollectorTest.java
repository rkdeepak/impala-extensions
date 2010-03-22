package org.impalaframework.extension.mvc.annotation.collector;

import static org.easymock.EasyMock.expect;

import java.lang.annotation.Annotation;

import org.springframework.web.bind.annotation.RequestParam;

public class RequestParameterArgumentCollectorTest extends BaseArgumentCollectorTest {

	public void testGetArgument() {
		
		Annotation annotation = getParamAnnotation(RequestParameterLongClass.class, "method1", Long.class);
		RequestParameterArgumentCollector collector = new RequestParameterArgumentCollector((RequestParam) annotation, Long.class);
		
		expect(request.getParameter("param1")).andReturn("1");
		
		replayMocks();
		
		Object value = collector.getArgument(request, implicitModel, typeConverter);
		
		assertEquals(1L, value);
		
		verifyMocks();
	}
	
	public void testGetArgumentMissing() {
		
		Annotation annotation = getParamAnnotation(RequestParameterStringClass.class, "method1", String.class);
		RequestParameterArgumentCollector collector = new RequestParameterArgumentCollector((RequestParam) annotation, String.class);
		
		expect(request.getParameter("param1")).andReturn(null);
		
		replayMocks();
		
		Object value = collector.getArgument(request, implicitModel, typeConverter);
		
		assertEquals(null, value);
		
		verifyMocks();
	}
	
	public void testGetArgumentMissingRequired() {
		
		Annotation annotation = getParamAnnotation(RequestParameterRequiredStringClass.class, "method1", String.class);
		RequestParameterArgumentCollector collector = new RequestParameterArgumentCollector((RequestParam) annotation, String.class);
		
		expect(request.getParameter("param1")).andReturn(null);
		
		replayMocks();
		
		try {
			collector.getArgument(request, implicitModel, typeConverter);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Parameter 'param1' is required.", e.getMessage());
		}
		
		verifyMocks();
	}

	//enable for Spring 3
	public void _testGetArgumentDefault() {
		
		Annotation annotation = getParamAnnotation(RequestParameterDefaultStringClass.class, "method1", String.class);
		RequestParameterArgumentCollector collector = new RequestParameterArgumentCollector((RequestParam) annotation, String.class);
		
		expect(request.getParameter("param1")).andReturn(null);
		
		replayMocks();
		
		Object value = collector.getArgument(request, implicitModel, typeConverter);
		
		assertEquals("def", value);
		
		verifyMocks();
	}


}

class RequestParameterLongClass {
	public void method1(@RequestParam("param1") Long param1) {}
}

class RequestParameterStringClass {
	public void method1(@RequestParam(value="param1",required=false) String param1) {}
}

class RequestParameterRequiredStringClass {
	public void method1(@RequestParam(value="param1",required=true) String param1) {}
}

class RequestParameterDefaultStringClass {
	public void method1(@RequestParam(value="param1"
		//,defaultValue="def" - Spring 3.0 only
			) String param1) {}
}
