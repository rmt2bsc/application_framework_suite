package testcases.ldap.beans;

/**
 * @author rterrell
 * 
 */
public class TestLdapGeneralCode extends TestLdapCommonEntity {
    private String uid;

    /**
     * 
     */
    public TestLdapGeneralCode() {
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
