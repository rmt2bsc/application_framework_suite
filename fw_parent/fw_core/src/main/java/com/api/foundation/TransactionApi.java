package com.api.foundation;

import java.io.Serializable;
import java.util.Properties;

import com.api.persistence.DaoClient;

/**
 * A base interface designed to track the activity of the user currently in
 * control of a transaction API instance.
 * 
 * @author rterrell
 * 
 */
public interface TransactionApi {
    
    /**
     * Return a Properties instance which will serve as the application's configuration.
     * 
     * @return {@link Properties}
     */
    Properties getConfig();

    /**
     * Get the DAO instance that is assoicated with this API
     * 
     * @return {@link DaoClient}
     */
    DaoClient getSharedDao();
    
    /**
     * Get the login id of the user in controll of the API.
     * 
     * @return String
     */
    String getApiUser();

    /**
     * Set the login id of the user in controll of the API.
     * 
     * @param apiUser
     *            the user's login id
     */
    void setApiUser(String apiUser);

    /**
     * Starts a unit of work for one or more transactions.
     */
    void beginTrans();

    /**
     * Commits one or more transactions which make up a unit of work.
     */
    void commitTrans();

    /**
     * Rollback one or more transactions which make up a unit of work.
     */
    void rollbackTrans();

    /**
     * Close the connection assoicated with this DAO client.
     */
    void close();
  
    /**
     * Send a message to its destination.
     * 
     * @param messageId
     *            the unique identifier of the message
     * @param payload
     *            the message content
     * @return the results, if applicable.
     * @throws TransactionApiException
     */
    Object sendMessage(String messageId, Serializable payload) throws TransactionApiException;
}
