package com.api.pool;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.RMT2Base;
import com.api.config.old.ConfigAttributes;
import com.api.config.old.ResourceConfigurator;

/**
 * Manages the application's properties.
 * 
 * @author Roy Terrell
 * @deprecated No longer needed
 * 
 */
public class CopyOfAppPropertyPool extends RMT2Base {
    private static Logger logger = Logger.getLogger("AppPropertyPool");

    private static ConfigAttributes PROPS;

    /**
     * Default constructor which initializes AppPropertyPool
     * 
     */
    private CopyOfAppPropertyPool() {
        super();
    }

    /**
     * Creates an instance of AppPropertyPool class.
     * 
     * @return DatabaseConnectionPool.
     */
    public static CopyOfAppPropertyPool getInstance() {
        try {
            CopyOfAppPropertyPool pool = new CopyOfAppPropertyPool();
            return pool;
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            return null;
        }

    }

    /**
     * Initializes the application properties collection from an outside source.
     * 
     * @param props
     *            ({@link com.api.config.ConfigAttributes ConfigAttributes}
     * @deprecated No longer needed
     */
    public final void addProperties(ConfigAttributes props) {
        CopyOfAppPropertyPool.PROPS = props;
    }

    /**
     * Returns the value of the property named <i>key</i>.
     * 
     * @param key
     *            The name of the property to access.
     * @return The property value or null if property does not exist or if the
     *         properties collection is invalid.
     * @deprecated No longer needed
     */
    public static final String getProperty(String key) {
        if (CopyOfAppPropertyPool.PROPS == null) {
            return null;
        }
        return CopyOfAppPropertyPool.PROPS.getProperty(key);
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