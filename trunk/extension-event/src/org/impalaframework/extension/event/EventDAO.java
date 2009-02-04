package org.impalaframework.extension.event;


public interface EventDAO {
	
	public void insertEvent(Event event);

	public void insertProcessedEvent(String eventId, String consumer);

	public void insertFailedEvent(String eventId, String consumer, String error);

}
