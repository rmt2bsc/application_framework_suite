package testcases.bean;

import java.util.Date;

import com.SystemException;
import com.api.persistence.db.orm.OrmBean;

/**
 * Peer object that maps to the departments database table/view.
 * 
 * 
 */
public class Departments extends OrmBean {

    // Property name constants that belong to respective DataSource,
    // DepartmentsView

    /**
     * The property name constant equivalent to property, Departmentid, of
     * respective DataSource view.
     */
    public static final String PROP_DEPARTMENTID = "Departmentid";
    /**
     * The property name constant equivalent to property, Departmentname, of
     * respective DataSource view.
     */
    public static final String PROP_DEPARTMENTNAME = "Departmentname";
    /**
     * The property name constant equivalent to property, Departmentheadid, of
     * respective DataSource view.
     */
    public static final String PROP_DEPARTMENTHEADID = "Departmentheadid";

    /**
     * The javabean property equivalent of database column
     * departments.departmentid
     */
    private int departmentid;
    /**
     * The javabean property equivalent of database column
     * departments.departmentname
     */
    private String departmentname;
    /**
     * The javabean property equivalent of database column
     * departments.departmentheadid
     */
    private int departmentheadid;

    // Getter/Setter Methods

    /**
     * Default constructor.
     * 
     * 
     */
    public Departments() throws SystemException {
        super();
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
     * Sets the value of member variable departmentname
     * 
     * 
     */
    public void setDepartmentname(String value) {
        this.departmentname = value;
    }

    /**
     * Gets the value of member variable departmentname
     * 
     * 
     */
    public String getDepartmentname() {
        return this.departmentname;
    }

    /**
     * Sets the value of member variable departmentheadid
     * 
     * 
     */
    public void setDepartmentheadid(int value) {
        this.departmentheadid = value;
    }

    /**
     * Gets the value of member variable departmentheadid
     * 
     * 
     */
    public int getDepartmentheadid() {
        return this.departmentheadid;
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