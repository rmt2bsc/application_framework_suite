package com;

/**
 * Exception class for identifying situations where a resource or data item is
 * in violation of duplicating itself.
 * 
 * @author rterrell
 *
 */
public class DuplicateKeyException extends RMT2RuntimeException {

    private static final long serialVersionUID = -3561623095185613369L;

    /**
     * 
     */
    public DuplicateKeyException() {
        super();
    }

    /**
     * @param message
     */
    public DuplicateKeyException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public DuplicateKeyException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public DuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
    }

}
