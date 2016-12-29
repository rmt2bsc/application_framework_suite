package com.api;

import com.api.persistence.DatabaseException;


/**
 * This exception inherits the Exception class and is used in cases where the
 * Persistence Storage cannot be connected to for any purpose.
 * 
 * @author Roy Terrell
 */

public class CannotConnectException extends DatabaseException {

    private static final long serialVersionUID = -4562728054413124733L;

    public CannotConnectException(String msg) {
        super(msg);
    }

    public CannotConnectException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
