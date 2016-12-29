package com.api.ldap.beans;

/**
 * ORM bean for mapping computer data as it relates to database servers coming
 * from a LDAP server.
 * 
 * @author Roy Terrell
 * 
 */
public class LdapComputerDatabase extends LdapCommonComputer {

    private String serverConnectString;

    private String serverDriver;

    private String serverPw;

    private String serveruser;

    /**
     * Creates a LdapComputerDatabase object withou intializing any of its
     * properties.
     */
    public LdapComputerDatabase() {
        super();
    }

    /**
     * Get database conection string or URL
     * 
     * @return String
     */
    public String getServerConnectString() {
        return serverConnectString;
    }

    /**
     * Set database conection string or URL
     * 
     * @param serverConnectString
     *            String
     */
    public void setServerConnectString(String serverConnectString) {
        this.serverConnectString = serverConnectString;
    }

    /**
     * Get the database driver
     * 
     * @return String
     */
    public String getServerDriver() {
        return serverDriver;
    }

    /**
     * set the database driver
     * 
     * @param serverDriver
     *            String
     */
    public void setServerDriver(String serverDriver) {
        this.serverDriver = serverDriver;
    }

    /**
     * Get the password to the database server
     * 
     * @return String
     */
    public String getServerPw() {
        return serverPw;
    }

    /**
     * Set the password to the database server
     * 
     * @param serverPw
     *            String
     */
    public void setServerPw(String serverPw) {
        this.serverPw = serverPw;
    }

    /**
     * Get the login id
     * 
     * @return String
     */
    public String getServeruser() {
        return serveruser;
    }

    /**
     * Set the login id
     * 
     * @param serveruser
     *            String
     */
    public void setServeruser(String serveruser) {
        this.serveruser = serveruser;
    }

}
