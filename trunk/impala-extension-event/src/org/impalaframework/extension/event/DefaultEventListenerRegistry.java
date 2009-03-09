package org.impalaframework.extension.event;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.util.Assert;

//FIXME test
public class DefaultEventListenerRegistry implements EventListenerRegistry {
	
	private Map<String, List<EventListener>> eventListeners = new ConcurrentHashMap<String, List<EventListener>>();

	/* ******************** main interface methods ****************** */

	public void clear() {
		eventListeners.clear();
	}

	public void addListener(String eventType, EventListener listener) {

		Assert.notNull(eventType);
		Assert.notNull(listener);

		List<EventListener> list = eventListeners.get(eventType);
		if (list == null) {
			list = new CopyOnWriteArrayList<EventListener>();
			eventListeners.put(eventType, list);
		}
		list.add(listener);

	}

	public boolean removeListener(String eventTypeName, EventListener listener) {
		List<EventListener> list = eventListeners.get(eventTypeName);
		boolean remove = false;
		if (list != null) {
			remove = list.remove(listener);
		}
		return remove;
	}

	public List<EventListener> getEventListeners(String eventTypeName) {
		List<EventListener> list = eventListeners.get(eventTypeName);
		if (list != null)
			return Collections.unmodifiableList(list);
		else
			return Collections.emptyList();
	}

	/* ******************** private implementation methods ****************** */

	public int getListenerCount() {
		int count = 0;
		for (String eventType : eventListeners.keySet()) {
			count += getListenerCountForType(eventType);
		}
		return count;
	}

	public int getListenerCountForType(String type) {
		List<EventListener> list = getEventListeners(type);
		if (list == null) {
			return 0;
		}
		return list.size();
	}

}
