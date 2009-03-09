package org.impalaframework.extension.event;
import junit.framework.TestCase;

public class DefaultEventListenerRegistryTest extends TestCase {

	public void testAddRemoveListener() {

		TestEventListener[] listeners = EventTestUtils.listeners();
		
		EventListenerRegistry registry = new DefaultEventListenerRegistry();
		
		registry.addListener("mytype", listeners[0]);
		registry.addListener("anothertype", listeners[1]);
		registry.addListener("mytype", listeners[2]);
		
		assertEquals(2, registry.getListenerCountForType("mytype"));
		assertEquals(1, registry.getListenerCountForType("anothertype"));
		
		assertTrue(registry.removeListener("mytype", listeners[0]));
		assertFalse(registry.removeListener("mytype", listeners[1]));
		assertTrue(registry.removeListener("mytype", listeners[2]));
		
		assertTrue(registry.removeListener("anothertype", listeners[1]));
		assertFalse(registry.removeListener("anothertype", listeners[0]));
		
		assertEquals(0, registry.getListenerCountForType("mytype"));
		assertEquals(0, registry.getListenerCountForType("anothertype"));
		assertEquals(0, registry.getListenerCount());
	}

}
