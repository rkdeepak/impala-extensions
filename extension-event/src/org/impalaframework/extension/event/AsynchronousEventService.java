package org.impalaframework.extension.event;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

public class AsynchronousEventService implements EventService, InitializingBean, DisposableBean {

	private final Log log = LogFactory.getLog(AsynchronousEventService.class);

	private PriorityBlockingQueue<Object> priorityEventQueue = new PriorityBlockingQueue<Object>();

	private ExecutorService queueExecutorService;

	private ExecutorService taskExecutorService;

	private EventListenerRegistry eventListenerRegistry;
	
	private EventTaskFactory eventTaskFactory;
	
	private EventSynchronizer eventSynchronizer;

	private AtomicBoolean started = new AtomicBoolean(false);

	/* ******************** EventService interface method ****************** */

	public void submitEvent(Event event) {
		if (!started.get()) {
			throw new IllegalStateException("Cannot accept events as event manager has stopped");
		}

		if (log.isDebugEnabled()) {
			log.debug("Adding event " + event + " to the event queue");
		}

		try {
			priorityEventQueue.offer(event);
		} catch (ClassCastException e) {
			//got this in here because of peculiar bug on Mac OSX JVM which appears to have
			//no functional impact other than throwing ClassCastException
			e.printStackTrace();
		}
		
		eventSynchronizer.activate(event);
	}

	/* ******************** Life cycle methods ****************** */
	
	public void start() {

		Assert.notNull(eventListenerRegistry, "eventListenerRegistry cannot be null");
		
		log.info("Starting event manager");

		queueExecutorService = Executors.newSingleThreadExecutor();
		taskExecutorService = Executors.newCachedThreadPool();
		started.set(true);

		queueExecutorService.execute(new Runnable() {
			public void run() {
				while (started.get()) {
					consumeEvent();
				}
			}
		});

		log.info("Finished starting event manager");
	}

	public void stop() {

		log.info("Stopping event manager");

		started.set(false);
		if (!queueExecutorService.isShutdown()) {
			queueExecutorService.shutdown();
		}
		if (!taskExecutorService.isShutdown()) {
			taskExecutorService.shutdown();
		}

		//remove events
		priorityEventQueue.clear();
		
		//remove listeners
		eventListenerRegistry.clear();

		log.info("Finished stopping event manager");
	}

	/* ******************** Spring life cycle methods methods ****************** */

	public void afterPropertiesSet() throws Exception {
		start();
	}

	public void destroy() throws Exception {
		stop();
	}
	
	/* ******************** private implementation methods ****************** */

	protected void consumeEvent() {

		try {

			// try every second to get an event
			// we do this because we don't want this to hang
			// indefinitely
			Event Event = (Event) priorityEventQueue.poll(1, TimeUnit.SECONDS);

			if (Event != null) {

				if (log.isDebugEnabled()) {
					log.debug("Removing event " + Event + " from the event queue");
				}

				processEvent(Event);
			}
		}
		catch (Exception e) {

		}

	}

	private void processEvent(Event event) {
		String type = event.getEventType().getType();
		List<EventListener> list = eventListenerRegistry.getEventListeners(type);

		if (list != null) {
			for (EventListener eventListener : list) {
				EventTask eventTask = newEventTask(event, eventListener);
				taskExecutorService.submit(eventTask);
			}
		}
	}

	protected EventTask newEventTask(Event Event, EventListener eventListener) {
		Assert.notNull(eventTaskFactory);
		EventTask eventTask = eventTaskFactory.newEventTask(Event, eventListener);
		return eventTask;
	}

	boolean isTransactionActive() {
		return TransactionSynchronizationManager.isActualTransactionActive();
	}

	/* ******************** state getter methods ****************** */

	public int getEventQueueSize() {
		return priorityEventQueue.size();
	}
	
	public boolean isActive() {
		return started.get() && !taskExecutorService.isShutdown() && !queueExecutorService.isShutdown();
	}

	public boolean isStopped() {
		return !started.get() && taskExecutorService.isShutdown() && queueExecutorService.isShutdown();
	}
	
	/* ******************** injected setters ****************** */

	public void setEventTaskFactory(EventTaskFactory eventTaskFactory) {
		this.eventTaskFactory = eventTaskFactory;
	}

	public void setEventSynchronizer(EventSynchronizer eventSynchronizer) {
		this.eventSynchronizer = eventSynchronizer;
	}

	public void setEventListenerRegistry(EventListenerRegistry eventListenerRegistry) {
		this.eventListenerRegistry = eventListenerRegistry;
	}

}
