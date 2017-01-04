/**
 * 
 */
package com.api.security.creditcard;

import com.RMT2BaseBean;
import com.SystemException;

/**
 * Manages credit card address details that are normally required for Address
 * Verification Service (AVS).
 * 
 * @author RTerrell
 * 
 */
class AddressVerification extends RMT2BaseBean {
    private static final long serialVersionUID = 5804431118868031623L;

    private String firstName;

    private String lastName;

    private String addr1;

    private String addr2;

    private String city;

    private String state;

    private String zip;

    private String zipExt;

    private String phone;

    private String country;

    /**
     * 
     * @throws SystemException
     */
    public AddressVerification() throws SystemException {
        super();
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see bean.RMT2BaseBean#initBean()
     */
    public void initBean() throws SystemException {
        // TODO Auto-generated method stub

    }

    /**
     * @return the addr1
     */
    public String getAddr1() {
        return addr1;
    }

    /**
     * @param addr1
     *            the addr1 to set
     */
    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    /**
     * @return the addr2
     */
    public String getAddr2() {
        return addr2;
    }

    /**
     * @param addr2
     *            the addr2 to set
     */
    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city
     *            the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country
     *            the country to set
     */
    public void setCountry(String country) {
        this.country = country;
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
    public void setFirstName(String firstName) {
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
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     *            the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state
     *            the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the zip
     */
    public String getZip() {
        return zip;
    }

    /**
     * @param zip
     *            the zip to set
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * @return the zipExt
     */
    public String getZipExt() {
        return zipExt;
    }

    /**
     * @param zipExt
     *            the zipExt to set
     */
    public void setZipExt(String zipExt) {
        this.zipExt = zipExt;
    }

}
