package com.api.security;

import java.util.List;

/**
 * Contract for a User instance.
 * 
 * @author rterrell
 * 
 */
public interface User {

    /**
     * @return the uid
     */
    int getUid();

    /**
     * @param uid
     *            the uid to set
     */
    void setUid(int uid);

    /**
     * @return the loginId
     */
    String getLoginId();

    /**
     * @param loginId
     *            the loginId to set
     */
    void setLoginId(String loginId);

    /**
     * @return the description
     */
    String getUserDescription();

    /**
     * @param description
     *            the description to set
     */
    void setUserDescription(String description);

    /**
     * @return the password
     */
    String getPassword();

    /**
     * @param password
     *            the password to set
     */
    void setPassword(String password);

    /**
     * @return the totalLogons
     */
    int getTotalLogons();

    /**
     * @param totalLogons
     *            the totalLogons to set
     */
    void setTotalLogons(int totalLogons);

    /**
     * @return the active
     */
    int getActive();

    /**
     * @param active
     *            the active to set
     */
    void setActive(int active);

    /**
     * @return the personId
     */
    int getPersonId();

    /**
     * @param personId
     *            the personId to set
     */
    void setPersonId(int personId);

    /**
     * @return the firstname
     */
    String getFirstname();

    /**
     * @param firstname
     *            the firstname to set
     */
    void setFirstname(String firstname);

    /**
     * @return the midname
     */
    String getMidname();

    /**
     * @param midname
     *            the midname to set
     */
    void setMidname(String midname);

    /**
     * @return the lastname
     */
    String getLastname();

    /**
     * @param lastname
     *            the lastname to set
     */
    void setLastname(String lastname);

    /**
     * @return the maidenname
     */
    String getMaidenname();

    /**
     * @param maidenname
     *            the maidenname to set
     */
    void setMaidenname(String maidenname);

    /**
     * @return the generation
     */
    String getGeneration();

    /**
     * @param generation
     *            the generation to set
     */
    void setGeneration(String generation);

    /**
     * @return the shortname
     */
    String getShortname();

    /**
     * @param shortname
     *            the shortname to set
     */
    void setShortname(String shortname);

    /**
     * @return the title
     */
    int getTitle();

    /**
     * @param title
     *            the title to set
     */
    void setTitle(int title);

    /**
     * @return the title Description
     */
    String getTitleName();

    /**
     * @param title
     *            the title description
     */
    void setTitleName(String value);

    /**
     * @return the genderId
     */
    int getGenderId();

    /**
     * @param genderId
     *            the genderId to set
     */
    void setGenderId(int genderId);

    /**
     * @return the maritalStatus
     */
    int getMaritalStatus();

    /**
     * @param maritalStatus
     *            the maritalStatus to set
     */
    void setMaritalStatus(int maritalStatus);

    /**
     * @return the birthDate
     */
    java.util.Date getBirthDate();

    /**
     * @param birthDate
     *            the birthDate to set
     */
    void setBirthDate(java.util.Date birthDate);

    /**
     * @return the raceId
     */
    int getRaceId();

    /**
     * @param raceId
     *            the raceId to set
     */
    void setRaceId(int raceId);

    /**
     * @return the ssn
     */
    String getSsn();

    /**
     * @param ssn
     *            the ssn to set
     */
    void setSsn(String ssn);

    /**
     * @return the email
     */
    String getEmail();

    /**
     * @param email
     *            the email to set
     */
    void setEmail(String email);

    /**
     * @return the roles
     */
    List<String> getRoles();

    /**
     * @param roles
     *            the roles to set
     */
    void setRoles(List<String> roles);

    /**
     * Returns the total number of appliations the user is signed on.
     * 
     * @return the total number of appliations the user is signed on.
     */
    int getAppCount();

    /**
     * Sets the total number of applications the user is signed on.
     * 
     * @param count
     *            the application count.
     */
    void setAppCount(int count);

    /**
     * Increments the total number of applications a user is signed on by 1.
     */
    void incrementAppCount();

    /**
     * Decrements the total number of applications a user is signed on by 1.
     */
    void decrementAppCount();

    /**
     * 
     * @param role
     */
    void addRole(String role);

    /**
     * Get Address
     * 
     * @return
     */
    String getAddress();

    /**
     * Set Address
     * 
     * @param value
     */
    void setAddress(String value);

    /**
     * Get City
     * 
     * @return
     */
    String getCity();

    /**
     * Set City
     * 
     * @param value
     */
    void setCity(String value);

    /**
     * Get State
     * 
     * @return
     */
    String getState();

    /**
     * Set State
     * 
     * @param value
     */
    void setState(String value);

    /**
     * Get Zip
     * 
     * @return
     */
    String getZip();

    /**
     * Set Zip
     * 
     * @param value
     */
    void setZip(String value);

    /**
     * Get Country
     * 
     * @return
     */
    String getCountry();

    /**
     * Set Country
     * 
     * @param value
     */
    void setCountry(String value);

    /**
     * Get Home Phone
     * 
     * @return
     */
    String getHomePhone();

    /**
     * Set Home Phone
     * 
     * @param value
     */
    void setHomePhone(String value);

    /**
     * Get Work Phone
     * 
     * @return
     */
    String getOfficePhone();

    /**
     * Set Work Phone
     * 
     * @param value
     */
    void setOfficePhone(String value);

    /**
     * Get Mobile Phone
     * 
     * @return
     */
    String getMobilePhone();

    /**
     * Set mobile phone
     * 
     * @param value
     */
    void setMobilePhone(String value);

    /**
     * Get Fax.
     * 
     * @return
     */
    String getFax();

    /**
     * Set Fax.
     * 
     * @param value
     */
    void setFax(String value);

    /**
     * Get other phone
     * 
     * @return
     */
    String getOtherPhone();

    /**
     * Set other phone
     * 
     * @param value
     */
    void setOtherPhone(String value);

    /**
     * Get pager number
     * 
     * @return
     */
    String getPager();

    /**
     * Set pager number
     * 
     * @param value
     */
    void setPager(String value);

    /**
     * Return the user's group description
     * 
     * @return group name.
     */
    String getGrp();

    /**
     * Set the user's group description
     * 
     * @param value
     *            group name.
     */
    void setGrp(String value);

    /**
     * Return the user's group description
     * 
     * @return group description.
     */
    String getGrpDescription();

    /**
     * Set the user's group description
     * 
     * @param value
     *            group description.
     */
    void setGrpDescription(String value);

    /**
     * Get the user's job title description
     * 
     * @return user's job title description
     */
    String getJobTitleDescription();

    /**
     * Set the user's job title description
     * 
     * @param password
     *            the user's job title description
     */
    void setJobTitleDescription(String value);

    /**
     * Returns the user's login status.
     * 
     * @return true when user is logged in to one or more applications.
     *         Otherwise, false is returned.
     */
    int getLoggedIn();

    /**
     * Sets the user's login status.
     * 
     * @param flag
     *            true = login and false = not logged in
     */
    void setLoggedIn(int flag);

    /**
     * Sets the value of member variable startDate
     */
    void setStartDate(java.util.Date value);

    /**
     * Gets the value of member variable startDate
     */
    java.util.Date getStartDate();

    /**
     * Sets the value of member variable terminationDate
     */
    void setTerminationDate(java.util.Date value);

    /**
     * Gets the value of member variable terminationDate
     */
    java.util.Date getTerminationDate();

}