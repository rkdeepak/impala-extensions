package org.impalaframework.extension.event;


public class SimpleEventTaskFactory implements EventTaskFactory {

	public SimpleEventTaskFactory() {
		super();
	}

	public EventTask newEventTask(Event event, EventListener eventListener) {
		return new EventTask(event, eventListener);
	}

}
