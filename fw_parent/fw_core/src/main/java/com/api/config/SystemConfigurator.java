package com.api.config;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.RMT2Base;
import com.SystemException;
import com.api.ConnectionProvider;
import com.api.config.jaxb.AppServerConfig;
import com.api.config.jaxb.AppServerConfig.ApiConfigurators.ApiConfigurator;
import com.api.config.jaxb.AppServerConfig.JaxbContexts.JaxbContext;
import com.api.config.jaxb.AppServerConfig.SystemProperties;
import com.api.config.jaxb.AppServerConfig.SystemProperties.EmailConfig;
import com.api.persistence.db.DatabaseConnectionFactory;
import com.api.pool.DatabaseConnectionPool;
import com.api.xml.jaxb.JaxbUtil;
import com.api.xml.jaxb.JaxbUtilException;
import com.util.RMT2BeanUtility;
import com.util.RMT2File;
import com.util.RMT2String2;

/**
 * Initializes the application by loading system level and application level
 * properties, establishing the JDBC connection pool, and setting the the
 * application's logger.
 * <p>
 * User-defined JVM and application properties are loaded into memory from
 * localized sources
 * 
 * @author roy terrell
 * 
 */
public class SystemConfigurator extends RMT2Base {

    private static final long serialVersionUID = 7358434218143689146L;

    private static Logger logger;

    private String appName;

    private String env;

    private static AppServerConfig config;

    private static Map<String, AppServerConfig.DestinationMappings.Destination> destinationXref;

    private static Map<String, JaxbUtil> jaxb;

    private static Map<String, String> jaxbPackageName;

    public SystemConfigurator() {
        super();
        jaxb = new Hashtable<String, JaxbUtil>();
        jaxbPackageName = new Hashtable<String, String>();
    }

    public SystemConfigurator(String appName, String env) {
        this();
        this.appName = appName;
        this.env = env;
    }

    /**
     * Initialize logging, system/application level configurations, and JDBC
     * connection pool.
     */
    public void start(String appServConfigFile) throws ConfigException {
        String msg = null;

        // Get application server configuration file
        String xmlDoc = null;
        try {
            xmlDoc = RMT2File.getTextFileContents(appServConfigFile);
        } catch (Exception e) {
            e.printStackTrace();
            this.msg = "Severe Error: Unable to load the contents of application configuration file, "
                    + appServConfigFile;
            System.out.println(this.msg);
            throw new ConfigException(this.msg);
        }

        JaxbUtil jaxb = new JaxbUtil(ConfigConstants.JAXB_CONFIG_PKG);
        AppServerConfig appConfig = (AppServerConfig) jaxb.unMarshalMessage(xmlDoc);

        SystemConfigurator.config = appConfig;

        // Setup Logging environment
        String logPath = appConfig.getLoggerConfigPath();
        System.out.println("Application configuration Logger path ==========> " + logPath);
        PropertyConfigurator.configure(logPath);
        logger = Logger.getLogger(SystemConfigurator.class);
        logger.info("\n\nBegin Application Configuration for " + appName + "...");

        // Finish loading remaining JAXB contexts
        logger.info("Finish loading remaining JAXB contexts...");
        if (appConfig.getJaxbContexts() != null) {
            List<JaxbContext> contexts = appConfig.getJaxbContexts().getJaxbContext();
            for (JaxbContext item : contexts) {
                try {
                    JaxbUtil util = new JaxbUtil(item.getValue());
                    SystemConfigurator.jaxb.put(item.getName(), util);
                    SystemConfigurator.jaxbPackageName.put(item.getName(), item.getValue());
                } catch (JaxbUtilException e) {
                    logger.fatal("Unable to setup JAXB context, " + item, e);
                }
            }
        }
        logger.info("Loading of remaining JAXB contexts complete.");

        // Load system and application properties for this web app being
        // initialized.
        logger.info("Loading core system/application properties...");
        this.loadProperties(appConfig);
        logger.info("Loading of core system/application properties complete.");

        // Initialize database connections
        logger.info("Begin database initialization...");
        this.setupDatabaseApi(appConfig);
        logger.info("Database initialization complete.");

        // Initialize each API.
        logger.info("Begin initialization of all API collection...");
        this.setupApiCollection(appConfig);
        logger.info("Initialization of API collection complete.");

        // Build destinaion mappings has
        logger.info("Begin building destination mappings hash...");
        this.setupDestinationMappings(appConfig);
        logger.info("Building of destinaion mappings hash complete.");

        // Configuration process complete...
        logger.info("Application initialization complete");
    }

    /**
     * Loads system and application properties.
     * 
     * @param config
     * @throws ConfigException
     */
    protected void loadProperties(AppServerConfig config) throws ConfigException {
        this.loadSystemProperties(config);
        this.loadLocalProperties(config);
        return;
    }

    private void loadSystemProperties(AppServerConfig config) {
        AppPropertyPool props = AppPropertyPool.getInstance();
        SystemProperties sysProps = config.getSystemProperties();
        if (sysProps != null) {
            if (RMT2String2.isEmpty(this.env)) {
                // Depend on configuration file for this value
                props.addSystemProperty("environment", sysProps.getEnvironment());
                props.addSystemProperty("env", sysProps.getEnvironment());
            }
            else {
                // The caller provided the value of env
                props.addSystemProperty("environment", this.env);
                props.addSystemProperty("env", this.env);
            }
            props.addSystemProperty("soaphost", sysProps.getSoaphost());
            props.addSystemProperty("SaxDriver", sysProps.getSaxDriver());

            EmailConfig emailProps = sysProps.getEmailConfig();
            if (emailProps != null) {
                props.addSystemProperty("Authentication", emailProps.getAuthentication());
                props.addSystemProperty("Defaultcontent", emailProps.getDefaultcontent());
                props.addSystemProperty("HostPop3", emailProps.getHostPop3());
                props.addSystemProperty("HostSmtp", emailProps.getHostSmtp());
                props.addSystemProperty("InternalSmtpDomain", emailProps.getInternalSmtpDomain());
                props.addSystemProperty("UserId", emailProps.getUserId());
                props.addSystemProperty("Password", emailProps.getPassword());
                props.addSystemProperty("Resourcetype", emailProps.getResourcetype());
                props.addSystemProperty("TemplatePath", emailProps.getTemplatePath());
            }
            props.addSystemProperty(sysProps.getConsumerMsgHandlerMappingsKey(),
                    sysProps.getConsumerMsgHandlerMappingsLocation());
        }
        return;
    }

    private void loadLocalProperties(AppServerConfig config) {
        AppPropertyPool props = AppPropertyPool.getInstance();
        props.addProperty("LoggerConfigPath", config.getLoggerConfigPath());
        props.addProperty("ContainerManagedPool", config.getContainerManagedPool());
        props.addSystemProperty("PageLinkMax", String.valueOf(config.getPageLinkMax()));
        props.addSystemProperty("PaginationPageSize", String.valueOf(config.getPaginationPageSize()));
        props.addSystemProperty("PollingPage", config.getPollingPage());
        props.addSystemProperty("ProtocolInformation", config.getProtocolInformation());
        props.addSystemProperty("RemoteSrvcApp", config.getRemoteSrvcApp());
        props.addSystemProperty("RptFileExt", config.getRptFileExt());
        props.addSystemProperty("RptXsltPath", config.getRptXsltPath());
        props.addSystemProperty("SerialDrive", config.getSerialDrive());
        props.addSystemProperty("SerialExt", config.getSerialExt());
        props.addSystemProperty("SerialPath", config.getSerialPath());
        props.addSystemProperty("TimeoutInterval", String.valueOf(config.getTimeoutInterval()));
        props.addSystemProperty("WebAppMapping", config.getWebAppMapping());
        props.addSystemProperty("WebAppsDir", config.getWebAppsDir());
        props.addSystemProperty("WebAppsDrive", config.getWebAppsDrive());
    }

    /**
     * Load Database connections, if not container managed.
     * 
     * @param config
     */
    private void setupDatabaseApi(AppServerConfig config) {
        // Determine if database pool is container managed.
        boolean containerManagedPool = Boolean.parseBoolean(config.getContainerManagedPool());
        if (containerManagedPool) {
            logger.info("Database pool is container managed.");
            logger.info("Bypass database pool initialization.");
            return;
        }

        logger.info("Initializing non-container managed database pool... ");
        DatabaseConnectionFactory f = new DatabaseConnectionFactory();
        ConnectionProvider dbProvider = f.getEnvConnectionProviderApi(null);
        // Setup database pool and assign database connection provider api to
        // the connection pool manager
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();
        pool.addConnectionApi(dbProvider);
        logger.info("Initializing non-container managed database pool complete. ");
    }

    /**
     * Initializes all API's.
     * 
     * @param config
     */
    private void setupApiCollection(AppServerConfig config) {
        List<ApiConfigurator> list = config.getApiConfigurators().getApiConfigurator();
        for (ApiConfigurator apiConfig : list) {
            RMT2BeanUtility util = new RMT2BeanUtility();
            com.api.config.ApiConfigurator configurator = (com.api.config.ApiConfigurator) util.createBean(apiConfig
                    .getClassName());
            configurator.start();
        }
    }

    /**
     * Build destination mapping hash
     * 
     * @param config
     */
    private void setupDestinationMappings(AppServerConfig config) {
        List<AppServerConfig.DestinationMappings.Destination> list = config.getDestinationMappings().getDestination();
        destinationXref = new HashMap<String, AppServerConfig.DestinationMappings.Destination>();
        for (AppServerConfig.DestinationMappings.Destination item : list) {
            destinationXref.put(item.getRmt2Name(), item);
            logger.info("Destination mapping created for " + item.getRmt2Name() + "==>" + item.getJndiName());
        }
    }

    /**
     * @return the config
     */
    public static AppServerConfig getConfig() {
        return config;
    }

    /**
     * Returns an instance of {@link JaxbUtil} based on the JAXB context name,
     * <i>contextName</i>.
     * 
     * @return {@link JaxbUtil} or null of <i>contextName</i> does not exitst.
     */
    public static JaxbUtil getJaxb(String contextName) {
        return jaxb.get(contextName);
    }

    /**
     * Returns JAXB context package name associated with an JAXB instance based
     * on the JAXB context name, <i>contextName</i>.
     * 
     * @return {@link JaxbUtil} or null of <i>contextName</i> does not exitst.
     */
    public static String getJaxbPackageName(String contextName) {
        return jaxbPackageName.get(contextName);
    }

    /**
     * Get JNDI destinaion name that is mapped to <i>rmt2DestName</i>.
     * 
     * @param rmt2DestName
     *            the internal destinaion name.
     * @return the JNDI name or null if it does not exist in the Map
     * @throws SystemException
     *             when the destination mappings cross reference map is not
     *             initialized.
     */
    public static String getJndiDestinationName(String rmt2DestName) {
        if (destinationXref == null) {
            throw new SystemException("Destination Mappings cross reference Map is not initialized");
        }
        AppServerConfig.DestinationMappings.Destination item = destinationXref.get(rmt2DestName);
        if (item == null) {
            return null;
        }
        return item.getJndiName();
    }
}