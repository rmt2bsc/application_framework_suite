package com.ui.event;

import java.lang.reflect.Method;
import java.util.EventListener;

import javax.swing.event.EventListenerList;

import com.util.RMT2BeanUtility;

/**
 * An implementation of {@link EventDispatcher} providing the ability to
 * introspect a parent component and invoke one or more of its registered event
 * handlers.
 * 
 * @author rterrell
 *
 */
public class EventDispatcherImpl implements EventDispatcher {

    private EventListenerList listeners;

    /**
     * Create an EventDispatcherImpl that is unaware of any event listeners.
     * 
     */
    public EventDispatcherImpl() {
        return;
    }

    /**
     * Create an EventDispatcherImpl with a List of event listeners belonging to
     * component that is capable of dispatching events to interested listeners.
     */
    public EventDispatcherImpl(EventListenerList listeners) {
        this();
        this.listeners = listeners;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ui.event.NmEventDispatcher#fireEvent(com.ui.event.NmEvent)
     */
    @Override
    public void fireEvent(BaseEvent evt) {
        if (this.listeners == null) {
            return;
        }
        Object listeners[] = this.listeners.getListenerList();
        for (int ndx = 0; ndx < listeners.length; ndx += 2) {
            if (listeners[ndx].getClass().isInstance(EventListener.class)) {
                // Use reflection to invoke the appropriate event handler from
                // the object
                // that implements an EventListener derived interface.
                EventListener obj = (EventListener) listeners[ndx + 1];
                System.out.println("Inspecting class, "
                        + obj.getClass().getName() + ", for event handlers");

                // Inspect all methods containing a single event parameter
                // signature. Only invoke
                // the method where the event parameter class name matches
                // exactly to "evt".
                Method m[] = obj.getClass().getMethods();
                int methodNdx;
                for (methodNdx = 0; methodNdx < m.length; methodNdx++) {
                    Class pt[] = m[methodNdx].getParameterTypes();
                    // We are only looking for the method that has one
                    // parameter.
                    if (pt.length == 1) {
                        String parmClass = pt[0].getName();
                        String evtClass = evt.getClass().getName();
                        if (parmClass.equalsIgnoreCase(evtClass)) {
                            // Since the current method's parameter is of the
                            // same type as
                            // "evt", trigger that event handler.
                            try {
                                RMT2BeanUtility util = new RMT2BeanUtility(obj);
                                Object parms[] = { evt };
                                util.triggerMethod(m[methodNdx], parms);
                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                    }
                }
                System.out.println("Total methods interrogated: " + methodNdx);
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ui.event.EventDispatcher#setListenerList(javax.swing.event.
     * EventListenerList)
     */
    @Override
    public void setListenerList(EventListenerList listeners) {
        this.listeners = listeners;
        return;
    }

}
