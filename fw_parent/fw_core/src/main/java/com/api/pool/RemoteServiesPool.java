package com.api.pool;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import java.util.Properties;

import com.RMT2Base;

/**
 * Manages a list of URL's pertaining to remote services.
 * 
 * @author Roy Terrell
 * 
 */
public class RemoteServiesPool extends RMT2Base {
    private static Logger logger = Logger.getLogger("RemoteServiesPool");

    private static Properties SERVICES;

    private static Properties SERVICE_TYPES;

    private static Properties SERVICE_SUBTYPES;

    /**
     * Default constructor which initializes className which is not accessible
     * to the public.
     * 
     */
    private RemoteServiesPool() {
        super();
    }

    /**
     * Instantiates a DatabaseConnectionPool class.
     * 
     * @return DatabaseConnectionPool.
     */
    public static RemoteServiesPool getInstance() {
        try {
            RemoteServiesPool pool = new RemoteServiesPool();
            return pool;
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            return null;
        }

    }

    public void setServices(Properties services) {
        RemoteServiesPool.SERVICES = services;
    }

    public void setServiceTypes(Properties types) {
        RemoteServiesPool.SERVICE_TYPES = types;
    }

    public void setServiceSubTypes(Properties types) {
        RemoteServiesPool.SERVICE_SUBTYPES = types;
    }

    /**
     * Get complete list of remote services that are configured for the system.
     * 
     * @return Properties
     */
    public static Properties getServices() {
        return RemoteServiesPool.SERVICES;
    }

    /**
     * Get complete list of remote service types that are configured for the
     * system.
     * 
     * @return Properties
     */
    public static Properties getServiceTypes() {
        return RemoteServiesPool.SERVICE_TYPES;
    }

    /**
     * Get complete list of remote service sub types that are configured for the
     * system.
     * 
     * @return Prooperties
     */
    public static Properties getServiceSubTypes() {
        return RemoteServiesPool.SERVICE_SUBTYPES;
    }

}