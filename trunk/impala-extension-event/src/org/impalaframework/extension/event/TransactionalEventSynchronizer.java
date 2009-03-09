package org.impalaframework.extension.event;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.Lifecycle;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

/**
 * Implementation of {@link EventSynchronizer}, designed to be used with
 * persistent asynchronous events. Backed by an instance of {@link ExecutorService}.
 * {@link #awaitTransactionCompletion(EventTask)} method implementation which
 * which only passes {@link EventTask}s to if and when transaction successfully
 * completes.
 * 
 * @author Phil Zoio
 */
public class TransactionalEventSynchronizer implements EventSynchronizer, Lifecycle, InitializingBean, DisposableBean {

	private static final ThreadLocal<EventTransactionalSynchronization> threadLocalSynchronization = new ThreadLocal<EventTransactionalSynchronization>();

	private ExecutorService taskExecutorService;

	/* **************************  Life cycle methods ****************************** */
	/**
	 * Implements start of life cycle by instantiating new {@link ExecutorService}
	 */
	public void start() {
		taskExecutorService = Executors.newCachedThreadPool();
	}

	/**
	 * Implements end of life cycle by shutting down {@link ExecutorService}
	 */
	public void stop() {
		if (isRunning()) {
			taskExecutorService.shutdown();
		}
	}

	/**
	 * Returns true if {@link ExecutorService} is running and is not shutdown
	 * @see ExecutorService#isShutdown()
	 */
	public boolean isRunning() {
		return taskExecutorService != null && !taskExecutorService.isShutdown();
	}

	/* ************************** Initializing and Disposable bean methods ****************************** */
	
	public void afterPropertiesSet() throws Exception {
		start();
	}

	public void destroy() throws Exception {
		stop();
	}

	/* **************************  public methods ****************************** */
	
	/**
	 * Clears the current threads {@link TransactionSynchronization} instance, used to associate events with a
	 * transaction awaiting completion. Called by {@link TransactionSynchronization} as part of after completion processing.
	 * @see TransactionSynchronization#afterCompletion(int)
	 */
	public void clear() {
		threadLocalSynchronization.set(null);
	}

	/**
	 * Passes {@link EventTask} to synchronizer, during which it gets associated with a {@link TransactionSynchronization} 
	 * instance associated with the current thread's transaction. The {@link EventTask} will only be submitted to the 
	 * {@link ExecutorService} if and after the transaction successfully commits.
	 */
	public void awaitTransactionCompletion(EventTask task) {
		EventTransactionalSynchronization eventTransactionSynchronization = getTransactionSynchronization();
		eventTransactionSynchronization.addEventTask(task);
	}

	/**
	 * Passes task directoy to {@link ExecutorService}. Appropriate to call this only when there is no transaction 
	 * associated with the current thread
	 */
	public void submitTask(EventTask eventTask) {
		taskExecutorService.submit(eventTask);
	}

	/* **************************  protected methods ****************************** */

	/**
	 * Implements logic to submit {@link EventTask} instances to {@link ExecutorService}
	 */
	protected final void submitTasks(Set<EventTask> eventsAsKeys,
			boolean committed) {
		if (committed) {
			for (EventTask eventTask : eventsAsKeys) {
				submitTask(eventTask);
			}
		} else {
			// log fact that task cannot be completed
		}
	}

	/* **************************  protected methods ****************************** */

	EventTransactionalSynchronization getTransactionSynchronization() {
		EventTransactionalSynchronization eventTransactionSynchronization = threadLocalSynchronization
				.get();

		if (eventTransactionSynchronization != null) {
			List<TransactionSynchronization> synchronizations = getSynchronization();

			// make sure that synchronization is present
			Assert.isTrue(synchronizations
					.contains(eventTransactionSynchronization));

		} else {
			eventTransactionSynchronization = new EventTransactionalSynchronization(
					this);
			registerSynchronization(eventTransactionSynchronization);

			threadLocalSynchronization.set(eventTransactionSynchronization);
		}
		return eventTransactionSynchronization;
	}

	boolean hasTransactionalSynchronization() {
		return threadLocalSynchronization.get() != null;
	}

	/* *********************** Methods which interact with TransactionSynchronizationManager ******************** */

	void registerSynchronization(
			EventTransactionalSynchronization eventTransactionSynchronization) {
		// register synchronization with transaction to receive events when
		// transaction commits
		TransactionSynchronizationManager
				.registerSynchronization(eventTransactionSynchronization);
	}

	@SuppressWarnings("unchecked")
	List<TransactionSynchronization> getSynchronization() {
		List<TransactionSynchronization> synchronizations = TransactionSynchronizationManager.getSynchronizations();
		return synchronizations;
	}
	
	/**
	 * Internal class - implementation of {@link TransactionSynchronization}
	 * which allows {@link EventTask}s to be associated with the current
	 * transaction, and uses the {@link #afterCompletion(int)} callback allow
	 * these to be submitted to an {@link ExecutorService} for execution.
	 * 
	 * @author Phil Zoio
	 */
	static class EventTransactionalSynchronization implements TransactionSynchronization {

		private IdentityHashMap<EventTask, Boolean> events = new IdentityHashMap<EventTask, Boolean>();

		private final TransactionalEventSynchronizer synchronizer;

		public EventTransactionalSynchronization(TransactionalEventSynchronizer synchronizer) {
			super();
			this.synchronizer = synchronizer;
		}

		/* ***************** afterCompletion implementaion **************** */
		
		public void afterCompletion(int status) {

			// if transaction successfully committed then submit tasks to event
			// synchronizer
			boolean committed = (status == TransactionSynchronization.STATUS_COMMITTED);
			Set<EventTask> eventsAsKeys = events.keySet();
			synchronizer.submitTasks(eventsAsKeys, committed);
			synchronizer.clear();
		}

		/* ***************** no-op TransactionSynchronization methods *************** */
		
		public void afterCommit() {
		}

		public void beforeCommit(boolean readOnly) {
		}

		public void beforeCompletion() {
		}

		public void resume() {
		}

		public void suspend() {
		}
		
		/**
		 * Allows {@link EventTask}s to be associated with this {@link TransactionSynchronization} instance
		 */
		void addEventTask(EventTask event) {
			events.put(event, true);
		}

	}

}
