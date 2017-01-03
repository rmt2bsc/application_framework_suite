package com;

/**
 * An excption that provides information for general invalid data situations.
 * 
 * @author appdev
 * 
 */
public class InvalidDataException extends RMT2RuntimeException {

    private static final long serialVersionUID = -4727114029986685783L;

    /**
     * Default constructor
     */
    public InvalidDataException() {
        return;
    }

    /**
     * Creates an InvalidDataException object identifying the exception message.
     * 
     * @param msg
     *            The message text of the exception
     */
    public InvalidDataException(String msg) {
        super(msg);
    }

    /**
     * Creates an InvalidDataException object identifying the message text and
     * error code.
     * 
     * @param msg
     *            The message text of the exception
     * @param code
     *            an int designated as the error code.
     */
    public InvalidDataException(String msg, int code) {
        super(msg, code);
    }

    /**
     * Creates an InvalidDataException object from an Exception instance.
     * 
     * @param e
     *            instance of Exception
     */
    public InvalidDataException(Exception e) {
        super(e);
        // TODO Auto-generated constructor stub
    }

    /**
     * Cerates an InvalidDataException object that will preserve the stack trace
     * of a previous exception using the message text and a Throable instance.
     * 
     * @param msg
     *            The message text of the exception
     * @param cause
     *            The Throwable instance that caused the exception to be
     *            invoked.
     */
    public InvalidDataException(String msg, Throwable cause) {
        super(msg, cause);
        // TODO Auto-generated constructor stub
    }

}
