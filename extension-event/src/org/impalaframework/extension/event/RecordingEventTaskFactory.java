package org.impalaframework.extension.event;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.Assert;

public class RecordingEventTaskFactory implements EventTaskFactory {
	
	private PlatformTransactionManager transactionManager;
	
	private EventSynchronizer eventSynchronizer;
	
	private EventDAO eventDAO;

	public RecordingEventTaskFactory() {
		super();
	}

	public RecordingEventTaskFactory(EventSynchronizer eventSynchronizer, PlatformTransactionManager transactionManager, EventDAO eventDAO) {
		super();
		Assert.notNull(eventSynchronizer);
		Assert.notNull(transactionManager);
		Assert.notNull(eventDAO);
		this.eventSynchronizer = eventSynchronizer;
		this.transactionManager = transactionManager;
		this.eventDAO = eventDAO;
	}
	
	public EventTask newEventTask(Event Event, EventListener eventListener) {
		return new RecordingEventTask(transactionManager, eventDAO, eventSynchronizer, Event, eventListener);
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public void setEventSynchronizer(EventSynchronizer eventSynchronizer) {
		this.eventSynchronizer = eventSynchronizer;
	}

	public void setEventDAO(EventDAO eventDAO) {
		this.eventDAO = eventDAO;
	}
	
	
}
