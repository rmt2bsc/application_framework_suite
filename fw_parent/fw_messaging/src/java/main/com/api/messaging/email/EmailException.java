package com.api.messaging.email;

import com.RMT2Exception;

/**
 * This exception is thrown by classes using the Email api.
 * 
 * @author RTerrell
 * 
 */
public class EmailException extends RMT2Exception {
    private static final long serialVersionUID = 2469045739436149290L;

    public EmailException() {
        super();
    }

    public EmailException(String msg) {
        super(msg);
    }

    public EmailException(String msg, int code) {
        super(msg, code);
    }

    public EmailException(Exception cause) {
        super(cause);
    }

    public EmailException(String message, Throwable cause) {
        super(message);
    }
}
