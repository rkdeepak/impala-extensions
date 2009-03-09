package org.impalaframework.extension.event;


/**
 * Defines interface for handling synchronous or asynchronous events. Information for these events are contained in Event
 * Note that implementations must be thread safe
 * @author pzoio
 */
public interface EventListener {
	public void onEvent(Event event);
	public String getConsumerName();
	public boolean getMarkProcessed();
}
