package com.api.security.authentication.web;

import com.RMT2RuntimeException;

public class LoginException extends RMT2RuntimeException {
    private static final long serialVersionUID = 3146419736970380825L;

    /**
     * Default constructor that creates an LoginException object with a null
     * message.
     * 
     */
    public LoginException() {
        super();
    }

    /**
     * Creates an LoginException with a message.
     * 
     * @param msg
     *            The text message.
     */
    public LoginException(String msg) {
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
    public LoginException(String msg, int code) {
        super(msg, code);
    }

    /**
     * Creates an LoginException using an Exception.
     * 
     * @param e
     *            An Exception object.
     */
    public LoginException(Exception e) {
        super(e);
    }

    /**
     * 
     * @param msg
     * @param e
     */
    public LoginException(String msg, Exception e) {
        super(msg, e);
    }

}
