package com.ui.components;

import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import net.miginfocom.swing.MigLayout;

import com.ui.event.ComponentUpdateCompletedListener;
import com.ui.event.EventDispatcher;
import com.ui.event.EventDispatcherImpl;

/**
 * An abstract object for building resuable visual components to used insided of
 * various window objects.
 * <p>
 * This component uses MigLayout to layout its content. If a different layout is
 * desired, override this constructor with a call to "this.setLayout()".
 * 
 * @author Roy Terrell
 *
 */
public abstract class AbstractGuiComponentTemplate<E> extends JPanel {

    private static final long serialVersionUID = 4885982443482429370L;

    private E data;

    protected EventDispatcher evtDispatcher;

    /**
     * Create an AbstractGuiComponentTemplate that will setup the component
     * layout without displaying any data.
     * <p>
     * Initializes the component to use a 1 row by 1 column MigLayout for its
     * content. If a different layout is desired, the set the layout in the
     * {@link AbstractGuiComponentTemplate#setupView() setupView} method
     * implementation. Next, it calls
     * {@link AbstractGuiComponentTemplate#initFields() initFields} and
     * {@link AbstractGuiComponentTemplate#setupView() setupView} template
     * methods to layout the contents of the component in that order.
     * 
     */
    protected AbstractGuiComponentTemplate() {
        super();
        this.setLayout(new MigLayout("", "[]", "[]"));
        this.initFields();
        // Setup event listener
        this.listenerList = new EventListenerList();
        // Setup Event dispatcher
        this.evtDispatcher = new EventDispatcherImpl(this.listenerList);
    }

    /**
     * Create an AbstractGuiComponentTemplate that will display a List of
     * arbitrary data items.
     * <p>
     * This component uses MigLayout to layout its content. If a different
     * layout is desired, override this constructor with a call to
     * "this.setLayout()".
     * <p>
     * It calls the default constructor and
     * {@link AbstractGuiComponentTemplate#updateView(List) updateView} template
     * method in order to display the data represented as <i>data</i>, if
     * applicable.
     */
    // public AbstractGuiComponentTemplate(List<E> data) {
    public AbstractGuiComponentTemplate(E data) {
        this();
        this.data = data;
        this.setupView();
        // Try letting parent call this method in order to preveny threading
        // issues.
        // this.updateView(data);
        return;
    }

    /**
     * Adds an instance of {@link ComponentUpdateCompletedListener} interface to
     * the List of Listeners.
     * 
     * @param listener
     *            an instance of a class that has implemented the
     *            {@link ComponentUpdateCompletedListener} interface.
     */
    public void addUpdateCompletedListener(
            ComponentUpdateCompletedListener listener) {
        if (this.listenerList == null) {
            this.listenerList = new EventListenerList();
        }
        this.listenerList.add(ComponentUpdateCompletedListener.class, listener);
    }

    /**
     * Removes a listener from the list of
     * {@link ComponentUpdateCompletedListener} implementations.
     * 
     * @param listener
     *            an instance of {@link ComponentUpdateCompletedListener}
     *            interface targeted for removal.
     */
    public void removeUpdateCompletedListener(
            ComponentUpdateCompletedListener listener) {
        this.listenerList.remove(ComponentUpdateCompletedListener.class,
                listener);
    }

    // /**
    // * A Worker Thread inner class used to save the data changes of the
    // * component.
    // * <p>
    // * <b>NOTE</b> Calling {@link AbstractGuiComponentTemplate#saveChanges()}
    // in
    // * the background has a tendency to cause problems when making back to
    // back
    // * DB calls for multiple components. For example, creating a TabPane where
    // * each tab is a component that invokes multiple DB calls.
    // *
    // * @author rterrell
    // *
    // */
    // private class UpdateWorker extends SwingWorker<Void, Void> {
    //
    // protected UpdateWorker() {
    // return;
    // }
    //
    // @Override
    // protected Void doInBackground() throws Exception {
    // saveChanges();
    // return null;
    // }
    // }

    /**
     * A template method for initializing the input controls that are displayed
     * from within the component.
     */
    protected abstract void initFields();

    /**
     * Initially setup the component's view for the first time.
     */
    protected abstract void setupView();

    /**
     * Update the view based on the input data.
     * 
     * @param data
     *            the data to display.
     * 
     */
    public abstract void updateView(E data);

    /**
     * Saves the component changes.
     */
    public abstract void saveChanges();

    /**
     * @return the data
     */
    public E getData() {
        return data;
    }
}
