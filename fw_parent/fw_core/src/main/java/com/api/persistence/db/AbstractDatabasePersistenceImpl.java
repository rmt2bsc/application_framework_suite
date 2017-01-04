package com.api.persistence.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.RMT2Base;
import com.SystemException;
import com.api.persistence.DatabaseException;
import com.constants.RMT2SystemExceptionConst;

/**
 * This class serves as a abstract base implementation of the persistence api in
 * regards to common database functionality. Formerly used as a base
 * implementation for the DatabaseTransApi interface of the first versioon of
 * the persistence API.
 * 
 * @author roy.terrell
 * 
 */
public abstract class AbstractDatabasePersistenceImpl extends RMT2Base {
    private static Logger logger = Logger
            .getLogger(AbstractDatabasePersistenceImpl.class);

    protected Connection con = null;

    /** The JDBC Statement object */
    protected Statement stmt = null;

    /** The JDBC ResultSet object */
    protected ResultSet rs = null;

    /** Accumulates the total number rows effected by a SQL transaction */
    protected int dbChangeCount;

    /** The Database Connectin Bean wrapper */
    protected DatabaseConnectionBean connector;

    /** User's login id */
    protected String loginId;

    /**
     * Default constructor
     * 
     */
    public AbstractDatabasePersistenceImpl() {
        super();
        logger.log(Level.DEBUG,
                "Starting constructor, AbstractDatabasePersistenceImpl()");
    }

    /**
     * Initializes AbstractDatabasePersistenceImpl object using
     * {@link bean.db.DatabaseConnectionBean}
     * 
     * @param dbConn
     * @throws DatabaseException
     * @throws SystemException
     */
    public AbstractDatabasePersistenceImpl(DatabaseConnectionBean dbConn)
            throws DatabaseException, SystemException {
        super();
        logger.log(Level.DEBUG,
                "Database Transaction initialized with a ConnectionBean only");
        if (dbConn == null) {
            throw new SystemException(
                    "Api Could not be created due to invalid database object");
        }
        connector = dbConn;
        this.con = dbConn.getNativeConnection();
        return;
    }

    /**
     * Initializes AbstractDatabasePersistenceImpl object using a Connection
     * object and the loginId.
     * 
     * @param con
     * @param loginId
     * @throws DatabaseException
     * @throws SystemException
     */
    public AbstractDatabasePersistenceImpl(Connection con, String loginId)
            throws DatabaseException, SystemException {
        this();
        logger.log(Level.DEBUG,
                "Database Transaction initialized with a JDBC Connection and User's ogin id");
        this.con = con;
        this.loginId = loginId;
        this.verifyInstance();
    }

    /**
     * Initializes the connection object and the statement object that is to be
     * used with this class.
     */
    protected void initApi() throws DatabaseException, SystemException {
        logger.log(Level.DEBUG, "Starting initApi()");
        this.stmt = null;
        this.rs = null;
        this.connector = null;
        this.dbChangeCount = 0;
    } // End init

    /**
     * Verifies that the connection object has been properly initialized.
     * 
     * @throws DatabaseException
     *             General database errors
     * @throws DatabaseConnectionClosedException
     *             Connection is closed
     * @throws SystemException
     *             If the connection object is null or invalid.
     */
    private void verifyInstance() throws DatabaseException, SystemException {
        logger.info("Verifying ConnectionBean Instance");
        try {
            if (this.con == null) {
                this.msg = RMT2SystemExceptionConst.MSG_CONNECTION_INVALID
                        + " for user: " + this.loginId;
                throw new SystemException(this.msg);
            }
            logger.info("Verify if database connection is open or closed");
            if (this.con.isClosed()) {
                this.msg = "Database connection is closed.  \n"
                        + this.connector.getInfo();
                throw new DatabaseConnectionClosedException(this.msg);
            }
            this.dbChangeCount = 0;
            logger.info("ConnectionBean instance was verified successfully");
        } catch (SQLException e) {
            this.msg = "A sever SQL error occurred while verifying database connection, "
                    + this.con.hashCode();
            throw new DatabaseException(this.msg, e);
        }
    }

    /**
     * Release typical JDBC database resources such as the {@link ResultSet},
     * {@link Statement} and the {@link Connection} objects.
     * 
     * @throws DatabaseException
     */
    public void close() {
        logger.info("Releasing database transaction object");
        try {
            if (rs != null) {
                try {
                    this.rs.close();
                } catch (Exception e) {
                    // Do nothing...
                }
            }
            if (this.stmt != null) {
                this.stmt.close();
            }
            if (this.connector != null) {
                this.connector.close();
            }
        } catch (SQLException e) {
            throw new DatabaseException("JDBC SQL Exception occurred", e);
        } catch (Exception e) {
            throw new DatabaseException(
                    "A General error occurred while trying to release transaction object",
                    e);
        }
    } // End close

    /**
     * {@inheritDoc}
     */
    public final Object getConnector() {
        return connector;
    }

    /**
     * Sets the connectin bean wrapper object. The connection is set to null if
     * value is of a type other than DatabaseConnectionBean.
     * 
     * @param value
     *            {@link bean.db.DatabaseConnectionBean DatabaseConnectionBean}
     */
    public void setConnector(Object value) {
        if (value instanceof DatabaseConnectionBean) {
            this.connector = (DatabaseConnectionBean) value;
        }
        else {
            this.connector = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public Connection getConnection() {
        Connection goodCon = null;
        try {
            if (this.con.isClosed()) {
                if (this.connector.getNativeConnection().isClosed()) {
                    this.msg = "Connection is closed";
                    logger.log(Level.ERROR, this.msg);
                    throw new SystemException(this.msg);
                }
                goodCon = this.connector.getNativeConnection();
            }
            else {
                goodCon = this.con;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return goodCon;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.db.DatabaseTransApi#connect(java.lang.String,
     * java.lang.String, java.lang.Object)
     */
    public Object connect(String user, String password, Object dataSource)
            throws DatabaseException {
        return null;
    }

} // end class

