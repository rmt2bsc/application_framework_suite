package com.api.messaging.rmi.client;

import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.NotFoundException;
import com.RequiredDataMissingException;
import com.SystemException;
import com.api.config.ConfigConstants;
import com.api.config.old.ProviderConfig;
import com.api.config.old.ProviderConnectionException;
import com.api.messaging.MessagingResourceFactory;
import com.api.messaging.rmi.RmiConstants;
import com.util.RMT2File;

/**
 * Facotry class for creating RMI client related instances.
 * 
 * @author Roy Terrell
 * 
 */
public class RmiClientFactory extends MessagingResourceFactory {

    /**
     * Create a clean RmiClientFactory.
     */
    public RmiClientFactory() {
        super();
        return;
    }

    /**
     * Creates a ProviderConfig instance from RMI configuration known as
     * <i>config.RMI-Config</i> which is nothing more that a java .properties
     * file found in the classpath.
     * <p>
     * This factory method expects to find <i>RmiConfig.properties</i> in the
     * root of the classpath. The properties file must contain name/value pairs
     * for the following keys: <i>ServerName</i>, and <i>ServerPort</i> where
     * each key name is prefix with an environment indicator. For example,
     * DEV.ServerName.
     * 
     * @return An instance of {@link ProviderConfig}
     * @throws SystemException
     *             General system error when loading properties file
     * @throws NotFoundException
     *             The properties file, <i>RmiConfig.properties</i>, is not
     *             found.
     * @throws RequiredDataMissingException
     *             Either of the required key names do not exist in the
     *             .properties file: <i>ServerName</i>, <i>ServerPort</i>, and
     *             <i>ServerPolicyFile</i>.
     */
    public ProviderConfig createProviderConfigFromPropertiesFile()
            throws NotFoundException, SystemException,
            RequiredDataMissingException {
        ProviderConfig config = MessagingResourceFactory.getConfigInstance();
        ResourceBundle rb = RMT2File
                .loadAppConfigProperties(RmiConstants.CONFIG_SOURCE_PROPERTY_FILE);
        String env = System.getProperty(ConfigConstants.PROPNAME_ENVIRONMENT);
        if (env == null) {
            env = ConfigConstants.ENVTYPE_DEV;
        }
        try {
            String host = rb.getString(env + ".ServerName");
            String port = rb.getString(env + ".ServerPort");
            config.setHost(host);
            config.setPort(port);
            return config;
        } catch (Exception e) {
            this.msg = "Unable to create a instance of ProviderConfig due to a required key is missing in RmiConfig.properties";
            throw new RequiredDataMissingException(this.msg, e);
        }
    }

    /**
     * Creates a ProviderConfig instance from environment variables contained
     * contianed in the context of a web application.
     * <p>
     * This factory method expects to find web.xml to be configured with a
     * certain amount of environment variables. These variables are accessed via
     * JNDI and are identified as <i>ServerName</i>, <i>ServerPort</i>, and
     * <i>ServerPolicyFile</i>.
     * 
     * @return An instance of {@link ProviderConfig}
     * @throws SystemException
     *             General directory naming access errors or the required key
     *             names are not found: <i>ServerName</i>, <i>ServerPort</i>,
     *             and <i>ServerPolicyFile</i>.
     */
    public ProviderConfig createProviderConfigFromWebContext() {
        ProviderConfig config = MessagingResourceFactory.getConfigInstance();
        try {
            Context env = null;
            Context ctx = new InitialContext();
            env = (Context) ctx.lookup("java:comp/env");
            String host = (String) env.lookup("ServerName");
            String port = (String) env.lookup("ServerPort");
            String policyFile = (String) env.lookup("ServerPolicyFile");
            config.setHost(host);
            config.setPort(port);
            config.setPolicyFile(policyFile);
            return config;
        } catch (NamingException e) {
            this.msg = "Unable to create a instance of ProviderConfig from web application context due to a general directory naming error";
            throw new SystemException(this.msg, e);
        }
    }

    /**
     * Creates an instance of RmiClient in which a connection to the target RMI
     * server is obtained.
     * 
     * @return {@link RmiClient}
     * @throws RmiClientException
     */
    public RmiClient getRmiClient() throws RmiClientException {
        ProviderConfig config = this.createProviderConfigFromPropertiesFile();
        // Create RMI client using the newly acquired ProviderConfig instance.
        try {
            RmiClient rmi = new DefaultRmiClientImpl();
            rmi.connect(config);
            return rmi;
        } catch (ProviderConnectionException e) {
            this.msg = "Unable to create RMI client due error connecting to RMI server";
            throw new RmiClientException(this.msg, e);
        }
    }

}
