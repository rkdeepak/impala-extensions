package org.impalaframework.extension.event;

import static junit.framework.Assert.assertEquals;

import java.util.List;

public class EventTestUtils {

	static TestEventListener[] listeners() {
		TestEventListener listener1 = new TestEventListener("listener1");
		TestEventListener listener2 = new TestEventListener("listener2");
		TestEventListener listener3 = new TestEventListener("listener3");
	
		TestEventListener[] listeners = new TestEventListener[]{listener1, listener2, listener3};
		return listeners;
	}

	static void registerListeners(EventListenerRegistry registry, TestEventListener[] listeners) {
		registry.addListener("mytype", listeners[0]);
		
		registry.addListener("anothertype", listeners[1]);
		
		registry.addListener("mytype", listeners[2]);
		
		assertEquals(2, registry.getListenerCountForType("mytype"));
		assertEquals(1, registry.getListenerCountForType("anothertype"));
	}

	static void checkListeners(TestEventListener[] listeners) {
		//verify that two events where consumed by listener 1
		List<Event> listener1Events = listeners[0].getEventList();
		assertEquals(2, listener1Events.size());
		
		for (Event data : listener1Events) {
			assertEquals("mytype", data.getEventType().getType());
		}
	
		//verify that one event was consumed by listener 1
		List<Event> listener2Events = listeners[1].getEventList();
		assertEquals(1, listener2Events.size());
		assertEquals("anothertype", listener2Events.get(0).getEventType().getType());
		
		//check listener 3
		List<Event> listener3Events = listeners[2].getEventList();
		assertEquals(2, listener3Events.size());
	}

}
