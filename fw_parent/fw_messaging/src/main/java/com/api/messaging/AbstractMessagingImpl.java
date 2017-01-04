package com.api.messaging;

import java.io.Serializable;

import com.RMT2Base;
import com.SystemException;
import com.api.config.old.ProviderConfig;
import com.api.config.old.ProviderConnectionException;
import com.api.messaging.MessageException;

/**
 * @author RTerrell
 * 
 */
public abstract class AbstractMessagingImpl extends RMT2Base {
    protected ProviderConfig config;

    /**
     * Creates an empty AbstractMessagingImpl instance.
     */
    public AbstractMessagingImpl() {

    }

    /**
     * Releases all resources allocated to used the underlying messaging
     * provider.
     * 
     * @throws SystemException
     */
    public void close() throws SystemException {
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.messaging.MessageManager#getConfig()
     */
    public ProviderConfig getConfig() throws MessageException {
        return this.config;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.api.messaging.MessageManager#getConnection()
     */
    public abstract Object connect(ProviderConfig config)
            throws ProviderConnectionException;

    /*
     * (non-Javadoc)
     * 
     * @see com.api.messaging.MessageManager#getHost()
     */
    public String getHost() {
        return this.config.getHost();
    }

    /**
     * Returns the authentication flag.
     * 
     * @return boolean returns true when user authentication is required and
     *         false otherwise.
     */
    public boolean isAuthRequired() {
        return this.config.isAuthenticate();
    }

    /**
     * Must be implemented to receive a message from the message producer.
     * 
     * @return a Serializable object
     */
    public abstract Serializable getMessage() throws MessageException;

    /**
     * Must be implemented to send a message to the message consumer.
     * 
     * @return an arbitrary object.
     */
    public abstract Object sendMessage(Serializable data)
            throws MessageException;
}
