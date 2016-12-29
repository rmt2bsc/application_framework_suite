package com.api.config.old;

import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.SystemException;
import com.api.config.ConfigConstants;
import com.api.config.ConfigException;
import com.api.ldap.LdapClient;
import com.api.ldap.LdapFactory;
import com.api.ldap.beans.LdapAppServer;
import com.api.ldap.operation.LdapSearchOperation;
import com.util.RMT2BeanUtility;

/**
 * This class objtains system environment variables and application level
 * properties from the J2EE Global component environment as well as a LDAP
 * server using JNDI.
 * <p>
 * This implementation is generally used for intializing web applications.
 * Currently, it manages system related data such as application environment,
 * configuration location, application name, and the server which the
 * application is governed. All information is accessed using JNDI and requires
 * the user to setup the proper resources for successful operation.
 * <p>
 * To properly identify the current environment in which the applicaltion is
 * running, the user must setup a resource identifying environment as
 * "ENVIRONMENT". Valid values for "ENV" are:
 * <ul>
 * <li><b>DEV</b> = development</li>
 * <li><b>TEST</b> = test</li>
 * <li><b>STAGE</b> = staging</li>
 * <li><b>PROD</b> = production</li>
 * </ul>
 * This property should exist in the web container's main web.xml configuration
 * file.
 * <p>
 * All system environment variables can be obtained from a named LDAP server
 * under one of the following distinguished names:
 * <i>ou=dev,ou=RMT2-Internet,ou=
 * App,ou=Servers,ou=Computers,ou=Resources,o=RMT2BSC,dc=rmt2,dc=net</i>,
 * <i>ou=prod
 * ,ou=RMT2-Internet,ou=App,ou=Servers,ou=Computers,ou=Resources,o=RMT2BSC
 * ,dc=rmt2,dc=net</i>, or
 * <i>ou=test,ou=RMT2-Internet,ou=App,ou=Servers,ou=Computers
 * ,ou=Resources,o=RMT2BSC,dc=rmt2,dc=net</i>. The system environment variable,
 * appConfigPath, points to a java .properties file that contains the
 * application level properties. These properties can be loaded at the
 * application level.
 * <p>
 * <b><u>Note</u></b><br>
 * When notating file paths for the value of an environment variable, it is best
 * to use UNC file system naming conventions in order to be compatible to
 * different types of OS's.
 * 
 * @author Roy Terrell
 * 
 */
class JndiSystemResourceConfigImpl extends AbstractSystemConfigImpl {

    private static final Logger logger = Logger
            .getLogger(JndiSystemResourceConfigImpl.class);

    private Context ctx;

    private String ctxName;

    protected LdapAppServer appServerConfig;

    /**
     * Creates a JndiSystemResourceConfigImpl object for obtaining resource
     * configuration information via JNDI.
     * <p>
     * This implementation relies on the naming context that points to the J2EE
     * environemnt related bindings as well as a LDAP server. These bindings are
     * generally vendor specific and are defined in J2EE descriptor files such
     * as "web.xml" which belongs to a J2EE type application server or Tomcat's
     * "context.xml" configuration file.
     * 
     * @throws SystemException
     *             Unable to obtain the J2DD Blobal JNDI bindings context or
     *             initialize with the Properties file or web deployment
     *             descriptor file.
     */
    JndiSystemResourceConfigImpl() {
        super();
        Context env = null;
        try {
            Context ctx = new InitialContext();
            env = (Context) ctx.lookup("java:comp/env");
            this.init(env);
        } catch (NamingException e) {
            this.msg = "Unable to obtain the J2EE Global JNDI component's environment bindings context";
            logger.error(this.msg);
            throw new SystemException(this.msg, e);
        } catch (ConfigException e) {
            this.msg = "Error initializing JndiSystemResourceConfigImpl object with Properties file/deployment descriptor";
            logger.error(this.msg);
            throw new SystemException(this.msg, e);
        }
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
        if (initCtx == null) {
            this.msg = "Unable to obtain system configuration data from J2EE JNDI due to JNDI Context is invalid or null";
            logger.error(this.msg);
            throw new ConfigException(this.msg);
        }

        if (initCtx instanceof Context) {
            this.ctx = (Context) initCtx;
        }
        try {
            ctxName = ctx.getNameInNamespace();
        } catch (NamingException e) {
            ctxName = "Unknown";
        }
        logger.info("Successfully obtained configuration data from the J2EE Global JNDI environment Logger initialized successfully for application, "
                + this.appName);
    }

    /**
     * Retreives has value from application context using JNDI
     * 
     * @param ctx
     *            a JNDI connection to the application context
     * @param key
     *            the key to search
     * @return the value of the key or null if it does not exist.
     */
    private String lookupStringValue(Context ctx, String key) {
        // Get system configuration properties that are common to JNDI data
        // sources
        String val = null;
        try {
            val = (String) ctx.lookup(key);
            return val;
        } catch (NamingException e) {
            logger.warn("Configuration was not found for " + key + " in "
                    + ctxName);
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.config.AbstractSystemConfigImpl#doSetup()
     */
    @Override
    public void doSetup() throws ConfigException {
        super.doSetup();

        // Get system configuration properties that are common to JNDI data
        // sources
        this.env = this.lookupStringValue(ctx,
                ConfigConstants.PROPNAME_ENVIRONMENT);
        this.addSysProperty(ConfigConstants.PROPNAME_ENVIRONMENT, this.getEnv());
        this.addSysProperty(ConfigConstants.PROPNAME_ENV, this.getEnv());
        appServerConfig = this.getAppServerProperties();

        // Get System level environment variables
        this.setupSystemProperties(appServerConfig);
        logger.log(Level.INFO, "System level configuration proerties fetched.");

        // Get application level properties
        this.setupApplicationProperties(appServerConfig);
        logger.log(Level.INFO,
                "Application level configuration properties fetched.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.config.AbstractSystemConfigImpl#doPostSetup(java.lang.Object)
     */
    @Override
    public void doPostSetup(Object ctx) throws ConfigException {
        super.doPostSetup(ctx);

        // load the remaining system variables contained in the LdapAppServer
        // instance.
        this.addSysProperty(ConfigConstants.PROPNAME_SAX_DRIVER,
                appServerConfig.getSaxDriver());

        this.addSysProperty(ConfigConstants.PROPNAME_SERVICE_REGISTRY,
                appServerConfig.getServiceRegistry());
        this.addSysProperty(ConfigConstants.PROPNAME_SOAP_HOST,
                appServerConfig.getSoaphost());
        this.addSysProperty(ConfigConstants.PROPNAME_SOAP_NAMESPACE_AWARE,
                appServerConfig.getSoapNameSpaceAware());

        this.addSysProperty(ConfigConstants.PROPNAME_SESSION_TIMEOUT,
                appServerConfig.getTimeoutInterval());
        this.addSysProperty(ConfigConstants.PROPNAME_SESSION_TIMEOUT,
                appServerConfig.getTimeoutInterval());

        // TODO: Add the remaining system variables.
    }

    /**
     * Loads application level name/value pairs from the LDAP server residing on
     * the Security API.
     * <p>
     * Calls the web service responsible for fetching configuration data for a
     * particular web server and packaging the data into a Properties
     * collection. The service should target one of the environment nodes under
     * the LDAP entry,
     * <i>ou=RMT2-Internet,ou=Web,ou=Servers,ou=Computers,ou=Resources
     * ,o=RMT2BSC,dc=rmt2,dc=net</i>. The results of the service call should be
     * a List of LdapComputerWeb objects.
     * 
     * @throws ConfigException
     *             General LDAP access error.
     */
    private LdapAppServer getAppServerProperties() throws ConfigException {
        String baseDn = "ou=RMT2-Internet,ou=App,ou=Servers,ou=Computers,ou=Resources";
        if (this.env.equalsIgnoreCase(ConfigConstants.ENVTYPE_PROD)) {
            baseDn = "ou=prod," + baseDn;
        }
        if (this.env.equalsIgnoreCase(ConfigConstants.ENVTYPE_DEV)) {
            baseDn = "ou=dev," + baseDn;
        }

        List<LdapAppServer> appList = null;
        LdapClient ldap = null;
        try {
            LdapFactory f = new LdapFactory();
            ldap = f.createAttributeClient(null);
            String mappingClass = "com.api.ldap.beans.LdapAppServer";
            LdapSearchOperation op = new LdapSearchOperation();
            op.setDn(baseDn);
            op.setScope(SearchControls.OBJECT_SCOPE);
            op.getSearchFilterArgs().put("objectClass", "*");
            // op.getSearchFilterArgs().put("cn", criteria.getName());
            op.setUseSearchFilterExpression(true);
            op.setMappingBeanName(mappingClass);
            Object results[] = ldap.retrieve(op);
            appList = ldap.extractLdapResults(results);
            if (appList == null) {
                throw new ConfigException(
                        "Unable to find profile for application server using base DN: "
                                + baseDn);
            }
        } catch (Exception e) {
            throw new ConfigException(e);
        } finally {
            ldap.close();
            ldap = null;
        }

        for (LdapAppServer item : appList) {
            return item;
        }
        return null;
    }

    private void setupSystemProperties(LdapAppServer server)
            throws ConfigException {
        this.validateSystemProperties(server);
        // add properties to the System properties class
        RMT2BeanUtility beanUtil = new RMT2BeanUtility(server);
        Properties props = beanUtil.toProperties();
        Enumeration<?> propEnum = props.propertyNames();
        while (propEnum.hasMoreElements()) {
            String propName = propEnum.nextElement().toString();
            String propValue = props.getProperty(propName);
            System.setProperty(propName, propValue);
            logger.info("Added System property [" + propName + ":" + propValue
                    + "]");
        }
    }

    private void validateSystemProperties(LdapAppServer server)
            throws ConfigException {
        if (this.env == null) {
            this.msg = "Web Server environment variable is required";
            logger.error(this.msg);
            throw new ConfigException(this.msg);
        }
        if (!ENV_TYPES.containsKey(this.env)) {
            this.msg = "Server Name variable is required";
            logger.error(this.msg);
            throw new ConfigException(this.msg);
        }
        // Validate application name.
        if (this.appName == null) {
            throw new ConfigException("Application Name variable is required");
        }
        // Validate server name.
        if (server.getServer() == null) {
            this.msg = "Server Name variable is required";
            logger.error(this.msg);
            throw new ConfigException(this.msg);
        }
        // Validate web directory
        if (server.getWebAppsDir() == null) {
            this.msg = "Web container directory variable is required";
            logger.error(this.msg);
            throw new ConfigException(this.msg);
        }
        // Validate web container drive letter.
        if (server.getWebAppsDrive() == null) {
            this.msg = "Web container driver letter variable is required";
            logger.error(this.msg);
            throw new ConfigException(this.msg);
        }

        // Validate web server configuration location.
        if (server.getSystemConfig() == null) {
            throw new ConfigException(
                    "Web server configuration path variable is required");
        }
        // Validate application configuration location.
        if (server.getAppConfigPath() == null) {
            throw new ConfigException(
                    "Application configuration path variable is required");
        }
    }

    private void setupApplicationProperties(LdapAppServer server) {
        return;
    }

    private int setupServiceRegisty() {
        int count = 0;

        return count;
    }

    /**
     * Clears the application properties collection.
     * 
     * @throws ConfigException
     */
    @Override
    public void destroy() throws ConfigException {
        this.getAppProps().clear();
    }

}
