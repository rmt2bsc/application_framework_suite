package com.api.security.authentication.web;

import com.RMT2RuntimeException;

public class AuthenticationException extends RMT2RuntimeException {
    private static final long serialVersionUID = 3146419736970380825L;

    /**
     * Default constructor that creates an LoginException object with a null
     * message.
     * 
     */
    public AuthenticationException() {
        super();
    }

    /**
     * Creates an LoginException with a message.
     * 
     * @param msg
     *            The text message.
     */
    public AuthenticationException(String msg) {
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
    public AuthenticationException(String msg, int code) {
        super(msg, code);
    }

    /**
     * Creates an LoginException using an Exception.
     * 
     * @param e
     *            An Exception object.
     */
    public AuthenticationException(Exception e) {
        super(e);
    }

    /**
     * Creates a new AuthenticationException with a the specified message and
     * the causing throwable instance.
     * 
     * @param msg
     *            the message that explains the error.
     * @param cause
     *            the cause (which is saved for later retrieval by the
     *            Throwable.getCause() method). (A null value is permitted, and
     *            indicates that the cause is nonexistent or unknown.)
     * 
     */
    public AuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
