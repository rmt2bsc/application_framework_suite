package com.api.messaging.webservice;

/**
 * A factory class for creating ServiceRegistry objects from various
 * implementation types.
 * 
 * @author rterrell
 * 
 */
public class ServiceRegistryFactoryImpl implements ServiceRegistryFactory {

    /**
     * Default constructor for creating a ServiceRegistryFactoryImpl
     */
    public ServiceRegistryFactoryImpl() {
        return;
    }

    /**
     * Create a ServiceRegistry instance from a SystemConfigurator
     * implementation.
     * 
     * @return {@link ServiceRegistry}
     */
    @Override
    public ServiceRegistry getSystemConfiguratorServiceRegistryManager() {
        ServiceRegistry obj = new SystemConfiguratorServiceRegistryLoaderImpl();
        return obj;
    }

    /**
     * Create a ServiceRegistry instance from a HTTP implementation.
     * 
     * @return {@link ServiceRegistry}
     */
    public ServiceRegistry getHttpServiceRegistryManager() {
        ServiceRegistry obj = new HttpServiceRegistryLoaderImpl();
        return obj;
    }

    /**
     * Create a ServiceRegistry instance from an implementation that uses a XML
     * file as its source of data.
     * 
     * @param filePath
     * @return {@link ServiceRegistry}
     */
    public ServiceRegistry getFileServiceRegistryManager(String filePath) {
        ServiceRegistry obj = new FileServiceRegistryLoaderImpl(filePath);
        return obj;
    }

    /**
     * Create a ServiceRegistry instance from a LDAP implementation.
     * 
     * @return {@link ServiceRegistry}
     */
    public ServiceRegistry getLdapServiceRegistryManager(String baseDn,
            String mapperClass) {
        ServiceRegistry obj = new LdapServiceRegistryLoaderImpl(baseDn,
                mapperClass);
        return obj;
    }

    /**
     * Create a ServiceRegistry instance from a RDBMS database implementation.
     * 
     * @return Always null.
     */
    public ServiceRegistry getDatabaseServiceRegistryManager() {
        ServiceRegistry obj = null;
        return obj;
    }

}
