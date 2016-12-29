package com.api;

import com.RMT2BaseBean;
import com.SystemException;

/**
 * Entity object that possesses multiple representations of itself as a result
 * of some complex build process.
 * 
 * @author appdev
 * 
 */
public class Product extends RMT2BaseBean {
    private static final long serialVersionUID = 6198227074450698980L;

    private String stringVal;

    private Object objectVal;

    /**
     * Constructs a Product without any values.
     * 
     * @throws SystemException
     */
    protected Product() throws SystemException {
        super();
    }

    /**
     * Creates an instance of Product in which the String and Object states are
     * not initialized.
     * 
     * @return Product
     */
    public static Product getInstance() {
        try {
            Product prod = new Product();
            return prod;
        } catch (SystemException e) {
            return null;
        }
    }

    /**
     * Creates an instance of Product with valid object and String states.
     * 
     * @param objVal
     *            The object representation of the Product.
     * @param strVal
     *            The String representation of the Product.
     * @return {@link Product}
     */
    public static Product getInstance(Object objVal, String strVal) {
        try {
            Product prod = new Product();
            prod.stringVal = strVal;
            prod.objectVal = objVal;
            return prod;
        } catch (SystemException e) {
            return null;
        }
    }

    /**
     * Initializes the bean.
     * 
     * @see bean.RMT2BaseBean#initBean()
     */
    @Override
    public void initBean() throws SystemException {
        this.stringVal = null;
        this.objectVal = null;
        return;
    }

    /**
     * @param objectVal
     *            the objectVal to set
     */
    public void setObjectVal(Object objectVal) {
        this.objectVal = objectVal;
    }

    /**
     * @param stringVal
     *            the stringVal to set
     */
    public void setStringVal(String stringVal) {
        this.stringVal = stringVal;
    }

    /**
     * Returns the Product as a String type.
     * 
     */
    public String toString() {
        return this.stringVal;
    }

    /**
     * Gets the Product as an Object type.
     * 
     * @return Product disguised as an Object.
     */
    public Object toObject() {
        return this.objectVal;
    }
}
