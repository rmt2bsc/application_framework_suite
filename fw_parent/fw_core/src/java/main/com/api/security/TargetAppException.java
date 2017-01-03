package com.api.security;

import com.api.security.authentication.web.MissingLoginCredentialsException;

public class TargetAppException extends MissingLoginCredentialsException {
    private static final long serialVersionUID = -2320582233990240863L;

    /**
     * Default constructor that creates an UserAuthenticationException object
     * with a null message.
     * 
     */
    public TargetAppException() {
        super();
    }

    /**
     * Creates an UserAuthenticationException with a message.
     * 
     * @param msg
     *            The text message.
     */
    public TargetAppException(String msg) {
        super(msg);
    }

    /**
     * Creates an UserAuthenticationException with a message and a code.
     * 
     * @param msg
     *            The text message.
     * @param code
     *            The integer code.
     */
    public TargetAppException(String msg, int code) {
        super(msg, code);
    }

    /**
     * Creates an UserAuthenticationException using an Exception.
     * 
     * @param e
     *            An Exception object.
     */
    public TargetAppException(Exception e) {
        super(e);
    }
}
