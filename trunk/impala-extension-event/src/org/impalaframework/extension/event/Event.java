package org.impalaframework.extension.event;

import java.io.Serializable;
import java.util.Date;

import org.joda.time.DateTime;
import org.springframework.util.ObjectUtils;

/**
 * Represents a single event
 * @author Phil Zoio
 */
public class Event implements Serializable, Comparable<Event> {

    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for event
     */
    private String eventId;
    
    /**
     * The id of the event subject
     */
    private final String subjectId; 
    
    /**
     * The entity for which the event applies (e.g. Order, Product, etc.)
     */
    private final String entity;
    
    /**
     * The name or id of the user associated with the event
     */
    private final String user;
    
    /**
     * An identifier for the event
     */
    private final EventType eventType;

    /**
     * Event data which should not be persisted
     */
    private final Serializable transientData;
    
    /**
     * The persistent representation of the transient data.
     */
    private final String persistentData;

    /**
     * The time at which the event occurred
     */
    private final Date dateTime;

    /**
     * The expected process by date. Used for priority ordering
     */
    private final Date processedByDate;
    
    /**
     * Holds authentication data associated with the event
     */
    private final Object authenticationData;

    public Event(
            EventType eventType, 
            String user, 
            String subjectId,
            String entity,  
            Serializable currentData, 
            String persistentData, 
            Date date, 
            Object authenticationData) {
        super();
        this.user = user;
        this.subjectId = subjectId;
        this.entity = entity;
        this.eventType = eventType;
        this.transientData = currentData;
        this.persistentData = persistentData;
        this.dateTime = date;
        this.processedByDate = processedByDate(eventType, dateTime);
        this.authenticationData = authenticationData;
    }
    
    public Event(EventType eventType, String user, String subjectId, String entity) {
        this(eventType, user, subjectId, entity, null, null, new Date(), null);
    }

    public Event(EventType eventType, String user, String subjectId, String entity, Object authenticationData) {
        this(eventType, user, subjectId, entity, null, null, new Date(), authenticationData);
    }
    
    private Date processedByDate(EventType eventType, Date dateTime) {
        Date processedByDate = new DateTime(dateTime).plus(eventType.getTargetDelay()).toDate();
        return processedByDate;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getEntity() {
        return entity;
    }

    public String getUser() {
        return user;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Serializable getTransientData() {
        return transientData;
    }

    public String getPersistentData() {
        return persistentData;
    }

    public Date getDateTime() {
        return dateTime;
    }
    
    public Date getProcessedByDate() {
        return processedByDate;
    }

    public Object getAuthenticationData() {
		return authenticationData;
	}
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(500);
        buffer.append(this.getClass().getName()).append(": ");
        buffer.append("eventId = ");
        buffer.append(this.eventId);
        buffer.append(", ");
        buffer.append("subjectId = ");
        if (this.subjectId != null)
            buffer.append(this.subjectId.toString());
        else
            buffer.append("value is null");
        buffer.append(", ");
        buffer.append("entity = ");
        buffer.append(this.entity);
        buffer.append(", ");
        buffer.append("eventType = ");
        if (this.eventType != null)
            buffer.append(this.eventType.toString());
        else
            buffer.append("value is null");
        buffer.append(", ");
        buffer.append("transientData = ");
        if (this.transientData != null)
            buffer.append(ObjectUtils.identityToString(this.transientData));
        else
            buffer.append("value is null");
        buffer.append(", ");
        buffer.append("persistentData = ");
        buffer.append(ObjectUtils.identityToString(this.transientData));
        buffer.append(", ");
        buffer.append("dateTime = ");
        if (this.dateTime != null)
            buffer.append(this.dateTime.toString());
        else
            buffer.append("value is null");
        buffer.append("\n");
        return buffer.toString();
    }

    public int compareTo(Event o) {
        Date otherDate = o.processedByDate;
        return (int) (this.processedByDate.getTime() - otherDate.getTime());
    }
    
    
}
