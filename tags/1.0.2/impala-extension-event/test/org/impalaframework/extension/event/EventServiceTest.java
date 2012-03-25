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

        AsynchronousEventService service = new AsynchronousEventService() {

            @Override
            protected void consumeQueuedEvent() {
                try {
                    //put in a small delay to prevent the race condition described below
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                }
                super.consumeQueuedEvent();
            }
            
        };
        
        service.setDelayInMilliseconds(0);
        service.setPollIntervalInMilliseconds(10);
        
        DefaultEventListenerRegistry registry = new DefaultEventListenerRegistry();
        service.setEventListenerRegistry(registry);
        service.setEventTaskFactory(new SimpleEventTaskFactory());
        
        Event[] events = Event();
        TestEventListener[] listeners = EventTestUtils.listeners();

        EventTestUtils.registerListeners(registry, listeners);
        
        service.start();
        
        Thread.sleep(1000);
        
        submitEvents(service, events);
        
        //have to watch out for a race condition here as it is theoretically
        //possible for all four events to be executed before this can be handled
        assertTrue(service.getEventQueueSize() > 0);
        
        //wait a little while, to allow the events to be processed
        Thread.sleep(500);
        
        EventTestUtils.checkListeners(listeners);
        
        assertEquals(0, service.getEventQueueSize());
        
        //still active
        assertTrue(service.isActive());

        assertEquals(3, registry.getListenerCount());
        
        //now shut down
        service.stop();
        assertEquals(0, registry.getListenerCount());
        
        //check is stopped
        assertTrue(service.isStopped());
        
        try {
            service.submitEvent(events[0]);
        }
        catch (IllegalStateException e) {
            assertEquals("Cannot accept events as event manager has stopped", e.getMessage());
        }
        
        //now restart
        service.start();
        assertTrue(service.isActive());
    }

    private void submitEvents(EventService manager, Event[] events) {

        manager.submitEvent(events[0]);
        manager.submitEvent(events[2]);
        manager.submitEvent(events[3]);
        
        //this should not come off the queue last as it is high priority - check in debug mode
        manager.submitEvent(events[1]);
    }

    private Event[] Event() {
        EventType lowPriority = new EventType("mytype", false, true, new Period().withMillis(10));
        EventType highPriority = new EventType("mytype", false, true, Period.ZERO);
        EventType anotherType = new EventType("anothertype", false, true, new Period().withMillis(20));
        EventType irrelevantType = new EventType("irrelevanttype", false, true, new Period().withMillis(20));

        Event event1 = new Event(lowPriority, "user", "1", "mytype");
        Event event2 = new Event(highPriority,  "user", "2", "mytype");
        Event event3 = new Event(anotherType, "user", "3", "anothertype");
        Event event4 = new Event(irrelevantType,  "user", "4", "mytype");
        
        Event[] Event = new Event[] { event1, event2, event3, event4 };
        return Event;
    }

}
