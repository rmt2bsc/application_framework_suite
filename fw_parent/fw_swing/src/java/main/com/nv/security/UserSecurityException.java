package com.nv.security;

import com.RMT2RuntimeException;

/**
 * Exception class for security related errors.
 * 
 * @author rterrell
 *
 */
public class UserSecurityException extends RMT2RuntimeException {

    private static final long serialVersionUID = 6250160411343754913L;

    /**
     * 
     */
    public UserSecurityException() {
        super();
    }

    /**
     * @param message
     */
    public UserSecurityException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public UserSecurityException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public UserSecurityException(String message, Throwable cause) {
        super(message, cause);
    }

}
