package org.impalaframework.extension.event;


public interface EventSynchronizer {
	
	void awaitTransactionCompletion(EventTask Event);
	void submitTask(EventTask eventTask);
	
}