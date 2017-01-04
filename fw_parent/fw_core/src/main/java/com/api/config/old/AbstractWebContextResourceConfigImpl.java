package com.api.config.old;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingException;

import com.RMT2Base;
import com.api.config.ConfigConstants;
import com.api.config.ConfigException;

/**
 * This class provides base functionality to obtain environment related
 * properties from the J2EE Global component environment for a given application
 * using JNDI.
 * <p>
 * Currently, it manages data pertaining to application environment,
 * configuration location, application name, and the server which the
 * application is governed. All information is accessed using JNDI and requires
 * the user to setup the proper resources for successful operation.
 * <p>
 * To properly identify the current environment in which the applicaltion is
 * running, the user must setup a resource identifying environment as "ENV".
 * Valid values for "ENV" are:
 * <ul>
 * <li><b>DEV</b> = development</li>
 * <li><b>TEST</b> = test</li>
 * <li><b>STAGE</b> = staging</li>
 * <li><b>PROD</b> = production</li>
 * </ul>
 * This property should exist in the web container's main web.xm configuration
 * file.
 * <p>
 * The location of the application configuration data can be identified as
 * "CONFIG_LOCATION" and its value must be a file name path in the format of
 * format: &lt;drive letter&gt;:/&lt;sub dir&gt;/&lt;sub dir&gt;/... This
 * property should exist in the web container's main web.xml configuration file.
 * <p>
 * The application name is identified as "APP_NAME" and its value should
 * equivalent to the subdirectory where the target application is stored. This
 * property should exist in the application's web.xml configuration file. For
 * example, if the app is located in c:\progam files\apache\webapps\music then
 * the app name will be "music".
 * <p>
 * The server which the application exist is identified as "SERVER" and its
 * value should be in the format of &lt;protocol&gt;://&lt;server
 * name&gt;:&lt;port number&gt;. This property should exist in the web
 * container's main web.xm configuration file.
 * <p>
 * An example of adding environment variables to the web.xml file of the web
 * container: <blockquote>
 * 
 * <pre>
 * &lt;env-entry&gt; 	
 *    &lt;env-entry-name&gt;ENVIRONMENT&lt;/env-entry-name&gt;  	
 *    &lt;env-entry-value&gt;DEV&lt;/env-entry-value&gt;	
 *    &lt;env-entry-type&gt;java.lang.String&lt;/env-entry-type&gt;  
 * &lt;/env-entry&gt;
 * &lt;env-entry&gt;  	
 *    &lt;env-entry-name&gt;CONFIG_LOCATION&lt;/env-entry-name&gt;  	
 *    &lt;env-entry-value&gt;C:/Program Files/Apache Software Foundation/Tomcat 6.0/lib&lt;/env-entry-value&gt;  	
 *    &lt;env-entry-type&gt;java.lang.String&lt;/env-entry-type&gt;  
 * &lt;/env-entry&gt;  
 *  &lt;env-entry&gt;  	
 *    &lt;env-entry-name&gt;APP_CONFIG_LOCATION&lt;/env-entry-name&gt;  	
 *    &lt;env-entry-value&gt;WEB-INF/classes/config&lt;/env-entry-value&gt;  	
 *    &lt;env-entry-type&gt;java.lang.String&lt;/env-entry-type&gt;  
 * &lt;/env-entry&gt;  
 * &lt;env-entry&gt;  	
 *    &lt;env-entry-name&gt;SERVER&lt;/env-entry-name&gt;  	
 *    &lt;env-entry-value&gt;http://rmtdaldev03:8080&lt;/env-entry-value&gt;  	
 *    &lt;env-entry-type&gt;java.lang.String&lt;/env-entry-type&gt;
 * &lt;/env-entry&gt;
 * </pre>
 * 
 * </blockquote> Similar entries can be made for the environment variables in
 * the web application's web.xml configuration file.
 * <p>
 * <b><u>Note</u></b><br>
 * When notating file paths for the value of an environment variable, it is best
 * to use UNC file system naming conventions in order to be compatible to
 * different types of OS's.
 * <p>
 * 
 * @author Roy Terrell
 * 
 */
public abstract class AbstractWebContextResourceConfigImpl extends RMT2Base
        implements ResourceConfigurator, ConfigAttributes {

    private static final Logger logger = Logger
            .getLogger(AbstractWebContextResourceConfigImpl.class);

    private static Properties PROPS;

    protected String env;

    protected String server;

    protected String dbmsVendor;

    protected String webapps_drive;

    protected String webapps_dir;

    protected String defaultRoot;

    protected Hashtable envTypes;

    /** Application display name */
    protected String appDisplayName;

    protected String configLoc;

    protected String appConfigLoc;

    protected String appName;

    protected String appRootDirName;

    protected String authentication;

    /** Application properties */
    protected Properties appProps;

    protected static Map<String, String> DBMS_TYPES;

    /**
     * Default constructor which functions to initialize application properties
     * hash and creates a master map of environment types.
     */
    public AbstractWebContextResourceConfigImpl() {
        super();
        AbstractWebContextResourceConfigImpl.PROPS = new Properties();

        AbstractWebContextResourceConfigImpl.DBMS_TYPES = new HashMap<String, String>();
        AbstractWebContextResourceConfigImpl.DBMS_TYPES.put("1",
                ConfigConstants.DBMSTYPE_ASA);
        AbstractWebContextResourceConfigImpl.DBMS_TYPES.put("2",
                ConfigConstants.DBMSTYPE_ASE);
        AbstractWebContextResourceConfigImpl.DBMS_TYPES.put("3",
                ConfigConstants.DBMSTYPE_ORACLE);
        AbstractWebContextResourceConfigImpl.DBMS_TYPES.put("4",
                ConfigConstants.DBMSTYPE_SQLSERVER);
        AbstractWebContextResourceConfigImpl.DBMS_TYPES.put("5",
                ConfigConstants.DBMSTYPE_DB2);

        this.envTypes = new Hashtable();

        // Set master environment type hash.
        this.envTypes.put(ConfigConstants.ENVTYPE_DEV, 1);
        this.envTypes.put(ConfigConstants.ENVTYPE_TEST, 2);
        this.envTypes.put(ConfigConstants.ENVTYPE_STAGE, 3);
        this.envTypes.put(ConfigConstants.ENVTYPE_PROD, 4);
    }

    /**
     * Gathers the user-defined environment variables set in the web container.
     * Determines the environment the application is running, the configuration
     * root location, Application name, server and database vendor code.
     * Environment types are: "DEV", TEST", "STAGE", or "PROD". Also, the master
     * list of environment types are setup as a Hashtable as well as log4j
     * logger.
     * <p>
     * The path that is to represent the location of the application's
     * configuration should be entered into the data source using the following
     * format: &lt;drive letter&gt;:/&lt;sub dir&gt;/&lt;sub
     * dir&gt;/...&nbsp;&nbsp;&nbsp; Despite the fact that some resources may be
     * expected to be included in the classpath, <b>absolute paths</b> should be
     * used in order to locate/load the resources from the file system.
     * Depending on when the descendent instance of this class is loaded into
     * the JVM, the target resource may not be available to the classpath in the
     * event the descendent is instantiated during the time of server start up.
     * <p>
     * Valid DBMSVendor code goes as follows:
     * <ul>
     * <li>1 = Sybase Sql Anywhere, Adaptive Server Anywhere</li>
     * <li>2 = Sybase Adaptive Server Enterprise</li>
     * <li>3 = Oracle</li>
     * <li>4 = MS SQL Server</li>
     * <li>5 = DB2</li>
     * </ul>
     * 
     * @param initCtx
     *            arbitrary object representing the application's configuration.
     * @throws ConfigException
     *             If environment, configuration root path, server, and/or
     *             application name are not defined, or the environment variable
     *             defined with an invalid value.
     */
    public void init(Object initCtx) throws ConfigException {
        // Context ctx = null;
        // if (initCtx instanceof Context) {
        // ctx = (Context) initCtx;
        // }
        //
        // // Get system configuration properties that are common to JNDI data
        // sources
        // try {
        // this.env = (String) ctx.lookup("ENVIRONMENT");
        // this.webapps_drive = (String) ctx.lookup("webapps_drive");
        // this.webapps_dir = (String) ctx.lookup("webapps_dir");
        // defaultRoot = (String) ctx.lookup("DEFAULT_APP_ROOT_DIR_NAME");
        // this.server = (String) ctx.lookup("SERVER");
        // this.dbmsVendor = (String) ctx.lookup("DBMSVendor");
        // }
        // catch (NamingException e) {
        // throw new ConfigException(e);
        // }
        // // Validate environment value.
        // if (this.env == null) {
        // this.msg =
        // "Web Server environment variable is not defined in configuration";
        // logger.error(this.msg);
        // throw new ConfigException(this.msg);
        // }
        // if (!this.envTypes.containsKey(this.env)) {
        // this.msg = "Server Name variable is not defined in configuration";
        // logger.error(this.msg);
        // throw new ConfigException(this.msg);
        // }
        //
        // // Validate server name.
        // if (this.server == null) {
        // this.msg = "Server Name variable is not defined in configuration";
        // logger.error(this.msg);
        // throw new ConfigException(this.msg);
        // }
        // // Validate web directory
        // if (this.webapps_dir == null) {
        // this.msg =
        // "Web container directory variable is not defined in configuration";
        // logger.error(this.msg);
        // throw new ConfigException(this.msg);
        // }
        // // Validate web container drive letter.
        // if (this.webapps_drive == null) {
        // this.msg =
        // "Web container driver letter variable is not defined in configuration";
        // logger.error(this.msg);
        // throw new ConfigException(this.msg);
        // }
    }

    /**
     * Assigns properties from the web container environment resoures, web.xml
     * and SystemConfigParms.properties, to JVM's System instance.
     * 
     * @throws ConfigException
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
     */
    @Override
    public void doPostSetup(Object ctx) throws ConfigException {
        logger.log(
                Level.INFO,
                "Configuring system properties for application, "
                        + this.getAppName() + "...");
        // Environment variable should be valid
        this.addSysProperty(ConfigConstants.PROPNAME_ENVIRONMENT, this.getEnv());
        // Configuration path should be valid
        this.addSysProperty(ConfigConstants.PROPNAME_CFG_LOC,
                this.getConfigLoc());
        // Server should be valid
        this.addSysProperty(ConfigConstants.PROPNAME_SERVER, this.getServer());

        // Web container drive letter
        this.addSysProperty(ConfigConstants.PROPNAME_WEB_DRIVE,
                this.getWebapps_drive());
        // Web container directory root
        this.addSysProperty(ConfigConstants.PROPNAME_WEB_DIR,
                this.getWebapps_dir());

        this.addSysProperty(ConfigConstants.PROPNAME_DEFAULT_APP_CTX_NAME,
                this.defaultRoot);
        this.addSysProperty(ConfigConstants.PROPNAME_DBMS_VENDOR,
                this.dbmsVendor);
        this.addSysProperty(ConfigConstants.PROPNAME_APP_NAME, this.appName);
        this.addSysProperty(ConfigConstants.PROPNAME_AUTHENTICATOR,
                this.authentication);
        this.addSysProperty(ConfigConstants.PROPNAME_CONFIG_APP,
                this.appConfigLoc);
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
     */

    private void addSysProperty(String key, String value)
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
     * Searches for a property based on the specified key.
     * 
     * @param key
     *            the property key.
     * @return The value of the specified key.
     */
    public String getAppProperty(String key) {
        if (AbstractWebContextResourceConfigImpl.PROPS == null) {
            return null;
        }
        return AbstractWebContextResourceConfigImpl.PROPS.getProperty(key);
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
        return this.envTypes;
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
        return AbstractWebContextResourceConfigImpl.DBMS_TYPES.get(key);
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
        return appProps;
    }

}
