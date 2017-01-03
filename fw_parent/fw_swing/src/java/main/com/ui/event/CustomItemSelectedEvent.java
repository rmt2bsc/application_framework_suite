package com.ui.event;

/**
 * An event notification source for detecting when an individual row is selected
 * on a given component by the user.
 * 
 * @author rterrell
 *
 */
public class CustomItemSelectedEvent extends CustomSelectionEvent {

    private static final long serialVersionUID = -4118435768509924170L;

    /**
     * Create a CustomItemSelectedEvent initialized with the source of the
     * event, the item selected in the grid, and the event id.
     * 
     * @param source
     *            the object that triggered the event
     * @param selectedItem
     *            an object corresponding to the table model item currently
     *            selected.
     * @param eventId
     *            the id of the event.
     */
    public CustomItemSelectedEvent(Object source, Object selectedItem,
            int eventId) {
        super(source, selectedItem, eventId);
    }

}
