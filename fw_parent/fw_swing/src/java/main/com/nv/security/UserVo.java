package com.nv.security;

import java.util.Date;
import java.util.List;

import com.api.security.User;

/**
 * @author rterrell
 *
 */
public class UserVo implements User {

    private String loginId;
    private String password;

    /**
     * 
     */
    public UserVo() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getUid()
     */
    @Override
    public int getUid() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setUid(int)
     */
    @Override
    public void setUid(int uid) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getLoginId()
     */
    @Override
    public String getLoginId() {
        return this.loginId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setLoginId(java.lang.String)
     */
    @Override
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getUserDescription()
     */
    @Override
    public String getUserDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setUserDescription(java.lang.String)
     */
    @Override
    public void setUserDescription(String description) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getPassword()
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setPassword(java.lang.String)
     */
    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getTotalLogons()
     */
    @Override
    public int getTotalLogons() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setTotalLogons(int)
     */
    @Override
    public void setTotalLogons(int totalLogons) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getActive()
     */
    @Override
    public int getActive() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setActive(int)
     */
    @Override
    public void setActive(int active) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getPersonId()
     */
    @Override
    public int getPersonId() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setPersonId(int)
     */
    @Override
    public void setPersonId(int personId) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getFirstname()
     */
    @Override
    public String getFirstname() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setFirstname(java.lang.String)
     */
    @Override
    public void setFirstname(String firstname) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getMidname()
     */
    @Override
    public String getMidname() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setMidname(java.lang.String)
     */
    @Override
    public void setMidname(String midname) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getLastname()
     */
    @Override
    public String getLastname() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setLastname(java.lang.String)
     */
    @Override
    public void setLastname(String lastname) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getMaidenname()
     */
    @Override
    public String getMaidenname() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setMaidenname(java.lang.String)
     */
    @Override
    public void setMaidenname(String maidenname) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getGeneration()
     */
    @Override
    public String getGeneration() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setGeneration(java.lang.String)
     */
    @Override
    public void setGeneration(String generation) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getShortname()
     */
    @Override
    public String getShortname() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setShortname(java.lang.String)
     */
    @Override
    public void setShortname(String shortname) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getTitle()
     */
    @Override
    public int getTitle() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setTitle(int)
     */
    @Override
    public void setTitle(int title) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getTitleName()
     */
    @Override
    public String getTitleName() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setTitleName(java.lang.String)
     */
    @Override
    public void setTitleName(String value) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getGenderId()
     */
    @Override
    public int getGenderId() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setGenderId(int)
     */
    @Override
    public void setGenderId(int genderId) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getMaritalStatus()
     */
    @Override
    public int getMaritalStatus() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setMaritalStatus(int)
     */
    @Override
    public void setMaritalStatus(int maritalStatus) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getBirthDate()
     */
    @Override
    public Date getBirthDate() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setBirthDate(java.util.Date)
     */
    @Override
    public void setBirthDate(Date birthDate) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getRaceId()
     */
    @Override
    public int getRaceId() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setRaceId(int)
     */
    @Override
    public void setRaceId(int raceId) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getSsn()
     */
    @Override
    public String getSsn() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setSsn(java.lang.String)
     */
    @Override
    public void setSsn(String ssn) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getEmail()
     */
    @Override
    public String getEmail() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setEmail(java.lang.String)
     */
    @Override
    public void setEmail(String email) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getRoles()
     */
    @Override
    public List<String> getRoles() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setRoles(java.util.List)
     */
    @Override
    public void setRoles(List<String> roles) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getAppCount()
     */
    @Override
    public int getAppCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setAppCount(int)
     */
    @Override
    public void setAppCount(int count) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#incrementAppCount()
     */
    @Override
    public void incrementAppCount() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#decrementAppCount()
     */
    @Override
    public void decrementAppCount() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#addRole(java.lang.String)
     */
    @Override
    public void addRole(String role) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getAddress()
     */
    @Override
    public String getAddress() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setAddress(java.lang.String)
     */
    @Override
    public void setAddress(String value) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getCity()
     */
    @Override
    public String getCity() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setCity(java.lang.String)
     */
    @Override
    public void setCity(String value) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getState()
     */
    @Override
    public String getState() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setState(java.lang.String)
     */
    @Override
    public void setState(String value) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getZip()
     */
    @Override
    public String getZip() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setZip(java.lang.String)
     */
    @Override
    public void setZip(String value) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getCountry()
     */
    @Override
    public String getCountry() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setCountry(java.lang.String)
     */
    @Override
    public void setCountry(String value) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getHomePhone()
     */
    @Override
    public String getHomePhone() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setHomePhone(java.lang.String)
     */
    @Override
    public void setHomePhone(String value) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getOfficePhone()
     */
    @Override
    public String getOfficePhone() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setOfficePhone(java.lang.String)
     */
    @Override
    public void setOfficePhone(String value) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getMobilePhone()
     */
    @Override
    public String getMobilePhone() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setMobilePhone(java.lang.String)
     */
    @Override
    public void setMobilePhone(String value) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getFax()
     */
    @Override
    public String getFax() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setFax(java.lang.String)
     */
    @Override
    public void setFax(String value) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getOtherPhone()
     */
    @Override
    public String getOtherPhone() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setOtherPhone(java.lang.String)
     */
    @Override
    public void setOtherPhone(String value) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getPager()
     */
    @Override
    public String getPager() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setPager(java.lang.String)
     */
    @Override
    public void setPager(String value) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getGrp()
     */
    @Override
    public String getGrp() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setGrp(java.lang.String)
     */
    @Override
    public void setGrp(String value) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getGrpDescription()
     */
    @Override
    public String getGrpDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setGrpDescription(java.lang.String)
     */
    @Override
    public void setGrpDescription(String value) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getJobTitleDescription()
     */
    @Override
    public String getJobTitleDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setJobTitleDescription(java.lang.String)
     */
    @Override
    public void setJobTitleDescription(String value) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getLoggedIn()
     */
    @Override
    public int getLoggedIn() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setLoggedIn(int)
     */
    @Override
    public void setLoggedIn(int flag) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setStartDate(java.util.Date)
     */
    @Override
    public void setStartDate(Date value) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getStartDate()
     */
    @Override
    public Date getStartDate() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#setTerminationDate(java.util.Date)
     */
    @Override
    public void setTerminationDate(Date value) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.security.User#getTerminationDate()
     */
    @Override
    public Date getTerminationDate() {
        // TODO Auto-generated method stub
        return null;
    }

}
