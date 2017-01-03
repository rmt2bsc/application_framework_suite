package com.api.ldap;

import com.api.persistence.DatabaseException;

/**
 * LDAP related exception
 * 
 * @author roy.terrell
 * 
 */
public class RMT2LdapException extends DatabaseException {
    private static final long serialVersionUID = -6755175657939760814L;

    /**
     * Default constructor which its message is null.
     * 
     */
    public RMT2LdapException() {
        super();
    }

    /**
     * Constructs an RMT2LdapException object with a message.
     * 
     * @param msg
     *            The exception's message.
     */
    public RMT2LdapException(String msg) {
        super(msg);
    }

    /**
     * Constructs an RMT2LdapException using an Exception object.
     * 
     * @param e
     *            Exception
     */
    public RMT2LdapException(Exception e) {
        super(e);
    }

    /**
     * Creates a new RMT2LdapException with a the specified message and the
     * causing throwable instance.
     * 
     * @param msg
     *            the message that explains the error.
     * @param cause
     *            the cause (which is saved for later retrieval by the
     *            Throwable.getCause() method). (A null value is permitted, and
     *            indicates that the cause is nonexistent or unknown.)
     * 
     */
    public RMT2LdapException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
