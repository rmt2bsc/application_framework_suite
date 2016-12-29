package com.api.security;

import java.util.Hashtable;
import java.util.Map;

import com.RMT2BaseBean;
import com.SystemException;

import com.constants.RMT2SystemExceptionConst;

/**
 * Object used to store key data and selectin criteria pertaining to data
 * queries originating from server processes onto the user's session.
 * 
 * @author Roy Terrell
 * 
 */
public class RMT2TagQueryBean extends RMT2BaseBean {
    private static final long serialVersionUID = -155967120367056054L;

    private String querySource;

    private String queryType;

    private String whereClause;

    private String orderByClause;

    private Hashtable keys;

    private String pageTitle;

    private String requestOrigin;

    private Object customObj;

    private Map auxCustomObj;

    private boolean waitPage;

    /**
     * Default constructor
     * 
     * @throws SystemException
     */
    public RMT2TagQueryBean() throws SystemException {
        super();
        this.keys = new Hashtable();
    }

    /**
     * Constructs a RMT2TagQueryBean object initializing the query source, query
     * type, SQL where clause, and SQL order by clause member variables.
     * 
     * @param _querySource
     *            Must be string containing the name of a datasource view.
     * @param _queryType
     *            Could be .xml or .sql
     * @param _whereClause
     *            Selection criteria that is to be applied to the datasource
     *            view.
     * @param _orderByClause
     *            Ordering that is to be applied tot he datasource view.
     * @throws SystemException
     */
    public RMT2TagQueryBean(String _querySource, String _queryType,
            String _whereClause, String _orderByClause) throws SystemException {
        this();
        this.querySource = _querySource;
        this.queryType = _queryType;
        this.whereClause = _whereClause;
        this.orderByClause = _orderByClause;
    }

    /**
     * Used to perform any additional initialization of this object. Currently
     * stubbed.
     */
    public void initBean() {
        return;
    }

    /**
     * Creates a RMT2TagQueryBean instance.
     * 
     * @return An initialized {@link RMT2TagQueryBean} instance.
     */
    public static RMT2TagQueryBean getInstance() {
        try {
            return new RMT2TagQueryBean();
        } catch (SystemException e) {
            return null;
        }
    }

    /**
     * Adds a key/value pair to an internal collection that will need to persist
     * between requests.
     * 
     * @param key
     *            The name that identifies value.
     * @param value
     *            The data that is linked to key.
     * @throws SystemException
     */
    public void addKeyValues(String key, Object value) throws SystemException {
        String method = "addKey";
        try {
            this.keys.put(key, value);
        } catch (NullPointerException e) {
            throw new SystemException(
                    RMT2SystemExceptionConst.MSG_NULL_METHOD_ARGUMENT,
                    RMT2SystemExceptionConst.RC_NULL_METHOD_ARGUMENT);
        }
    }

    /**
     * Obtains a value that is mapped to key from an internal collection of
     * key/value pairs.
     * 
     * @param key
     *            The identifier of the value to retrieve.
     * @return The value mapped to key.
     */
    public Object getKeyValues(String key) {
        Object value = this.keys.get(key);
        return value;
    }

    /**
     * Removes a key/value pair fro the internal collection specified by key.
     * 
     * @param key
     *            The key that is to be removed.
     * @return The value that was linked to key.
     */
    public Object removeKeyValues(String key) {
        return this.keys.remove(key);
    }

    /**
     * Removes all key.value pairs from the internal collection.
     * 
     */
    public void clearAllKeyValues() {
        try {
            this.keys.clear();
            return;
        } catch (UnsupportedOperationException e) {
            this.keys = null;
            this.keys = new Hashtable();
        }
    }

    /**
     * Gets the query source.
     * 
     * @return The name of the query source.
     */
    public String getQuerySource() {
        return this.querySource;
    }

    /**
     * Sets the name of the query source.
     * 
     * @param value
     */
    public void setQuerySource(String value) {
        this.querySource = value;
    }

    /**
     * Gets the query source type.
     * 
     * @return .xml or .sql
     */
    public String getQueryType() {
        return this.queryType;
    }

    /**
     * Sets the query source type.
     * 
     * @param value
     *            .xml or .sql
     */
    public void setQueryType(String value) {
        this.queryType = value;
    }

    /**
     * Gets the selection criteria that is to be applied to the datasource view.
     * 
     * @return The selection criteria.
     */
    public String getWhereClause() {
        return this.whereClause;
    }

    /**
     * Sets the selection criteria.
     * 
     * @param value
     *            The selection criteria.
     */
    public void setWhereClause(String value) {
        this.whereClause = value;
    }

    /**
     * Gets the specified ordering that is to applied to teh datasource view.
     * 
     * @return The order by clause.
     */
    public String getOrderByClause() {
        return this.orderByClause;
    }

    /**
     * Sets the order by clause.
     * 
     * @param value
     *            The order by clause.
     */
    public void setOrderByClause(String value) {
        this.orderByClause = value;
    }

    /**
     * Sets the page title.
     * 
     * @param value
     *            The page title.
     */
    public void setPageTitle(String value) {
        this.pageTitle = value;
    }

    /**
     * Gets the page title.
     * 
     * @return The page title.
     */
    public String getPageTitle() {
        return this.pageTitle;
    }

    /**
     * Gets the custom object.
     * 
     * @return Custom Object.
     */
    public Object getCustomObj() {
        return this.customObj;
    }

    /**
     * Sets the custom object that is to be used by the client. Generally this
     * is a user defined object that is used to recall the use's selection
     * criteria input.
     * 
     * @param value
     *            A user defined object.
     */
    public void setCustomObj(Object value) {
        this.customObj = value;
    }

    /**
     * Sets the origin of the request.
     * 
     * @param value
     *            The name of a resource or an URL.
     */
    public void setRequestOrigin(String value) {
        this.requestOrigin = value;
    }

    /**
     * Gets the request's origin.
     * 
     * @return The name of a resource or an URL.
     */
    public String getRequestOrigin() {
        return this.requestOrigin;
    }

    /**
     * Sets the wait page flag.
     * 
     * @param value
     *            true indicating wait page is displayed or false if it not
     *            displayed.
     */
    public void setWaitPage(boolean value) {
        this.waitPage = value;
    }

    /**
     * Gets the wait page flag.
     * 
     * @return true indicating wait page is displayed or false if it not
     *         displayed.
     */
    public boolean isWaitPage() {
        return this.waitPage;
    }

    /**
     * Adds an object to the auxillary custom object collection.
     * 
     * @param key
     *            The key
     * @param value
     *            The value
     */
    public void addAuxCustomObj(Object key, Object value) {
        if (this.auxCustomObj == null) {
            this.auxCustomObj = new Hashtable();
        }
        this.auxCustomObj.put(key, value);
    }

    /**
     * Retrieves an object from the auxillary custom object collection.
     * 
     * @param key
     *            The key of the target object
     * @return An arbitrary object.
     */
    public Object getAuxCustomObj(Object key) {
        if (this.auxCustomObj == null) {
            return null;
        }
        return this.auxCustomObj.get(key);
    }
} // End of Class