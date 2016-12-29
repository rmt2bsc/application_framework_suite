package com.api.messaging.webservice;

import java.util.Map;

import com.RMT2Base;
import com.api.messaging.MessageRoutingInfo;

/**
 * An class providing common functionality to access maintain the actual service
 * registry.
 * 
 * @author Roy Terrell
 * 
 */
public abstract class AbstractServiceRegistryImpl extends RMT2Base implements
        ServiceRegistry {

    private static Map<String, MessageRoutingInfo> SERVICES;

    /**
     * Creates an AbstractServiceRegistryImpl with a null registry.
     */
    public AbstractServiceRegistryImpl() {
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.api.messaging.webservice.ServiceRegistry#getEntry(java.lang.String)
     */
    public MessageRoutingInfo getEntry(String messageId) {
        if (AbstractServiceRegistryImpl.SERVICES == null) {
            this.loadServices();
        }
        MessageRoutingInfo srvc = (MessageRoutingInfo) AbstractServiceRegistryImpl.SERVICES
                .get(messageId);
        return srvc;
    }

    /**
     * @return the sERVICES
     */
    public static Map<String, MessageRoutingInfo> getServices() {
        return SERVICES;
    }

    /**
     * @param sERVICES
     *            the sERVICES to set
     */
    protected static void setServices(Map<String, MessageRoutingInfo> map) {
        SERVICES = map;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.messaging.webservice.ServiceRegistry#getServiceCount()
     */
    @Override
    public int getServiceCount() {
        if (AbstractServiceRegistryImpl.SERVICES != null) {
            return AbstractServiceRegistryImpl.SERVICES.size();
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.messaging.webservice.ServiceRegistry#resetServices()
     */
    @Override
    public void resetServices() {
        if (AbstractServiceRegistryImpl.SERVICES == null) {
            return;
        }
        AbstractServiceRegistryImpl.SERVICES.clear();
        AbstractServiceRegistryImpl.SERVICES = null;
    }

}
