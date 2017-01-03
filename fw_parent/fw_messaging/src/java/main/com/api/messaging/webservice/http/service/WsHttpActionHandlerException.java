package com.api.messaging.webservice.http.service;

import com.RMT2RuntimeException;

/**
 * An exception that is thrown by descendent classes of AbstractActionHandler.
 * 
 * @author roy.terrell
 * 
 */
public class WsHttpActionHandlerException extends RMT2RuntimeException {
    private static final long serialVersionUID = 8852424708037697306L;

    /**
     * Default constructor which its message is null.
     * 
     */
    public WsHttpActionHandlerException() {
        super();
    }

    /**
     * Constructs an ActionHandlerException object with a message.
     * 
     * @param msg
     *            The exception's message.
     */
    public WsHttpActionHandlerException(String msg) {
        super(msg);
    }

    /**
     * Constructs an ActionHandlerException object with a message and a code.
     * 
     * @param msg
     *            The exception message.
     * @param code
     *            The exception code.
     */
    public WsHttpActionHandlerException(String msg, int code) {
        super(msg, code);
    }

    /**
     * Constructs an ActionHandlerException using an Exception object.
     * 
     * @param e
     *            Exception
     */
    public WsHttpActionHandlerException(Exception e) {
        super(e);
    }

    /**
     * 
     * @param msg
     * @param e
     */
    public WsHttpActionHandlerException(String msg, Throwable e) {
        super(msg, e);
    }
}
