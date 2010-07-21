package org.impalaframework.extension.event;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

/**
 * Implementation of {@link EventService} which is backed by a {@link PriorityBlockingQueue} queue.
 * @author Phil Zoio
 */
public class AsynchronousEventService implements EventService, InitializingBean, DisposableBean {

	private static final Log logger = LogFactory.getLog(AsynchronousEventService.class);

	private PriorityBlockingQueue<Object> priorityEventQueue = new PriorityBlockingQueue<Object>();

	private ScheduledExecutorService queueExecutorService;

	private EventListenerRegistry eventListenerRegistry;
	
	private EventTaskFactory eventTaskFactory;
	
	private EventSynchronizer eventSynchronizer;
	
	private static final int DEFAULT_POLL_INTERVAL = 1000;
	
	private static final int DEFAULT_DELAY = 1000;

	private Integer delayInMilliseconds;
	
	private Integer pollIntervalInMilliseconds;

	private AtomicBoolean started = new AtomicBoolean(false);

	/* ******************** EventService interface method ****************** */

	public void submitEvent(Event event) {
		
		if (!started.get()) {
			throw new IllegalStateException("Cannot accept events as event manager has stopped");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Adding event " + event + " to the event queue");
		}

		try {
			priorityEventQueue.offer(event);
		} catch (ClassCastException e) {
			//got this in here because of peculiar bug on Mac OSX JVM which appears to have
			//no functional impact other than throwing ClassCastException
			logger.error("Unexpected class cast exception.", e);
		}
	}

	/* ******************** Life cycle methods ****************** */
	
	public void start() {

		Assert.notNull(eventListenerRegistry, "eventListenerRegistry cannot be null");
		Assert.notNull(eventTaskFactory, "eventTaskFactory cannot be null");
		Assert.notNull(eventSynchronizer, "eventSynchronizer cannot be null");
		
		if (pollIntervalInMilliseconds == null) {
			pollIntervalInMilliseconds = DEFAULT_POLL_INTERVAL;
		}
		
		if (delayInMilliseconds == null) {
			delayInMilliseconds = DEFAULT_DELAY;
		}
		
		logger.info("Starting event manager");

		queueExecutorService = Executors.newSingleThreadScheduledExecutor();
		started.set(true);

		queueExecutorService.scheduleAtFixedRate(new Runnable() {
			public void run() {
				consumeQueuedEvent();
			}
		},
		delayInMilliseconds,
		pollIntervalInMilliseconds,
		TimeUnit.MILLISECONDS);

		logger.info("Finished starting event manager");
	}

	public void stop() {

		logger.info("Stopping event manager");

		started.set(false);
		if (!queueExecutorService.isShutdown()) {
			queueExecutorService.shutdown();
		}

		//remove events
		priorityEventQueue.clear();
		
		//remove listeners
		eventListenerRegistry.clear();

		logger.info("Finished stopping event manager");
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

			Event event = (Event) priorityEventQueue.peek();

			final boolean debug = logger.isDebugEnabled();
			
			if (event != null) {

				if (debug) {
					logger.debug("Removing event " + event + " from the event queue");
				}
				
				final Date processedByDate = event.getProcessedByDate();
				if (processedByDate.getTime() <= System.currentTimeMillis()) {

					if (debug) {
						logger.debug("Processing event: " + event);
					}
					
					priorityEventQueue.remove(event);
					processQueuedEvent(event);
				} else {
					if (debug) {
						System.out.println("Not processing event: still waiting as process by date is still in the future " + processedByDate);
					}
				}
			} else {

				if (debug) {
					logger.debug("No event found on queue");
				}
			}
		}
		catch (Exception e) {
			logger.error("Error processing queued event", e);
		}
	}

	private void processQueuedEvent(Event event) {
		
		String type = event.getEventType().getType();		
		List<EventListener> list = eventListenerRegistry.getEventListeners(type);

		boolean transactionActive = isTransactionActive();
		
		if (logger.isDebugEnabled()) {
			logger.debug("Registered asynchronous listeners registered for type " + type + ": " + list);
			logger.debug("Transaction active " + transactionActive);
		}
		
		for (EventListener eventListener : list) {			
			final String consumerName = eventListener.getConsumerName();
			try {

				EventTask eventTask = newEventTask(event, eventListener);
				
				if (logger.isDebugEnabled()){
					logger.debug("Processing queue for listener '" + consumerName + "' for event: " + event);
				}

				if (transactionActive) {
					//transaction active so need to wait for eventSynchronizer to handle this
					eventSynchronizer.awaitTransactionCompletion(eventTask);
				}
				else {
					eventSynchronizer.submitTask(eventTask);
				}				
			} catch (Exception e) {
				try {
					onEventError(event, eventListener, e);
				} catch (Exception ee) {
					logger.error("Event error logging failed: " + ee, ee);
					logger.error("Original error: " + e.getMessage(), e);		
				}
			}
		}
	}

	/**
	 * Logs an event error. Subclasses can override this to handle special processing
	 */
	protected void onEventError(Event event, EventListener eventListener, Exception e) {
		logger.error("Failed to successfully perform event processing using listener: " + eventListener.getConsumerName(), e);
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

	public void setPollIntervalInMilliseconds(Integer pollIntervalInMilliseconds) {
		this.pollIntervalInMilliseconds = pollIntervalInMilliseconds;
	}
	
	public void setDelayInMilliseconds(Integer delayInMilliseconds) {
		this.delayInMilliseconds = delayInMilliseconds;
	}

}
