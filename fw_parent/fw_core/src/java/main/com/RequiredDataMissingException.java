package com;

/**
 * Exception class for identifying and handling error pertaining to missing
 * required data.
 * 
 * @author rterrell
 * 
 */
public class RequiredDataMissingException extends RMT2RuntimeException {
    private static final long serialVersionUID = 1441957717014777177L;

    /**
     * Default constructor that creates an RequiredDataMissingException object
     * with a null message.
     * 
     */
    public RequiredDataMissingException() {
        super();
    }

    /**
     * Creates an RequiredDataMissingException with a message.
     * 
     * @param msg
     *            The text message.
     */
    public RequiredDataMissingException(String msg) {
        super(msg);
    }

    /**
     * Creates an RequiredDataMissingException using an Exception.
     * 
     * @param e
     *            An Exception object.
     */
    public RequiredDataMissingException(Exception e) {
        super(e);
    }

    /**
     * Creates a new RequiredDataMissingException with a the specified message
     * and the causing throwable instance.
     * 
     * @param msg
     *            the message that explains the error.
     * @param cause
     *            the cause (which is saved for later retrieval by the
     *            Throwable.getCause() method). (A null value is permitted, and
     *            indicates that the cause is nonexistent or unknown.)
     * 
     */
    public RequiredDataMissingException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
