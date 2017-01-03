package com.api.ldap.beans;

/**
 * @author rterrell
 * 
 */
public class LdapComputerDatabase extends LdapCommonComputer {

    private String serverConnectString;

    private String serverDriver;

    private String serverPw;

    private String serveruser;

    private String dbDataSource;

    public LdapComputerDatabase() {
        super();
    }

    /**
     * @return the serverConnectString
     */
    public String getServerConnectString() {
        return serverConnectString;
    }

    /**
     * @param serverConnectString
     *            the serverConnectString to set
     */
    public void setServerConnectString(String serverConnectString) {
        this.serverConnectString = serverConnectString;
    }

    /**
     * @return the serverDriver
     */
    public String getServerDriver() {
        return serverDriver;
    }

    /**
     * @param serverDriver
     *            the serverDriver to set
     */
    public void setServerDriver(String serverDriver) {
        this.serverDriver = serverDriver;
    }

    /**
     * @return the serverPw
     */
    public String getServerPw() {
        return serverPw;
    }

    /**
     * @param serverPw
     *            the serverPw to set
     */
    public void setServerPw(String serverPw) {
        this.serverPw = serverPw;
    }

    /**
     * @return the serveruser
     */
    public String getServeruser() {
        return serveruser;
    }

    /**
     * @param serveruser
     *            the serveruser to set
     */
    public void setServeruser(String serveruser) {
        this.serveruser = serveruser;
    }

    /**
     * @return the dbDataSource
     */
    public String getDbDataSource() {
        return dbDataSource;
    }

    /**
     * @param dbDataSource
     *            the dbDataSource to set
     */
    public void setDbDataSource(String dbDataSource) {
        this.dbDataSource = dbDataSource;
    }
}
