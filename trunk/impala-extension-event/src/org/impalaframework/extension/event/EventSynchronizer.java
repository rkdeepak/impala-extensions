package org.impalaframework.extension.event;

/**
 * 
 * @author Phil Zoio
 */
public interface EventSynchronizer {
	
	void awaitTransactionCompletion(EventTask Event);
	void submitTask(EventTask eventTask);
	
}