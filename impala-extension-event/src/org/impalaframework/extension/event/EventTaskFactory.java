package org.impalaframework.extension.event;

/**
 * Defines mechanism for creating an {@link EventTask} instance.
 * @author Phil Zoio
 */
public interface EventTaskFactory {

    EventTask newEventTask(Event event, EventListener eventListener);

}
