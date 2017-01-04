package com.api.ldap.operation;

/**
 * LDAP operation class for binding, rebinding, and unbinding objects pertaining
 * to the LDAP provider.
 * 
 * @author Roy Terrell
 * 
 */
public class LdapBindOperation extends LdapOperation {
    private Object ldapObject;

    /**
     * 
     */
    public LdapBindOperation() {
        super();
    }

    /**
     * @return the ldapObject
     */
    public Object getLdapObject() {
        return ldapObject;
    }

    /**
     * @param ldapObject
     *            the ldapObject to set
     */
    public void setLdapObject(Object ldapObject) {
        this.ldapObject = ldapObject;
    }

}
