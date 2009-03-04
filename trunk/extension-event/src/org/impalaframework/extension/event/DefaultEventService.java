package org.impalaframework.extension.event;

import org.springframework.util.Assert;


public class DefaultEventService implements EventService {
	
	private EventDAO eventDAO;
	private EventService asynchronousEventService;
	private EventService synchronousEventService;

	public void submitEvent(Event event) {
		
		final EventType eventType = event.getEventType();
		
		if (eventType.isPersistent()) {
			Assert.notNull(eventDAO, "Cannot use persistent events if no " + EventDAO.class.getSimpleName() +
					" has been wired in to " + DefaultEventService.class.getName());
			eventDAO.insertEvent(event);
		}
		
		if (eventType.isHandleInProcess()) {
			//transactions will fail if propagated at this point
			synchronousEventService.submitEvent(event);
			
			//any processing here will execute in separate transaction
			asynchronousEventService.submitEvent(event);
		}
	}
	
	/* *************** wired in setters **************** */

	public void setEventDAO(EventDAO eventDAO) {
		this.eventDAO = eventDAO;
	}

	public void setAsynchronousEventService(EventService asynchronousEventService) {
		this.asynchronousEventService = asynchronousEventService;
	}

	public void setSynchronousEventService(EventService synchronousEventService) {
		this.synchronousEventService = synchronousEventService;
	}

}
