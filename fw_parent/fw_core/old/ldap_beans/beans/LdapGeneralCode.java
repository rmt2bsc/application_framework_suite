package com.api.ldap.beans;

/**
 * @author rterrell
 * 
 */
public class LdapGeneralCode extends LdapCommonEntity {
    private String uid;

    /**
     * 
     */
    public LdapGeneralCode() {
        super();
    }

    /**
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @param uid
     *            the uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

}
