package testcases.bean;

import java.util.Date;

import com.SystemException;
import com.api.persistence.db.orm.OrmBean;

/**
 * Peer object that maps to the contacts database table/view.
 * 
 * 
 */
public class Contacts extends OrmBean {

    // Property name constants that belong to respective DataSource,
    // ContactsView

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
     * The property name constant equivalent to property, Title, of respective
     * DataSource view.
     */
    public static final String PROP_TITLE = "Title";
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
     * The property name constant equivalent to property, Fax, of respective
     * DataSource view.
     */
    public static final String PROP_FAX = "Fax";
    /**
     * The property name constant equivalent to property, Customerid, of
     * respective DataSource view.
     */
    public static final String PROP_CUSTOMERID = "Customerid";

    /** The javabean property equivalent of database column contacts.id */
    private int id;
    /** The javabean property equivalent of database column contacts.surname */
    private String surname;
    /** The javabean property equivalent of database column contacts.givenname */
    private String givenname;
    /** The javabean property equivalent of database column contacts.title */
    private String title;
    /** The javabean property equivalent of database column contacts.street */
    private String street;
    /** The javabean property equivalent of database column contacts.city */
    private String city;
    /** The javabean property equivalent of database column contacts.state */
    private String state;
    /** The javabean property equivalent of database column contacts.country */
    private String country;
    /** The javabean property equivalent of database column contacts.postalcode */
    private String postalcode;
    /** The javabean property equivalent of database column contacts.phone */
    private String phone;
    /** The javabean property equivalent of database column contacts.fax */
    private String fax;
    /** The javabean property equivalent of database column contacts.customerid */
    private int customerid;

    // Getter/Setter Methods

    /**
     * Default constructor.
     * 
     * 
     */
    public Contacts() throws SystemException {
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
     * Sets the value of member variable surname
     * 
     * 
     */
    public void setSurname(String value) {
        this.surname = value;
    }

    /**
     * Gets the value of member variable surname
     * 
     * 
     */
    public String getSurname() {
        return this.surname;
    }

    /**
     * Sets the value of member variable givenname
     * 
     * 
     */
    public void setGivenname(String value) {
        this.givenname = value;
    }

    /**
     * Gets the value of member variable givenname
     * 
     * 
     */
    public String getGivenname() {
        return this.givenname;
    }

    /**
     * Sets the value of member variable title
     * 
     * 
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of member variable title
     * 
     * 
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the value of member variable street
     * 
     * 
     */
    public void setStreet(String value) {
        this.street = value;
    }

    /**
     * Gets the value of member variable street
     * 
     * 
     */
    public String getStreet() {
        return this.street;
    }

    /**
     * Sets the value of member variable city
     * 
     * 
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of member variable city
     * 
     * 
     */
    public String getCity() {
        return this.city;
    }

    /**
     * Sets the value of member variable state
     * 
     * 
     */
    public void setState(String value) {
        this.state = value;
    }

    /**
     * Gets the value of member variable state
     * 
     * 
     */
    public String getState() {
        return this.state;
    }

    /**
     * Sets the value of member variable country
     * 
     * 
     */
    public void setCountry(String value) {
        this.country = value;
    }

    /**
     * Gets the value of member variable country
     * 
     * 
     */
    public String getCountry() {
        return this.country;
    }

    /**
     * Sets the value of member variable postalcode
     * 
     * 
     */
    public void setPostalcode(String value) {
        this.postalcode = value;
    }

    /**
     * Gets the value of member variable postalcode
     * 
     * 
     */
    public String getPostalcode() {
        return this.postalcode;
    }

    /**
     * Sets the value of member variable phone
     * 
     * 
     */
    public void setPhone(String value) {
        this.phone = value;
    }

    /**
     * Gets the value of member variable phone
     * 
     * 
     */
    public String getPhone() {
        return this.phone;
    }

    /**
     * Sets the value of member variable fax
     * 
     * 
     */
    public void setFax(String value) {
        this.fax = value;
    }

    /**
     * Gets the value of member variable fax
     * 
     * 
     */
    public String getFax() {
        return this.fax;
    }

    /**
     * Sets the value of member variable customerid
     * 
     * 
     */
    public void setCustomerid(int value) {
        this.customerid = value;
    }

    /**
     * Gets the value of member variable customerid
     * 
     * 
     */
    public int getCustomerid() {
        return this.customerid;
    }

    /**
     * Stubbed initialization method designed to implemented by developer.
     * 
     * 
     * 
     */
    public void initBean() throws SystemException {
    }
}