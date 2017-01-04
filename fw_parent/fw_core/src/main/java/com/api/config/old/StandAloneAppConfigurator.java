package com.api.config.old;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.RMT2Base;
import com.RMT2RuntimeException;
import com.api.ConnectionProvider;
import com.api.config.AppPropertyPool;
import com.api.config.ConfigConstants;
import com.api.config.ConfigException;
import com.api.persistence.db.DatabaseConnectionFactory;
import com.api.pool.DatabaseConnectionPool;
import com.util.RMT2File;

/**
 * A stand alone configurator which functions to initialize the application by
 * loading system environment variables and application properties, establishing
 * the JDBC connection pool, and setting the the application's logger.
 * <p>
 * This class normally depends on the availability of
 * <i>SystemConfigParms.properties</i>, <i>AppParms.properties</i>, and
 * <i>DB-ORM-Config.properites</i> files which is used as the main source for
 * configuration data. User-defined JVM and application properties are loaded
 * into memory from localized sources. Configure this class to load
 * automatically when the web container or server is started.
 * 
 * @author roy terrell
 * 
 */
public class StandAloneAppConfigurator extends RMT2Base {

    private static Logger logger;

    private String appName;

    /**
     * Initialize logging, system/application level configurations, and JDBC
     * connection pool.
     */
    public StandAloneAppConfigurator(String appName) {
        super();
        this.appName = appName;

        // Configuration process complete...
        msg = "Initialization of application, "
                + AppPropertyPool
                        .getProperty(ConfigConstants.PROPNAME_APP_NAME)
                + ", in the " + AppPropertyPool.getEnvirionment()
                + " completed.";
        logger.info(msg);
    }

    public void init() {
        // Setup Logging environment
        String logPath = "config.log4j";
        Properties props = RMT2File.loadPropertiesFromClasspath(logPath);
        PropertyConfigurator.configure(props);
        logger = Logger.getLogger(StandAloneAppConfigurator.class);
        logger.info("\n\nBegin Application Configuration for " + appName
                + "...");

        // Load system and application properties for this web app being
        // initialized.
        logger.info("Loading core system/application configuration for application, "
                + appName);
        try {
            this.setupProperties(appName);
        } catch (ConfigException e) {
            throw new RMT2RuntimeException(e);
        }

        // Load Database connections, if not container managed.
        logger.info("Setup non-container managed database pool configuration for application, "
                + appName);
        this.setupDatabaseApi(appName);
    }

    private void setupProperties(String appName) throws ConfigException {
        // TODO: Create a more dynamic call to obtain system configurator. For
        // example, invoke SystemConfiguratorFactory.getSystemConfigurator()
        // which will obtain and instantiate the class specified in the
        // container's web.xml.

        // Looking for default SystemConfigParms.properties
        ResourceConfigurator cfg = CoreSysConfigFactory
                .getPropertiesSystemConfigurator(null);
        ((AbstractSystemConfigImpl) cfg).setAppName(appName);
        if (!AppPropertyPool.isAppConfigured()) {
            cfg.doSetup();
            cfg.doPostSetup(null);
            ConfigAttributes appConfig = (ConfigAttributes) cfg;
            AppPropertyPool pool = AppPropertyPool.getInstance();
            pool.addProperties(appConfig);
        }
    }

    private void setupDatabaseApi(String appName) {
        // Create database connection provider
        DatabaseConnectionFactory f = new DatabaseConnectionFactory();
        ConnectionProvider dbProvider = f.getEnvConnectionProviderApi(null);
        // Setup database pool and assign database connection provider api to
        // the connection pool manager
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();
        pool.addConnectionApi(dbProvider);

        logger.info("Database configuration for application, " + appName
                + ", completed.");
    }

    /**
     * Stub method for creating custom setup procedure.
     */
    protected void doCustomSetup() {
        return;
    }
}