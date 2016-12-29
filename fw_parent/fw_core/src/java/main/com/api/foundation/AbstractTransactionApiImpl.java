package com.api.foundation;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.RMT2Base;
import com.api.ApiInitException;
import com.api.persistence.DaoClient;
import com.api.persistence.DatabaseException;
import com.util.RMT2File;

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

public abstract class AbstractTransactionApiImpl extends RMT2Base implements
        TransactionApi {

    private static boolean LOGGER_LOADED = false;

    protected static final String LOG_API_PATH = "config/log4j.properties";

    protected static final String APP_PARMS_PATH = "config/AppParms.properties";

    private static Logger logger;

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
        if (LOGGER_LOADED) {
            return true;
        }

        // Load logger configuration.
        String msg = null;
        try {
            Properties props = RMT2File
                    .loadPropertiesObject(AbstractTransactionApiImpl.LOG_API_PATH);
            PropertyConfigurator.configure(props);
            AbstractTransactionApiImpl.logger = Logger
                    .getLogger(AbstractTransactionApiImpl.class);
            logger.info("Logger initialized for API successfully");
            LOGGER_LOADED = true;
            return LOGGER_LOADED;
        } catch (Exception e) {
            msg = "Unable to locate log4j configuration, "
                    + AbstractTransactionApiImpl.LOG_API_PATH;
            throw new ApiInitException(msg, e);
        }
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

} // End class
