package com.api.foundation;

import com.RMT2Exception;

/**
 * This exception is thrown in cases where an error has occurred during the act of an Api transaction.
 * 
 * @author roy.terrell
 * 
 */
public class TransactionApiException extends RMT2Exception {
    private static final long serialVersionUID = -6755175657939760814L;

    /**
     * Default constructor which its message is null.
     * 
     */
    public TransactionApiException() {
        super();
    }

    /**
     * Constructs an TransactionApiException object with a message.
     * 
     * @param msg
     *            The exception's message.
     */
    public TransactionApiException(String msg) {
        super(msg);
    }

    /**
     * Constructs an TransactionApiException using an Exception
     * object.
     * 
     * @param e
     *            Exception
     */
    public TransactionApiException(Exception e) {
        super(e);
    }

    /**
     * Creates a new TransactionApiException with a the specified
     * message and the causing throwable instance.
     * 
     * @param msg
     *            the message that explains the error.
     * @param cause
     *            the cause (which is saved for later retrieval by the
     *            Throwable.getCause() method). (A null value is permitted, and
     *            indicates that the cause is nonexistent or unknown.)
     * 
     */
    public TransactionApiException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
