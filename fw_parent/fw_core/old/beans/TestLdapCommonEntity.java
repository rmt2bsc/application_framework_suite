/**
 * 
 */
package testcases.ldap.beans;

/**
 * @author rterrell
 * 
 */
public class TestLdapCommonEntity {
    private String cn;

    private String description;

    /**
     * 
     */
    public TestLdapCommonEntity() {
        return;
    }

    /**
     * @return the cn
     */
    public String getCn() {
        return cn;
    }

    /**
     * @param cn
     *            the cn to set
     */
    public void setCn(String cn) {
        this.cn = cn;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
