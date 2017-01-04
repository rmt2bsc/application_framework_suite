package com;

/**
 * Exception class for identifying situations when the Singleton pattern is
 * violated.
 * 
 * @author rterrell
 *
 */
public class TooManyInstancesException extends RMT2RuntimeException {

    private static final long serialVersionUID = -3561623095185613369L;

    /**
     * 
     */
    public TooManyInstancesException() {
        super();
    }

    /**
     * @param message
     */
    public TooManyInstancesException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public TooManyInstancesException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public TooManyInstancesException(String message, Throwable cause) {
        super(message, cause);
    }

}
