package com.api.messaging.webservice.http;

import com.api.messaging.MessageException;

public class HttpException extends MessageException {
    private static final long serialVersionUID = -6716347267991012444L;

    /**
     * Default constructor that creates an HttpException object with a null
     * message.
     * 
     */
    public HttpException() {
        super();
    }

    /**
     * Creates an HttpException with a message.
     * 
     * @param msg
     *            The text message.
     */
    public HttpException(String msg) {
        super(msg);
    }

    /**
     * Creates an HttpException with a message and a code.
     * 
     * @param msg
     *            The text message.
     * @param code
     *            The integer code.
     */
    public HttpException(String msg, int code) {
        super(msg, code);
    }

    /**
     * Creates an HttpException using an Exception.
     * 
     * @param e
     *            An Exception object.
     */
    public HttpException(Exception e) {
        super(e);
    }

    /**
     * Creates a HttpException using a String message and an instance of
     * Throwable
     * 
     * @param msg
     *            The message
     * @param e
     *            The assoicated Throwable object.
     */
    public HttpException(String msg, Throwable e) {
        super(msg, e);
    }

}
