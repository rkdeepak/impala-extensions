package org.impalaframework.extension.event;

import java.util.ArrayList;
import java.util.List;

public class TestEventListener implements EventListener {

	private List<Event> eventList = new ArrayList<Event>();
	
	private boolean markProcessed;
	
	public synchronized void onEvent(Event event) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		eventList.add(event);
	}

	public List<Event> getEventList() {
		return eventList;
	}

	public boolean getMarkProcessed() {
		return markProcessed;
	}

	public String getConsumerName() {
		return "test";
	}
	
	public void setMarkProcessed(boolean markProcessed) {
		this.markProcessed = markProcessed;
	}

}
