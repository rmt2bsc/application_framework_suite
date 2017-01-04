package com.api;

import com.RMT2RuntimeException;

/**
 * An exception class for handling API intialization errors.
 * 
 * @author rterrell
 * 
 */
public class ApiInitException extends RMT2RuntimeException {

    private static final long serialVersionUID = -7667239669740064955L;

    /**
     * 
     */
    public ApiInitException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public ApiInitException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public ApiInitException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public ApiInitException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}
