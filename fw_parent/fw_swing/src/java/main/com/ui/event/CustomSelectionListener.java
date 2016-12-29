package com.ui.event;

import java.util.EventListener;

/**
 * Listener for handling events pertaining to selecting an item from a
 * component.
 * 
 * @author rterrell
 *
 */
public interface CustomSelectionListener extends EventListener {

    /**
     * Implement this method to capture and process a row that has been double
     * clicked by the user.
     * 
     * @param evt
     *            an instance of {@link CustomItemDoubleClickedEvent}
     */
    void handleDoubleClickedRow(CustomItemDoubleClickedEvent evt);

    /**
     * Implement this method to capture and process the row that is the
     * recipient of a selection change in the data grid.
     * 
     * @param evt
     *            an instance of {@link CustomItemSelectedEvent}
     */
    void handleSelectionChanged(CustomItemSelectedEvent evt);

}
