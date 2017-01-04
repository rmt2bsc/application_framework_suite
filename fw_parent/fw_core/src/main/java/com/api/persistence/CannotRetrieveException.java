package com.api.persistence;


/**
 * This exception inherits the Exception class and is used in cases where the
 * Persistence Storage cannot retrieve a pair from its storage mechanism
 * (leading to failure of mechanism)
 * 
 * @author Roy Terrell
 */

public class CannotRetrieveException extends DatabaseException {

    private static final long serialVersionUID = -5989244896539149738L;

    public CannotRetrieveException(String msg) {
        super(msg);
    }

    public CannotRetrieveException(Exception cause) {
        super(cause);
    }

    public CannotRetrieveException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
