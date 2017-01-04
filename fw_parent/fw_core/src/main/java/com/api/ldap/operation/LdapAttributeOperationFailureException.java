package com.api.ldap.operation;

import com.RMT2RuntimeException;

/**
 * Handles LDAP attribute operation failures.
 * 
 * @author Roy Terrell
 * 
 */
public class LdapAttributeOperationFailureException extends
        RMT2RuntimeException {

    private static final long serialVersionUID = 6121861198208065770L;

    /**
     * 
     */
    public LdapAttributeOperationFailureException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param msg
     */
    public LdapAttributeOperationFailureException(String msg) {
        super(msg);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param e
     */
    public LdapAttributeOperationFailureException(Exception e) {
        super(e);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param e
     */
    public LdapAttributeOperationFailureException(Throwable e) {
        super(e);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param msg
     * @param cause
     */
    public LdapAttributeOperationFailureException(String msg, Throwable cause) {
        super(msg, cause);
        // TODO Auto-generated constructor stub
    }

}
