package com.api.config.old;

import org.apache.log4j.Logger;

import com.api.config.ConfigException;

/**
 * Creates various objects that are used for examining and updating system
 * configuration data or to serve as a data source for the system configuration
 * repository.
 * 
 * @author rterrell
 * @deprecated No longer needed
 */
public class CoreSysConfigFactory {

    private static Logger logger = Logger.getLogger(CoreSysConfigFactory.class);

    /**
     * Creates a ResourceConfigurator instance using the JNDI implementation
     * provided by this API.
     * <p>
     * The chosen implementation relies on the naming context that points to the
     * J2EE environemnt related bindings as well as a LDAP server. These
     * bindings are generally vendor specific and are defined in J2EE descriptor
     * files such as "web.xml" which belongs to a J2EE type application server
     * or Tomcat's "context.xml" configuration file.
     * 
     * @return
     * @deprecated No longer needed
     * 
     */
    public static final ResourceConfigurator getDefaultSystemConfigurator() {
        ResourceConfigurator r = getJndiSystemConfigurator();
        logger.info("The default system configurator is: "
                + r.getClass().getName());
        return r;
    }

    /**
     * 
     * @return
     * @deprecated No longer needed
     */
    public static final ResourceConfigurator getJndiSystemConfigurator() {
        ResourceConfigurator cfg;
        try {
            cfg = new JndiSystemResourceConfigImpl();
            return cfg;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 
     * @param propertiesFile
     * @return
     * @deprecated No longer needed
     */
    public static final ResourceConfigurator getPropertiesSystemConfigurator(
            String propertiesFile) {
        ResourceConfigurator cfg;
        try {
            cfg = new PropertyFileSystemResourceConfigImpl(propertiesFile);
            return cfg;
        } catch (ConfigException e) {
            return null;
        }
    }
}
