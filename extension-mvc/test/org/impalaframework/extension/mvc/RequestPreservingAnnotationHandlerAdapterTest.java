  package org.impalaframework.extension.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class RequestPreservingAnnotationHandlerAdapterTest extends TestCase {
	
	private RequestPreservingAnnotationHandlerAdapter adapter;
	private HttpServletRequest request;
	private HttpSession session;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		adapter = new RequestPreservingAnnotationHandlerAdapter();
		request = createMock(HttpServletRequest.class);
		session = createMock(HttpSession.class);
	}

	public void testIsRedirect() {
		ModelAndView modelAndView = new ModelAndView();
		assertFalse(adapter.isRedirect(modelAndView));
		
		modelAndView = new ModelAndView("redirect:home");
		assertTrue(adapter.isRedirect(modelAndView));
		
		modelAndView = new ModelAndView(new RedirectView());
		assertTrue(adapter.isRedirect(modelAndView));
	}
	
	public void testImplicitParams() throws Exception {
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
		adapter.setParameters(request, modelMap);
		assertEquals(2, modelMap.size());
		verify(request);
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
