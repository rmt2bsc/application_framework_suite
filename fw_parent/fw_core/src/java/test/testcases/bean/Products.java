package testcases.bean;

import java.util.Date;
import java.io.*;

import com.SystemException;
import com.api.persistence.db.orm.OrmBean;

/**
 * Peer object that maps to the products database table/view.
 * 
 * 
 */
public class Products extends OrmBean {

    // Property name constants that belong to respective DataSource,
    // ProductsView

    /**
     * The property name constant equivalent to property, Id, of respective
     * DataSource view.
     */
    public static final String PROP_ID = "Id";
    /**
     * The property name constant equivalent to property, Name, of respective
     * DataSource view.
     */
    public static final String PROP_NAME = "Name";
    /**
     * The property name constant equivalent to property, Description, of
     * respective DataSource view.
     */
    public static final String PROP_DESCRIPTION = "Description";
    /**
     * The property name constant equivalent to property, Size, of respective
     * DataSource view.
     */
    public static final String PROP_SIZE = "Size";
    /**
     * The property name constant equivalent to property, Color, of respective
     * DataSource view.
     */
    public static final String PROP_COLOR = "Color";
    /**
     * The property name constant equivalent to property, Quantity, of
     * respective DataSource view.
     */
    public static final String PROP_QUANTITY = "Quantity";
    /**
     * The property name constant equivalent to property, Unitprice, of
     * respective DataSource view.
     */
    public static final String PROP_UNITPRICE = "Unitprice";
    /**
     * The property name constant equivalent to property, Photo, of respective
     * DataSource view.
     */
    public static final String PROP_PHOTO = "Photo";

    /** The javabean property equivalent of database column products.id */
    private int id;
    /** The javabean property equivalent of database column products.name */
    private String name;
    /** The javabean property equivalent of database column products.description */
    private String description;
    /** The javabean property equivalent of database column products.size */
    private String size;
    /** The javabean property equivalent of database column products.color */
    private String color;
    /** The javabean property equivalent of database column products.quantity */
    private int quantity;
    /** The javabean property equivalent of database column products.unitprice */
    private double unitprice;
    /** The javabean property equivalent of database column products.photo */
    private InputStream photo;

    // Getter/Setter Methods

    /**
     * Default constructor.
     * 
     * 
     */
    public Products() throws SystemException {
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
     * Sets the value of member variable name
     * 
     * 
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of member variable name
     * 
     * 
     */
    public String getName() {
        return this.name;
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
     * Sets the value of member variable size
     * 
     * 
     */
    public void setSize(String value) {
        this.size = value;
    }

    /**
     * Gets the value of member variable size
     * 
     * 
     */
    public String getSize() {
        return this.size;
    }

    /**
     * Sets the value of member variable color
     * 
     * 
     */
    public void setColor(String value) {
        this.color = value;
    }

    /**
     * Gets the value of member variable color
     * 
     * 
     */
    public String getColor() {
        return this.color;
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
     * Sets the value of member variable unitprice
     * 
     * 
     */
    public void setUnitprice(double value) {
        this.unitprice = value;
    }

    /**
     * Gets the value of member variable unitprice
     * 
     * 
     */
    public double getUnitprice() {
        return this.unitprice;
    }

    /**
     * Sets the value of member variable photo
     * 
     * 
     */
    public void setPhoto(InputStream value) {
        this.photo = value;
    }

    /**
     * Gets the value of member variable photo
     * 
     * 
     */
    public InputStream getPhoto() {
        return this.photo;
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