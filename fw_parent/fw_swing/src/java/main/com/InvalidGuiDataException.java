package com;

/**
 * Exception class for identifying invalid data errors.
 * 
 * @author rterrell
 *
 */
public class InvalidGuiDataException extends RMT2RuntimeException {

    private static final long serialVersionUID = -3561623095185613369L;

    /**
     * 
     */
    public InvalidGuiDataException() {
        super();
    }

    /**
     * @param message
     */
    public InvalidGuiDataException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public InvalidGuiDataException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidGuiDataException(String message, Throwable cause) {
        super(message, cause);
    }

}
