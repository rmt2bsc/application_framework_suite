package com.api.messaging.webservice;

/**
 * Contract interface for creating a ServiceRegistry.
 * 
 * @author Roy Terrell
 * 
 */
public interface ServiceRegistryFactory {

    /**
     * Create a ServiceRegistry instance from a HTTP implementation.
     * 
     * @return {@link ServiceRegistry}
     */
    ServiceRegistry getHttpServiceRegistryManager();

    /**
     * Create a ServiceRegistry instance from an implementation that uses a XML
     * file as its source of data.
     * 
     * @param filePath
     * @return {@link ServiceRegistry}
     */
    ServiceRegistry getFileServiceRegistryManager(String filePath);

    /**
     * Create a ServiceRegistry instance from a LDAP implementation.
     * 
     * @return {@link ServiceRegistry}
     */
    ServiceRegistry getLdapServiceRegistryManager(String baseDn,
            String mapperClass);

    /**
     * Create a ServiceRegistry instance from a RDBMS database implementation.
     * 
     * @return Always null.
     */
    ServiceRegistry getDatabaseServiceRegistryManager();
}
