package org.impalaframework.extension.event;

import junit.framework.TestCase;

public class TransactionalAsynchronousEventServiceTest extends TestCase {

	private boolean synchronizationActive = false;
	private TransactionalAsynchronousEventService service;
	private Event event;
	private EventType eventType;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		service = new TransactionalAsynchronousEventService() {
			@Override
			boolean synchronizationActive() {
				return synchronizationActive;
			}
		};

	}

	public void testEventIsTransactional() {
		setEventAndType(false);
		assertFalse(service.eventIsTransactional(event));
		assertFalse(service.waitingForTransaction(event));
		
		setEventAndType(true);
		assertTrue(service.eventIsTransactional(event));
		assertFalse(service.waitingForTransaction(event));
		
		synchronizationActive = true;
		assertTrue(service.waitingForTransaction(event));
	}
	
	void setEventAndType(boolean transactional) {
		eventType = new EventType("mytype", false, true, transactional, null);
        event = new Event(eventType, "user", "1", "mytype");
	}

}
