package com;

/**
 * Exception class for identifying situations where the processing of a GUI's
 * data has failed.
 * 
 * @author rterrell
 *
 */
public class ProcessDataFailureException extends RMT2RuntimeException {

    private static final long serialVersionUID = -3561623095185613369L;

    /**
     * 
     */
    public ProcessDataFailureException() {
        super();
    }

    /**
     * @param message
     */
    public ProcessDataFailureException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ProcessDataFailureException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ProcessDataFailureException(String message, Throwable cause) {
        super(message, cause);
    }

}
