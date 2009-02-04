package org.impalaframework.extension.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

public class EventListenerContributorTest extends TestCase {

	public void testGetRegistry() throws Exception {
		EventListenerContributor contributor = new EventListenerContributor();
		DefaultEventListenerRegistry registry = new DefaultEventListenerRegistry();
		contributor.setRegistry(registry);
		
		//create three listeners
		TestEventListener[] listeners = EventTestUtils.listeners();
		Map<String, EventListener> contributedListeners = new HashMap<String, EventListener>();
		
		contributedListeners.put("typeOne.1", listeners[0]);
		contributedListeners.put("typeOne.2", listeners[1]);
		
		contributedListeners.put("typeTwo.1", listeners[0]);
		contributedListeners.put("typeTwo", listeners[2]);
		
		contributedListeners.put("typeThree", listeners[1]);
		contributedListeners.put("typeThree", listeners[2]);
		
		contributor.setContributedListeners(contributedListeners);
		contributor.afterPropertiesSet();
		
		List<EventListener> typeOneListeners = registry.getEventListeners("typeOne");
		List<EventListener> typeTwoListeners = registry.getEventListeners("typeTwo");
		List<EventListener> typeThreeListeners = registry.getEventListeners("typeThree");
		
		assertEquals(2, typeOneListeners.size());
		assertTrue(typeOneListeners.contains(listeners[0]));
		assertTrue(typeOneListeners.contains(listeners[1]));
		
		assertEquals(2, typeTwoListeners.size());
		assertTrue(typeTwoListeners.contains(listeners[0]));
		assertTrue(typeTwoListeners.contains(listeners[2]));
		
		assertEquals(1, typeThreeListeners.size());
		assertTrue(typeThreeListeners.contains(listeners[2]));
		
		contributor.destroy();
		assertEquals(0, registry.getEventListeners("typeOne").size());
		assertEquals(0, registry.getListenerCount());
	}

}
