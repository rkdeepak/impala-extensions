package org.impalaframework.extension.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Extension of {@link AsynchronousEventService} which is transaction aware.
 * @author Phil Zoio
 */
public class TransactionalAsynchronousEventService extends AsynchronousEventService {
	
	private static final Log logger = LogFactory.getLog(TransactionalAsynchronousEventService.class);
	
	//FIXME write unit tests for this.
	
	@Override
	protected void doSubmitEvent(Event event) {
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			addEventToQueue(event);
		} else {
			TransactionSynchronizationManager.registerSynchronization(new EventTransactionSynchronization(event));
		}
	}
	
	class EventTransactionSynchronization extends TransactionSynchronizationAdapter {
		
		private Event event;

		public EventTransactionSynchronization(Event event) {
			super();
			this.event = event;
		}

		@Override
		public void afterCommit() {
			if (logger.isDebugEnabled()) {
				logger.debug("Transaction committed: adding event to queue: " + event);
			}
			TransactionalAsynchronousEventService.this.addEventToQueue(event);
		}
		
		@Override
		public void afterCompletion(int status) {			
			if (logger.isDebugEnabled()) {
				logger.debug("Transaction event synchronization completed with status: " + status);
			}
		}
	}

}
