package org.impalaframework.extension.event;

import junit.framework.TestCase;

public class SimpleEventSynchronizerTest extends TestCase {

	public void testActivate() {
		SimpleEventSynchronizer synchronizer = new SimpleEventSynchronizer();
		
		EventType eventType = new EventType("Event type");
		Event Event = new Event(eventType, "myid", 1L, "myvalue");
		synchronizer.doActivate(Event);
		
		//check that the event is active
		assertTrue(synchronizer.isEventActive(Event));
		
		//deactivate all the events for the thread
		assertTrue(synchronizer.deactivateThreadEvents());
		
		//check that the event is not active
		assertFalse(synchronizer.isEventActive(Event));
		
		//deactivate now returns false because events were previously deactivated
		assertFalse(synchronizer.deactivateThreadEvents());
	}
	
	
	public void testActivateDeactivate() {
		SimpleEventSynchronizer synchronizer = new SimpleEventSynchronizer();
		
		EventType eventType = new EventType("sometype");
		Event Event = new Event(eventType, "myid", null, "myvalue");
		synchronizer.activate(Event);
		
		//deactivate events 

		//check that the event is not active
		assertFalse(synchronizer.isEventActive(Event));
	}
	

}
