package testcases.bean;

import java.util.Date;

import com.SystemException;
import com.api.persistence.db.orm.OrmBean;

/**
 * Peer object that maps to the customers database table/view.
 * 
 * 
 */
public class Customers extends OrmBean {

    // Property name constants that belong to respective DataSource,
    // CustomersView

    /**
     * The property name constant equivalent to property, Id, of respective
     * DataSource view.
     */
    public static final String PROP_ID = "Id";
    /**
     * The property name constant equivalent to property, Surname, of respective
     * DataSource view.
     */
    public static final String PROP_SURNAME = "Surname";
    /**
     * The property name constant equivalent to property, Givenname, of
     * respective DataSource view.
     */
    public static final String PROP_GIVENNAME = "Givenname";
    /**
     * The property name constant equivalent to property, Street, of respective
     * DataSource view.
     */
    public static final String PROP_STREET = "Street";
    /**
     * The property name constant equivalent to property, City, of respective
     * DataSource view.
     */
    public static final String PROP_CITY = "City";
    /**
     * The property name constant equivalent to property, State, of respective
     * DataSource view.
     */
    public static final String PROP_STATE = "State";
    /**
     * The property name constant equivalent to property, Country, of respective
     * DataSource view.
     */
    public static final String PROP_COUNTRY = "Country";
    /**
     * The property name constant equivalent to property, Postalcode, of
     * respective DataSource view.
     */
    public static final String PROP_POSTALCODE = "Postalcode";
    /**
     * The property name constant equivalent to property, Phone, of respective
     * DataSource view.
     */
    public static final String PROP_PHONE = "Phone";
    /**
     * The property name constant equivalent to property, Companyname, of
     * respective DataSource view.
     */
    public static final String PROP_COMPANYNAME = "Companyname";

    /** The javabean property equivalent of database column customers.id */
    private int id;
    /** The javabean property equivalent of database column customers.surname */
    private String surname;
    /** The javabean property equivalent of database column customers.givenname */
    private String givenname;
    /** The javabean property equivalent of database column customers.street */
    private String street;
    /** The javabean property equivalent of database column customers.city */
    private String city;
    /** The javabean property equivalent of database column customers.state */
    private String state;
    /** The javabean property equivalent of database column customers.country */
    private String country;
    /** The javabean property equivalent of database column customers.postalcode */
    private String postalcode;
    /** The javabean property equivalent of database column customers.phone */
    private String phone;
    /**
     * The javabean property equivalent of database column customers.companyname
     */
    private String companyname;

    // Getter/Setter Methods

    /**
     * Default constructor.
     * 
     * 
     */
    public Customers() throws SystemException {
        super();
    }

    /**
     * Sets the value of member variable id
     * 
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Gets the value of member variable id
     * 
     * 
     */
    public int getId() {
        return this.id;
    }

    /**
     * Stubbed initialization method designed to implemented by developer.
     * 
     * 
     * 
     */
    public void initBean() throws SystemException {
    }

    /**
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname
     *            the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @return the givenname
     */
    public String getGivenname() {
        return givenname;
    }

    /**
     * @param givenname
     *            the givenname to set
     */
    public void setGivenname(String givenname) {
        this.givenname = givenname;
    }

    /**
     * @return the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * @param street
     *            the street to set
     */
    public void setStreet(String street) {
        this.street = street;
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
     * @return the postalcode
     */
    public String getPostalcode() {
        return postalcode;
    }

    /**
     * @param postalcode
     *            the postalcode to set
     */
    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
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
     * @return the companyname
     */
    public String getCompanyname() {
        return companyname;
    }

    /**
     * @param companyname
     *            the companyname to set
     */
    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }
}