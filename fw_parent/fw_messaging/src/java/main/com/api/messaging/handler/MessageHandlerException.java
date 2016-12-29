package com.api.messaging.handler;

import com.RMT2RuntimeException;

/**
 * This exception is thrown by messaging handler classes
 * 
 * @author RTerrell
 * 
 */
public class MessageHandlerException extends RMT2RuntimeException {

    private static final long serialVersionUID = 2469045739436149290L;

    public MessageHandlerException() {
        super();
    }

    public MessageHandlerException(String msg) {
        super(msg);
    }

    public MessageHandlerException(String msg, int code) {
        super(msg, code);
    }

    public MessageHandlerException(Exception cause) {
        super(cause);
    }

    public MessageHandlerException(String message, Throwable cause) {
        super(message);
    }
}
