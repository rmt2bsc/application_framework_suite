package com.api.ldap.beans;

/**
 * @author rterrell
 * 
 */
public class LdapGeneralGroup extends LdapCommonEntity {
    private String uid;

    /**
     * 
     */
    public LdapGeneralGroup() {
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
