package com.api.security.authentication.web;

import com.RMT2Base;

/**
 * Bean containing user authenticaion credentials
 * 
 * 
 */
public class UserCredentials extends RMT2Base {

    private String userId;

    private String password;

    private String appCode;

    private String currentSessionId;

    /**
     * Default constructor.
     * 
     * @author rterrell
     */
    public UserCredentials() {
        super();
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
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the appCode
     */
    public String getAppCode() {
        return appCode;
    }

    /**
     * @param appCode
     *            the appCode to set
     */
    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    /**
     * @return the currentSessionId
     */
    public String getCurrentSessionId() {
        return currentSessionId;
    }

    /**
     * @param currentSessionId
     *            the currentSessionId to set
     */
    public void setCurrentSessionId(String currentSessionId) {
        this.currentSessionId = currentSessionId;
    }
}