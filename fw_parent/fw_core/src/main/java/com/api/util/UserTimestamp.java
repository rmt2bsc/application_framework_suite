package com.api.util;

import java.util.Date;

import com.RMT2BaseBean;
import com.SystemException;

/**
 * Javabean that stores the current time and user id that is normally used to
 * track user timestamps of database actions.
 * 
 * @author Roy Terrell.
 */
public class UserTimestamp extends RMT2BaseBean {
    private static final long serialVersionUID = 1L;

    /** Date of timestamp */
    private Date dateCreated;

    /** The user id of timestamp */
    private String loginId;

    /** The IP address of the user */
    private String ipAddr;

    /**
     * Default constructor.
     * 
     * @author Roy Terrell.
     */
    public UserTimestamp() throws SystemException {
        return;
    }

    /**
     * Cerates an instance of {@link UserTimestamp} and returns to caller
     * 
     * @return UserTimestamp
     */
    public static UserTimestamp getInstance() {
        try {
            return new UserTimestamp();
        } catch (SystemException e) {
            return null;
        }
    }

    /**
     * Sets the value of member variable Date Created
     * 
     */
    public void setDateCreated(Date value) {
        this.dateCreated = value;
    }

    /**
     * Gets the value of member variable Date Created
     * 
     * @return Date
     */
    public Date getDateCreated() {
        return this.dateCreated;
    }

    /**
     * Sets the value of member variable Login Id
     * 
     */
    public void setLoginId(String value) {
        this.loginId = value;
    }

    /**
     * Gets the value of member variable Login Id
     * 
     * @return String
     */
    public String getLoginId() {
        return this.loginId;
    }

    /**
     * Stubbed initialization method designed to implemented by developer.
     * 
     * @author Roy Terrell.
     */
    public void initBean() throws SystemException {
    }

    /**
     * @return the ipAddr
     */
    public String getIpAddr() {
        return ipAddr;
    }

    /**
     * @param ipAddr
     *            the ipAddr to set
     */
    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }
}