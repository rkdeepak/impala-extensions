package org.impalaframework.extension.event;


public interface EventSynchronizer {
	
	void activate(Event Event);
	boolean isEventActive(Event Event);
	boolean deactivateThreadEvents();
	
}