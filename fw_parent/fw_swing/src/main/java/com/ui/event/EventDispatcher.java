package com.ui.event;

import javax.swing.event.EventListenerList;

/**
 * An interface for firing events for any component capable of managing the
 * custom event, {@link BaseEvent}, or any of its descendents.
 * 
 * @author rterrell
 *
 */
public interface EventDispatcher {

    /**
     * Assigns a List of event listeners.
     * 
     * @param listeners
     *            an instance of {@link EventListenerList}
     */
    void setListenerList(EventListenerList listeners);

    /**
     * Notifes all listeners that have registerd interests for events of type
     * {@link BaseEvent}.
     * 
     * @param evt
     *            an instance of {@link BaseEvent}
     */
    void fireEvent(BaseEvent evt);

}
