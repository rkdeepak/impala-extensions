package org.impalaframework.extension.event;

import org.springframework.util.Assert;

/**
 */
public class EventTask implements Runnable {

	final private Event Event;

	final private EventListener eventListener;

	final private EventSynchronizer eventSynchronizer;

	public EventTask(
			final EventSynchronizer eventSynchronizer, 
			final Event Event,
			final EventListener eventListener) {
		super();
		Assert.notNull(eventSynchronizer);
		Assert.notNull(Event);
		Assert.notNull(eventListener);
		this.eventSynchronizer = eventSynchronizer;
		this.Event = Event;
		this.eventListener = eventListener;
	}

	public void run() {
		maybeAwaitTransactionCompletion();
		eventListener.onEvent(Event);
	}

	void maybeAwaitTransactionCompletion() {

		// this will block until the event is no longer active. This change will
		// take place when the EventAwareHibernateTransactionManager completes
		if (eventListener instanceof AsynchronousEventListener) {
			
			AsynchronousEventListener listener = (AsynchronousEventListener) eventListener;
			
			if (listener.awaitTransactionCompletion()) {

				// FIXME should we have a maximum period after event consumption
				// will definitely stop blocking
				while (eventSynchronizer.isEventActive(Event)) {
					try {
						Thread.sleep(20);
					}
					catch (InterruptedException e) {
					}
				}
			}
		}
	}

	protected Event getEvent() {
		return Event;
	}

	protected EventListener getEventListener() {
		return eventListener;
	}

}