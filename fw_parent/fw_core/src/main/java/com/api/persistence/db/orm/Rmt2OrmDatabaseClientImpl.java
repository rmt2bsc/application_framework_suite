package com.api.persistence.db.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.RMT2Base;
import com.api.DaoApi;
import com.api.persistence.DatabaseException;
import com.api.persistence.PersistenceClient;
import com.api.persistence.db.DatabaseConnectionBean;
import com.api.persistence.db.DatabaseTransactionErrorException;
import com.api.persistence.db.orm.query.pagination.PaginationApi;
import com.api.persistence.db.orm.query.pagination.PaginationFactory;
import com.api.persistence.db.orm.query.pagination.PaginationQueryResults;

/**
 * Provides the mapping of data between a relational SQL database and Java
 * focusing on querying, inserting, updating, and deleting data housed in a
 * RDBMS.
 * 
 * @author RTerrell
 * 
 */
class Rmt2OrmDatabaseClientImpl extends RMT2Base implements PersistenceClient {
    private static Logger logger;

    private DaoApi daoApi;

    private PaginationApi pageApi;

    private DatabaseConnectionBean conBean;

    /**
     * Default constructor that creates the logger.
     */
    protected Rmt2OrmDatabaseClientImpl() {
        logger = Logger.getLogger(Rmt2OrmDatabaseClientImpl.class);
    }

    /**
     * Creates a Rmt2OrmDatabaseClientImpl object using an arbitrary connection
     * object.
     * 
     * @param connection
     *            A generic object that represents a database connection.
     * @throws OrmInitialziationException
     *             when connection is null or the connection instance is not a
     *             descendent of DatabaseConnectionBean.
     */
    protected Rmt2OrmDatabaseClientImpl(Object connection)
            throws OrmInitialziationException {
        this();
        if (connection == null) {
            this.msg = "Orm client implementation class could not be instantiated due to invalid connection object";
            logger.log(Level.ERROR, this.msg);
            throw new OrmInitialziationException(this.msg);
        }
        if (!(connection instanceof DatabaseConnectionBean)) {
            this.msg = "Orm client implementation class could not be instantiated due to invalid connection object";
            logger.log(Level.ERROR, this.msg);
            throw new OrmInitialziationException(this.msg);
        }
        DatabaseConnectionBean con = (DatabaseConnectionBean) connection;
        this.daoApi = DataSourceFactory.createDao(con);
        this.pageApi = PaginationFactory.createDao(con);
        this.conBean = con;
        this.conBean.open();
    }

    /**
     * Get the native JDBC relational database connection.
     * 
     * @return an instance of {@link Connection} or null if the connection has
     *         not be initialized.
     */
    @Override
    public Object getConnection() {
        if (this.conBean == null) {
            return null;
        }
        return this.conBean.getNativeConnection();
    }

    /**
     * Get the DatabaseConnectionBean object that wraps the native JDBC
     * relational database connection.
     * 
     * @return an instance of {@link DatabaseConnectionBean}
     */
    @Override
    public Object getConnectionWrapper() {
        return this.conBean;
    }

    /**
     * Starts a unit of work for one or more transactions.
     * <p>
     * The database connection's <i>autoCommit</i> property is set to false
     * allow the client to control unit of work by issuing commit and/or
     * rollbacks.
     * 
     * @throws DatabaseTransactionErrorException
     */
    @Override
    public void beginTrans() throws DatabaseTransactionErrorException {
        Connection con = this.conBean.getNativeConnection();
        if (con == null) {
            this.msg = "Unable to start database transaction due to invalid or null connection object";
            throw new DatabaseTransactionErrorException(this.msg);
        }
        try {
            con.setAutoCommit(false);
        } catch (SQLException e) {
            this.msg = "Error occurred while starting database transaction due to inability to set auto commit property to \"true\"";
            throw new DatabaseTransactionErrorException(this.msg, e);
        }
    }

    /**
     * Commits database transaction with exceptoin handling
     * 
     * @throws DatabaseTransactionErrorException
     */
    @Override
    public void commitTrans() throws DatabaseTransactionErrorException {
        Connection con = this.conBean.getNativeConnection();
        if (con == null) {
            this.msg = "Unable to commit database transaction due to invalid or null connection object";
            throw new DatabaseTransactionErrorException(this.msg);
        }
        try {
            con.commit();
            con.setAutoCommit(false);
            // con.setAutoCommit(true);
        } catch (SQLException e) {
            this.msg = "Error occurred while committing and/or setting auto commit property to \"false\" for database transaction";
            throw new DatabaseTransactionErrorException(this.msg, e);
        }
    }

    /**
     * Rollback database transaction with exceptoin handling.
     * 
     * @throws DatabaseTransactionErrorException
     */
    @Override
    public void rollbackTrans() throws DatabaseTransactionErrorException {
        Connection con = this.conBean.getNativeConnection();
        if (con == null) {
            this.msg = "Unable to rollback database transaction due to invalid or null connection object";
            throw new DatabaseTransactionErrorException(this.msg);
        }
        try {
            con.rollback();
            con.setAutoCommit(false);
            // con.setAutoCommit(true);
        } catch (SQLException e) {
            this.msg = "Error occurred while rolling back and/or setting auto commit property to \"false\" for database transaction";
            throw new DatabaseTransactionErrorException(this.msg, e);
        }
    }

    /**
     * 
     */
    public void close() {
        logger = null;
        this.daoApi.close();
        this.daoApi = null;
        this.pageApi.close();
        this.pageApi = null;
        this.conBean.close();
    }

    /**
     * Retrieves data from a RDBMS in the form of a single object.
     * 
     * @param obj
     *            The target ORM object used for data retrieval.
     * @return An arbitrary object or null if object is not found.
     * @throws {@link com.api.db.DatabaseException DatabaseException}
     */
    public Object retrieveObject(Object obj) throws DatabaseException {
        if (obj == null) {
            return null;
        }
        try {
            Object data[] = this.daoApi.retrieve(obj);
            if (data != null && data.length > 0) {
                return data[0];
            }
            return null;
        } catch (Exception e) {
            throw new DatabaseException(
                    "Problem retrieving single ORMBean instance data", e);
        }
    }

    /**
     * Retrieves data from a RDBMS in the form of a List.
     * 
     * @param obj
     *            The ORM object to retrieve data.
     * @return A List of arbitrary data objects or null when no data is found
     * @throws {@link com.api.db.DatabaseException DatabaseException}
     */
    public List retrieveList(Object obj) throws DatabaseException {
        if (obj == null) {
            return null;
        }
        try {
            Object data[] = this.daoApi.retrieve(obj);
            if (data != null && data.length > 0) {
                // Copy the elements in the list so that the List is muteable
                return new ArrayList(java.util.Arrays.asList(data));
            }
            return null;
        } catch (Exception e) {
            throw new DatabaseException("Problem retrieving ORMBean List data",
                    e);
        }
    }

    /**
     * 
     * 
     * @param obj
     *            An arbitrary ORM object containing the selection criteria used
     *            to retrieve data.
     * @return The total number of records fetched or -1 indicating no data
     *         found.
     * @throws DatabaseException
     */
    public long retrieveCount(Object obj) throws DatabaseException {
        if (obj == null) {
            return -2;
        }
        try {
            // Get row count
            this.pageApi.setReturnRowCount(true);
            this.pageApi.setPageNo(0);
            Object data[] = this.pageApi.retrieve(obj);
            if (data != null && data.length > 0) {
                Long count = (Long) data[0];
                return count;
            }
            return -1;
        } catch (Exception e) {
            throw new DatabaseException(
                    "Problem retrieving database count of ORMBean criteria", e);
        }
    }

    /**
     * 
     * @param obj
     * @param pageNo
     * @return A List of arbitrary data objects or null when no data is found
     * @throws DatabaseException
     * 
     */
    public PaginationQueryResults retrieveList(Object obj, int pageNo)
            throws DatabaseException {
        if (obj == null) {
            return null;
        }

        this.pageApi.setPageNo(pageNo);
        // Do a non-pagination query when page number is less than or equal to
        // zero
        if (pageNo <= 0) {
            List list = this.retrieveList(obj);
            PaginationQueryResults results = new PaginationQueryResults();
            results.setResults(list);
            return results;
        }

        PaginationQueryResults pageObj = new PaginationQueryResults();
        pageObj.setPageNo(pageNo);
        try {
            // Get complete dataset
            this.pageApi.setReturnRowCount(false);
            Object data[] = this.pageApi.retrieve(obj);
            if (data != null && data.length > 0) {
                // Copy the elements in the list so that the List is muteable
                pageObj.setResults(new ArrayList(java.util.Arrays.asList(data)));
            }
            else {
                return null;
            }
            // Get row count
            Long count = this.retrieveCount(obj);
            pageObj.setTotalRowCount(count);
            return pageObj;
        } catch (Exception e) {
            throw new DatabaseException(
                    "Problem retrieving ORMBean List data for page #" + pageNo,
                    e);
        }
    }

    /**
     * Performs an ORM update based on the object, <i>obj</i>, which targets a
     * certain structure in an external data source.
     * 
     * @param obj
     *            A POJO containing the data to update
     * @return A count of the number of rows effected by this operation
     * @throws DatabaseException
     */
    @Override
    public int updateRow(Object obj) throws DatabaseException {
        try {
            int rc = this.daoApi.updateRow(obj);
            return rc;
        } catch (Exception e) {
            throw new DatabaseException(
                    "Problem applying update to the database with ORM Bean", e);
        }
    }

    /**
     * Performs an ORM Delete based on the object, <i>obj</i>, which targets a
     * certain structure in an external data source.
     * 
     * @param obj
     *            A POJO containing the data to delete.
     * @return A count of the number of rows effected by this operation.
     * @throws DatabaseException
     */
    @Override
    public int deleteRow(Object obj) throws DatabaseException {
        try {
            int rc = this.daoApi.deleteRow(obj);
            return rc;
        } catch (Exception e) {
            throw new DatabaseException(
                    "Problem applying delete to the database with ORM Bean", e);
        }
    }

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
    @Override
    public int insertRow(Object obj, boolean autoKey) throws DatabaseException {
        try {
            int key = this.daoApi.insertRow(obj, autoKey);
            return key;
        } catch (Exception e) {
            throw new DatabaseException(
                    "Problem applying insert to the database with ORM Bean", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.persistence.PersistenceClient#executeSql(java.lang.String)
     */
    @Override
    public ResultSet executeSql(String sql) throws DatabaseException {
        return this.daoApi.executeQuery(sql);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.persistence.PersistenceClient#executeUpdate(java.lang.String)
     */
    @Override
    public int executeUpdate(String sql) throws DatabaseException {
        return this.daoApi.executeUpdate(sql);
    }

}
