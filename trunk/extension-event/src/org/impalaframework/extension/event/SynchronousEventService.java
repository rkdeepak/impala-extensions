package org.impalaframework.extension.event;

import java.util.List;

import org.springframework.util.Assert;

public class SynchronousEventService implements EventService {

	private EventListenerRegistry eventListenerRegistry;
	
	public void submitEvent(Event event) {
		
		Assert.notNull(eventListenerRegistry);
		
		EventType eventType = event.getEventType();
		String type = eventType.getType();
		List<EventListener> list = eventListenerRegistry.getEventListeners(type);

		if (list != null) {
			for (EventListener eventListener : list) {
				eventListener.onEvent(event);
			}
		}
	}

	/* ********************* wired in setters ******************* */

	public void setEventListenerRegistry(EventListenerRegistry eventListenerRegistry) {
		this.eventListenerRegistry = eventListenerRegistry;
	}	
	
}
