package org.impalaframework.extension.event;



public interface EventTaskFactory {

	EventTask newEventTask(Event Event, EventListener eventListener);

}