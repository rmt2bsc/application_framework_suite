package com.api.ldap.beans;

import java.util.List;

import com.api.ldap.beans.LdapCommonEntity;

/**
 * ORM bean for mapping user related data coming from a LDAP server.
 * <p>
 * Using the java bean naming specification, the accessor and mutator method
 * names can be used to determine the name of the LDAP attribute in which they
 * manage. For example, the method <i>getFn()</i> accessing the LDAP attribute,
 * <i>fn</i>.
 * 
 * @author Roy Terrell
 * 
 */
public class LdapUser extends LdapCommonEntity {

    private String fn;

    private List<String> sn;

    private String addr;

    private String city;

    private String state;

    private String zip;

    private String loginid;

    private String loggedIn;

    private String dob;

    private String ssn;

    private String startDate;

    private String termDate;

    private String grp;

    private String grpId;

    private String country;

    private String phoneHome;

    private String phoneOffice;

    private String phoneMobile;

    private String phoneFax;

    private String phonePager;

    private String phoneOther;

    private List<String> ar;

    private List<String> as;

    private String pt;

    private String jt;

    private String email;

    private String lc;

    private String userPassword;

    /**
     * Creates a LdapUser object.
     */
    public LdapUser() {
        super();
    }

    /**
     * Return the user's first name.
     * 
     * @return String
     */
    public String getFn() {
        return fn;
    }

    /**
     * Set the user's first name.
     * 
     * @param fn
     *            Stirng
     */
    public void setFn(String fn) {
        this.fn = fn;
    }

    /**
     * Return the user's last name.
     * 
     * @return String
     */
    public List<String> getSn() {
        return sn;
    }

    /**
     * Set the user's first name.
     * 
     * @param sn
     *            an instance of {@link List} containing String objects
     */
    public void setSn(List<String> sn) {
        this.sn = sn;
    }

    /**
     * Return the user's address.
     * 
     * @return String
     */
    public String getAddr() {
        return addr;
    }

    /**
     * Set the user's address.
     * 
     * @param addr
     *            String
     */
    public void setAddr(String addr) {
        this.addr = addr;
    }

    /**
     * Return the user's city.
     * 
     * @return String
     */
    public String getCity() {
        return city;
    }

    /**
     * Set the user's city.
     * 
     * @param city
     *            String
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Return the user's state.
     * 
     * @return String
     */
    public String getState() {
        return state;
    }

    /**
     * Set the user's state.
     * 
     * @param state
     *            String
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Return the user's zip code
     * 
     * @return String
     */
    public String getZip() {
        return zip;
    }

    /**
     * Set the user's zip code
     * 
     * @param zip
     *            String
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * Return the user's login id also known as user name.
     * 
     * @return String
     */
    public String getLoginid() {
        return loginid;
    }

    /**
     * Set the user's login id also known as user name.
     * 
     * @param loginid
     *            String
     */
    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    /**
     * Return the user's date of birth.
     * 
     * @return String
     */
    public String getDob() {
        return dob;
    }

    /**
     * Set the user's date of birth.
     * 
     * @param dob
     */
    public void setDob(String dob) {
        this.dob = dob;
    }

    /**
     * Return the user's social security number
     * 
     * @return String
     */
    public String getSsn() {
        return ssn;
    }

    /**
     * Set the user's social security number
     * 
     * @param ssn
     */
    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    /**
     * Return the user's start date.
     * 
     * @return Steing
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Set the user's start date.
     * 
     * @param startDate
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Return the user's termination date.
     * 
     * @return String
     */
    public String getTermDate() {
        return termDate;
    }

    /**
     * Set the user's start date.
     * 
     * @param termDate
     */
    public void setTermDate(String termDate) {
        this.termDate = termDate;
    }

    /**
     * Return the user's group name
     * 
     * @return String
     */
    public String getGrp() {
        return grp;
    }

    /**
     * Set the user's group name
     * 
     * @param grp
     */
    public void setGrp(String grp) {
        this.grp = grp;
    }

    /**
     * Return the country.
     * 
     * @return String
     */
    public String getCountry() {
        return country;
    }

    /**
     * Set the country.
     * 
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Return the user's home phone number.
     * 
     * @return String
     */
    public String getPhoneHome() {
        return phoneHome;
    }

    /**
     * Set the user's home phone number.
     * 
     * @param phoneHome
     */
    public void setPhoneHome(String phoneHome) {
        this.phoneHome = phoneHome;
    }

    /**
     * Return the user's office phone number.
     * 
     * @return the phoneOffice
     */
    public String getPhoneOffice() {
        return phoneOffice;
    }

    /**
     * Set the user's office phone number.
     * 
     * @param phoneOffice
     */
    public void setPhoneOffice(String phoneOffice) {
        this.phoneOffice = phoneOffice;
    }

    /**
     * Return the user's celluar phone number.
     * 
     * @return String
     */
    public String getPhoneMobile() {
        return phoneMobile;
    }

    /**
     * Set the user's celluar phone number.
     * 
     * @param phoneMobile
     */
    public void setPhoneMobile(String phoneMobile) {
        this.phoneMobile = phoneMobile;
    }

    /**
     * Return the user's fax number.
     * 
     * @return String
     */
    public String getPhoneFax() {
        return phoneFax;
    }

    /**
     * Set the user's fax number.
     * 
     * @param phoneFax
     */
    public void setPhoneFax(String phoneFax) {
        this.phoneFax = phoneFax;
    }

    /**
     * Return the user's pager number.
     * 
     * @return String
     */
    public String getPhonePager() {
        return phonePager;
    }

    /**
     * Return the user's pager number.
     * 
     * @param phonePager
     */
    public void setPhonePager(String phonePager) {
        this.phonePager = phonePager;
    }

    /**
     * Return the user's other phone number.
     * 
     * @return String
     */
    public String getPhoneOther() {
        return phoneOther;
    }

    /**
     * Return the user's other phone number.
     * 
     * @param phoneOther
     */
    public void setPhoneOther(String phoneOther) {
        this.phoneOther = phoneOther;
    }

    /**
     * Return the user's granted roles.
     * 
     * @return an instance of {@link List} containing String objects
     */
    public List<String> getAr() {
        return ar;
    }

    /**
     * Set the user's granted roles.
     * 
     * @param ar
     *            a instance of {@link List} containing String objects
     */
    public void setAr(List<String> ar) {
        this.ar = ar;
    }

    /**
     * Return the user's personal title.
     * 
     * @return String
     */
    public String getPt() {
        return pt;
    }

    /**
     * Set the user's personal title.
     * 
     * @param pt
     */
    public void setPt(String pt) {
        this.pt = pt;
    }

    /**
     * Return the user's job title.
     * 
     * @return String
     */
    public String getJt() {
        return jt;
    }

    /**
     * Set the user's job title.
     * 
     * @param jt
     */
    public void setJt(String jt) {
        this.jt = jt;
    }

    /**
     * Return the user's email
     * 
     * @return String
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the user's email
     * 
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Return the user's login count
     * 
     * @return numeric String
     */
    public String getLc() {
        return lc;
    }

    /**
     * Set the user's login count
     * 
     * @param lc
     *            numeric String
     */
    public void setLc(String lc) {
        this.lc = lc;
    }

    /**
     * Return the user's password
     * 
     * @return String
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * Set the user's password
     * 
     * @param userPassword
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    /**
     * Return the user's active session id's
     * 
     * @return an instance of {@link List}
     */
    public List<String> getAs() {
        return as;
    }

    /**
     * Return the user's activve session id's
     * 
     * @param as
     *            an instance of {@link List}
     */
    public void setAs(List<String> as) {
        this.as = as;
    }

    /**
     * Return the user's logged in flag
     * 
     * @return String
     */
    public String getLoggedIn() {
        return loggedIn;
    }

    /**
     * Set the user's logged in flag
     * 
     * @param loggedIn
     */
    public void setLoggedIn(String loggedIn) {
        this.loggedIn = loggedIn;
    }

    /**
     * Return the user's group id.
     * 
     * @return String
     */
    public String getGrpId() {
        return grpId;
    }

    /**
     * Set the user's group id.
     * 
     * @param grpId
     */
    public void setGrpId(String grpId) {
        this.grpId = grpId;
    }

}
