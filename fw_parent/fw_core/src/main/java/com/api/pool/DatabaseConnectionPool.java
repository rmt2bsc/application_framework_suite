package com.api.pool;

import java.sql.SQLException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.RMT2Base;
import com.SystemException;
import com.api.ConnectionProvider;
import com.api.constants.RMT2SystemExceptionConst;
import com.api.persistence.db.DatabaseConnectionBean;

/**
 * This class provides the basic method implementations to setup and manage a
 * database connection pool for a web application. Valid users are associated
 * with and disassociated from a database connection object via login id. The
 * scope in which the connection pool will reside is at the entire web
 * container. This will allow all web applications contained within a given
 * servlet context to access and share the connection pool resources.
 * 
 * When a connection bean is obtained from the pool, DatabaseConnectionPool sets
 * that connection to "In Use" status by invoking {@link DatabaseConnectionBean
 * 's DatabaseConnectionBean} open() method. Conversely, the user is required to
 * return the connection to the database pool by invoking
 * DatabaseConnectionBean's close() method.
 * 
 * @author Roy Terrell
 * 
 */
public class DatabaseConnectionPool extends RMT2Base {
    public static int CONNECTION_INVALID = -200;

    private static Logger logger = Logger.getLogger("DatabaseConnectionPool");

    private static ConnectionProvider DBAPI;

    /**
     * Default constructor which initializes className which is not accessible
     * to the public.
     * 
     */
    private DatabaseConnectionPool() {
        super();
    }

    /**
     * Instantiates a DatabaseConnectionPool class.
     * 
     * @return DatabaseConnectionPool.
     */
    public static DatabaseConnectionPool getInstance() {
        try {
            DatabaseConnectionPool api = new DatabaseConnectionPool();
            return api;
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            return null;
        }

    }

    /**
     * Adds a ConnectionProvider to this instance.
     * 
     * @param ConnectionProvider
     */
    public synchronized void addConnectionApi(ConnectionProvider pool) {
        DatabaseConnectionPool.DBAPI = pool;
    }

    // /**
    // * Obtains the total number of "In USe" connections in the connection
    // pool.
    // *
    // * @param request servlet request object.
    // * @return A value >= 0 or -1 if connection pool could not be obtained.
    // */
    // public synchronized static int getInUseCount(HttpServletRequest request)
    // {
    // if (DatabaseConnectionPool.DBAPI == null) {
    // return DatabaseConnectionPool.CONNECTION_INVALID;
    // }
    // return DatabaseConnectionPool.DBAPI.getInUseCount();
    // }

    /**
     * Obtains the next available database connection from the contatiner
     * managed connection pool.
     * 
     * @return {@link DatabaseConnectionBean}
     * @throws SystemException
     *             Connection is unobtainable from the pool.
     */
    public synchronized static DatabaseConnectionBean getAvailConnectionBean()
            throws SystemException {
        String msg = null;
        if (DatabaseConnectionPool.DBAPI == null) {
            msg = "Database connection pool is not initialized";
            logger.error(msg);
            throw new SystemException(msg);
        }
        // The following line of code will handle obtaining a vaild unused
        // connection bean object. If one does not exist it will create one.
        DatabaseConnectionBean conBean = (DatabaseConnectionBean) DatabaseConnectionPool.DBAPI
                .getConnection();
        if (conBean == null) {
            logger.error(RMT2SystemExceptionConst.MSG_CONNECTBEAN_INVALID);
            throw new SystemException(
                    RMT2SystemExceptionConst.MSG_CONNECTBEAN_INVALID);
        }
        // Render connection bean as "In Use".
        conBean.open();
        initConnectionBeanData(conBean);
        return conBean;
    }

    /**
     * Obtains the next available database connection from the RMT2 custom
     * connection pooling implementation. The connection is flagged as "In Use".
     * If the user is found to be already associated with a connection, then
     * that connection is returned. Otherwise, an unused connection is obtained
     * from the pool.
     * 
     * @param conId
     *            an arbitrary String value representing the connection id.
     * @return {@link DatabaseConnectionBean}
     * @throws SystemException
     *             Connection is unobtainable from the pool.
     */
    public synchronized static DatabaseConnectionBean getAvailConnectionBean(
            String conId) throws SystemException {
        // Try to recover any stale connections in the pool before fetching a
        // connection.
        // DatabaseConnectionPool.DBAPI.recoverStaleConnections();

        // The following line of code will handle obtaining a vaild unused
        // connection bean object. If one does not exist it will create one.
        DatabaseConnectionBean conBean = (DatabaseConnectionBean) DatabaseConnectionPool.DBAPI
                .getConnection();
        if (conBean == null) {
            throw new SystemException(
                    RMT2SystemExceptionConst.MSG_CONNECTBEAN_INVALID);
        }
        conBean.setLoginId(conId);
        // Render connection bean as "In Use".
        conBean.open();

        initConnectionBeanData(conBean);
        return conBean;
    }

    /**
     * Initializes database connection bean instance with database vendor
     * specific meta data.
     * 
     * @param conBean
     *            The connection bean to intialize.
     */
    private static void initConnectionBeanData(DatabaseConnectionBean conBean) {
        try {
            conBean.setDbms(conBean.getNativeConnection().getMetaData()
                    .getDatabaseProductName());
            conBean.setDbmsVersion(conBean.getNativeConnection().getMetaData()
                    .getDatabaseProductVersion());
            conBean.setDbUrl(conBean.getNativeConnection().getMetaData()
                    .getURL());
            conBean.setDbUserId(conBean.getNativeConnection().getMetaData()
                    .getUserName());
            conBean.setDriverName(conBean.getNativeConnection().getMetaData()
                    .getDriverName());
            conBean.setDriverMajorVerNo(conBean.getNativeConnection()
                    .getMetaData().getDriverMajorVersion());
            conBean.setDriverMinorVerNo(conBean.getNativeConnection()
                    .getMetaData().getDriverMinorVersion());
            return;
        } catch (SQLException e) {
            logger.log(Level.WARN,
                    "Problem occurred initializing connection bean with database metadata.  "
                            + e.getMessage());
        }
    }

    /**
     * Determines if connection pool exist on the servlet context using the flag
     * set in method, getConnectionPool. If found to be false, the pool is set
     * on the context.
     * 
     * @param request
     *            HttpServletRequest object
     * @param conPool
     *            The target databsase connection pool.
     */
    public synchronized static boolean isConnectionPoolValid() {
        return (DatabaseConnectionPool.DBAPI != null);
    }

}