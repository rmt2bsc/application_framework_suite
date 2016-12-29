package com.api.persistence.db.orm.query;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import com.RMT2BaseBean;
import com.SystemException;
import com.api.Product;

/**
 * Abstract class that provides basic functionality for building a
 * {@link com.api.Product Product} derived object.
 * 
 * @author appdev
 * 
 */
public abstract class AbstractQueryBuilder extends RMT2BaseBean {

    public static final String QUERY_ROWCOUNT = "rowcount";

    private Object src;

    private Object queryComp;

    private String queryString;

    private Logger logger;

    /**
     * Default constructor.
     * 
     * @throws SystemException
     */
    private AbstractQueryBuilder() throws SystemException {
        super();
        return;
    }

    /**
     * Constructs an AbstractQueryBuilder object by initializing an arbitrary
     * object serving as the source of data.
     * 
     * @param src
     *            Arbitrary data object.
     * @throws SystemException
     */
    public AbstractQueryBuilder(Object src) throws SystemException {
        this();
        // Check the validity of the datasource.
        if (src == null) {
            this.msg = "Query source is invalid";
            this.logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }
        this.src = src;
        return;
    }

    /**
     * Performs additional object initialization.
     * 
     * @see bean.RMT2BaseBean#initBean()
     */
    @Override
    public void initBean() throws SystemException {
        this.src = null;
        this.queryComp = null;
        this.queryString = null;
        this.logger = Logger.getLogger("AbstractQueryBuilder");
        return;
    }

    /**
     * Creates an instance of Product and populates the instance with the
     * results of the assembly and/or disassembly process.
     * 
     * @return {@link com.api.Product Product}
     */
    public Product getProduct() {
        Product prod = Product.getInstance(this.queryComp, this.queryString);
        return prod;
    }

    /**
     * Get query source.
     * 
     * @return the src
     */
    protected Object getSrc() {
        return src;
    }

    /**
     * Get the object component that represents the query
     * 
     * @return the queryComp
     */
    protected Object getQueryComp() {
        return queryComp;
    }

    /**
     * Set the object component that represents the query.
     * 
     * @param obj
     *            the queryComp to set
     */
    protected void setQueryComp(Object obj) {
        this.queryComp = obj;
    }

    /**
     * Get the String that represents the query.
     * 
     * @return the queryString
     */
    protected String getQueryString() {
        return queryString;
    }

    /**
     * Set the String that represents the query.
     * 
     * @param value
     *            the queryString to set
     */
    protected void setQueryString(String value) {
        this.queryString = value;
    }
}
