package testcases.bean;

import java.util.Date;

import com.SystemException;
import com.api.persistence.db.orm.OrmBean;

/**
 * Peer object that maps to the viewsalesorders database table/view.
 * 
 * 
 */
public class Viewsalesorders extends OrmBean {

    // Property name constants that belong to respective DataSource,
    // ViewsalesordersView

    /**
     * The property name constant equivalent to property, Id, of respective
     * DataSource view.
     */
    public static final String PROP_ID = "Id";
    /**
     * The property name constant equivalent to property, Lineid, of respective
     * DataSource view.
     */
    public static final String PROP_LINEID = "Lineid";
    /**
     * The property name constant equivalent to property, Productid, of
     * respective DataSource view.
     */
    public static final String PROP_PRODUCTID = "Productid";
    /**
     * The property name constant equivalent to property, Quantity, of
     * respective DataSource view.
     */
    public static final String PROP_QUANTITY = "Quantity";
    /**
     * The property name constant equivalent to property, Orderdate, of
     * respective DataSource view.
     */
    public static final String PROP_ORDERDATE = "Orderdate";
    /**
     * The property name constant equivalent to property, Shipdate, of
     * respective DataSource view.
     */
    public static final String PROP_SHIPDATE = "Shipdate";
    /**
     * The property name constant equivalent to property, Region, of respective
     * DataSource view.
     */
    public static final String PROP_REGION = "Region";
    /**
     * The property name constant equivalent to property,
     * Salesrepresentativename, of respective DataSource view.
     */
    public static final String PROP_SALESREPRESENTATIVENAME = "Salesrepresentativename";

    /** The javabean property equivalent of database column viewsalesorders.id */
    private int id;
    /**
     * The javabean property equivalent of database column
     * viewsalesorders.lineid
     */
    private int lineid;
    /**
     * The javabean property equivalent of database column
     * viewsalesorders.productid
     */
    private int productid;
    /**
     * The javabean property equivalent of database column
     * viewsalesorders.quantity
     */
    private int quantity;
    /**
     * The javabean property equivalent of database column
     * viewsalesorders.orderdate
     */
    private java.util.Date orderdate;
    /**
     * The javabean property equivalent of database column
     * viewsalesorders.shipdate
     */
    private java.util.Date shipdate;
    /**
     * The javabean property equivalent of database column
     * viewsalesorders.region
     */
    private char region;
    /**
     * The javabean property equivalent of database column
     * viewsalesorders.salesrepresentativename
     */
    private String salesrepresentativename;

    // Getter/Setter Methods

    /**
     * Default constructor.
     * 
     * 
     */
    public Viewsalesorders() throws SystemException {
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
     * Sets the value of member variable lineid
     * 
     * 
     */
    public void setLineid(int value) {
        this.lineid = value;
    }

    /**
     * Gets the value of member variable lineid
     * 
     * 
     */
    public int getLineid() {
        return this.lineid;
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
     * Sets the value of member variable quantity
     * 
     * 
     */
    public void setQuantity(int value) {
        this.quantity = value;
    }

    /**
     * Gets the value of member variable quantity
     * 
     * 
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * Sets the value of member variable orderdate
     * 
     * 
     */
    public void setOrderdate(java.util.Date value) {
        this.orderdate = value;
    }

    /**
     * Gets the value of member variable orderdate
     * 
     * 
     */
    public java.util.Date getOrderdate() {
        return this.orderdate;
    }

    /**
     * Sets the value of member variable shipdate
     * 
     * 
     */
    public void setShipdate(java.util.Date value) {
        this.shipdate = value;
    }

    /**
     * Gets the value of member variable shipdate
     * 
     * 
     */
    public java.util.Date getShipdate() {
        return this.shipdate;
    }

    /**
     * Sets the value of member variable region
     * 
     * 
     */
    public void setRegion(char value) {
        this.region = value;
    }

    /**
     * Gets the value of member variable region
     * 
     * 
     */
    public char getRegion() {
        return this.region;
    }

    /**
     * Sets the value of member variable salesrepresentativename
     * 
     * 
     */
    public void setSalesrepresentativename(String value) {
        this.salesrepresentativename = value;
    }

    /**
     * Gets the value of member variable salesrepresentativename
     * 
     * 
     */
    public String getSalesrepresentativename() {
        return this.salesrepresentativename;
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