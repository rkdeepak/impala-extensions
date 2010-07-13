package org.impalaframework.extension.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

/**
 * Extends {@link EventTask} to support persistence of {@link Event}
 * @author Phil Zoio
 */
public class RecordingEventTask extends EventTask {
	
	private static final Log logger = LogFactory.getLog(RecordingEventTask.class);
	
	private final PlatformTransactionManager transactionManager;

	private final EventDAO eventDAO;

	public RecordingEventTask(PlatformTransactionManager transactionManager, EventDAO eventDAO, EventSynchronizer eventSynchronizer,
			Event event, EventListener eventListener) {
		super(event, eventListener);
		Assert.notNull(eventDAO);
		Assert.notNull(transactionManager);
		this.eventDAO = eventDAO;
		this.transactionManager = transactionManager;
	}

	@Override
	public void run() {

		if (!getEventListener().getMarkProcessed()) {
			super.run();
		}
		else {
			Throwable throwable = null;
			try {
				super.run();
			}
			catch (Throwable error) {
				throwable = error;
			}

			final Event event = getEvent();
			if (event.getEventType().isPersistent()) {
				
				String eventId = event.getEventId();
				
				if (eventId == null) {
					throw new IllegalStateException("Event is persisent but no event ID has been set. Event details: " + event);
				}
				doStatusRecording(eventId, getEventListener().getConsumerName(), throwable);
			}
		}
	}

	void doStatusRecording(final String eventId, final String consumer, final Throwable error) {
		try {
			new TransactionTemplate(transactionManager).execute(new TransactionCallback() {

				public Object doInTransaction(TransactionStatus status) {
					if (error != null) {
						eventDAO.insertFailedEvent(eventId, consumer, error);
					} else {
						eventDAO.insertProcessedEvent(eventId, consumer);
					}
					return null;
				}});
		}
		catch (Exception e) {
			logger.error("Event " + eventId + " processed by consumer " + consumer + " but unable to record this in the ProcessedEvent table");
		}
	}

}
