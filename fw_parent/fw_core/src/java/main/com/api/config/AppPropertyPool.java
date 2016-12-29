package com.api.config;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.RMT2Base;
import com.api.config.old.ConfigAttributes;
import com.api.config.old.ResourceConfigurator;

/**
 * Manages the application's properties.
 * 
 * @author Roy Terrell
 * 
 */
public class AppPropertyPool extends RMT2Base {
    private static Logger logger = Logger.getLogger("AppPropertyPool");

    private static Properties PROPS;

    /**
     * Default constructor which initializes AppPropertyPool
     * 
     */
    private AppPropertyPool() {
        super();
    }

    public void init() {
        if (PROPS == null) {
            PROPS = new Properties();
        }
    }

    /**
     * Creates an instance of AppPropertyPool class.
     * 
     * @return DatabaseConnectionPool.
     */
    public static AppPropertyPool getInstance() {
        AppPropertyPool pool = new AppPropertyPool();
        return pool;
    }

    /**
     * Adds a key/value pair to the local properties pool.
     * 
     * @param key
     * @param value
     */
    public final void addProperty(String key, String value) {
        this.init();
        AppPropertyPool.PROPS.put(key, value);
    }

    /**
     * Adds a key/value pair to the System properties collection.
     * 
     * @param key
     * @param value
     */
    public final void addSystemProperty(String key, String value) {
        System.setProperty(key, value);
    }

    /**
     * Returns the value of the property named <i>key</i>.
     * <p>
     * First, the local application property pool is searched. If <i>key</i> is
     * not found, then the System properties is searched
     * 
     * @param key
     *            The name of the property to access.
     * @return The property value or null if property does not exist or if the
     *         properties collection is invalid.
     */
    public static final String getProperty(String key) {
        if (AppPropertyPool.PROPS == null) {
            return null;
        }
        String val = AppPropertyPool.PROPS.getProperty(key);
        if (val == null) {
            val = System.getProperty(key);
        }
        return val;
    }

    /**
     * Initializes the application properties collection from an outside source.
     * 
     * @param props
     *            ({@link com.api.config.ConfigAttributes ConfigAttributes}
     * @deprecated No longer needed
     */
    public final void addProperties(ConfigAttributes props) {
        // AppPropertyPool.PROPS = props;
    }

    /**
     * Verifies whether or not an AppPropertyPool instance's configuration
     * property pool has been setup.
     * 
     * @return true when initialized and false for not initiailized.
     * @deprecated No longer needed
     */
    public static final boolean isAppConfigured() {
        return (PROPS != null);
    }

    /**
     * 
     * @return
     * @deprecated No longer needed
     */
    public static final String getEnvirionment() {
        if (PROPS == null) {
            return null;
        }
        if (PROPS instanceof ResourceConfigurator) {
            return ((ResourceConfigurator) PROPS).getEnv();
        }
        return null;

    }

}