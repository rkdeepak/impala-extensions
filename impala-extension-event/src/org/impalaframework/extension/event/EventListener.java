package org.impalaframework.extension.event;

import org.springframework.core.Ordered;


/**
 * Defines interface for handling synchronous or asynchronous events. Information for these events are contained in Event
 * Note that implementations must be thread safe
 * 
 * @author Phil Zoio
 */
public interface EventListener extends Ordered {
	
	/**
	 * Callback that holds event processing logic
	 */
	public void onEvent(Event event);
	
	/**
	 * The name of the event listener
	 */
	public String getConsumerName();
	
	/**
	 * Whether successful execution of the {@link EventListener} 
	 * should result in {@link EventDAO#insertProcessedEvent(String, String)} being called.
	 */
	public boolean getMarkProcessed();
}
