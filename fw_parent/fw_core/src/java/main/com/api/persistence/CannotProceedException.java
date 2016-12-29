package com.api.persistence;


/**
 * This exception inherits the Exception class and is used in cases where the
 * Persistence Storage cannot proceed due to reasons such as invalid data.
 * 
 * @author Roy Terrell
 */

public class CannotProceedException extends DatabaseException {

    private static final long serialVersionUID = -5989244896539149738L;

    public CannotProceedException(String msg) {
        super(msg);
    }

    public CannotProceedException(Exception cause) {
        super(cause);
    }

    public CannotProceedException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
