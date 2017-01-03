package testcases.bean;

import java.util.Date;

import com.SystemException;
import com.api.persistence.db.orm.OrmBean;

/**
 * Peer object that maps to the marketinginformation database table/view.
 * 
 * 
 */
public class Marketinginformation extends OrmBean {

    // Property name constants that belong to respective DataSource,
    // MarketinginformationView

    /**
     * The property name constant equivalent to property, Id, of respective
     * DataSource view.
     */
    public static final String PROP_ID = "Id";
    /**
     * The property name constant equivalent to property, Productid, of
     * respective DataSource view.
     */
    public static final String PROP_PRODUCTID = "Productid";
    /**
     * The property name constant equivalent to property, Description, of
     * respective DataSource view.
     */
    public static final String PROP_DESCRIPTION = "Description";

    /**
     * The javabean property equivalent of database column
     * marketinginformation.id
     */
    private int id;
    /**
     * The javabean property equivalent of database column
     * marketinginformation.productid
     */
    private int productid;
    /**
     * The javabean property equivalent of database column
     * marketinginformation.description
     */
    private String description;

    // Getter/Setter Methods

    /**
     * Default constructor.
     * 
     * 
     */
    public Marketinginformation() throws SystemException {
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
     * Sets the value of member variable productid
     * 
     * 
     */
    public void setProductid(int value) {
        this.productid = value;
    }

    /**
     * Gets the value of member variable productid
     * 
     * 
     */
    public int getProductid() {
        return this.productid;
    }

    /**
     * Sets the value of member variable description
     * 
     * 
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of member variable description
     * 
     * 
     */
    public String getDescription() {
        return this.description;
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