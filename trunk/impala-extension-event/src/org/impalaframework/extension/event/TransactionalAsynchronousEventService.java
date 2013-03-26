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
    
    /**
     * Submits event immediately if event is not transactional or event transaction is not active
     */
    @Override
    protected void doSubmitEvent(Event event) {
    	
    	boolean waitingForTransaction = waitingForTransaction(event);
		
		if (!waitingForTransaction) {
            this.addEventToQueue(event);
        } else {
            TransactionSynchronizationManager.registerSynchronization(new EventTransactionSynchronization(event));
        }
    }

    /**
     * Returns true if synchronization is active and event type is transactional
     * @see #synchronizationActive()
     * @see #eventIsTransactional(Event)
     */
	boolean waitingForTransaction(Event event) {
		boolean transactional = eventIsTransactional(event);
        boolean waitingForTransaction = transactional && synchronizationActive();
		return waitingForTransaction;
	}
	
	/**
	 * Returns true if event's type is transactional
	 */
	boolean eventIsTransactional(Event event) {
		EventType eventType = event.getEventType();
		boolean transactional = eventType.isTransactional();
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Event '%s' is transactional: %s", eventType, transactional));
		}
		return transactional;
	}

	/**
	 * Returns true if transaction synchronization is active
	 * @see TransactionSynchronizationManager#isSynchronizationActive()
	 */
	boolean synchronizationActive() {
		boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
		if (logger.isDebugEnabled()) {
			logger.debug("Synchronization active: " + synchronizationActive);
		}
		return synchronizationActive;
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
