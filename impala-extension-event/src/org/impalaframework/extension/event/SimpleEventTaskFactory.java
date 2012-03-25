package org.impalaframework.extension.event;

/**
 * Implementation of {@link EventTaskFactory} which simply returns an {@link EventTask}
 * instance.
 * @author Phil Zoio
 */
public class SimpleEventTaskFactory implements EventTaskFactory {

    public SimpleEventTaskFactory() {
        super();
    }

    public EventTask newEventTask(Event event, EventListener eventListener) {
        return new EventTask(event, eventListener);
    }

}
