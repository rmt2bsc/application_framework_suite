package com.api.persistence;

import java.sql.ResultSet;
import java.util.List;

import com.api.persistence.db.DatabaseTransactionErrorException;
import com.api.persistence.db.orm.query.pagination.PaginationQueryResults;

/**
 * A light weight Java data mapping interface for querying, adding, modifying,
 * removing information contained in some arbitrary data source.
 * 
 * @author rterrell
 * 
 */
public interface PersistenceClient {

    /**
     * Release the resources belonging to this persistience client
     */
    void close();

    /**
     * Get the native connection object that allows the user to interact with
     * the external data source.
     * 
     * @return an arbitary object representing the connection
     */
    Object getConnection();

    /**
     * Get the wrapper object that contains the native connection object that
     * allows the user to interact with the external data source.
     * 
     * @return an arbitary object represtinting the connection wrapper.
     */
    Object getConnectionWrapper();

    /**
     * Starts a unit of work for one or more transactions.
     * 
     * @throws DatabaseTransactionErrorException
     */
    void beginTrans() throws DatabaseTransactionErrorException;

    /**
     * Commits database transaction with exceptoin handling
     * 
     * @throws DatabaseTransactionErrorException
     */
    void commitTrans() throws DatabaseTransactionErrorException;

    /**
     * Rollback database transaction with exceptoin handling.
     * 
     * @throws DatabaseTransactionErrorException
     */
    void rollbackTrans() throws DatabaseTransactionErrorException;

    /**
     * Retrieves a single data object from some external datasource.
     * 
     * @param obj
     *            An arbitrary ORM object containing the selection criteria used
     *            to retrieve data.
     * @return An arbitrary object or null if object is not found.
     * @throws {@link com.api.db.DatabaseException DatabaseException}
     */
    Object retrieveObject(Object obj) throws DatabaseException;

    /**
     * Retrieves a List of one or more data objects from some external
     * datasource.
     * 
     * @param obj
     *            An arbitrary ORM object containing the selection criteria used
     *            to retrieve data.
     * @return A List of arbitrary data objects or null when no data is found.
     * @throws {@link com.api.db.DatabaseException DatabaseException}
     */
    List retrieveList(Object obj) throws DatabaseException;

    /**
     * Retrieves the total number of records that the query <i>obj</i> whould
     * yield.
     * 
     * @param obj
     *            An arbitrary ORM object containing the selection criteria used
     *            to retrieve data.
     * @return The total number of records fetched.
     * @throws DatabaseException
     */
    long retrieveCount(Object obj) throws DatabaseException;

    /**
     * Performs an ORM Delete based on the object, <i>obj</i>, which targets a
     * certain structure in an external data source.
     * 
     * @param obj
     *            A POJO containing the data to delete.
     * @return A count of the number of rows effected by this operation.
     * @throws DatabaseException
     */
    int deleteRow(Object obj) throws DatabaseException;

    /**
     * Performs an ORM insert based on the object, <i>obj</i>, which targets a
     * certain structure in an external data source.
     * 
     * @param obj
     *            A POJO containing the data to insert.
     * @param autoKey
     *            A flag indicating whether or not primary keys are generated
     *            and made available for retrieval.
     * @return The id of the key inserted when autoKey is set to true.
     *         Otherwise, 0 is returned.
     * @throws DatabaseException
     */
    int insertRow(Object obj, boolean autoKey) throws DatabaseException;

    /**
     * Performs an ORM update based on the object, <i>obj</i>, which targets a
     * certain structure in an external data source.
     * 
     * @param obj
     *            A POJO containing the data to update
     * @return A count of the number of rows effected by this operation
     * @throws DatabaseException
     */
    int updateRow(Object obj) throws DatabaseException;

    /**
     * Retrieves a List of data objects from som external datasource for a given
     * page number.
     * 
     * @param obj
     *            An arbitrary ORM object containing the selection criteria used
     *            to retrieve data.
     * @param pageNo
     *            The page number corresponding to the subset of records from
     *            the result set.
     * @return an instance for {@link PaginationQueryResults} or null if no data
     *         is found.
     * @throws DatabaseException
     * 
     */
    PaginationQueryResults retrieveList(Object obj, int pageNo)
            throws DatabaseException;

    /**
     * Executes a SQL query string to retrieve data from the database.
     * <p>
     * The result set returned is forward only type.
     * 
     * @param sql
     *            a SQL select statement String
     * @return an instance of {@link ResultSet}.
     * @throws DatabaseException
     */
    ResultSet executeSql(String sql) throws DatabaseException;

    /**
     * Executes a DML SQL Statement such as an Insert, Delete or Update.
     * 
     * @param sql
     *            An Insert, Update, or Delete statement or an SQL statement
     *            that returns nothing.
     * @return A count of the number of rows effected by the DML statement or
     *         zero if nothing was returned from the SQL Statement.
     * @throws DatabaseException
     * @throws SystemException
     */
    int executeUpdate(String sql) throws DatabaseException;

}
