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

	private Event Event;

	private EventSynchronizer eventSynchronizer;

	private PlatformTransactionManager transactionManager;

	private EventDAO eventDAO;

	public void setUp() {
		eventType = new EventType("mytype");
		Event = new Event(eventType, "mysubject", 1L, "mytype");
		eventSynchronizer = new SimpleEventSynchronizer();
		transactionManager = new DummyTransactionManager();
		eventDAO = createMock(EventDAO.class);
	}

	public void testRunWithRecording() {
		TestEventListener eventListener = new TestEventListener();
		eventListener.setMarkProcessed(true);
		RecordingEventTask eventTask = new RecordingEventTask(transactionManager, eventDAO, eventSynchronizer, Event, eventListener);

		eventDAO.insertProcessedEvent(isA(String.class), eq(eventListener.getConsumerName()));
		
		replayMocks();
		eventTask.run();
		verifyMocks();
	}
	
	public void testRunNoRecording() {
		TestEventListener eventListener = new TestEventListener();
		eventListener.setMarkProcessed(false);
		RecordingEventTask eventTask = new RecordingEventTask(transactionManager, eventDAO, eventSynchronizer, Event, eventListener);

		//note: no insertProcessedEvent is called
		
		replayMocks();
		eventTask.run();
		verifyMocks();
	}
	
	public void testFailedEvent() {
		EventListener eventListener = new AsyncTestEventListener(true);
		RecordingEventTask eventTask = new RecordingEventTask(transactionManager, eventDAO, eventSynchronizer, Event, eventListener);

		eventDAO.insertFailedEvent("myeventId", "myconsumer", "some error");
		
		replayMocks();
		eventTask.doStatusRecording("myeventId", "myconsumer", "some error");
		verifyMocks();
	}

	public void testSucceedEvent() {
		EventListener eventListener = new AsyncTestEventListener(true);
		RecordingEventTask eventTask = new RecordingEventTask(transactionManager, eventDAO, eventSynchronizer, Event, eventListener);

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


