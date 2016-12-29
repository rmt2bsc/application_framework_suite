package testcases.bean;

import java.util.Date;

import com.SystemException;
import com.api.persistence.db.orm.OrmBean;

/**
 * Peer object that maps to the employees database table/view.
 * 
 * 
 */
public class Employees extends OrmBean {

    // Property name constants that belong to respective DataSource,
    // EmployeesView

    /**
     * The property name constant equivalent to property, Employeeid, of
     * respective DataSource view.
     */
    public static final String PROP_EMPLOYEEID = "Employeeid";
    /**
     * The property name constant equivalent to property, Managerid, of
     * respective DataSource view.
     */
    public static final String PROP_MANAGERID = "Managerid";
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
     * The property name constant equivalent to property, Departmentid, of
     * respective DataSource view.
     */
    public static final String PROP_DEPARTMENTID = "Departmentid";
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
     * The property name constant equivalent to property, Status, of respective
     * DataSource view.
     */
    public static final String PROP_STATUS = "Status";
    /**
     * The property name constant equivalent to property, Socialsecuritynumber,
     * of respective DataSource view.
     */
    public static final String PROP_SOCIALSECURITYNUMBER = "Socialsecuritynumber";
    /**
     * The property name constant equivalent to property, Salary, of respective
     * DataSource view.
     */
    public static final String PROP_SALARY = "Salary";
    /**
     * The property name constant equivalent to property, Startdate, of
     * respective DataSource view.
     */
    public static final String PROP_STARTDATE = "Startdate";
    /**
     * The property name constant equivalent to property, Terminationdate, of
     * respective DataSource view.
     */
    public static final String PROP_TERMINATIONDATE = "Terminationdate";
    /**
     * The property name constant equivalent to property, Birthdate, of
     * respective DataSource view.
     */
    public static final String PROP_BIRTHDATE = "Birthdate";
    /**
     * The property name constant equivalent to property,
     * Benefithealthinsurance, of respective DataSource view.
     */
    public static final String PROP_BENEFITHEALTHINSURANCE = "Benefithealthinsurance";
    /**
     * The property name constant equivalent to property, Benefitlifeinsurance,
     * of respective DataSource view.
     */
    public static final String PROP_BENEFITLIFEINSURANCE = "Benefitlifeinsurance";
    /**
     * The property name constant equivalent to property, Benefitdaycare, of
     * respective DataSource view.
     */
    public static final String PROP_BENEFITDAYCARE = "Benefitdaycare";
    /**
     * The property name constant equivalent to property, Sex, of respective
     * DataSource view.
     */
    public static final String PROP_SEX = "Sex";

    /** The javabean property equivalent of database column employees.employeeid */
    private int employeeid;
    /** The javabean property equivalent of database column employees.managerid */
    private int managerid;
    /** The javabean property equivalent of database column employees.surname */
    private char surname;
    /** The javabean property equivalent of database column employees.givenname */
    private char givenname;
    /**
     * The javabean property equivalent of database column
     * employees.departmentid
     */
    private int departmentid;
    /** The javabean property equivalent of database column employees.street */
    private char street;
    /** The javabean property equivalent of database column employees.city */
    private char city;
    /** The javabean property equivalent of database column employees.state */
    private char state;
    /** The javabean property equivalent of database column employees.country */
    private char country;
    /** The javabean property equivalent of database column employees.postalcode */
    private char postalcode;
    /** The javabean property equivalent of database column employees.phone */
    private char phone;
    /** The javabean property equivalent of database column employees.status */
    private char status;
    /**
     * The javabean property equivalent of database column
     * employees.socialsecuritynumber
     */
    private char socialsecuritynumber;
    /** The javabean property equivalent of database column employees.salary */
    private double salary;
    /** The javabean property equivalent of database column employees.startdate */
    private java.util.Date startdate;
    /**
     * The javabean property equivalent of database column
     * employees.terminationdate
     */
    private java.util.Date terminationdate;
    /** The javabean property equivalent of database column employees.birthdate */
    private java.util.Date birthdate;
    /**
     * The javabean property equivalent of database column
     * employees.benefithealthinsurance
     */
    private int benefithealthinsurance;
    /**
     * The javabean property equivalent of database column
     * employees.benefitlifeinsurance
     */
    private int benefitlifeinsurance;
    /**
     * The javabean property equivalent of database column
     * employees.benefitdaycare
     */
    private int benefitdaycare;
    /** The javabean property equivalent of database column employees.sex */
    private char sex;

    // Getter/Setter Methods

    /**
     * Default constructor.
     * 
     * 
     */
    public Employees() throws SystemException {
        super();
    }

    /**
     * Sets the value of member variable employeeid
     * 
     * 
     */
    public void setEmployeeid(int value) {
        this.employeeid = value;
    }

    /**
     * Gets the value of member variable employeeid
     * 
     * 
     */
    public int getEmployeeid() {
        return this.employeeid;
    }

    /**
     * Sets the value of member variable managerid
     * 
     * 
     */
    public void setManagerid(int value) {
        this.managerid = value;
    }

    /**
     * Gets the value of member variable managerid
     * 
     * 
     */
    public int getManagerid() {
        return this.managerid;
    }

    /**
     * Sets the value of member variable surname
     * 
     * 
     */
    public void setSurname(char value) {
        this.surname = value;
    }

    /**
     * Gets the value of member variable surname
     * 
     * 
     */
    public char getSurname() {
        return this.surname;
    }

    /**
     * Sets the value of member variable givenname
     * 
     * 
     */
    public void setGivenname(char value) {
        this.givenname = value;
    }

    /**
     * Gets the value of member variable givenname
     * 
     * 
     */
    public char getGivenname() {
        return this.givenname;
    }

    /**
     * Sets the value of member variable departmentid
     * 
     * 
     */
    public void setDepartmentid(int value) {
        this.departmentid = value;
    }

    /**
     * Gets the value of member variable departmentid
     * 
     * 
     */
    public int getDepartmentid() {
        return this.departmentid;
    }

    /**
     * Sets the value of member variable street
     * 
     * 
     */
    public void setStreet(char value) {
        this.street = value;
    }

    /**
     * Gets the value of member variable street
     * 
     * 
     */
    public char getStreet() {
        return this.street;
    }

    /**
     * Sets the value of member variable city
     * 
     * 
     */
    public void setCity(char value) {
        this.city = value;
    }

    /**
     * Gets the value of member variable city
     * 
     * 
     */
    public char getCity() {
        return this.city;
    }

    /**
     * Sets the value of member variable state
     * 
     * 
     */
    public void setState(char value) {
        this.state = value;
    }

    /**
     * Gets the value of member variable state
     * 
     * 
     */
    public char getState() {
        return this.state;
    }

    /**
     * Sets the value of member variable country
     * 
     * 
     */
    public void setCountry(char value) {
        this.country = value;
    }

    /**
     * Gets the value of member variable country
     * 
     * 
     */
    public char getCountry() {
        return this.country;
    }

    /**
     * Sets the value of member variable postalcode
     * 
     * 
     */
    public void setPostalcode(char value) {
        this.postalcode = value;
    }

    /**
     * Gets the value of member variable postalcode
     * 
     * 
     */
    public char getPostalcode() {
        return this.postalcode;
    }

    /**
     * Sets the value of member variable phone
     * 
     * 
     */
    public void setPhone(char value) {
        this.phone = value;
    }

    /**
     * Gets the value of member variable phone
     * 
     * 
     */
    public char getPhone() {
        return this.phone;
    }

    /**
     * Sets the value of member variable status
     * 
     * 
     */
    public void setStatus(char value) {
        this.status = value;
    }

    /**
     * Gets the value of member variable status
     * 
     * 
     */
    public char getStatus() {
        return this.status;
    }

    /**
     * Sets the value of member variable socialsecuritynumber
     * 
     * 
     */
    public void setSocialsecuritynumber(char value) {
        this.socialsecuritynumber = value;
    }

    /**
     * Gets the value of member variable socialsecuritynumber
     * 
     * 
     */
    public char getSocialsecuritynumber() {
        return this.socialsecuritynumber;
    }

    /**
     * Sets the value of member variable salary
     * 
     * 
     */
    public void setSalary(double value) {
        this.salary = value;
    }

    /**
     * Gets the value of member variable salary
     * 
     * 
     */
    public double getSalary() {
        return this.salary;
    }

    /**
     * Sets the value of member variable startdate
     * 
     * 
     */
    public void setStartdate(java.util.Date value) {
        this.startdate = value;
    }

    /**
     * Gets the value of member variable startdate
     * 
     * 
     */
    public java.util.Date getStartdate() {
        return this.startdate;
    }

    /**
     * Sets the value of member variable terminationdate
     * 
     * 
     */
    public void setTerminationdate(java.util.Date value) {
        this.terminationdate = value;
    }

    /**
     * Gets the value of member variable terminationdate
     * 
     * 
     */
    public java.util.Date getTerminationdate() {
        return this.terminationdate;
    }

    /**
     * Sets the value of member variable birthdate
     * 
     * 
     */
    public void setBirthdate(java.util.Date value) {
        this.birthdate = value;
    }

    /**
     * Gets the value of member variable birthdate
     * 
     * 
     */
    public java.util.Date getBirthdate() {
        return this.birthdate;
    }

    /**
     * Sets the value of member variable benefithealthinsurance
     * 
     * 
     */
    public void setBenefithealthinsurance(int value) {
        this.benefithealthinsurance = value;
    }

    /**
     * Gets the value of member variable benefithealthinsurance
     * 
     * 
     */
    public int getBenefithealthinsurance() {
        return this.benefithealthinsurance;
    }

    /**
     * Sets the value of member variable benefitlifeinsurance
     * 
     * 
     */
    public void setBenefitlifeinsurance(int value) {
        this.benefitlifeinsurance = value;
    }

    /**
     * Gets the value of member variable benefitlifeinsurance
     * 
     * 
     */
    public int getBenefitlifeinsurance() {
        return this.benefitlifeinsurance;
    }

    /**
     * Sets the value of member variable benefitdaycare
     * 
     * 
     */
    public void setBenefitdaycare(int value) {
        this.benefitdaycare = value;
    }

    /**
     * Gets the value of member variable benefitdaycare
     * 
     * 
     */
    public int getBenefitdaycare() {
        return this.benefitdaycare;
    }

    /**
     * Sets the value of member variable sex
     * 
     * 
     */
    public void setSex(char value) {
        this.sex = value;
    }

    /**
     * Gets the value of member variable sex
     * 
     * 
     */
    public char getSex() {
        return this.sex;
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