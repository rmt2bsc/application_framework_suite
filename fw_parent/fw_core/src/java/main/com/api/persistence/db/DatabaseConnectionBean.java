package com.api.persistence.db;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.SQLException;

import com.RMT2Base;
import com.SystemException;

//import com.api.config.PropertyFileSystemResourceConfigImpl;

/**
 * Class that serves as a wrapper for a JDBC Database Connection object. Also
 * responsible for retrieving SQL Datasource (sql and xml).
 * 
 * @author appdev
 * 
 */
public class DatabaseConnectionBean extends RMT2Base implements Serializable {
    private static final long serialVersionUID = -2122518924309758033L;

    private Connection nativeConnection;

    private String dbDriverClass;

    private boolean inUse;

    private String name;

    private String dbUserId;

    private String dbPassword;

    private String dbms;

    private String dbmsVersion;

    private String dbURL;

    private String loginId;

    private String driverName;

    private int driverMinorVerNo;

    private int driverMajorVerNo;

    private Logger logger;

    // //////////////////////////////////
    // Constructor
    // //////////////////////////////////
    public DatabaseConnectionBean() throws SystemException {
        super();
        logger = Logger.getLogger("DatabaseConnectionBean");
    }

    /**
     * Ensures that the internal connection is marked "In Use".
     * 
     */
    public void open() {
        String msg = "+++++++ A database connection was opened: "
                + this.nativeConnection.hashCode();
        logger.info(msg);
        return;
    }

    /**
     * Ensures that the database connection is closed. When managing connections
     * via the straight JDBC approach, the connection is physically closed. When
     * a database connection pool is employed, the connection is returned to the
     * pool for reuse
     * 
     */
    public void close() {
        try {
            if (!this.nativeConnection.isClosed()) {
                msg = "------- A database connection was closed:  "
                        + this.nativeConnection.hashCode();
                logger.log(Level.INFO, msg);
                this.nativeConnection.close();
            }
        } catch (SQLException e) {
            throw new SystemException(msg, e);
        }
    }

    /**
     * Gets the JDBC Connection object.
     * 
     * @return Connection
     */
    public Connection getNativeConnection() {
        return this.nativeConnection;
    }

    /**
     * Sets the JDBC Connection object.
     * 
     * @param value
     *            Connection
     */
    public void setNativeConnection(Connection value) {
        this.nativeConnection = value;
    }

    public String getDbDriverClass() {
        return this.dbDriverClass;
    }

    public void setDbDriverClass(String value) {
        this.dbDriverClass = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getDbms() {
        return this.dbms;
    }

    public void setDbms(String value) {
        this.dbms = value;
    }

    public String getDbmsVersion() {
        return this.dbmsVersion;
    }

    public void setDbmsVersion(String value) {
        this.dbmsVersion = value;
    }

    public String getDbUrl() {
        String val = null;
        if (this.dbURL == null) {
            if (this.nativeConnection != null) {
                try {
                    val = this.nativeConnection.getMetaData().getURL();
                } catch (SQLException e) {
                    val = "DB URL could not be obtained from meta data";
                }
            }
            else {
                val = "DB URL information is N/A";
            }
        }
        else {
            val = this.dbURL;
        }
        return val;
    }

    public void setDbUrl(String value) {
        this.dbURL = value;
    }

    public String getDbUserId() {
        return this.dbUserId;
    }

    public void setDbUserId(String value) {
        this.dbUserId = value;
    }

    public String getLoginId() {
        return this.loginId;
    }

    public void setLoginId(String value) {
        this.loginId = value;
    }

    public String getDbPassword() {
        return this.dbPassword;
    }

    public void setInUse(boolean value) {
        this.inUse = value;
    }

    public boolean getInUse() {
        return this.inUse;
    }

    public int getDriverMajorVerNo() {
        return driverMajorVerNo;
    }

    public void setDriverMajorVerNo(int driverMajorVerNo) {
        this.driverMajorVerNo = driverMajorVerNo;
    }

    public int getDriverMinorVerNo() {
        return driverMinorVerNo;
    }

    public void setDriverMinorVerNo(int driverMinorVerNo) {
        this.driverMinorVerNo = driverMinorVerNo;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void initBean() throws SystemException {

        try {
            this.inUse = false;
            this.name = "DBConn";
            return;
        } catch (Exception e) {
            throw new SystemException(e.getMessage());
        }
    }

    /**
     * Returns information about the connection as String delimited by line
     * breaks
     * 
     * @return String
     */
    public String getInfo() {
        StringBuffer details = new StringBuffer();
        details.append("Connection name=");
        details.append(this.name);
        details.append("\n");
        details.append("Connected User Id=");
        details.append(this.dbUserId);
        details.append("\n");
        return details.toString();
    }
} // End of Class