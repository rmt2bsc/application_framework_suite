package com.ui.event;

import java.util.EventListener;

/**
 * Listener for notifying the user that a component's update process has
 * completed.
 * 
 * @author rterrell
 *
 */
public interface ComponentUpdateCompletedListener extends EventListener {

    /**
     * Implement this method to capture and handle the notification that a
     * component's update process has completed.
     * 
     * @param evt
     *            an instance of {@link ComponentUpdateCompletedEvent}
     */
    void handleUpdateCompletedNotification(ComponentUpdateCompletedEvent evt);

}
