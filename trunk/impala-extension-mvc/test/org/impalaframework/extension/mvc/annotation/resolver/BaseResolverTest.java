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

package org.impalaframework.extension.mvc.annotation.resolver;

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
