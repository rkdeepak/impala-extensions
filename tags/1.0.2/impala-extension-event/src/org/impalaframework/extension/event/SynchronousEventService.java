package org.impalaframework.extension.event;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

/**
 * Implementation of {@link EventService} which synchronously calls through to 
 * registered event listeners, passing through the {@link Event} instance to each.
 * Exceptions are not caught: instead, they are propagated to the original caller.
 * @author Phil Zoio
 */
public class SynchronousEventService implements EventService {

    private static final Log logger = LogFactory.getLog(AsynchronousEventService.class);

    private EventListenerRegistry eventListenerRegistry;
    
    public void submitEvent(Event event) {
        
        Assert.notNull(eventListenerRegistry);
        
        EventType eventType = event.getEventType();
        String type = eventType.getType();
        List<EventListener> list = eventListenerRegistry.getEventListeners(type);
        
        if (logger.isDebugEnabled()) {
            logger.debug("Registered synchronous listeners registered for type " + type + ": " + list);
        }

        if (list != null) {
            for (EventListener eventListener : list) {
                
                if (logger.isDebugEnabled()) {
                    logger.debug("Executing onEvent for listener: " + eventListener);
                }
                
                //note: exceptions not caught as they are propagated to original caller
                //can do this because this is synchronous
                eventListener.onEvent(event);
            }
        }
    }

    /* ********************* wired in setters ******************* */

    public void setEventListenerRegistry(EventListenerRegistry eventListenerRegistry) {
        this.eventListenerRegistry = eventListenerRegistry;
    }   
    
}
