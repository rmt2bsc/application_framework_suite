package com.api.messaging.webservice;

import java.util.Map;

import com.api.messaging.webservice.router.MessageRoutingInfo;

/**
 * A contract for loading the services registry for a given application.
 * 
 * @author rterrell
 * 
 */
public interface ServiceRegistry {

    /**
     * Load the application service registry with all entries.
     * 
     * @return Map<String,
     *         {@link com.api.messaging.webservice.MessageRoutingInfo
     *         MessageRoutingInfo}>
     * @throws SystemException
     *             General and database access errors.
     */
    Map<String, MessageRoutingInfo> loadServices();

    /**
     * Load the application service registry with entries associated with a
     * particular resource sub type, <i>rsrcSubTypeId</i>.
     * 
     * @param rsrcSubTypeId
     *            the resource sub type
     * @return Map<String,
     *         {@link com.api.messaging.webservice.MessageRoutingInfo
     *         MessageRoutingInfo}>
     * @throws SystemException
     *             General and database access errors.
     */
    Map<String, MessageRoutingInfo> loadServices(int rsrcSubTypeId);

    /**
     * 
     * @param messageId
     * @return
     */
    MessageRoutingInfo getEntry(String messageId);

    /**
     * Returns the total number of services the registry is managing.
     * 
     * @return int
     */
    int getServiceCount();

    /**
     * Removes all services from the list and assigns null to the list.
     */
    void resetServices();
}
