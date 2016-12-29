package com.api;

import java.sql.ResultSet;
import java.util.List;

import com.NotFoundException;
import com.SystemException;
import com.api.persistence.DatabaseException;
import com.api.persistence.db.orm.bean.DataSourceColumn;
import com.api.persistence.db.orm.bean.ObjectMapperAttrib;
import com.api.security.RMT2TagQueryBean;

/**
 * This interface contain methods intended to provide a communication link
 * between java objects and an external data source using an object relational
 * mapping approach.
 * 
 * @author roy.terrell
 * 
 */
public interface DataSourceApi {

    /**
     * Release database resources.
     */
    void close();

    /**
     * Retrieves data from the database based using the property values from
     * ObjectMapperAttrib.
     * 
     * @return
     * @throws DatabaseException
     * @throws SystemException
     */
    ResultSet executeQuery() throws DatabaseException, SystemException;

    /**
     * Retrieves data from the database based on "_sql".
     * 
     * @param _sql
     * @return Result Set.
     * @throws DatabaseException
     * @throws SystemException
     */
    ResultSet executeQuery(String _sql) throws DatabaseException,
            SystemException;

    /**
     * Retrieves data from the database. Uses isScrollable and isUpdatable to
     * determine the type and concurrency of the ResultSet when the Statement
     * object is created in the method, executeQuery().
     * 
     * @param isScrollable
     * @param isUpdateable
     * @return Result Set.
     * @throws DatabaseException
     * @throws SystemException
     */
    ResultSet executeQuery(boolean isScrollable, boolean isUpdateable)
            throws DatabaseException, SystemException;

    /**
     * Generic method that can be used to retrieve data in any form using custom
     * selection criteria. The user is responsible for setting the proper
     * datasource view and class as well as applying the proper cast on the
     * returned value.
     * 
     * @param _whereClause
     *            SQL where clasue to provide a condition used to restrict the
     *            result set of data.
     * @param _orderByClause
     *            SQL order by clasue.
     * @return An List of any type of object.
     * @throws SystemException
     */
    List findData(String _whereClause, String _orderByClause)
            throws SystemException;

    /**
     * Executes SLQ Query i which the reuslts will be in the format of XML.
     * 
     * @param sql
     *            The query to exectute
     * @return XML as String
     * @throws DatabaseException
     * @throws SystemException
     */
    String executeXmlQuery(String sql) throws DatabaseException,
            SystemException;

    /**
     * Returns the row count for the current result set.
     * 
     * @return the total number of rows.
     */
    int getRowCount();

    /**
     * Navigates to the first row of the ResultSet object.
     * 
     * @return true if a row was found, and false if no more rows exist.
     * @throws DatabaseException
     * @throws SystemException
     */
    boolean firstRow() throws DatabaseException, SystemException;

    /**
     * Navigates to the last row of the ResultSet object.
     * 
     * @return true if a row was found, and false if no more rows exist.
     * @throws DatabaseException
     * @throws SystemException
     */
    boolean lastRow() throws DatabaseException, SystemException;

    /**
     * Navigates to the next row of the ResultSet object.
     * 
     * @return true if a row was found, and false if no more rows exist.
     * @throws DatabaseException
     * @throws SystemException
     */
    boolean nextRow() throws DatabaseException, SystemException;

    /**
     * Navigates to the previous row of the ResultSet object.
     * 
     * @return true if a row was found, and false if no more rows exist.
     * @throws DatabaseException
     * @throws SystemException
     */
    boolean previousRow() throws DatabaseException, SystemException;

    /**
     * Moves cursor to a new row in the result set.
     * 
     * @return 1 for success.
     * @throws DatabaseException
     */
    int createRow() throws DatabaseException;

    /**
     * Adds the insert row of the result set to the databse. This method should
     * be called after all setColumnValue calls have been made. Upon successful
     * insertion, move the cursor back to the row that was current before insert
     * row.
     * 
     * @return int >= 0 Total number of rows affected, -1 Invalid mode was
     *         supplied, or -2 Insert statement was not initialize.
     * @throws DatabaseException
     */
    int insertRow() throws DatabaseException;

    /**
     * Updates the database via the DataSource view.
     * 
     * @return int >= 0 Total number of rows affected, -1 Invalid mode was
     *         supplied, or -2 Update statement was not initialize.
     * @throws NotFoundException
     * @throws DatabaseException
     * @throws SystemException
     */
    int updateRow() throws NotFoundException, DatabaseException,
            SystemException;

    /**
     * Deletes row(s) from the database.
     * 
     * @return int >= 0 Total number of rows affected, -1 Invalid mode was
     *         supplied, or -2 Delete statement was not initialize.
     * @throws DatabaseException
     */
    int deleteRow() throws DatabaseException;

    /**
     * Returns the associated JDBC ResultSet object.
     * 
     * @return ResultSet
     */
    ResultSet getRs();

    /**
     * Gets the value of a SQL Select Statement clause contained in the
     * datasource object.
     * 
     * @param sqlKey
     * @return The SQl statement peratining to the sqlKey.
     */
    String getDatasourceSql(int sqlKey);

    /**
     * Sets the value of a SQL Select Statement clause contained in the
     * datasource object.
     * 
     * @param sqlKey
     * @param value
     */
    void setDatasourceSql(int sqlKey, String value);

    /**
     * Retreives the column value from the ResultSet, rs, based on the input
     * property, _property. The value is returned to the caller as type String.
     * 
     * @param _property
     * @return String as the value
     * @throws DatabaseException
     * @throws NotFoundException
     * @throws SystemException
     */
    String getColumnValue(String _property) throws DatabaseException,
            NotFoundException, SystemException;

    /**
     * Retrieves value as an InputStream from the data source based on the
     * property name.
     * 
     * @param property
     *            The name of the property to retrieve
     * @return Object an arbitrary object
     * @throws DatabaseException
     * @throws NotFoundException
     * @throws SystemException
     */
    Object getColumnBinaryValue(String property) throws DatabaseException,
            NotFoundException, SystemException;

    /**
     * Retreives the column value from the ResultSet, rs, based on the input
     * property, _property. The value is returned to the caller as a formatted
     * value of type String.
     * 
     * @param _property
     * @param _format
     * @return String as the value
     * @throws DatabaseException
     * @throws NotFoundException
     * @throws SystemException
     */
    String getColumnValue(String _property, String _format)
            throws DatabaseException, NotFoundException, SystemException;

    /**
     * Sets the value of the datasource's target column, _property.
     * 
     * @param _property
     * @param value
     * @throws SystemException
     * @throws NotFoundException
     * @throws DatabaseException
     */
    void setColumnValue(String _property, Object value) throws SystemException,
            NotFoundException, DatabaseException;

    /**
     * Formats data values originating from a datasource provider in order to
     * conform and be applied to a SQL statement using colBean.
     * 
     * @param colBean
     *            {@link bean.db.DataSourceColumn DataSourceColumn}
     * @return Properly formatted value.
     */
    String getSqlColumnValue(DataSourceColumn colBean);

    /**
     * Formats _value according to _dataType so that the value can be properly
     * applied to a SQL statement for processing.
     * 
     * @param _dataType
     *            The data type of the value to be applied to the SQL statement.
     * @param _value
     *            Teh value to be formatted.
     * @return Properly formatted value.
     */
    String getSqlColumnValue(String _dataType, Object _value);

    /**
     * Queries the datasource property to determine if a column exist.
     * 
     * @param _value
     * @return true when column is valid, and false when column is not valid.
     */
    boolean isColumnValid(String _value);

    /**
     * Gets the DataSourceAttrib object associated with this api object.
     * 
     * @return {@link ObjectMapperAttrib}
     */
    ObjectMapperAttrib getDataSourceAttib();

    /**
     * Sets the base view.
     * 
     * @param value
     *            view name
     * @return the name of the previous view
     */
    String setBaseView(String value);

    /**
     * Sets the base class.
     * 
     * @param value
     *            class name
     * @return the name of the previous class
     */
    String setBaseClass(String value);

    /**
     * Gets the base view name.
     * 
     * @return String
     */
    String getBaseView();

    /**
     * Gets the base class name
     * 
     * @return String
     */
    String getBaseClass();

    /**
     * Gets the state of the internal result set's scrollable property (type).
     * 
     * @return true for scrollable and false for forward only.
     */
    boolean isRsScrollable();

    /**
     * Sets the internal result set's scrollable property (type).
     * 
     * @param value
     *            true for scrollable, and false for forward only
     */
    void setRsScrollable(boolean value);

    /**
     * Gets the state of the internal results set's updateable property
     * (concurrency).
     * 
     * @return true for updateable and false for read only.
     */
    boolean isRsUpdatable();

    /**
     * Sets the internal result set's updateable property (concurrency).
     * 
     * @param value
     *            true for updateable and false for read only.
     */
    void setRsUpdatable(boolean value);

    /**
     * Sets the api's query bean property.
     * 
     * @param value
     *            {@link RMT2TagQueryBean}
     */
    void setQueryData(RMT2TagQueryBean value);

    /**
     * Gets the api's query bean.
     * 
     * @return {@link RMT2TagQueryBean}
     */
    RMT2TagQueryBean getQueryData();

    /**
     * Instructs api to return a row count or a complete dataset
     * 
     * @param flag
     *            true indicate a row count and false indicates a complete
     *            dataset.
     */
    void setReturnRowCount(boolean flag);

    /**
     * Returns flag that indicates if a row count or a complete dataset is to be
     * returned as a result of the query.
     * 
     * @return boolean true indicates a row count and false indicates a complete
     *         dataset.
     */
    boolean isReturnRowCount();

} // End interface
