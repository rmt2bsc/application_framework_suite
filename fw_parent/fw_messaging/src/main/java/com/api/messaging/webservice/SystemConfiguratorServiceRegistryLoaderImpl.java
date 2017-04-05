package com.api.messaging.webservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.api.config.ConfigException;
import com.api.config.SystemConfigurator;
import com.api.config.jaxb.AppServerConfig.ServiceRegistry.Service;
import com.api.messaging.MessageRoutingInfo;

/**
 * An implementation of the ServiceRegistry interface where the service entries
 * are loaded from SystemConfigurator.
 * 
 * @author Roy Terrell
 * 
 */
class SystemConfiguratorServiceRegistryLoaderImpl extends AbstractServiceRegistryImpl {

    private static Logger logger = Logger.getLogger(SystemConfiguratorServiceRegistryLoaderImpl.class);

    /**
     * Create a HttpServiceRegistryLoaderImpl containing an uninitialized
     * service registry.
     */
    public SystemConfiguratorServiceRegistryLoaderImpl() {
        super();
    }

    /**
     * Build service registry based on the resource sub type,
     * <i>rsrcSubTypeId</i>.
     * <p>
     * By dfault, all web services with a resource type of "3" are loaded.
     * 
     * @param rsrcSubTypeId
     *            the resource sub type
     * @return Map<String,
     *         {@link com.api.messaging.webservice.MessageRoutingInfo
     *         MessageRoutingInfo}>
     * @throws SystemException
     *             General and database access errors.
     */
    public Map<String, MessageRoutingInfo> loadServices() {
        if (AbstractServiceRegistryImpl.getServices() != null) {
            return AbstractServiceRegistryImpl.getServices();
        }

        // get list of services from the service registry contained in the
        // SystemConfigurator
        if (SystemConfigurator.getConfig() == null) {
            this.msg = "Error building service registry due to Application Configuration is not available";
            logger.error(this.msg);
            throw new ConfigException(this.msg);
        }
        List<Service> services = SystemConfigurator.getConfig().getServiceRegistry().getService();
        Map<String, MessageRoutingInfo> registeredServices = this.createMessageRoutingInfo(services);
        SystemConfiguratorServiceRegistryLoaderImpl.setServices(registeredServices);
        return registeredServices;
    }

    /**
     * Creates a list of available services in the form of key/value pairs and
     * persist the list in memory for repetitive usage. The key represents the
     * service name, and the value represents the service URL. The list is used
     * as a lookup mechanism for service requests. The query method used to
     * extract the data is XPath, which targets each user_resource tag element
     * as a row of data.
     * 
     * @param services
     *            A list of
     *            {@link com.api.config.jaxb.AppServerConfig.ServiceRegistry.Service}
     *            objects where each element represents the routing information
     *            pertaining to a service
     * @return Map of service name/service URL key/value pairs.
     */
    private Map<String, MessageRoutingInfo> createMessageRoutingInfo(List<Service> services) {
        Map<String, MessageRoutingInfo> routingMap = new HashMap<String, MessageRoutingInfo>();
        int loadCount = 0;
        logger.info("Loading Service Registry entries...");
        for (Service item : services) {
            MessageRoutingInfo srvc = new MessageRoutingInfo();
            srvc.setName(item.getName());
            srvc.setRouterType(item.getServiceType());
            srvc.setApplicatoinId(item.getApplication());
            srvc.setModuleId(item.getModule());
            srvc.setMessageId(item.getTransaction());
            srvc.setDestination(item.getDestination());
            srvc.setDescription(item.getDescription());
            srvc.setSecured(Boolean.parseBoolean(item.getSecure()));
            srvc.setHost(item.getHost());
            srvc.setDeliveryMode(item.getDeliveryMode());
            routingMap.put(srvc.getMessageId(), srvc);
            logger.info("[Application=" + srvc.getApplicatoinId() + "] [Module=" + srvc.getModuleId()
                    + "] [Transaction=" + srvc.getMessageId() + "]");
            loadCount++;
        }

        logger.log(Level.INFO, "Total number of service registry entries loaded: " + loadCount);
        return routingMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.messaging.webservice.ServiceRegistry#loadServices(int)
     */
    @Override
    public Map<String, MessageRoutingInfo> loadServices(int rsrcSubTypeId) {
        throw new NotImplementedException();
    }
}
