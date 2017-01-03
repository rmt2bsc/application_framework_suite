package testcases.ldap.beans;

/**
 * @author rterrell
 * 
 */
public class TestLdapGeneralGroup extends TestLdapCommonEntity {
    private String uid;

    private String ou;

    /**
     * 
     */
    public TestLdapGeneralGroup() {
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

    /**
     * @return the ou
     */
    public String getOu() {
        return ou;
    }

    /**
     * @param ou
     *            the ou to set
     */
    public void setOu(String ou) {
        this.ou = ou;
    }

}
