package org.impalaframework.extension.dao;

import org.impalaframework.extension.event.Event;
import org.impalaframework.extension.event.EventDAO;

public class EventDAOImpl implements EventDAO {

	public void insertEvent(Event event) {
		System.out.println("inserting " + event);
	}

	public void insertFailedEvent(String eventId, String consumer, String error) {
		System.out.println("inserting failed event for id " + eventId + ", consumer " + consumer);
	}

	public void insertProcessedEvent(String eventId, String consumer) {
		System.out.println("inserting processed event for id " + eventId + ", consumer " + consumer);
	}
	
}
