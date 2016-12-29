package com.api.ldap;

import com.RMT2RuntimeException;

/**
 * Handles LDAP place holder error when it comes to the substitution of variable
 * values.
 * 
 * @author Roy Terrell
 * 
 */
public class LdapFilterPlaceholderException extends RMT2RuntimeException {

    private static final long serialVersionUID = 6121861198208065770L;

    /**
     * 
     */
    public LdapFilterPlaceholderException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param msg
     */
    public LdapFilterPlaceholderException(String msg) {
        super(msg);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param e
     */
    public LdapFilterPlaceholderException(Exception e) {
        super(e);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param e
     */
    public LdapFilterPlaceholderException(Throwable e) {
        super(e);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param msg
     * @param cause
     */
    public LdapFilterPlaceholderException(String msg, Throwable cause) {
        super(msg, cause);
        // TODO Auto-generated constructor stub
    }

}
