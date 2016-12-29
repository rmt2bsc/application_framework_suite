package com.api.security.authentication.web;

import com.RMT2Exception;

public class InvalidUserCredentialsException extends RMT2Exception {
    private static final long serialVersionUID = -2320582233990240863L;

    /**
     * Default constructor that creates an UserAuthenticationException object
     * with a null message.
     * 
     */
    public InvalidUserCredentialsException() {
        super();
    }

    /**
     * Creates an UserAuthenticationException with a message.
     * 
     * @param msg
     *            The text message.
     */
    public InvalidUserCredentialsException(String msg) {
        super(msg);
    }

    /**
     * Creates an UserAuthenticationException with a code and no message.
     * 
     * @param code
     *            The integer code.
     */
    public InvalidUserCredentialsException(int code) {
        super(code);
    }

    /**
     * Creates an UserAuthenticationException with a message and a code.
     * 
     * @param msg
     *            The text message.
     * @param code
     *            The integer code.
     */
    public InvalidUserCredentialsException(String msg, int code) {
        super(msg, code);
    }

    /**
     * Creates an UserAuthenticationException using an Exception.
     * 
     * @param e
     *            An Exception object.
     */
    public InvalidUserCredentialsException(Exception e) {
        super(e);
    }
}
