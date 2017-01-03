package testcases.ldap.beans;

/**
 * @author rterrell
 * 
 */
public class TestLdapAppRoles extends TestLdapCommonEntity {

    private String appId;

    private String roleId;

    private String appRoleName;

    /**
     * 
     */
    public TestLdapAppRoles() {
        super();
    }

    /**
     * @return the appId
     */
    public String getAppId() {
        return appId;
    }

    /**
     * @param appId
     *            the appId to set
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * @return the roleId
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * @param roleId
     *            the roleId to set
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    /**
     * @return the appRoleName
     */
    public String getAppRoleName() {
        return appRoleName;
    }

    /**
     * @param appRoleName
     *            the appRoleName to set
     */
    public void setAppRoleName(String appRoleName) {
        this.appRoleName = appRoleName;
    }

}
