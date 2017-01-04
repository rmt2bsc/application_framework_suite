package com.api.xml.jaxb;

import com.RMT2RuntimeException;

/**
 * The class handles errors pertaining to JAXB operations.
 * 
 * @author rterrell
 *
 */
public class JaxbUtilException extends RMT2RuntimeException {
    private static final long serialVersionUID = 4879374016844141550L;

    /**
     * Default constructor
     * 
     */
    public JaxbUtilException() {
        super();
    }

    /**
     * Creates a JaxbUtilException intitialized with a String message.
     * 
     * @param msg
     *            Error message
     */
    public JaxbUtilException(String msg) {
        super(msg);
    }

    /**
     * Creates a JaxbUtilException intitialized with a Throwable instance.
     * 
     * @param e
     */
    public JaxbUtilException(Throwable e) {
        super(e);
    }

    /**
     * Creates a new JaxbUtilException with a the specified message and the
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
    public JaxbUtilException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
