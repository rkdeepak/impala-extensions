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

package org.impalaframework.extension.mvc.flash;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.springframework.ui.ModelMap;

public class FlashHelperTest extends TestCase {
    
    private FlashHelper adapter;
    private HttpServletRequest request;
    private HttpSession session;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        adapter = new FlashHelper();
        request = createMock(HttpServletRequest.class);
        session = createMock(HttpSession.class);
    }
    
    @SuppressWarnings("unchecked")
    public void testSetFlashState() throws Exception {
        
        HashMap<String, String> flashMap = new HashMap<String, String>();
        flashMap.put("flash:one", "v1");
        flashMap.put("flash:two", "v2");
        
        final ModelMap modelMap = new ModelMap();
        modelMap.putAll(flashMap);
        modelMap.put("three", "v3");
        
        HashMap<String, String> setMap = new HashMap<String, String>();
        setMap.put("one", "v1");
        setMap.put("two", "v2");

        expect(request.getSession()).andReturn(session);
        session.setAttribute("flashState", setMap);
        
        replay(request, session);
        adapter.setFlashState(request, modelMap);
        verify(request, session);
    }
    
    public void testSetSessionPrefix() throws Exception {
        
        adapter = new FlashHelper("pref:");
        
        HashMap<String, Object> flashMap = new HashMap<String, Object>();
        flashMap.put("flash:one", "v1");
        
        HashMap<String, String> setMap = new HashMap<String, String>();
        setMap.put("one", "v1");

        expect(request.getSession()).andReturn(session);
        session.setAttribute("pref:flashState", setMap);
        
        replay(request, session);
        adapter.setFlashState(request, flashMap);
        verify(request, session);
    }
    
    public void testUnpackFlashState() throws Exception {

        expect(request.getSession(false)).andReturn(session);
        
        HashMap<String, String> setMap = new LinkedHashMap<String, String>();
        setMap.put("one", "v1");
        setMap.put("two", "v2");
        expect(session.getAttribute("flashState")).andReturn(setMap);
        request.setAttribute("flashState", setMap);
        session.removeAttribute("flashState");
        
        //assume one is not present
        expect(request.getAttribute("one")).andReturn(null);
        request.setAttribute("one", "v1");

        //assume two is present
        expect(request.getAttribute("two")).andReturn("somethingelse");
        
        replay(request, session);
        adapter.unpackFlashState(request);
        verify(request, session);
    }
    

    public void testUnpackSessionPrefix() throws Exception {
        
        adapter = new FlashHelper("pref:");

        expect(request.getSession(false)).andReturn(session);
        
        HashMap<String, String> setMap = new LinkedHashMap<String, String>();
        setMap.put("one", "v1");
        setMap.put("two", "v2");
        expect(session.getAttribute("pref:flashState")).andReturn(setMap);
        request.setAttribute("flashState", setMap);
        session.removeAttribute("pref:flashState");
        
        //assume one is not present
        expect(request.getAttribute("one")).andReturn(null);
        request.setAttribute("one", "v1");

        //assume two is present
        expect(request.getAttribute("two")).andReturn("somethingelse");
        
        replay(request, session);
        adapter.unpackFlashState(request);
        verify(request, session);
    }
    
    @SuppressWarnings("unchecked")
    public void testMergeFlashState() throws Exception {
        final ModelMap modelMap = new ModelMap();
        modelMap.put("one", "existingOne");
        
        HashMap<String, String> setMap = new HashMap<String, String>();
        setMap.put("one", "v1");
        setMap.put("two", "v2");
        expect(request.getAttribute("flashState")).andReturn(setMap);
        request.removeAttribute("flashState");
        
        replay(request, session);
        adapter.mergeFlashState(request, modelMap);
        
        assertEquals("existingOne", modelMap.get("one"));
        assertEquals("v2", modelMap.get("two"));
        
        verify(request, session);
    }

}
