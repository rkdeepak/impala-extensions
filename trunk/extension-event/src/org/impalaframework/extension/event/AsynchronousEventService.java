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
	}

	/* ******************** Life cycle methods ****************** */
	
	public void start() {

		Assert.notNull(eventListenerRegistry, "eventListenerRegistry cannot be null");
		
		log.info("Starting event manager");

		queueExecutorService = Executors.newSingleThreadExecutor();
		started.set(true);

		queueExecutorService.execute(new Runnable() {
			public void run() {
				while (started.get()) {
					consumeQueuedEvent();
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

	/**
	 * Consume queued events
	 */
	protected void consumeQueuedEvent() {

		try {

			// try every second to get an event
			// we do this because we don't want this to hang
			// indefinitely
			Event event = (Event) priorityEventQueue.poll(1, TimeUnit.SECONDS);
			
			System.out.println("Pulled event off queue: " + event);

			if (event != null) {

				if (log.isDebugEnabled()) {
					log.debug("Removing event " + event + " from the event queue");
				}

				processQueuedEvent(event);
			}
		}
		catch (Exception e) {

		}

	}

	private void processQueuedEvent(Event event) {
		String type = event.getEventType().getType();
		System.out.println("Event type: " + type);
		
		List<EventListener> list = eventListenerRegistry.getEventListeners(type);

		boolean transactionActive = isTransactionActive();
		
		for (EventListener eventListener : list) {
			EventTask eventTask = newEventTask(event, eventListener);

			System.out.println("Processing queue for listener " + eventListener.getConsumerName() + " for event: " + event);
			
			try {
				if (transactionActive) {
					//transaction active so need to wait for eventSynchronizer to handle this
					eventSynchronizer.awaitTransactionCompletion(eventTask);
				}
				else {
					eventSynchronizer.submitTask(eventTask);
				}				
			} catch (Exception e) {
				//FIXME do something about this
				e.printStackTrace();
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
		return started.get() && !queueExecutorService.isShutdown();
	}

	public boolean isStopped() {
		return !started.get() && queueExecutorService.isShutdown();
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
