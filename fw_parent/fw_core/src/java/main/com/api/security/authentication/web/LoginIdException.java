package com.api.security.authentication.web;

public class LoginIdException extends MissingLoginCredentialsException {
    private static final long serialVersionUID = -2320582233990240863L;

    /**
     * Default constructor that creates an UserAuthenticationException object
     * with a null message.
     * 
     */
    public LoginIdException() {
        super();
    }

    /**
     * Creates an UserAuthenticationException with a message.
     * 
     * @param msg
     *            The text message.
     */
    public LoginIdException(String msg) {
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
    public LoginIdException(String msg, int code) {
        super(msg, code);
    }

    /**
     * Creates an UserAuthenticationException using an Exception.
     * 
     * @param e
     *            An Exception object.
     */
    public LoginIdException(Exception e) {
        super(e);
    }
}
