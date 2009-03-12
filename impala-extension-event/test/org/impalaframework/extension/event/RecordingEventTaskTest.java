package org.impalaframework.extension.event;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import junit.framework.TestCase;

import org.springframework.transaction.PlatformTransactionManager;

public class RecordingEventTaskTest extends TestCase {

	private EventType eventType;

	private Event event;

	private EventSynchronizer eventSynchronizer;

	private PlatformTransactionManager transactionManager;

	private EventDAO eventDAO;

	public void setUp() {
		eventType = new EventType("mytype");
		event = new Event(eventType, "user", "1", "mytype");
		eventSynchronizer = new SimpleEventSynchronizer();
		transactionManager = new DummyTransactionManager();
		eventDAO = createMock(EventDAO.class);
	}

	public void testRunWithNoId() {
		TestEventListener eventListener = new TestEventListener("runWithRecording");
		eventListener.setMarkProcessed(true);
		RecordingEventTask eventTask = new RecordingEventTask(transactionManager, eventDAO, eventSynchronizer, event, eventListener);
		
		replayMocks();
		try {
			eventTask.run();
			fail();
		} catch (IllegalStateException e) {
			assertTrue(e.getMessage().startsWith("Event is persisent but no event ID has been set."));
		}
		verifyMocks();
	}

	public void testRunWithRecording() {
		TestEventListener eventListener = new TestEventListener("runWithRecording");
		eventListener.setMarkProcessed(true);
		RecordingEventTask eventTask = new RecordingEventTask(transactionManager, eventDAO, eventSynchronizer, event, eventListener);

		eventDAO.insertProcessedEvent(isA(String.class), eq(eventListener.getConsumerName()));
		
		replayMocks();
		event.setEventId("myid");
		eventTask.run();
		verifyMocks();
	}
	
	public void testRunNoRecording() {
		TestEventListener eventListener = new TestEventListener("runNoRecording");
		eventListener.setMarkProcessed(false);
		RecordingEventTask eventTask = new RecordingEventTask(transactionManager, eventDAO, eventSynchronizer, event, eventListener);

		//note: no insertProcessedEvent is called
		
		replayMocks();
		eventTask.run();
		verifyMocks();
	}
	
	public void testFailedEvent() {
		EventListener eventListener = new AsyncTestEventListener(true);
		RecordingEventTask eventTask = new RecordingEventTask(transactionManager, eventDAO, eventSynchronizer, event, eventListener);

		final IllegalStateException exception = new IllegalStateException();
		eventDAO.insertFailedEvent("myeventId", "myconsumer", exception);
		
		replayMocks();
		eventTask.doStatusRecording("myeventId", "myconsumer", exception);
		verifyMocks();
	}

	public void testSucceedEvent() {
		EventListener eventListener = new AsyncTestEventListener(true);
		RecordingEventTask eventTask = new RecordingEventTask(transactionManager, eventDAO, eventSynchronizer, event, eventListener);

		eventDAO.insertProcessedEvent("myeventId", "myconsumer");
		
		replayMocks();
		eventTask.doStatusRecording("myeventId", "myconsumer", null);
		verifyMocks();
	}

	private void verifyMocks() {
		verify(eventDAO);
	}

	private void replayMocks() {
		replay(eventDAO);
	}

}


