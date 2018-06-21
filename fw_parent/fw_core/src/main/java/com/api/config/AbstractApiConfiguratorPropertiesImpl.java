package com.api.config;

import java.util.ResourceBundle;

import com.RMT2Base;
import com.api.util.RMT2File;

/**
 * Common processes for configuring an individual API
 * 
 * @author Roy Terrell
 *
 */
public abstract class AbstractApiConfiguratorPropertiesImpl extends RMT2Base implements ApiConfigurator {

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
        this.setupLogger();
        
        this.doPostStart();
    }

    /**
     * Handle any post startup tasks.
     */
    protected void doPostStart() {
        return;
    }
    
    /**
     * Setup logging environment for API
     */
    protected abstract void setupLogger();
}
