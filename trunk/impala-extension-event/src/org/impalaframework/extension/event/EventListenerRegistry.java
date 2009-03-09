package org.impalaframework.extension.event;

import java.util.List;

public interface EventListenerRegistry {

	void clear();

	void addListener(String eventType, EventListener listener);

	boolean removeListener(String eventTypeName, EventListener listener);

	List<EventListener> getEventListeners(String eventTypeName);

	int getListenerCount();

	int getListenerCountForType(String type);

}