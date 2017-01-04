package com.api.config.old;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.RMT2Base;
import com.api.config.ConfigConstants;
import com.api.config.ConfigException;

/**
 * This abstract class provides common template for to setting up, managing, and
 * persisting system environment variables and application level propeties.
 * <p>
 * <b><u>Note</u></b><br>
 * When notating file paths for the value of an environment variable, it is best
 * to use UNC file system naming conventions in order to be compatible to
 * different types of OS's.
 * 
 * @author Roy Terrell
 * @deprecated No longer needed
 * 
 */
public abstract class AbstractSystemConfigImpl extends RMT2Base implements
        ResourceConfigurator, ConfigAttributes {

    private static final Logger logger = Logger
            .getLogger(AbstractSystemConfigImpl.class);

    /** Application properties */
    private static Properties APP_PROPS;

    protected static Map<String, String> DBMS_TYPES;

    protected static Hashtable<String, Integer> ENV_TYPES;

    protected String env;

    protected String server;

    protected String dbmsVendor;

    protected String webapps_drive;

    protected String webapps_dir;

    protected String defaultRoot;

    /** Application display name */
    protected String appDisplayName;

    protected String configLoc;

    protected String appConfigLoc;

    protected String appName;

    protected String appRootDirName;

    protected String authentication;

    static {
        AbstractSystemConfigImpl.APP_PROPS = new Properties();
        AbstractSystemConfigImpl.DBMS_TYPES = new HashMap<String, String>();
        AbstractSystemConfigImpl.DBMS_TYPES.put("1",
                ConfigConstants.DBMSTYPE_ASA);
        AbstractSystemConfigImpl.DBMS_TYPES.put("2",
                ConfigConstants.DBMSTYPE_ASE);
        AbstractSystemConfigImpl.DBMS_TYPES.put("3",
                ConfigConstants.DBMSTYPE_ORACLE);
        AbstractSystemConfigImpl.DBMS_TYPES.put("4",
                ConfigConstants.DBMSTYPE_SQLSERVER);
        AbstractSystemConfigImpl.DBMS_TYPES.put("5",
                ConfigConstants.DBMSTYPE_DB2);

        // Set master environment type hash.
        ENV_TYPES = new Hashtable<String, Integer>();
        ENV_TYPES.put(ConfigConstants.ENVTYPE_DEV, 1);
        ENV_TYPES.put(ConfigConstants.ENVTYPE_TEST, 2);
        ENV_TYPES.put(ConfigConstants.ENVTYPE_STAGE, 3);
        ENV_TYPES.put(ConfigConstants.ENVTYPE_PROD, 4);
    }

    /**
     * Default constructor which functions to initialize application properties
     * hash and creates a master map of environment types.
     */
    public AbstractSystemConfigImpl() {
        super();
    }

    /**
     * Performs initiailization that involves the data context.
     * 
     * @param initCtx
     *            arbitrary object representing the application's configuration.
     * @throws ConfigException
     *             If environment, configuration root path, server, and/or
     *             application name are not defined, or the environment variable
     *             defined with an invalid value.
     */
    public void init(Object initCtx) throws ConfigException {
        return;
    }

    /**
     * Assigns properties from the web container environment resoures, web.xml
     * and SystemConfigParms.properties, to JVM's System instance.
     * 
     * @throws ConfigException
     * @deprecated No longer needed
     */
    public void doSetup() throws ConfigException {
        logger.log(Level.INFO, "Fetching system properties for application, "
                + this.getAppName() + "...");
        return;
    }

    /**
     * Adds system related properteis to the System class variable.
     * 
     * @param ctx
     *            An arbitrary object representing some form of data source.
     * @throws ConfigException
     * @deprecated No longer needed
     */
    @Override
    public void doPostSetup(Object ctx) throws ConfigException {
        logger.log(
                Level.INFO,
                "Configuring system properties for application, "
                        + this.getAppName() + "...");
        // Environment variable should be valid
        this.addSysProperty(ConfigConstants.PROPNAME_ENVIRONMENT, this.getEnv());
        this.addSysProperty(ConfigConstants.PROPNAME_APP_NAME, this.appName);

        // // Configuration path should be valid
        // this.addSysProperty(ConfigConstants.PROPNAME_CFG_LOC,
        // this.getConfigLoc());
        // // Server should be valid
        // this.addSysProperty(ConfigConstants.PROPNAME_SERVER,
        // this.getServer());
        //
        // // Web container drive letter
        // this.addSysProperty(ConfigConstants.PROPNAME_WEB_DRIVE,
        // this.getWebapps_drive());
        // // Web container directory root
        // this.addSysProperty(ConfigConstants.PROPNAME_WEB_DIR,
        // this.getWebapps_dir());
        //
        // this.addSysProperty(ConfigConstants.PROPNAME_DEFAULT_APP_CTX_NAME,
        // this.defaultRoot);
        // this.addSysProperty(ConfigConstants.PROPNAME_DBMS_VENDOR,
        // this.dbmsVendor);
        //
        // this.addSysProperty(ConfigConstants.PROPNAME_AUTHENTICATOR,
        // this.authentication);
        // this.addSysProperty(ConfigConstants.PROPNAME_CONFIG_APP,
        // this.appConfigLoc);
        logger.log(
                Level.INFO,
                "Configuring system properties for application, "
                        + this.getAppName() + "complete.");
        return;
    }

    /**
     * Adds a key/value pair to the System properties collection. A check is
     * performed to ensure that a system property is not duplicated from another
     * application context. As a requirement, the user must have adequate
     * security to access/assign the system prooperty, key and value must be a
     * non-null value, and the key cannot be an empty String.
     * 
     * @param key
     *            The name of the system property. This key is added provided
     *            that it does not already exist.
     * @param value
     *            The value of the system proeprty.
     * @throws ConfigException
     *             If a security restricts the access/assignment of the system
     *             property, or when the key and/or the value is null, or when
     *             the key is an empty String.
     * 
     */

    protected void addSysProperty(String key, String value)
            throws ConfigException {
        // Validate key and add only if it is not already included in the list
        // of System properties.
        try {
            boolean keyExist = (System.getProperty(key) != null ? true : false);
            if (keyExist) {
                return;
            }
        } catch (SecurityException e) {
            throw new ConfigException(
                    "Security problem exist in attempting to access system property variable, "
                            + key);
        } catch (NullPointerException e) {
            throw new ConfigException("System property key argument is null");
        } catch (IllegalArgumentException e) {
            throw new ConfigException(
                    "System property key argument is an empty String");
        }

        // Let's attempt to add key/value pair to System properties.
        try {
            System.setProperty(key, value);
        } catch (SecurityException e) {
            throw new ConfigException(
                    "Security problem exist in attempting to assign system property: Key="
                            + key + "  value=" + value);
        } catch (NullPointerException e) {
            throw new ConfigException(
                    "System property value argument is null for key, " + key);
        }
        return;
    }

    /**
     * Gets the property value of <i>key</i> from either system properties or
     * application properties. If <i>key</i> is not found in application
     * properties then an attempt will be made to locate <i>key</i> in system
     * properties.
     * 
     * @param key
     *            The property name.
     * @return The property value or null if not found.
     */
    public String getProperty(String key) {
        String result = null;
        if (APP_PROPS == null) {
            logger.log(Level.WARN,
                    "Application property could not be found due to properties are not loaded");
            return null;
        }
        else {
            result = APP_PROPS.getProperty(key);
        }
        if (result == null) {
            result = System.getProperty(key);
        }
        return result;
    }

    /**
     * Searches for a property based on the specified key.
     * 
     * @param key
     *            the property key.
     * @return The value of the specified key.
     */
    public String getAppProperty(String key) {
        if (AbstractSystemConfigImpl.APP_PROPS == null) {
            return null;
        }
        return AbstractSystemConfigImpl.APP_PROPS.getProperty(key);
    }

    /**
     * Return the environment indicator.
     * 
     * @return String as "DEV", TEST", "STAGE", or "PROD";
     */
    public String getEnv() {
        return this.env;
    }

    /**
     * Obtains a reference to the environment types master hash.
     * 
     * @return Hashtable.
     */
    protected Hashtable getEnvTypes() {
        return ENV_TYPES;
    }

    /**
     * Return the server name.
     * 
     * @return String
     */
    protected String getServer() {
        return server;
    }

    /**
     * Return the DBMS Vendor code.
     * 
     * @return the dbmsVendor
     */
    protected String getDbmsVendor() {
        return dbmsVendor;
    }

    /**
     * @return the webapps_dir
     */
    protected String getWebapps_dir() {
        return webapps_dir;
    }

    /**
     * @return the webapps_drive
     */
    protected String getWebapps_drive() {
        return webapps_drive;
    }

    /**
     * @return the defaultRoot
     */
    public String getDefaultRoot() {
        return defaultRoot;
    }

    /**
     * @param defaultRoot
     *            the defaultRoot to set
     */
    public void setDefaultRoot(String defaultRoot) {
        this.defaultRoot = defaultRoot;
    }

    /**
     * Retrieves the DBMS Vendor type description based on its key.
     * 
     * @param key
     *            The key of the DBMS system.
     * @return The value assoicated with <i>key</i> or null if the value is not
     *         mapped.
     */
    public static final String getDbmsType(String key) {
        return AbstractSystemConfigImpl.DBMS_TYPES.get(key);
    }

    /**
     * @return the appName
     */
    public String getAppName() {
        return appName;
    }

    /**
     * @param appName
     *            the appName to set
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * @return the configLoc
     */
    public String getConfigLoc() {
        return configLoc;
    }

    /**
     * @param configLoc
     *            the configLoc to set
     */
    public void setConfigLoc(String configLoc) {
        this.configLoc = configLoc;
    }

    /**
     * Get all properties.
     * 
     * @return the appProps
     */
    public Properties getAppProps() {
        return APP_PROPS;
    }

}
