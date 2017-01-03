package com.ui.event;

/**
 * An event notification source for detecting when an individual item has been
 * double clicked on a component by the user.
 * 
 * @author rterrell
 *
 */
public class CustomItemDoubleClickedEvent extends CustomSelectionEvent {

    private static final long serialVersionUID = -9141447822675723514L;

    /**
     * Create a CustomItemDoubleClickedEvent initialized with the source of
     * the event, the item selected in the grid, and the event id.
     * 
     * @param source
     *            the object that triggered the event
     * @param selectedItem
     *            an object corresponding to the table model item currently
     *            selected.
     * @param eventId
     *            the id of the event.
     */
    public CustomItemDoubleClickedEvent(Object source, Object selectedItem,
            int eventId) {
        super(source, selectedItem, eventId);
    }

}
