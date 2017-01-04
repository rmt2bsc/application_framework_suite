package com.api.security.authentication.web;

import com.RMT2RuntimeException;

public class MissingLoginCredentialsException extends RMT2RuntimeException {
    private static final long serialVersionUID = 3146419736970380825L;

    /**
     * Default constructor that creates an LoginException object with a null
     * message.
     * 
     */
    public MissingLoginCredentialsException() {
        super();
    }

    /**
     * Creates an LoginException with a message.
     * 
     * @param msg
     *            The text message.
     */
    public MissingLoginCredentialsException(String msg) {
        super(msg);
    }

    /**
     * Creates an LoginException with a message and a code.
     * 
     * @param msg
     *            The text message.
     * @param code
     *            The integer code.
     */
    public MissingLoginCredentialsException(String msg, int code) {
        super(msg, code);
    }

    /**
     * Creates an LoginException using an Exception.
     * 
     * @param e
     *            An Exception object.
     */
    public MissingLoginCredentialsException(Exception e) {
        super(e);
    }

}
