package com.api.foundation;

import java.io.Serializable;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.RMT2Base;
import com.api.ApiInitException;
import com.api.persistence.DaoClient;
import com.api.persistence.DatabaseException;
import com.api.util.RMT2File;
import com.api.util.RMT2String;

/**
 * Abstract transaction API class that provides common functionality.
 * <p>
 * When implementing this class, the design of each method should be thought of
 * as a separate transaction for the API.
 * <p>
 * In order to properly initialize the logger, the log configuration for the
 * hosting application must be found in <i>Application install
 * directory</i>/config/log4j.properties.
 * 
 * @author roy.terrell
 * 
 */

public abstract class AbstractTransactionApiImpl extends RMT2Base implements TransactionApi {

    private static boolean LOGGER_LOADED = false;

    protected static final String LOG_API_PATH = "config/log4j.properties";

    protected static final String APP_PARMS_PATH = "config.{$}-AppParms";
    
    private static Logger logger;
    
    private Properties config;

    /**
     * The login id of the user in control of this API.
     */
    protected String apiUser;

    private DaoClient sharedDao;

    /**
     * Creates a AbstractTransactionApiImpl object without a connection to the
     * datasource.
     * 
     * @throws DatabaseException
     * @throws SystemException
     */
    protected AbstractTransactionApiImpl() {
        super();
        this.initLogger();
    }

    /**
     * Creates a AbstractTransactionApiImpl based on the name of the application
     * and loads the application specific configuration, if available.
     * 
     * @param appName
     */
    protected AbstractTransactionApiImpl(String appName) {
        this();
        String propertyFileName = RMT2String.replace(APP_PARMS_PATH, appName, "{$}");
        this.initConfiguration(propertyFileName);
        return;
    }
    
    /**
     * Creates an AbstractTransactionApiImpl initialized with a connection to
     * the data source, <i>dao</i>. object.
     * 
     * @param dao
     *            an instance of {@link DaoClient}
     */
    protected AbstractTransactionApiImpl(DaoClient dao) {
        this();
        this.sharedDao = dao;
        return;
    }
    
    /**
     * 
     * @param appName
     * @param dao
     */
    protected AbstractTransactionApiImpl(String appName, DaoClient dao) {
        this(dao);
        String propertyFileName = RMT2String.replace(APP_PARMS_PATH, appName, "{$}");
        this.initConfiguration(propertyFileName);
    }

    /**
     * 
     * @param propertyFileName
     */
    protected void initConfiguration(String propertyFileName) {
        try {
            this.config = RMT2File.loadPropertiesFromClasspath(propertyFileName);
        } catch (Exception e) {
            this.msg = "Application specific configuration could not be loaded for project named, "
                    + propertyFileName
                    + ".  Ensure that project name is declared correctly";
            logger.warn(this.msg, e);
        }
    }
    
    /**
     * Sets up the logging environment that is to be used throughout the entire
     * application.
     * <p>
     * Uses a singleton pattern to only initialize the logger once for a given
     * application context.
     * 
     * 
     * @throws ApiInitException
     *             if the log4j.properties is not found or the occurrence of
     *             genreal IO errors
     */
    public boolean initLogger() throws ApiInitException {
        try {
            logger = Logger.getLogger(AbstractTransactionApiImpl.class);
            logger.info("Logger initialized for API successfully");
        } catch (Exception e) {
            msg = "Unable to locate log4j configuration, " + AbstractTransactionApiImpl.LOG_API_PATH;
            throw new ApiInitException(msg, e);
        }
        return (logger == null ? false : true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.foundation.TransactionApi#beginTrans()
     */
    @Override
    public void beginTrans() {
        if (this.sharedDao == null) {
            throw new DatabaseException(
                    "Unable to begin transaction...shared DAO is not initialized");
        }
        this.sharedDao.beginTrans();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.foundation.TransactionApi#commitTrans()
     */
    @Override
    public void commitTrans() {
        if (this.sharedDao == null) {
            throw new DatabaseException(
                    "Unable to commit transaction...shared DAO is not initialized");
        }
        this.sharedDao.commitTrans();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.foundation.TransactionApi#rollbackTrans()
     */
    @Override
    public void rollbackTrans() {
        if (this.sharedDao == null) {
            throw new DatabaseException(
                    "Unable to rollback transaction...shared DAO is not initialized");
        }
        this.sharedDao.rollbackTrans();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.foundation.TransactionApi#close()
     */
    @Override
    public void close() {
        if (this.sharedDao == null) {
            throw new DatabaseException(
                    "Unable to close DAO...shared DAO is not initialized");
        }
        this.sharedDao.close();
    }

    @Override
    public Object sendMessage(String messageId, Serializable payload) throws TransactionApiException {
        throw new UnsupportedOperationException("This operation is not supported at this level.  Must implemented at the descendent");
    }
    
    /**
     * Get the login id of the user in controll of the API.
     * 
     * @return String
     */
    public String getApiUser() {
        return apiUser;
    }

    /**
     * Set the login id of the user in controll of the API.
     * <p>
     * If the shared DAO is valid, the user id is assigned to the DAO as well.
     * 
     * @param apiUser
     *            the user's login id
     */
    public void setApiUser(String apiUser) {
        this.apiUser = apiUser;
        if (this.sharedDao != null) {
            this.sharedDao.setDaoUser(apiUser);
        }
    }

    /**
     * @return the sharedDao
     */
    public DaoClient getSharedDao() {
        return sharedDao;
    }

    /**
     * @param dao
     *            the sharedDao to set
     */
    protected void setSharedDao(DaoClient dao) {
        this.sharedDao = dao;
    }

    public Properties getConfig() {
        return config;
    }
 
} // End class
