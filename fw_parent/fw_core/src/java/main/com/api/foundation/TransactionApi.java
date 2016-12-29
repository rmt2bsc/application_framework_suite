package com.api.foundation;

/**
 * A base interface designed to track the activity of the user currently in
 * control of a transaction API instance.
 * 
 * @author rterrell
 * 
 */
public interface TransactionApi {

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
}
