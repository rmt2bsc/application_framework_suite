package com.printing;

import com.RMT2RuntimeException;

/**
 * An exception class for handling Swing component printing errors.
 * 
 * @author rterrell
 * 
 */
public class SwingComponentPrintingException extends RMT2RuntimeException {

    private static final long serialVersionUID = -7667239669740064955L;

    /**
     * 
     */
    public SwingComponentPrintingException() {
        super();
        return;
    }

    /**
     * @param message
     */
    public SwingComponentPrintingException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public SwingComponentPrintingException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public SwingComponentPrintingException(String message, Throwable cause) {
        super(message, cause);
    }

}
