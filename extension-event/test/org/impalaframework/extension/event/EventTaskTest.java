package org.impalaframework.extension.event;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class EventTaskTest extends TestCase {

	private EventType eventType;

	private Event Event;

	private EventSynchronizer eventSynchronizer;

	public void setUp() {
		eventType = new EventType("mytype");
		Event = new Event(eventType, "mysubject", 1L, "mytype");
		eventSynchronizer = new SimpleEventSynchronizer();
	}

	public void testMaybeAwaitTransactionCompletion() {
		EventListener eventListener = new AsyncTestEventListener(true);
		EventTask eventTask = new EventTask(eventSynchronizer, Event, eventListener);

		eventTask.maybeAwaitTransactionCompletion();
	}

	public void testMaybeAwaitTransactionCompletionWithWait() throws Exception {
		EventListener eventListener = new AsyncTestEventListener(true);
		final EventTask eventTask = new EventTask(eventSynchronizer, Event, eventListener);

		eventSynchronizer.activate(Event);

		// set up the thread which will call the method we are testing
		Thread waitingThread = new Thread(new Runnable() {
			public void run() {
				eventTask.maybeAwaitTransactionCompletion();
			}
		});

		waitingThread.start();

		// wait a few milliseconds
		sleep(50);

		// call deactivateThreadEvents. If you comment out this call, then the
		// test should hang
		eventSynchronizer.deactivateThreadEvents();

		// wait for waitingThread to execute
		waitingThread.join();
	}

	public void testNotExpectingToWait() throws Exception {
		EventListener eventListener = new AsyncTestEventListener(false);
		final EventTask eventTask = new EventTask(eventSynchronizer, Event, eventListener);

		eventSynchronizer.activate(Event);

		// set up the thread which will call the method we are testing
		Thread waitingThread = new Thread(new Runnable() {
			public void run() {
				eventTask.maybeAwaitTransactionCompletion();
			}
		});

		waitingThread.start();
		waitingThread.join();
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {
		}
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