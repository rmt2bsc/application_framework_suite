package com.ui.event;

import java.util.List;

/**
 * A common notification source containing information pertaining to item
 * selection based actions relative to a given component.
 * 
 * @author rterrell
 *
 */
public class CustomSelectionEvent extends BaseEvent {

    private static final long serialVersionUID = -4118435768509924170L;

    protected Object selectedItem;

    protected int selectedRowIndex;

    protected List grid;

    /**
     * Create a CustomSelectionEvent initialized with the source of the event,
     * the item selected in the grid, and the event id.
     * 
     * @param source
     *            the object that triggered the event
     * @param selectedItem
     *            an object corresponding to the table model item currently
     *            selected.
     * @param eventId
     *            the id of the event.
     */
    public CustomSelectionEvent(Object source, Object selectedItem, int eventId) {
        super(source, eventId);
        this.selectedItem = selectedItem;
    }

    /**
     * Create a CustomSelectionEvent initialized with the source of the event,
     * the grid's List component, and the event id.
     * 
     * @param source
     *            the object that triggered the event
     * @param grid
     *            a reference to the List contained within the grid component
     * @param eventId
     *            the id of the event.
     */
    public CustomSelectionEvent(Object source, List grid, int eventId) {
        super(source, eventId);
        this.grid = grid;
    }

    /**
     * Return the selected item.
     * 
     * @return the selectedItem
     */
    public Object getSelectedItem() {
        return selectedItem;
    }

    /**
     * set the selected item.
     * 
     * @param selectedItem
     *            the selectedItem to set
     */
    protected void setSelectedItem(Object selectedItem) {
        this.selectedItem = selectedItem;
    }

    /**
     * @return the selectedRowIndex
     */
    public int getSelectedRowIndex() {
        return selectedRowIndex;
    }

    /**
     * @param selectedRowIndex
     *            the selectedRowIndex to set
     */
    public void setSelectedRowIndex(int selectedRowIndex) {
        this.selectedRowIndex = selectedRowIndex;
    }

}
