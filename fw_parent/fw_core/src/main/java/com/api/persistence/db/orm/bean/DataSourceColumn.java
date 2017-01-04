package com.api.persistence.db.orm.bean;

import com.RMT2BaseBean;
import com.SystemException;

/**
 * This entity bean class serves as a conduit between the implementation of the
 * DataSourceApi interface and the datasource view xml document. Basically, this
 * class assists the DataSourceApi java implementation in translating and
 * binding the datasource view's column properties to a language structure the
 * external data source provider can understand so that data may be queried and
 * persisted. There is a one to one relationship between this class and a
 * database column/DataSource view property.
 * 
 * @author roy.terrell
 * 
 */
public class DataSourceColumn extends RMT2BaseBean {
    private static final long serialVersionUID = -6201996753478248184L;

    private String name;

    private String dbName;

    private String sqlType;

    private boolean nullable;

    private int javaType;

    private boolean primaryKey;

    private String tableName;

    private String displayValue;

    private boolean updateable;

    private boolean computed;

    private Object dataValue;

    /**
     * Constructs a DataSourceColumn object with uninitialized properties.
     * 
     * @throws SystemException
     */
    public DataSourceColumn() throws SystemException {
        this.initBean();
    }

    /**
     * No Action
     */
    public void initBean() throws SystemException {
        return;
    }

    /**
     * Gets the name of the DataSource's property.
     * 
     * @return String
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the DataSource's property's name.
     * 
     * @param value
     *            String
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the data value of the DataSource's column property.
     * 
     * @return OBject
     */
    public Object getDataValue() {
        return this.dataValue;
    }

    /**
     * Sets the value of the DataSource's column property.
     * 
     * @param value
     *            Object
     */
    public void setDataValue(Object value) {
        this.dataValue = value;
    }

    /**
     * Gets the database name equivalent of the DataSource's column property.
     * 
     * @return String.
     */
    public String getDbName() {
        return this.dbName;
    }

    /**
     * Sets the database name equivalent of the DataSource's column property.
     * 
     * @param value
     *            String.
     */
    public void setDbName(String value) {
        this.dbName = value;
    }

    /**
     * Gets the equivalent database column type of DataSource's column property.
     * 
     * @return String
     */
    public String getSqlType() {
        return this.sqlType;
    }

    /**
     * Sets the equivalent database column type of the DataSource's column
     * property.
     * 
     * @param value
     *            String.
     */
    public void setSqlType(String value) {
        this.sqlType = value;
    }

    /**
     * Gets the nullable state of the DataSource's column property.
     * 
     * @return boolean
     */
    public boolean isNullable() {
        return this.nullable;
    }

    /**
     * Sets the nullable state of the DataSource's column property.
     * 
     * @param value
     *            boolean.
     */
    public void setNullable(boolean value) {
        this.nullable = value;
    }

    /**
     * Gets the java type equivalent of the DataSource's column property.
     * 
     * @return int
     */
    public int getJavaType() {
        return this.javaType;
    }

    /**
     * Sets the java type equivalent of the DataSource's column property.
     * 
     * @param value
     *            int
     */
    public void setJavaType(int value) {
        this.javaType = value;
    }

    /**
     * Gets the primary key indicator for the DataSource's column property.
     * 
     * @return boolean.
     */
    public boolean isPrimaryKey() {
        return this.primaryKey;
    }

    /**
     * Sets the primary key indicator for the DataSource's column property.
     * 
     * @param value
     *            boolean
     */
    public void setPrimaryKey(boolean value) {
        this.primaryKey = value;
    }

    /**
     * Gets the view table name of the DataSource's column property.
     * 
     * @return String.
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * Sets the view table name of the DataSource's column property.
     * 
     * @param value
     *            String.
     */
    public void setTableName(String value) {
        this.tableName = value;
    }

    /**
     * Gets the display value of the DataSource's column property.
     * 
     * @return String.
     */
    public String getDisplayValue() {
        return this.displayValue;
    }

    /**
     * Sets the display value of the DataSource's column property.
     * 
     * @param value
     *            String
     */
    public void setDisplayValue(String value) {
        this.displayValue = value;
    }

    /**
     * Gets the updateable indicator for the DataSource's column property.
     * 
     * @return boolean.
     */
    public boolean isUpdateable() {
        return this.updateable;
    }

    /**
     * Sets the updateable indicator for the DataSource's column property.
     * 
     * @param value
     *            boolean.
     */
    public void setUpdateable(boolean value) {
        this.updateable = value;
    }

    /**
     * Gets the indicator that determines whether or not the DataSource's column
     * property is computed.
     * 
     * @return boolean.
     */
    public boolean isComputed() {
        return this.computed;
    }

    /**
     * Sets the indicator that determines whether or not the DataSource's column
     * property is computed.
     * 
     * @param value
     *            boolean
     */
    public void setComputed(boolean value) {
        this.computed = value;
    }
}
