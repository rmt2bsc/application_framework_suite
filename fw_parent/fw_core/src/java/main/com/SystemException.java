package com;

/**
 * The exception class is used to manage exceptions that are categorized as
 * "system". It is not require to add a throws clause or try/catch block for
 * method calls.
 * 
 * @author appdev
 * 
 */
public class SystemException extends RMT2RuntimeException {
    private static final long serialVersionUID = 7243915887555388092L;

    private int errorCode;

    /**
     * Default constructor that creates an SystemException object with a null
     * message.
     * 
     */
    public SystemException() {
        super();
    }

    /**
     * Creates an SystemException with a message.
     * 
     * @param msg
     *            The text message.
     */
    public SystemException(String msg) {
        super(msg);
    }

    /**
     * Creates an SystemException with a detail message and an error code.
     * 
     * @param msg
     *            The text message.
     * @param code
     *            The integer code.
     */
    public SystemException(String msg, int code) {
        this(msg);
        this.errorCode = code;
    }

    /**
     * Creates a new SystemException with a the specified message and the
     * causing throwable instance.
     * 
     * @param msg
     *            the message that explains the error.
     * @param cause
     *            the cause (which is saved for later retrieval by the
     *            Throwable.getCause() method). (A null value is permitted, and
     *            indicates that the cause is nonexistent or unknown.)
     * 
     */
    public SystemException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * Creates an SystemException using a throwable object.
     * 
     * @param e
     *            An Exception object.
     */
    public SystemException(Throwable e) {
        super(e);
    }

    /**
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

}
