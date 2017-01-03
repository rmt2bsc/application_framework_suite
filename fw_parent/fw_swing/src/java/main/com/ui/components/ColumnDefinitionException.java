package com.ui.components;

import com.RMT2RuntimeException;

/**
 * Exception class for identifying column definition errors for JTable related
 * instances.
 * 
 * @author rterrell
 *
 */
public class ColumnDefinitionException extends RMT2RuntimeException {

    private static final long serialVersionUID = -3561623095185613369L;

    /**
     * 
     */
    public ColumnDefinitionException() {
        super();
    }

    /**
     * @param message
     */
    public ColumnDefinitionException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ColumnDefinitionException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ColumnDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }

}
