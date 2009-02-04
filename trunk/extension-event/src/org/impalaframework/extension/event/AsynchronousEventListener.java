package org.impalaframework.extension.event;

public interface AsynchronousEventListener extends EventListener {
	boolean awaitTransactionCompletion();
}
