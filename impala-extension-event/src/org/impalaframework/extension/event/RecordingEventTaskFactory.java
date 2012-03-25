package org.impalaframework.extension.event;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.Assert;

/**
 * {@link EventTaskFactory} which generates instances of {@link RecordingEventTask}
 * @author Phil Zoio
 */
public class RecordingEventTaskFactory implements EventTaskFactory {
    
    private PlatformTransactionManager transactionManager;
    
    private EventDAO eventDAO;

    public RecordingEventTaskFactory() {
        super();
    }

    public RecordingEventTaskFactory(PlatformTransactionManager transactionManager, EventDAO eventDAO) {
        super();
        Assert.notNull(transactionManager);
        Assert.notNull(eventDAO);
        this.transactionManager = transactionManager;
        this.eventDAO = eventDAO;
    }
    
    public EventTask newEventTask(Event Event, EventListener eventListener) {
        return new RecordingEventTask(transactionManager, eventDAO, Event, eventListener);
    }
    
    /* *************************** Protected methods for use by subclasses ******************** */
    
    protected EventDAO getEventDAO() {
        return eventDAO;
    }
    
    protected PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    /* *************************** Wired in setters ******************** */
    
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setEventDAO(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }
    
    
}
