package org.impalaframework.extension.mvc.util;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class RequestModelHelperTest extends TestCase {

	@SuppressWarnings("unchecked")
	public void testSetParameters() {
		HttpServletRequest request = createMock(HttpServletRequest.class);
		HashMap model = new HashMap();
		model.put("p2", "2a");
		model.put("p3", null);
		
		expect(request.getParameterNames()).andReturn(Collections.enumeration(Arrays.asList("p1","p2","p3","p4")));
		expect(request.getParameter("p1")).andReturn("1");
		//expect(request.getParameter("p2")).andReturn("2");
		//expect(request.getParameter("p3")).andReturn("3");
		expect(request.getParameter("p4")).andReturn("");
		
		replay(request);
		
		RequestModelHelper.setParameters(request, model);
		
		assertEquals(4, model.size());
		assertEquals("1", model.get("p1"));
		assertEquals("2a", model.get("p2"));
		assertEquals(null, model.get("p3"));
		assertEquals("", model.get("p4"));
		
		verify(request);
	}
	
	public void testIsRedirect() throws Exception {
		assertTrue(RequestModelHelper.isRedirect(new ModelAndView(new RedirectView())));
		assertTrue(RequestModelHelper.isRedirect(new ModelAndView("redirect:view")));
		assertFalse(RequestModelHelper.isRedirect(new ModelAndView("view")));
	}
	
	public void testImplicitParams() throws Exception {
		HttpServletRequest request = createMock(HttpServletRequest.class);
		expect(request.getParameterNames()).andReturn(new Enumeration<String>() {
			
			private String[] list = {"one", "two"};
			int index;

			public boolean hasMoreElements() {
				return (index < list.length);
			}

			public String nextElement() {
				final String string = list[index];
				index++;
				return string;
			}
			
		});
		
		expect(request.getParameter("one")).andReturn("v1");
		expect(request.getParameter("two")).andReturn("v2");
		
		replay(request);
		final ModelMap modelMap = new ModelMap();
		RequestModelHelper.setParameters(request, modelMap);
		assertEquals(2, modelMap.size());
		verify(request);
	}
}
