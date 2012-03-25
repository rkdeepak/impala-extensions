package org.impalaframework.extension.event;

/**
 * Extends {@link EventListener} with method which is used to indicate
 * whether asynchronous events should await completion of the transaction associated with the original event.
 * @author Phil Zoio
 */
public interface AsynchronousEventListener extends EventListener {
    boolean awaitTransactionCompletion();
}
