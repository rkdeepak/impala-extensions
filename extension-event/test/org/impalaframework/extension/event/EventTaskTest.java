package org.impalaframework.extension.event;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class EventTaskTest extends TestCase {

	private EventType eventType;

	private Event event;

	private SimpleEventSynchronizer eventSynchronizer;

	public void setUp() {
		eventType = new EventType("mytype");
		event = new Event(eventType, "mysubject", "1", "mytype");
		eventSynchronizer = new SimpleEventSynchronizer();
	}

	public void testAsynchronous() throws Exception {
		EventListener eventListener = new AsyncTestEventListener(false);
		final EventTask eventTask = new EventTask(event, eventListener);
		eventSynchronizer.awaitTransactionCompletion(eventTask);
		assertEquals(0, eventSynchronizer.getSubmitted().size());
		
		Thread.sleep(200);
		
		assertEquals(1, eventSynchronizer.getSubmitted().size());
	}

}

class AsyncTestEventListener implements AsynchronousEventListener {

	private List<Event> events = new ArrayList<Event>();

	private boolean awaitCompletion;

	AsyncTestEventListener(boolean awaitCompletion) {
		super();
		this.awaitCompletion = awaitCompletion;
	}

	public boolean awaitTransactionCompletion() {
		return awaitCompletion;
	}

	public String getConsumerName() {
		return "consumer";
	}

	public boolean getMarkProcessed() {
		return true;
	}

	public void onEvent(Event event) {
		events.add(event);
	}

	public List<Event> getEvents() {
		return events;
	}

}