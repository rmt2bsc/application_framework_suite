package com.api.messaging;

import com.RMT2RuntimeException;

/**
 * This exception is thrown by classes using the Email api.
 * 
 * @author RTerrell
 * 
 */
public class MessageException extends RMT2RuntimeException {
    private static final long serialVersionUID = 2469045739436149290L;

    public MessageException() {
        super();
    }

    public MessageException(String msg) {
        super(msg);
    }

    public MessageException(String msg, int code) {
        super(msg, code);
    }

    public MessageException(Exception cause) {
        super(cause);
    }

    public MessageException(String message, Throwable cause) {
        super(message);
    }
}
