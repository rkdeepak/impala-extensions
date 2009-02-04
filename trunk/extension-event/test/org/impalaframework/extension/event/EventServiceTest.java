package org.impalaframework.extension.event;

import junit.framework.TestCase;

import org.joda.time.Period;

public class EventServiceTest extends TestCase {
	
	public void testSynchronous() throws InterruptedException {

		SynchronousEventService manager = new SynchronousEventService();
		DefaultEventListenerRegistry registry = new DefaultEventListenerRegistry();
		manager.setEventListenerRegistry(registry);
		
		Event[] Event = Event();
		TestEventListener[] listeners = EventTestUtils.listeners();

		EventTestUtils.registerListeners(registry, listeners);
		
		submitEvents(manager, Event);
		
		EventTestUtils.checkListeners(listeners);
	}
	
	public void testAsynchronous() throws InterruptedException {

		AsynchronousEventService manager = new AsynchronousEventService() {

			@Override
			protected void consumeEvent() {
				try {
					//put in a small delay to prevent the race condition described below
					Thread.sleep(5);
				} catch (InterruptedException e) {
				}
				super.consumeEvent();
			}
			
		};
		
		DefaultEventListenerRegistry registry = new DefaultEventListenerRegistry();
		manager.setEventListenerRegistry(registry);
		SimpleEventSynchronizer eventSynchronizer = new SimpleEventSynchronizer();
		manager.setEventSynchronizer(eventSynchronizer);
		manager.setEventTaskFactory(new SimpleEventTaskFactory(eventSynchronizer));
		
		Event[] events = Event();
		TestEventListener[] listeners = EventTestUtils.listeners();

		EventTestUtils.registerListeners(registry, listeners);
		
		manager.start();
		
		Thread.sleep(1000);
		
		submitEvents(manager, events);
		
		//have to watch out for a race condition here as it is theoretically
		//possible for all four events to be executed before this can be handled
		assertTrue(manager.getEventQueueSize() > 0);
		
		//wait a little while, to allow the events to be processed
		Thread.sleep(500);
		
		EventTestUtils.checkListeners(listeners);
		
		assertEquals(0, manager.getEventQueueSize());
		
		//still active
		assertTrue(manager.isActive());

		assertEquals(3, registry.getListenerCount());
		
		//now shut down
		manager.stop();
		assertEquals(0, registry.getListenerCount());
		
		//check is stopped
		assertTrue(manager.isStopped());
		
		try {
			manager.submitEvent(events[0]);
		}
		catch (IllegalStateException e) {
			assertEquals("Cannot accept events as event manager has stopped", e.getMessage());
		}
		
		//now restart
		manager.start();
		assertTrue(manager.isActive());
	}

	private void submitEvents(EventService manager, Event[] events) {

		manager.submitEvent(events[0]);
		manager.submitEvent(events[2]);
		manager.submitEvent(events[3]);
		
		//this should not come off the queue last as it is high priority - check in debug mode
		manager.submitEvent(events[1]);
	}

	private Event[] Event() {
		EventType lowPriority = new EventType("mytype", false, true, new Period().withSeconds(10));
		EventType highPriority = new EventType("mytype", false, true, new Period().withSeconds(0));
		EventType anotherType = new EventType("anothertype", false, true, null);
		EventType irrelevantType = new EventType("irrelevanttype", false, true, null);

		Event event1 = new Event(lowPriority, "1", 1L, "mytype");
		Event event2 = new Event(highPriority,  "2", 2L, "mytype");
		Event event3 = new Event(anotherType, "3", 3L, "mytype");
		Event event4 = new Event(irrelevantType,  "4", 4L, "mytype");
		
		Event[] Event = new Event[] { event1, event2, event3, event4 };
		return Event;
	}

}
