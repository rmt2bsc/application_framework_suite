package com.api.messaging.jms.handler;

import com.RMT2Exception;

/**
 * An exception that is thrown by descendent classes of AbstractMessageHandler.
 * 
 * @author roy.terrell
 * 
 */
public class MessageHandlerCommandException extends RMT2Exception {
    private static final long serialVersionUID = 8852424708037697306L;

    /**
     * Default constructor which its message is null.
     * 
     */
    public MessageHandlerCommandException() {
        super();
    }

    /**
     * Constructs an ActionHandlerException object with a message.
     * 
     * @param msg
     *            The exception's message.
     */
    public MessageHandlerCommandException(String msg) {
        super(msg);
    }

    /**
     * Constructs an ActionHandlerException using an Exception object.
     * 
     * @param e
     *            Exception
     */
    public MessageHandlerCommandException(Exception e) {
        super(e);
    }

    /**
     * 
     * @param msg
     * @param e
     */
    public MessageHandlerCommandException(String msg, Throwable e) {
        super(msg, e);
    }
}
