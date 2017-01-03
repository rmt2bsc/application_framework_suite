package com.api.persistence;


/**
 * This exception inherits the Exception class and is used in cases where the
 * Persistence Storage cannot persist a pair from its storage mechanism (leading
 * to fatal errors)
 * 
 * @author Roy Terrell
 */

public class CannotPersistException extends DatabaseException {

    private static final long serialVersionUID = -8908049552892867317L;

    public CannotPersistException(String msg) {
        super(msg);
    }

    public CannotPersistException(Exception e) {
        super(e);
    }

    public CannotPersistException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
