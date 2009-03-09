package org.impalaframework.extension.event;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import junit.framework.TestCase;

import org.joda.time.Period;

public class DefaultEventServiceTest extends TestCase {

	private DefaultEventService service;

	private EventDAO eventDAO;

	private EventService asynchEventService;
	private EventService synchEventService;

	public void setUp() {
		service = new DefaultEventService();
		eventDAO = createMock(EventDAO.class);
		asynchEventService = createMock(EventService.class);
		synchEventService = createMock(EventService.class);
		
		service.setEventDAO(eventDAO);
		service.setAsynchronousEventService(asynchEventService);
		service.setSynchronousEventService(synchEventService);
	}
	
	
	public void testPersist() {
		
		boolean persist = true;
		boolean inprocess = false;
		
		EventType eventType = new EventType("mytype", persist, inprocess, new Period().withSeconds(10));
		Event Event = new Event(eventType, "sub1", "1", "type");

		//expectations
		eventDAO.insertEvent(Event);
		
		replay(synchEventService);
		replay(eventDAO);
		replay(asynchEventService);
	
		service.submitEvent(Event);

		verify(synchEventService);
		verify(eventDAO);
		verify(asynchEventService);
	}
	
	public void testInProcess() {
		
		boolean persist = false;
		boolean inprocess = true;
		
		EventType eventType = new EventType("mytype", persist, inprocess, new Period().withSeconds(10));
		Event Event = new Event(eventType, "sub1", "1", "type");

		//expectations
		synchEventService.submitEvent(Event);
		asynchEventService.submitEvent(Event);

		replay(synchEventService);
		replay(eventDAO);
		replay(asynchEventService);
	
		service.submitEvent(Event);

		verify(synchEventService);
		verify(eventDAO);
		verify(asynchEventService);
	}
	
	public void testBoth() {
		
		boolean persist = true;
		boolean inprocess = true;
		
		EventType eventType = new EventType("mytype", persist, inprocess, new Period().withSeconds(10));
		Event Event = new Event(eventType, "sub1", "1", "type");

		//expectations
		eventDAO.insertEvent(Event);
		synchEventService.submitEvent(Event);
		asynchEventService.submitEvent(Event);

		replay(synchEventService);
		replay(eventDAO);
		replay(asynchEventService);
	
		service.submitEvent(Event);

		verify(synchEventService);
		verify(eventDAO);
		verify(asynchEventService);
	}

}
