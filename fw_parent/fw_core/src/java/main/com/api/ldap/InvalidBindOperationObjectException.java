package com.api.ldap;

/**
 * Handles invalid LDAP bind operation object.
 * 
 * @author Roy Terrell
 * 
 */
public class InvalidBindOperationObjectException extends RMT2LdapException {

    /**
     * 
     */
    private static final long serialVersionUID = 2754523606113677576L;

    /**
     * 
     */
    public InvalidBindOperationObjectException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param msg
     */
    public InvalidBindOperationObjectException(String msg) {
        super(msg);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param e
     */
    public InvalidBindOperationObjectException(Exception e) {
        super(e);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param msg
     * @param cause
     */
    public InvalidBindOperationObjectException(String msg, Throwable cause) {
        super(msg, cause);
        // TODO Auto-generated constructor stub
    }

}
