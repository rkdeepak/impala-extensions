package org.impalaframework.extension.event;

import java.util.List;

/**
 * Defines interface for managing a collection of event listeners, keyed by type.
 * @author Phil Zoio
 */
public interface EventListenerRegistry {

	/**
	 * Clear event listeners. {@link #getListenerCount()} should return zero after this has been called
	 */
	void clear();

	/**
	 * Adds listener for a particular event type
	 * @see EventType#getType()
	 */
	void addListener(String eventType, EventListener listener);

	/**
	 * Removes listeners for a particular event type
	 * @see EventType#getType()
	 */
	boolean removeListener(String eventTypeName, EventListener listener);

	/**
	 * Returns all the event listeners for a particular event type
	 */
	List<EventListener> getEventListeners(String eventTypeName);

	/**
	 * Gets the total number of listeners managed by this {@link EventListenerRegistry} instance
	 */
	int getListenerCount();

	/**
	 * Gets the listeners registered for a particular event type
	 */
	int getListenerCountForType(String type);

}