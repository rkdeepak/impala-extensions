package org.impalaframework.extension.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

public class SimpleEventSynchronizer implements EventSynchronizer {

	private static final ThreadLocal<List<Event>> threadLocalActive = new ThreadLocal<List<Event>>();

	private static final Map<Event, Boolean> globalActive = Collections.synchronizedMap(new IdentityHashMap<Event, Boolean>());

	public void activate(Event event) {

		if (isTransactionActive()) {

			doActivate(event);

		}
	}

	void doActivate(Event event) {
		Assert.notNull(event);
		List<Event> list = threadLocalActive.get();
		if (list == null) {
			list = new ArrayList<Event>();
			threadLocalActive.set(list);
		}
		list.add(event);
		globalActive.put(event, Boolean.TRUE);
	}

	public boolean deactivateThreadEvents() {
		boolean removed = false;
		List<Event> list = threadLocalActive.get();
		if (list != null) {
			for (Event event : list) {
				globalActive.remove(event);
				removed = true;
			}
		}
		threadLocalActive.remove();
		return removed;
	}

	public boolean isEventActive(Event event) {
		Assert.notNull(event);
		return globalActive.containsKey(event);
	}

	boolean isTransactionActive() {
		return TransactionSynchronizationManager.isActualTransactionActive();
	}

}
