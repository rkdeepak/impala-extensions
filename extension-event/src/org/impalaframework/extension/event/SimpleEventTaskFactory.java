package org.impalaframework.extension.event;


public class SimpleEventTaskFactory implements EventTaskFactory {

	private EventSynchronizer eventSynchronizer;

	public SimpleEventTaskFactory(EventSynchronizer eventSynchronizer) {
		super();
		this.eventSynchronizer = eventSynchronizer;
	}

	public EventTask newEventTask(Event Event, EventListener eventListener) {
		return new EventTask(eventSynchronizer, Event, eventListener);
	}

}
