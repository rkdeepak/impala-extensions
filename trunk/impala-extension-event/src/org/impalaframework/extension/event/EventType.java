package org.impalaframework.extension.event;

import org.joda.time.Period;

/**
 * Represents the type of an {@link Event}, against which listeners will be
 * registered
 * 
 * @author Phil Zoio
 */
public class EventType {
    
    private static final Period defaultTargetDelay = new Period().withMinutes(1);

    /** 
     * The type of the event, which listeners will need to know about
     */
    final private String type;

    /**
     * A flag indicating that the event should be persisted
     */
    final private boolean persistent;
    
    /**
     * Applies for asynchronous events, as synchronous events are automatically transactional.
     * If transactional, and event is fired in the context of a transaction, event will only be fired 
     * on successful completion of transaction
     */
    final private boolean transactional;

    /**
     * The delay that ordinarily should not be exceeded before the event is processed
     */
    final private Period targetDelay;

    /**
     *  A flag indicating that this event should be added to the in-process event handling queue
     */
    final private boolean handleInProcess;

    public EventType(String type) {
        super();

        this.type = type;
        this.persistent = true;
        this.handleInProcess = true;
        this.transactional = false;
        this.targetDelay = defaultTargetDelay;

    }

    public EventType(String type, boolean persist, boolean handleInProcess, boolean transactional, Period targetDelay) {
        super();

        if (!persist && !handleInProcess) {
            throw new IllegalArgumentException(
                    "Either the persist flag or the handleInProcess flag should be marked as true");
        }

        this.type = type;
        this.persistent = persist;
        this.handleInProcess = handleInProcess;
        this.transactional = transactional;

        if (targetDelay != null) {
            this.targetDelay = targetDelay;
        }
        else {
            this.targetDelay = defaultTargetDelay;
        }

    }
    
    public boolean isTransactional() {
		return transactional;
	}

    public boolean isHandleInProcess() {
        return handleInProcess;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public Period getTargetDelay() {
        return targetDelay;
    }

    public String getType() {
        return type;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(500);
        buffer.append(this.getClass().getName()).append(": ");
        buffer.append("type = ");
        buffer.append(this.type);
        buffer.append(", ");
        buffer.append("persistent = ");
        buffer.append(this.persistent);
        buffer.append(", ");
        buffer.append("targetDelay = ");
        if (this.targetDelay != null)
            buffer.append(this.targetDelay.toString());
        else
            buffer.append("value is null");
        buffer.append(", ");
        buffer.append("handleInProcess = ");
        buffer.append(this.handleInProcess);
        buffer.append("\n");
        return buffer.toString();

    }
}
