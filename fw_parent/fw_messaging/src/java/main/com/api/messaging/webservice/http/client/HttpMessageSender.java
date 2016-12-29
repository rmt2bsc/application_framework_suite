package com.api.messaging.webservice.http.client;

import com.api.DaoApi;

import com.api.messaging.MessageManager;

/**
 * Interface provides the contract needed for a client to consume web services
 * using HTTP URL Protocol.
 * 
 * @author appdev
 * 
 */
public interface HttpMessageSender extends MessageManager {
    /**
     * Attribute that identifies the data results of a service call.
     */
    static final String SERVICE_RESULTS = "serviceresults";

    /**
     * THe purpose of this method is to process and manage the results that were
     * returned from the service invocation, if applicable.
     * 
     * @param data
     *            Arbitrary data returned from the service call.
     * @throws ServiceHandlerException
     */
    void packageResults(Object data) throws HttpClientMessageException;

    /**
     * Returns the XML results of the service call as a DaoApi instance.
     * 
     * @return {@link com.api.DaoApi DaoApi}
     */
    DaoApi getXmlResults();

    /**
     * Indicates whether an error occurred.
     * 
     * @return Returns true for service call failure and false for success.
     */
    boolean isError();

    /**
     * Releases any allocated resources.
     * 
     */
    void close();
}
