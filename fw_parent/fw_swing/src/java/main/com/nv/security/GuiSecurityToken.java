package com.nv.security;

import java.util.Date;

/**
 * A valud object designed to manage security information as it relates to the
 * application environment and the logged on user.
 * <p>
 * The security token represents an authenticated user and is used to gain
 * access to various resources pertaining to the application.
 * 
 * @author rterrell
 *
 */
public class GuiSecurityToken {

    private String firstName;

    private String lastName;

    private String userId;

    private Date signOn;

    private Date lastActivity;

    private String server;

    private int securityLevel;

    private int timeOutInterval;

    /**
     * Creates a GuiSecurityToken in which non of its properties are
     * intitialized.
     */
    protected GuiSecurityToken() {
        return;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName
     *            the firstName to set
     */
    protected void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName
     *            the lastName to set
     */
    protected void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    protected void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the signOn
     */
    public Date getSignOn() {
        return signOn;
    }

    /**
     * @param signOn
     *            the signOn to set
     */
    protected void setSignOn(Date signOn) {
        this.signOn = signOn;
    }

    /**
     * @return the lastActivity
     */
    public Date getLastActivity() {
        return lastActivity;
    }

    /**
     * @param lastActivity
     *            the lastActivity to set
     */
    protected void setLastActivity(Date lastActivity) {
        this.lastActivity = lastActivity;
    }

    /**
     * @return the server
     */
    public String getServer() {
        return server;
    }

    /**
     * @param server
     *            the server to set
     */
    protected void setServer(String server) {
        this.server = server;
    }

    /**
     * @return the securityLevel
     */
    public int getSecurityLevel() {
        return securityLevel;
    }

    /**
     * @param securityLevel
     *            the securityLevel to set
     */
    protected void setSecurityLevel(int securityLevel) {
        this.securityLevel = securityLevel;
    }

    /**
     * @return the timeOutInterval
     */
    public int getTimeOutInterval() {
        return timeOutInterval;
    }

    /**
     * @param timeOutInterval
     *            the timeOutInterval to set
     */
    protected void setTimeOutInterval(int timeOutInterval) {
        this.timeOutInterval = timeOutInterval;
    }

}
