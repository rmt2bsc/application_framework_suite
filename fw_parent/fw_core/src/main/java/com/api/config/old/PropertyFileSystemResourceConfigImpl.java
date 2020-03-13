package com.api.config.old;

import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.SystemException;
import com.api.config.ConfigConstants;
import com.api.config.ConfigException;
import com.api.util.RMT2File;

/**
 * This class implements {@link com.api.config.ResourceConfigurator
 * ResourceConfigurator} for the purpose of providing a centralized facility for
 * a web application to identify properties. Properties can be setup on a global
 * level accessible by all applications within a single JVM or at the level of
 * an individual application. Global properties are stored and accessed via the
 * system properties entity of the JVM. On the other hand, appliation properties
 * are setup within each PropertyFileSystemResourceConfigImpl instance of a
 * single application.
 * <p>
 * The basic functionality of this class is to load various attributes from the
 * web server's environment variables and local properties files into the
 * current JVM's system properties and custom application properties. Once the
 * properties are properly loaded, they can be accessed using the {@link
 * com.api.config.PropertyFileSystemResourceConfigImpl.#getProperty(String)
 * getProperty(String key)} method. For web applications, it is advisable to
 * create an instance of PropertyFileSystemResourceConfigImpl with an argument
 * of type ServletConfig.
 * <p>
 * <b><u>How to use</u></b><br>
 * 
 * <pre>
 *  This code snippet is intended to be used inside the servlet method, <i>init(ServletConfig config)</i>.  
 *  Notice the ServletConfig parameter, <i>config</i>.
 *  
 *  import com.api.pool.AppPropertyPool;
 *      try {
 *         // Instantiate configurator.
 *         ResourceConfigurator cfg = new PropertyFileSystemResourceConfigImpl(config);
 *         // Load the actual application properties  
 *         cfg.doSetup();  
 *         // Assign configurator to the application property pool as a ConfigAttribute type.
 *         ConfigAttributes appConfig = (ConfigAttributes) cfg;   
 *         AppPropertyPool pool = AppPropertyPool.getInstance();
 *         pool.addProperties(appConfig);
 *      }   
 *      catch (ConfigException e1) {       
 *         e1.printStackTrace();      
 *      }
 * </pre>
 * 
 * @author RTerrell
 */
class PropertyFileSystemResourceConfigImpl extends AbstractSystemConfigImpl {
    private static Logger logger;

    /** Application properties */
    private Properties appProps;

    /** Application display name */
    private String appDisplayName;

    private String appConfigLoc;

    private String appName;

    private String appRootDirName;

    private String authentication;

    private String configFile;

    /**
     * Default constructor.
     */
    PropertyFileSystemResourceConfigImpl() {
        super();
    }

    /**
     * Creates an instance of PropertyFileSystemResourceConfigImpl using an
     * Object that serves as an initializer.
     * 
     * @param initCtx
     *            This should a valid instance of ServletConfig.
     * @throws ConfigException
     */
    PropertyFileSystemResourceConfigImpl(Object initCtx) throws ConfigException {
        this();
        this.init(initCtx);
    }

    /**
     * Determines the name of the web application being initilized, outputs
     * application information to the console, and initializes the logger.
     * 
     * @param initCtx
     *            The name of the .properties file which is fully package
     *            qualified. When null or invalid, it will default to
     *            {@link ConfigConstants#SYSTEM_CONFIG_PARMS}
     * @throws ConfigException
     */
    public void init(Object initCtx) throws ConfigException {
        super.init(initCtx);
        logger = Logger.getLogger(PropertyFileSystemResourceConfigImpl.class);

        // Context ctx = null;
        if (initCtx != null && initCtx instanceof String) {
            this.configFile = (String) initCtx;
        }
        else {
            this.configFile = ConfigConstants.SYSTEM_CONFIG_PARMS;
        }
    }

    /**
     * Assigns properties from the web container environment resoures, web.xml
     * and SystemConfigParms.properties, to JVM's System instance.
     * 
     * @throws ConfigException
     */
    public void doSetup() throws ConfigException {
        logger.log(Level.INFO, "Configuring application, " + this.getAppName()
                + "...");
        this.loadSystemProperties(this.configFile);

        logger.log(Level.INFO, "System level configuration completed.");
    }

    /**
     * Assigns properties from application's AppParms.properties to
     * AppPropertyPool collection.
     * 
     * @param ctx
     *            An arbitrary object that is ignored.
     * @throws ConfigException
     */
    public void doPostSetup(Object ctx) throws ConfigException {
        // Add name/value pairs from the target application's
        // AppParms.properties to the AppPropertyPool collection .
        this.appProps = this.loadApplicationProperties();

        // Add application name to AppPropertyPool properties
        if (this.getAppName() != null) {
            this.appProps.put(ConfigConstants.PROPNAME_APP_NAME,
                    this.getAppName());
        }
        // Add Authenticator class name to application properties
        if (this.getAuthentication() != null) {
            this.appProps.put(ConfigConstants.PROPNAME_AUTHENTICATOR,
                    this.getAuthentication());
        }
        // Add APplication resource configuration path location to
        // AppPropertyPool properties
        if (this.getAppConfigLoc() != null) {
            this.appProps.put(ConfigConstants.PROPNAME_CONFIG_APP,
                    this.getAppConfigLoc());
        }
        // Add AppParms.properties location to AppPropertyPool properties
        if (this.getAppConfigLoc() != null) {
            this.appProps.put(ConfigConstants.PROPNAME_APPPARMS_LOCATION,
                    this.getAppConfigLoc());
        }
        logger.log(Level.INFO,
                "Application level configuration completed for application, "
                        + this.appProps.getProperty("appName"));
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
        if (this.appProps == null) {
            logger.log(Level.WARN,
                    "Application property could not be found due to properties are not loaded");
            return null;
        }
        String result = this.appProps.getProperty(key);
        if (result == null) {
            result = System.getProperty(key);
        }
        return result;
    }

    /**
     * 
     * @param prop
     * @return
     */
    public String getAppParmProperty(String prop) {
        try {
            // Add name/value pairs from the target application's
            // AppParms.properties to the AppPropertyPool collection .
            Properties props = RMT2File
                    .loadPropertiesObject(ConfigConstants.CONFIG_APP);
            return props.getProperty(prop);
        } catch (Exception e) {
            String errmsg = "Unable to obtain value for property, "
                    + prop
                    + ", due to the load application properteis operaton failed";
            throw new SystemException(errmsg, e);
        }
    }

    /**
     * Releases any resources that may have been consumed by this instance.
     * 
     * @throws ConfigException
     */
    public void destroy() throws ConfigException {
        this.appProps.clear();
        this.appProps = null;
    }

    /**
     * Loads System level name/value pairs from .properties file found on the
     * classpath of the JVM and assigns each name/value pair as a property of
     * the JVM's System class.
     * <p>
     * The system level configuration should be a .properties file living in the
     * root of the classpath of the application or the web server as
     * SystemConfigParms.properties. This method relies on the classpath to
     * locate the .properties file as ResourceBundle. In the event the
     * configuration is not found on the classpath, a fail safe has been put
     * into place to locate the configuration as a file system resource, hence
     * employing the FileInputStream class. There is a distinct possiblity that
     * the resource has yet to be loaded into the JVM by the time this method is
     * invoked.
     * 
     * @param propFile
     *            The fully qualified package and resource name of the
     *            .properties file to load as a ResourceBundle. It is not
     *            recommended to include the file extension, .properties, as
     *            part of the file name since the interpreter will not find the
     *            resource in the classpath that way.
     * @throws ConfigException
     *             When <i>propFile</i> specified incorrectly or a general I/O
     *             failures or when a key/value pair is determined to be
     *             incorrect.
     */
    protected void loadSystemProperties(String propFile) {
        ResourceBundle rb = null;
        try {
            rb = ResourceBundle.getBundle(propFile);
        } catch (MissingResourceException e) {
            // this.msg = "Unable to load system properteis from, " + propFile;
            throw new ConfigException("Unable to load system properteis from, "
                    + propFile, e);
        }

        Properties globalProps = RMT2File.convertResourceBundleToProperties(rb);
        // Add local properties to the System property collection.
        for (Object key : globalProps.keySet()) {
            String propName = (String) key;
            this.addSysProperty(propName, globalProps.getProperty(propName));
            logger.log(Level.DEBUG,
                    "key/Value pair added as System Properties: " + propName
                            + "=" + globalProps.getProperty(propName));
        }
    }

    /**
     * Loads application level name/value pairs from .properties file found on
     * the classpath of the JVM and assigns each name/value pair as a property
     * of the application's Property collection.
     * <p>
     * The application level configuration should be a .properties file living
     * in the config package as AppParms.properties. This method relies on the
     * classpath to locate the .properties file as ResourceBundle. In the event
     * the configuration is not found on the classpath, a fail safe has been put
     * into place to locate the configuration as a file system resource, hence
     * employing the FileInputStream class. There is a distinct possiblity that
     * the resource has yet to be loaded into the JVM by the time this method is
     * invoked.
     * 
     * @throws ConfigException
     *             When <i>propFile</i> specified incorrectly or a general I/O
     *             failures or when a key/value pair is determined to be
     *             incorrect.
     */
    protected Properties loadApplicationProperties() throws ConfigException {
        String appConfigSource = this.getAppConfigLoc();
        if (appConfigSource == null) {
            appConfigSource = ConfigConstants.CONFIG_APP;
        }
        try {
            // Add name/value pairs from the target application's
            // AppParms.properties to the AppPropertyPool collection .
            Properties props = RMT2File.loadPropertiesObject(appConfigSource);
            return props;
        } catch (Exception e) {
            this.msg = "Unable to load application properteis from .properties file, "
                    + ConfigConstants.CONFIG_APP
                    + ".  Check the environment varibale in web.xml for accuracy: APP_CONFIG_LOCATION";
            throw new ConfigException(this.msg, e);
        }
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
            throw new ConfigException("System property value argument is null");
        }
        return;
    }

    /**
     * Get all properties.
     * 
     * @return the appProps
     */
    public Properties getAppProps() {
        return appProps;
    }

    /**
     * @return the appDisplayName
     */
    public String getAppDisplayName() {
        return appDisplayName;
    }

    /**
     * @param appDisplayName
     *            the appDisplayName to set
     */
    public void setAppDisplayName(String appDisplayName) {
        this.appDisplayName = appDisplayName;
    }

    /**
     * @return the appConfigLoc
     */
    public String getAppConfigLoc() {
        return appConfigLoc;
    }

    /**
     * @param appConfigLoc
     *            the appConfigLoc to set
     */
    public void setAppConfigLoc(String appConfigLoc) {
        this.appConfigLoc = appConfigLoc;
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
     * @return the appRootDirName
     */
    public String getAppRootDirName() {
        return appRootDirName;
    }

    /**
     * @param appRootDirName
     *            the appRootDirName to set
     */
    public void setAppRootDirName(String appRootDirName) {
        this.appRootDirName = appRootDirName;
    }

    /**
     * @return the authentication
     */
    public String getAuthentication() {
        return authentication;
    }

    /**
     * @param authentication
     *            the authentication to set
     */
    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    /**
     * @param appProps
     *            the appProps to set
     */
    public void setAppProps(Properties appProps) {
        this.appProps = appProps;
    }

}
