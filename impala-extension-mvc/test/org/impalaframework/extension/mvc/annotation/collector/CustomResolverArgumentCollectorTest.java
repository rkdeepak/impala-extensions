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

package org.impalaframework.extension.mvc.annotation.collector;

import static org.easymock.EasyMock.expect;

import org.impalaframework.extension.mvc.annotation.RequestAttribute;
import org.impalaframework.extension.mvc.annotation.resolver.RequestAttributeArgumentResolver;
import org.springframework.core.MethodParameter;

public class CustomResolverArgumentCollectorTest extends BaseArgumentCollectorTest {
	
	private CustomResolverArgumentCollector collector;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

    public void testGetArgument() {
        MethodParameter methodParameter = getMethodParameter(CustomResolverTestClass.class, "method1", Long.class);
        collector = new CustomResolverArgumentCollector(new RequestAttributeArgumentResolver(), methodParameter);
        
        expect(request.getAttribute("param1", 0)).andReturn(1L);
        
        replayMocks();
        assertEquals(1L, collector.getArgument(request, implicitModel, typeConverter));
        verifyMocks();
    }
    
}
class CustomResolverTestClass {
    public void method1(@RequestAttribute("param1") Long param1) {}
}
