package org.impalaframework.extension.event;

import org.springframework.util.Assert;

/**
 */
public class EventTask implements Runnable {

	final private Event Event;

	final private EventListener eventListener;

	public EventTask(
			final Event Event,
			final EventListener eventListener) {
		super();
		Assert.notNull(Event);
		Assert.notNull(eventListener);
		this.Event = Event;
		this.eventListener = eventListener;
	}

	public void run() {
		eventListener.onEvent(Event);
	}

	protected Event getEvent() {
		return Event;
	}

	protected EventListener getEventListener() {
		return eventListener;
	}

}