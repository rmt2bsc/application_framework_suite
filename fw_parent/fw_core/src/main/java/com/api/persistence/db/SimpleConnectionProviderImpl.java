package com.api.persistence.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.SystemException;
import com.api.constants.RMT2SystemExceptionConst;
import com.api.persistence.CannotReadManagerConfigurationException;
import com.api.persistence.DatabaseException;
import com.api.util.RMT2File;

/**
 * A vanilla implementation of the api, {@link ConnectionProvider}, which uses
 * the input of database URL, user id, and password to obtain the instructions
 * on how to find the required resources that will be used to establish database
 * connections.
 * 
 * @author Roy Terrell
 * 
 */
public class SimpleConnectionProviderImpl extends
        AbstractConnectionProviderImpl {

    private static Logger logger = Logger
            .getLogger(SimpleConnectionProviderImpl.class);

    private String dbDriverClass;

    private String dbURL;

    private String dbUserID;

    private String dbPassword;

    private Properties systemData;

    private Properties dbParms;

    private Driver dbDriver;

    private String database;

    private String dbPropName;

    private String appName;

    private String ctxName;

    // private ResourceBundle rb;

    /**
     * Default constructor
     */
    public SimpleConnectionProviderImpl() throws DatabaseException,
            SystemException {
        super();
        this.ctxName = null;
    }

    /**
     * 
     * @param dbUrl
     * @param userId
     * @param password
     * @throws DatabaseException
     * @throws SystemException
     */
    public SimpleConnectionProviderImpl(String dbUrl, String userId,
            String password) throws DatabaseException, SystemException {
        this();
        this.dbURL = dbUrl;
        this.dbUserID = userId;
        this.dbPassword = password;
    }

    /**
     * 
     * @param contextName
     * @param dbUrl
     * @param userId
     * @param password
     * @throws DatabaseException
     * @throws SystemException
     */
    public SimpleConnectionProviderImpl(String contextName, String dbUrl,
            String userId, String password) throws DatabaseException,
            SystemException {
        this(dbUrl, userId, password);
        this.ctxName = contextName;
    }

    /**
     * Initializes the JDBC SQL driver using database realted property values
     * stored in DB-ORM-Config.properties.
     * 
     * @throws DatabaseException
     *             When the SystemParms.properties file is not found or cannont
     *             be instantiated into a ResourceBundle.
     * @throws SystemException
     *             When the following properties are not found or do not have
     *             values in the SystemParms.properties: <blockquote> <uo> <li>
     *             dbdriver - the JDBC Driver name</li> <li>dburl - The URL of
     *             the database to establish a connection</li> <li>userid - the
     *             database user id</li> <li>password - the databae password</li>
     *             <li>defaultconnections - the total number of default
     *             connections</li> </uo>. </blockquote> Also, an exception is
     *             thrown when the total number of default connections is not a
     *             valid number.
     */
    public void init() {
        super.init();
        this.dbDriverClass = null;
        this.dbURL = null;
        this.dbUserID = null;
        this.dbPassword = null;
        // this.rb = RMT2File
        // .loadAppConfigProperties(DatabaseConnectionConstants.CONNECTION_CONFIG);
        return;
    }

    /**
     * Gets a database connection based on data that is mapped as
     * "DbResourceName" in the DB-ORM-Config.properties.
     * <p>
     * By default, the connection will be set to manage its transactions in an
     * Auto Commit fashion.
     * 
     * @return {@link bean.db.DatabaseConnectionBean DatabaseConnectionBean}.
     * @throws SystemException
     *             Problem obtaining database connection bean.
     */
    public synchronized DatabaseConnectionBean getConnection()
            throws SystemException {
        // Use the DB-ORM-Config.properties file to determine the specific
        // database configuration (.properties) file found mapped as
        // "DbResourceName"
        String dbPropFileKey = null;
        String envResourceKey = this.getEnvDbResourceName();
        if (this.ctxName == null) {
            dbPropFileKey = envResourceKey;
        }
        else {
            dbPropFileKey = this.ctxName + "." + envResourceKey;
        }
        String configFile = this.getConfig().getString(dbPropFileKey);

        // Initialize db parms from config file
        this.start(configFile);

        // Create the connection.
        DatabaseConnectionBean dbConn = new DatabaseConnectionBean();
        // At this point, not connections are available. Create one.
        Connection con = this.createConnection();
        dbConn.setNativeConnection(con);
        // Initialize name property of dbConn. Format will be <app name> + <date
        // long value> or <connection name> + <date long value>.
        String seq = String.valueOf(new Date().getTime());
        dbConn.setName(seq);
        return dbConn;
    }

    public synchronized DatabaseConnectionBean getConnection(
            String driverClassName, String dbUrl, String userId, String password)
            throws SystemException {
        this.dbURL = dbUrl;
        this.dbUserID = userId;
        this.dbPassword = password;
        this.dbDriverClass = driverClassName;

        this.validateParms();

        // Create the connection.
        DatabaseConnectionBean dbConn = new DatabaseConnectionBean();
        // At this point, not connections are available. Create one.
        Connection con = this.createConnection();
        dbConn.setNativeConnection(con);
        // Initialize name property of dbConn. Format will be <app name> + <date
        // long value> or <connection name> + <date long value>.
        String seq = String.valueOf(new Date().getTime());
        dbConn.setName(seq);
        return dbConn;

    }

    /**
     * Create a Connection and return to caller.
     * 
     * @return Connection
     * @throws SystemException
     *             Error establishing the database connection.
     */
    private synchronized Connection createConnection() throws SystemException {
        Connection con = null;
        try {
            con = this.dbDriver.connect(this.dbURL, this.dbParms);
            if (con == null) {
                con = DriverManager.getConnection(this.dbURL);
            }
            return con;
        } catch (SQLException e) {
            this.msg = RMT2SystemExceptionConst.MSG_DB_DOWN + " - "
                    + e.getMessage();
            throw new SystemException(this.msg, e);
        }
    }

    /**
     * Initializes the DatabaseConnectionPoolImpl instance using a specified
     * configuration .properties file, <i>configFile</i>.
     * 
     * Initializes the DatabaseConnectionPoolImpl instance by gathering all of
     * the data needed to establish connections, monitor the min and max number
     * of required connections, and initilizing the database connection pool
     * collective.
     * 
     * @param configFile
     *            .properties file containing the JDBC configuration data needed
     *            to establish a DB connection. This resource can be loaded from
     *            either the file systems or the classpath. First, an attempt is
     *            made to load the properties file from the files system. Upon
     *            failure, the next attempt is to load the properties from the
     *            classpath.
     * @throws DatabaseException
     * @throws SystemException
     *             When the <i>configFile</i> cannot be loaded
     */
    private void start(String configFile) throws DatabaseException,
            SystemException {
        // try to create Properties instance from .properties file residing
        // somewhere in the java classpath.
        try {
            ResourceBundle rb = RMT2File.loadAppConfigProperties(configFile);
            this.systemData = RMT2File.convertResourceBundleToProperties(rb);
        } catch (Exception ee) {
            this.msg = "JDBC Connection configuration could not be loaded.  Proerties file, "
                    + configFile
                    + ", could not be found in file system or classpath";
            logger.error(this.msg);
            throw new CannotReadManagerConfigurationException(this.msg, ee);
        }

        this.dbDriverClass = this.systemData.getProperty("dbdriver");
        this.database = this.systemData.getProperty("database");
        this.dbPropName = this.systemData.getProperty("dbPropertyName");
        this.appName = this.systemData.getProperty("appcode");
        this.validateParms();

        // if (this.appName == null) {
        // this.appName = "(Application Name was not configured)";
        // }
        //
        // // Validate essential database variables
        // if (this.dbDriverClass == null) {
        // throw new SystemException(
        // RMT2SystemExceptionConst.MSG_NULL_DB_DRIVER);
        // }
        // if (this.dbURL == null) {
        // throw new SystemException(RMT2SystemExceptionConst.MSG_NULL_DB_URL);
        // }
        // if (this.dbUserID == null) {
        // throw new SystemException(
        // RMT2SystemExceptionConst.MSG_NULL_DB_USERID);
        // }
        // if (this.dbPassword == null) {
        // throw new SystemException(
        // RMT2SystemExceptionConst.MSG_NULL_DB_PASSWORD);
        // }
        //
        // // Setup Database Driver Object and its parameters.
        // try {
        // this.dbDriver = (Driver) Class.forName(this.dbDriverClass)
        // .newInstance();
        // } catch (ClassNotFoundException e) {
        // this.msg =
        // "Unable to create database connection due to the database driver class was not found in classpath";
        // throw new DatabaseException(this.msg, e);
        // } catch (IllegalAccessException e) {
        // this.msg =
        // "Unable to create database connection due to illegal access of database driver class";
        // throw new DatabaseException(this.msg, e);
        // } catch (InstantiationException e) {
        // this.msg =
        // "Unable to create database connection due to the database driver encountered a class instantiation error";
        // throw new DatabaseException(this.msg, e);
        // }
        // this.dbParms = new Properties();
        // this.dbParms.setProperty("user", this.dbUserID);
        // this.dbParms.setProperty("password", this.dbPassword);
        // if (database != null) {
        // this.dbParms.setProperty(dbPropName, database);
        // }
        //
        // logger.info("Database parms initialized for application, "
        // + this.appName);
    }

    private void validateParms() throws SystemException {
        if (this.appName == null) {
            this.appName = "(Application Name was not configured)";
        }

        // Validate essential database variables
        if (this.dbDriverClass == null) {
            throw new SystemException(
                    RMT2SystemExceptionConst.MSG_NULL_DB_DRIVER);
        }
        if (this.dbURL == null) {
            throw new SystemException(RMT2SystemExceptionConst.MSG_NULL_DB_URL);
        }
        if (this.dbUserID == null) {
            throw new SystemException(
                    RMT2SystemExceptionConst.MSG_NULL_DB_USERID);
        }
        if (this.dbPassword == null) {
            throw new SystemException(
                    RMT2SystemExceptionConst.MSG_NULL_DB_PASSWORD);
        }

        // Setup Database Driver Object and its parameters.
        try {
            this.dbDriver = (Driver) Class.forName(this.dbDriverClass)
                    .newInstance();
        } catch (ClassNotFoundException e) {
            this.msg = "Unable to create database connection due to the database driver class was not found in classpath";
            throw new DatabaseException(this.msg, e);
        } catch (IllegalAccessException e) {
            this.msg = "Unable to create database connection due to illegal access of database driver class";
            throw new DatabaseException(this.msg, e);
        } catch (InstantiationException e) {
            this.msg = "Unable to create database connection due to the database driver encountered a class instantiation error";
            throw new DatabaseException(this.msg, e);
        }
        this.dbParms = new Properties();
        this.dbParms.setProperty("user", this.dbUserID);
        this.dbParms.setProperty("password", this.dbPassword);
        if (database != null) {
            this.dbParms.setProperty(dbPropName, database);
        }

        logger.info("Database parms initialized for application, "
                + this.appName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.ConnectionProvider#setContextName(java.lang.String)
     */
    @Override
    public void setContextName(String ctxName) {
        this.ctxName = ctxName;
    }

} // End of Class

