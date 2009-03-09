package org.impalaframework.extension.event;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class EventListenerContributor implements InitializingBean, DisposableBean {
	
	private EventListenerRegistry registry;
	
	private Map<String, EventListener> contributedListeners;

	public EventListenerRegistry getRegistry() {
		return registry;
	}

	public void setRegistry(EventListenerRegistry registry) {
		this.registry = registry;
	}

	public Map<String, EventListener> getContributedListeners() {
		return contributedListeners;
	}

	public void setContributedListeners(Map<String, EventListener> contributedListeners) {
		this.contributedListeners = contributedListeners;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(contributedListeners);
		Assert.notNull(registry);
		
		Set<String> eventTypes = contributedListeners.keySet();
		for (String eventType : eventTypes) {
			String contributionType  = getType(eventType);
			EventListener listener = contributedListeners.get(eventType);
			registry.addListener(contributionType, listener);
		}
	}

	public void destroy() throws Exception {
		Set<String> eventTypes = contributedListeners.keySet();
		for (String eventType : eventTypes) {
			String contributionType  = getType(eventType);
			EventListener listener = contributedListeners.get(eventType);
			registry.removeListener(contributionType, listener);
		}
	}

	String getType(String eventType) {
		String type = null;
		int dotIndex = eventType.indexOf('.');
		if (dotIndex >= 0) {
			type = eventType.substring(0, dotIndex);
		} else {
			type = eventType;
		}
		return type;
	}

}
