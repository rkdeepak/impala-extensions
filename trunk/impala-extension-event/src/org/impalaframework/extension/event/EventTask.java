package org.impalaframework.extension.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.ExecutionException;
import org.springframework.util.Assert;

/**
 * Task which will holds an {@link EventListener}, and invokes the {@link EventListener#onEvent(Event)} method
 * when the {@link #run()} method is called.
 */
public class EventTask implements Runnable {

	private static final Log logger = LogFactory.getLog(EventTask.class);
	
	final private Event event;

	final private EventListener eventListener;

	public EventTask(
			final Event Event,
			final EventListener eventListener) {
		super();
		Assert.notNull(Event);
		Assert.notNull(eventListener);
		this.event = Event;
		this.eventListener = eventListener;
	}

	public void run() {
		runInternal();
	}

	protected void runInternal() {
		try {
			eventListener.onEvent(event);
		} catch (Throwable e) {
			logger.error("Uncaught error running event: " + event + ". Message: " + e.getMessage(), e);
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				throw new ExecutionException(e);
			}
		}
	}

	protected Event getEvent() {
		return event;
	}

	protected EventListener getEventListener() {
		return eventListener;
	}

	@Override
	public String toString() {
		return "EventTask [event=" + event + ", eventListener=" + eventListener + "]";
	}

}