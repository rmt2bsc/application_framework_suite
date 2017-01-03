package com;

/**
 * Exception class for identifying situations where a resource or data item does
 * not exists.
 * 
 * @author rterrell
 *
 */
public class NotFoundException extends RMT2RuntimeException {

    private static final long serialVersionUID = -3561623095185613369L;

    /**
     * 
     */
    public NotFoundException() {
        super();
    }

    /**
     * @param message
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public NotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
