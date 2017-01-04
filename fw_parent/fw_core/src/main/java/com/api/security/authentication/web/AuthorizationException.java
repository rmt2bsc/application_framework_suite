package com.api.security.authentication.web;

import com.RMT2RuntimeException;

public class AuthorizationException extends RMT2RuntimeException {
    private static final long serialVersionUID = 3146419736970380825L;

    /**
     * Default constructor that creates an AuthorizationException object with a
     * null message.
     * 
     */
    public AuthorizationException() {
        super();
    }

    /**
     * Creates an AuthorizationException with a message.
     * 
     * @param msg
     *            The text message.
     */
    public AuthorizationException(String msg) {
        super(msg);
    }

    /**
     * Creates an AuthorizationException with a message and a code.
     * 
     * @param msg
     *            The text message.
     * @param code
     *            The integer code.
     */
    public AuthorizationException(String msg, int code) {
        super(msg, code);
    }

    /**
     * Creates an AuthorizationException using an Exception.
     * 
     * @param e
     *            An Exception object.
     */
    public AuthorizationException(Exception e) {
        super(e);
    }

    /**
     * Creates a AuthorizationException using a message text and the causable
     * exception.
     * 
     * @param msg
     * @param e
     */
    public AuthorizationException(String msg, Throwable e) {
        super(msg, e);
    }

}
