package com.ui.event;

import java.util.EventObject;

/**
 * Common event class for for the commons api.
 * 
 * @author rterrell
 *
 */
public class BaseEvent extends EventObject {

    private static final long serialVersionUID = -4165411417804173453L;

    private int eventId;

    /**
     * Create a BaseEvent initialized with the source of the event.
     * 
     * @param source
     *            the object that triggered the event
     */
    public BaseEvent(Object source) {
        super(source);
    }

    /**
     * Create a BaseEvent initialized with the source of the event, the item
     * selected in the grid, and the event id.
     * 
     * @param source
     *            the object that triggered the event
     * @param eventId
     *            the id of the event.
     */
    public BaseEvent(Object source, int eventId) {
        this(source);
        this.eventId = eventId;
    }

    /**
     * Return the event id.
     * 
     * @return the eventId
     */
    public int getEventId() {
        return eventId;
    }

}
