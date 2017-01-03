package com.api.messaging;

import com.RMT2RuntimeException;

/**
 * This exception class is used to manage error pertaining to message routing.
 * 
 * @author RTerrell
 * 
 */
public class MessageRoutingException extends RMT2RuntimeException {

    private static final long serialVersionUID = 2469045739436149290L;

    public MessageRoutingException() {
        super();
    }

    public MessageRoutingException(String msg) {
        super(msg);
    }

    public MessageRoutingException(Exception cause) {
        super(cause);
    }

    public MessageRoutingException(String message, Throwable cause) {
        super(message);
    }
}
