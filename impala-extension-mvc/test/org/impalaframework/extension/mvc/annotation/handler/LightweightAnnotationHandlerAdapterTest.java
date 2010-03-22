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

package org.impalaframework.extension.mvc.annotation.handler;

import junit.framework.TestCase;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

public class LightweightAnnotationHandlerAdapterTest extends TestCase {
	
	private LightweightAnnotationHandlerAdapter adapter;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		adapter = new LightweightAnnotationHandlerAdapter();
	}

	public void testSupports() {
		assertTrue(adapter.supports(new TestLightweightController()));
		assertFalse(adapter.supports(new TestNonLightweightController()));
	}
	
	public void testGetLastModified() throws Exception {
		assertEquals(-1, adapter.getLastModified(null, null));
	}

	public void testGetMethodResolver() throws Exception {
		final TestLightweightController handler = new TestLightweightController();
		final AnnotationHandlerMethodResolver methodResolver = adapter.getMethodResolver(handler);
		assertSame(adapter.getMethodResolver(handler), methodResolver);
		
		assertEquals(2, methodResolver.getModelAttributeMethods().size());
		assertEquals(3, methodResolver.getHandlerMethods().size());
		assertEquals(1, methodResolver.getHandlerAnnotations().size());
	}
	
}

@LightweightAdaptable
class TestLightweightController {
	
	@ModelAttribute("att1")
	public Integer getAtt1() { return 1; }
	
	@ModelAttribute("att1")
	public String getAtt2() { return "att2"; }	

	@RequestMapping("/method1")
	public void method1(){}
	
	@RequestMapping("/method2")
	public void method2(){}
	
	@RequestMapping("/method3")
	public void method3(){}
	
	public void method4(){}
	
}

@Controller
class TestNonLightweightController {

}
