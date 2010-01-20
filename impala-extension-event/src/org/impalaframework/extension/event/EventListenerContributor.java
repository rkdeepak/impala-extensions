package org.impalaframework.extension.event;

import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Class whose purpose is to populate event listeners. Event listeners wired in
 * using the {@link #contributedListeners} property are themselves wired in to
 * the {@link EventListenerRegistry} (which is wired in using the
 * {@link #registry} property.
 * 
 * This indirection allows for event listeners for a particular
 * {@link EventListenerRegistry} to receive contributions from multiple sources
 * via multiple {@link EventListenerContributor} bean instances.
 * 
 * @author Phil Zoio
 */
public class EventListenerContributor implements InitializingBean, DisposableBean {
	
	private static final Log logger = LogFactory.getLog(EventListenerContributor.class);
	
	private EventListenerRegistry registry;
	
	private Map<String, EventListener> contributedListeners;

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(contributedListeners);
		Assert.notNull(registry);
		
		Set<String> eventTypes = contributedListeners.keySet();
		for (String eventType : eventTypes) {
			String contributionType  = getType(eventType);
			EventListener listener = contributedListeners.get(eventType);
			
			if (listener != null && logger.isDebugEnabled()) {
				logger.debug("Contributing instance of " + listener.getClass().getName() + ", consumer '" + listener.getConsumerName() + "' for event type: " + eventType);
			}
			
			registry.addListener(contributionType, listener);
		}
	}

	public void destroy() throws Exception {
		Set<String> eventTypes = contributedListeners.keySet();
		for (String eventType : eventTypes) {
			String contributionType  = getType(eventType);
			EventListener listener = contributedListeners.get(eventType);
			
			if (listener != null && logger.isDebugEnabled()) {
				logger.debug("Contributing instance of " + listener.getClass().getName() + ", consumer '" + listener.getConsumerName() + "' for event type: " + eventType);
			}
			
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
	
	/* ************************* Spring-wired setters ********************** */

	public void setRegistry(EventListenerRegistry registry) {
		this.registry = registry;
	}

	public void setContributedListeners(Map<String, EventListener> contributedListeners) {
		this.contributedListeners = contributedListeners;
	}

}
