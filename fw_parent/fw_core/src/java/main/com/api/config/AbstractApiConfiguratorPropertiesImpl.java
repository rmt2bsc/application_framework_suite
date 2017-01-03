package com.api.config;

import java.util.Enumeration;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.RMT2Base;
import com.util.RMT2File;

/**
 * @author appdev
 *
 */
public abstract class AbstractApiConfiguratorPropertiesImpl extends RMT2Base
        implements ApiConfigurator {

    protected String configPath;

    protected String appName;

    protected ResourceBundle config;

    /**
     * Creates an AbstractApiConfiguratorPropertiesImpl.
     * <p>
     * Loads the configuration, AddressBook-AppParms.properties.
     * 
     * @throws com.SystemException
     *             error loading configuration.
     */
    public AbstractApiConfiguratorPropertiesImpl() {

    }

    /**
     * Initializes the api's environment using the loaded configuration.
     */
    @Override
    public void start() throws ConfigException {
        this.config = RMT2File.loadAppConfigProperties(configPath);

        this.appName = this.config.getString(ConfigConstants.API_APP_TITLE_KEY);

        // See if we need to to setup logger locally
        Logger rootLogger = Logger.getRootLogger();
        Enumeration appenders = rootLogger.getAllAppenders();
        if (!appenders.hasMoreElements()) {
            this.setupLogger();
        }
        else {
            rootLogger
                    .info(this.appName
                            + ": is configured to output logging statements to the container's log file");
        }
    }

    /**
     * Setup logging environment for API
     */
    protected abstract void setupLogger();
}
