package com.api.ldap.operation;

import com.RMT2Base;

/**
 * Common class for managing parameters for LDAP operations such as Search,
 * Compare, Update, Update ModifyDN, and Authentication.
 * <p>
 * Basically keeps track of the Distinguished Name (DN) of the entry targeted
 * for the operation.
 * 
 * @author rterrell
 * 
 */
public class LdapOperation extends RMT2Base {

    private String dn;

    /**
     * Default constructor
     */
    public LdapOperation() {
        super();
    }

    /**
     * Return the Distinguished Name of the LDAP entry where the operation will
     * start.
     * 
     * @return the dn
     */
    public String getDn() {
        return dn;
    }

    /**
     * Set the Distinguished Name of the LDAP entry where the operation will
     * start.
     * 
     * @param dn
     *            the dn to set
     */
    public void setDn(String dn) {
        this.dn = dn;
    }

}
