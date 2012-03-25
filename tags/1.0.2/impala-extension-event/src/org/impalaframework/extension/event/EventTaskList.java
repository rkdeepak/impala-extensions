package org.impalaframework.extension.event;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

/**
 * Holds collection of event tasks to be executed 
 * @author Phil Zoio
 */
public class EventTaskList implements Runnable {

    private static final Log logger = LogFactory.getLog(AsynchronousEventService.class);

    private final List<EventTask> eventTasks;

    public EventTaskList(List<EventTask> eventTasks) {
        super();
        Assert.notNull(eventTasks);
        this.eventTasks = eventTasks;
    }

    public void run() {
        
        for (EventTask eventTask : eventTasks) {

            try {

                if (logger.isDebugEnabled()) {
                    logger.debug("Processing event task: " + eventTask);
                }

                eventTask.run();
            } catch (Exception e) {
                try {
                    onEventError(eventTask, e);
                } catch (Exception ee) {
                    logger.error("Event error logging failed: " + ee, ee);
                    logger.error("Original error: " + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Logs an event error. Subclasses can override this to handle special
     * processing
     */
    protected void onEventError(EventTask eventTask, Exception e) {
        logger.error(
                "Failed to successfully perform event processing using listener: "
                        + eventTask, e);
    }

}
