package com.api.security.authentication.web;

import com.RMT2Exception;

public class LogoutException extends RMT2Exception {
    private static final long serialVersionUID = -2320582233990240863L;

    /**
     * Default constructor that creates an UserAuthenticationException object
     * with a null message.
     * 
     */
    public LogoutException() {
        super();
    }

    /**
     * Creates an UserAuthenticationException with a message.
     * 
     * @param msg
     *            The text message.
     */
    public LogoutException(String msg) {
        super(msg);
    }

    /**
     * Creates an UserAuthenticationException with a code and no message.
     * 
     * @param code
     *            The integer code.
     */
    public LogoutException(int code) {
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
    public LogoutException(String msg, int code) {
        super(msg, code);
    }

    /**
     * Creates an UserAuthenticationException using an Exception.
     * 
     * @param e
     *            An Exception object.
     */
    public LogoutException(Exception e) {
        super(e);
    }

    /**
     * Creates an UserAuthenticationException using an custom message and the
     * cause instance.
     * 
     * @param msg
     * @param e
     */
    public LogoutException(String msg, Throwable e) {
        super(msg, e);
    }
}
