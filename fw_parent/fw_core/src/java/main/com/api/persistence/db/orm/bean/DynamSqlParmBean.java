package com.api.persistence.db.orm.bean;

import com.RMT2BaseBean;
import com.SystemException;

/**
 * This class manages a SQL statement parameter and its attributes for dynamic
 * SQL transactions.
 * 
 * @author roy.terrell
 * 
 */
public class DynamSqlParmBean extends RMT2BaseBean {
    private static final long serialVersionUID = -1985932388758711348L;

    private String dbColName;

    private int dataType;

    private Object parmValue;

    private boolean outParm;

    private int index;

    /**
     * Constructs a DynamSqlParmBean object with uninitialized properties.
     * 
     * @throws SystemException
     */
    public DynamSqlParmBean() throws SystemException {
    }

    /**
     * Sets the database column name the parameter represents.
     * 
     * @param value
     *            String.
     */
    public void setDbColName(String value) {
        this.dbColName = value;
    }

    /**
     * Gets the database column name the parameter represents.
     * 
     * @return String.
     */
    public String getDbColName() {
        return this.dbColName;
    }

    /**
     * Sets the data type of the parameter.
     * 
     * @param value
     *            java.sql.Types
     */
    public void setDataType(int value) {
        this.dataType = value;
    }

    /**
     * Gets the data type of the parameter.
     * 
     * @return java.sql.Types.
     */
    public int getDataType() {
        return this.dataType;
    }

    /**
     * Sets the value of the parameter.
     * 
     * @param value
     *            Object
     */
    public void setParmValue(Object value) {
        this.parmValue = value;
    }

    /**
     * Gets the value of the parameter.
     * 
     * @return Object
     */
    public Object getParmValue() {
        return this.parmValue;
    }

    /**
     * Sets the indicator that determines whether or not the parameter is an out
     * parameter.
     * 
     * @param value
     *            boolean
     */
    public void setOutParm(boolean value) {
        this.outParm = value;
    }

    /**
     * Gets the out parameter inidcator.
     * 
     * @return boolean
     */
    public boolean isOutParm() {
        return this.outParm;
    }

    /**
     * Set the index of the parameter. The index determines the position
     * parameter in the SQL statement.
     * 
     * @param value
     *            int
     */
    public void setIndex(int value) {
        this.index = value;
    }

    /**
     * Gets the index of the parameter. The index determines the position
     * parameter in the SQL statement.
     * 
     * @return int
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * No Action.
     */
    public void initBean() throws SystemException {
    }
}