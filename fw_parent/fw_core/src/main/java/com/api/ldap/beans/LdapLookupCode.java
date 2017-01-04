package com.api.ldap.beans;

/**
 * ORM bean for mapping general lookup code values coming from a LDAP server.
 * 
 * @author Roy Terrell
 * 
 */
public class LdapLookupCode extends LdapCommonEntity {
    private String uid;

    /**
     * Creates a LdapLookupCode object withou intializing any of its properties.
     */
    public LdapLookupCode() {
        super();
    }

    /**
     * Get the unique identifier for this lookup code
     * 
     * @return the unique Identifier
     */
    public String getUid() {
        return uid;
    }

    /**
     * Set the unique identifier for this lookup code.
     * 
     * @param uid
     *            the unique identifier to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

}
