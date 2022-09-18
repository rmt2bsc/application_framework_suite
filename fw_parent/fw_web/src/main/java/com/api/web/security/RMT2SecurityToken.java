package com.api.web.security;

import java.util.List;

import com.RMT2Base;
import com.api.security.User;

/**
 * The security token representing an authenticated user.
 * <p>
 * It stores the detailed information about the user and his/her session.
 * 
 * @author rterrell
 * 
 */
public class RMT2SecurityToken extends RMT2Base {

    /**
     * Variable used to track the source of the authentication scheme.
     * 
     */
    private Object responseData;

    /**
     * This is User instance is assigned via the update method only and is used
     * only from within the SecurityApi application context.
     */
    private User user;

    /**
     * Variable used to track data regarding the user's session
     */
    private RMT2SessionBean session;

    private int uid;

    private String loginId;

    private String description;

    private String password;

    private int totalLogons;

    private int active;

    private int personId;

    private String firstname;

    private String midname;

    private String lastname;

    private String maidenname;

    private String generation;

    private String shortname;

    private int title;

    private int genderId;

    private int maritalStatus;

    private java.util.Date birthDate;

    private int raceId;

    private String ssn;

    private String email;

    private List<String> roles;

    private int appCount;

    /**
     * Create a RMT2SecurityToken with uninitialized properties.
     */
    public RMT2SecurityToken() {
        super();
    }

    /**
     * @return the uid
     */
    public int getUid() {
        return uid;
    }

    /**
     * @return the loginId
     */
    public String getLoginId() {
        return loginId;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the totalLogons
     */
    public int getTotalLogons() {
        return totalLogons;
    }

    /**
     * @return the active
     */
    public int getActive() {
        return active;
    }

    /**
     * @return the personId
     */
    public int getPersonId() {
        return personId;
    }

    /**
     * @return the first name
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @return the mid name
     */
    public String getMidname() {
        return midname;
    }

    /**
     * @return the last name
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @return the maiden name
     */
    public String getMaidenname() {
        return maidenname;
    }

    /**
     * @return the generation
     */
    public String getGeneration() {
        return generation;
    }

    /**
     * @return the short name
     */
    public String getShortname() {
        return shortname;
    }

    /**
     * @return the title
     */
    public int getTitle() {
        return title;
    }

    /**
     * @return the genderId
     */
    public int getGenderId() {
        return genderId;
    }

    /**
     * @return the maritalStatus
     */
    public int getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * @return the birthDate
     */
    public java.util.Date getBirthDate() {
        return birthDate;
    }

    /**
     * @return the raceId
     */
    public int getRaceId() {
        return raceId;
    }

    /**
     * @return the ssn
     */
    public String getSsn() {
        return ssn;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the roles
     */
    public List<String> getRoles() {
        return roles;
    }

    /**
     * @return the appCount
     */
    public int getAppCount() {
        return appCount;
    }

    /**
     * @return the session
     */
    public RMT2SessionBean getSession() {
        return session;
    }

    /**
     * @param session
     *            the session to set
     */
    public void setSession(RMT2SessionBean session) {
        this.session = session;
    }

    /**
     * @return the user associated with this security token
     */
    public User getUser() {
        return user;
    }

    /**
     * Updates the properties of this instance with the property values
     * belonging to an implementation of User.
     * 
     * @param usr
     *            an instance of {@link User}
     */
    public void update(User usr) {
        this.active = usr.getActive();
        this.birthDate = usr.getBirthDate();
        this.description = usr.getUserDescription();
        this.email = usr.getEmail();
        this.firstname = usr.getFirstname();
        this.genderId = usr.getGenderId();
        this.generation = usr.getGeneration();
        this.lastname = usr.getLastname();
        this.loginId = usr.getLoginId();
        this.maidenname = usr.getMaidenname();
        this.maritalStatus = usr.getMaritalStatus();
        this.midname = usr.getMidname();
        this.password = usr.getPassword();
        this.personId = usr.getPersonId();
        this.raceId = usr.getRaceId();
        this.roles = usr.getRoles();
        this.shortname = usr.getShortname();
        this.ssn = usr.getSsn();
        this.title = usr.getTitle();
        this.totalLogons = usr.getTotalLogons();
        this.uid = usr.getUid();
        this.appCount = usr.getAppCount();
        this.user = usr;
        return;
    }

    /**
     * Updates the properties of this instance with any applicable changes made
     * to its internal {@link User} instance.
     */
    public void update() {
        this.update(this.user);
        return;
    }

    /**
     * @return the token
     */
    public Object getResponseData() {
        return responseData;
    }

    /**
     * @param token
     *            the token to set
     */
    public void setResponseData(Object token) {
        this.responseData = token;
    }
}
