package com.api.messaging;

import java.io.Serializable;

import com.SystemException;
import com.api.config.old.ProviderConfig;
import com.api.config.old.ProviderConnectionException;

/**
 * Interface that provides a common contract of functionality for various
 * messaging systems.
 * 
 * @author appdev
 * 
 */
public interface MessageManager {
    /**
     * XML tag that is used as the opening root element for all XML based
     * messages.
     */
    static final String MSG_OPEN_TAG = "<RSPayload>";

    /**
     * XML tag that is used as the closing root element for all XML based
     * messages.
     */
    static final String MSG_CLOSE_TAG = "</RSPayload>";

    /**
     * XML tag that is used as the opening element representing the application
     * root location for all XML based messages.
     */
    static final String MSG_OPEN_APPROOT_TAG = "<appRoot>";

    /**
     * XML tag that is used as the closing element representing the application
     * root location for all XML based messages.
     */
    static final String MSG_CLOSE_APPROOT_TAG = "</appRoot>";

    /**
     * Returns the configuration object used to manage a particular messaging
     * provider. The implenmetor should create an internal member variable to
     * hold a reference to the ProviderConfig instance created, possibly as a
     * singleton, in the event this becomes an expensive operation. This will
     * facilitate reuseability for other member methods.
     * 
     * @return {@link com.api.messaging.ProviderConfig ProviderConfig} the
     *         intial configuration regariding a particular messaging provider
     * @throws MessageException
     */
    ProviderConfig getConfig() throws MessageException;

    /**
     * Establishes a connection to the messaging provider by relying on the
     * existence of the ProviderConfig instance stored internally.
     * 
     * @param config
     *            a {@link ProviderConfig} instance containing the necessary
     *            information needed to configure, connect, and use a messaging
     *            provider.
     * @return Object an arbitrary object representing the connection.
     * @throws ProviderConnectionException
     * @see {@link com.api.messaging.MessagingResourceFactory
     *      MessagingResourceFactory}
     */
    Object connect(ProviderConfig config) throws ProviderConnectionException;

    /**
     * Releases all resources allocated to used the underlying messaging
     * provider.
     * 
     * @throws SystemException
     */
    void close() throws SystemException;

    /**
     * Retrieves a messeage from the message provider.
     * 
     * @return Serializable an arbitrary object representing the message.
     * @throws MessageException
     */
    Serializable getMessage() throws MessageException;

    /**
     * Sends a message via the underlying message provider.
     * 
     * @param data
     *            the message to be sent
     * @return Serializable an arbitrary object representing the message.
     * @throws MessageException
     */
    Object sendMessage(Serializable data) throws MessageException;

    /**
     * Returns the name of the messaging host.
     * 
     * @return String.
     */
    String getHost();

    /**
     * Determines whether or not authentication is required.
     * 
     * @return boolean returns true when user authenticatio is required and
     *         false otherwise.
     */
    boolean isAuthRequired();
}
