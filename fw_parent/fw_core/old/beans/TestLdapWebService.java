/**
 * 
 */
package testcases.ldap.beans;

/**
 * @author rterrell
 * 
 */
public class TestLdapWebService extends TestLdapCommonEntity {

    private String isSecured;

    private String url;

    /**
     * 
     */
    public TestLdapWebService() {
        super();
    }

    /**
     * @return the isSecured
     */
    public String getIsSecured() {
        return isSecured;
    }

    /**
     * @param isSecured
     *            the isSecured to set
     */
    public void setIsSecured(String isSecured) {
        this.isSecured = isSecured;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

}
