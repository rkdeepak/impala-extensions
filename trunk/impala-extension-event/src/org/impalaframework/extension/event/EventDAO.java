package org.impalaframework.extension.event;

/**
 * Represents mechanism for events being persisted, as well
 * as information recording the success or failure of event listener processing.
 * @author Phil Zoio
 */
public interface EventDAO {
	
	public void insertEvent(Event event);

	public void insertProcessedEvent(String eventId, String consumer);

	public void insertFailedEvent(String eventId, String consumer, Throwable error);

}
