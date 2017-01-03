package com.printing;

import com.RMT2RuntimeException;

/**
 * An exception class for handling Swing component printing errors.
 * 
 * @author rterrell
 *
 */
public class ComponentPrintingException extends RMT2RuntimeException {

    private static final long serialVersionUID = -7667239669740064955L;

    /**
     * 
     */
    public ComponentPrintingException() {
        super();
    }

    /**
     * @param message
     */
    public ComponentPrintingException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ComponentPrintingException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ComponentPrintingException(String message, Throwable cause) {
        super(message, cause);
    }

}
