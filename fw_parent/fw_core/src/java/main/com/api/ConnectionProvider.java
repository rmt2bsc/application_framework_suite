package com.api;

import com.api.persistence.DatabaseException;

/**
 * An interface for managing connections pertaining to some external data
 * provider.
 * <p>
 * The implementation of ConnectionProvider should provide the ability to
 * initialize itself when instantiated. This approach should alleviate the
 * client from having to worry about specific tasks needed ensure the
 * ConnectionProvider is ready for use.
 * 
 * @author appdev
 * 
 */
public interface ConnectionProvider {

    /**
     * Get the next available connection from the collecitve of connections.
     * 
     * @return The connection.
     * @throws DatabaseException
     * @throws CannotConnectException
     */
    Object getConnection() throws CannotConnectException, DatabaseException;

    /**
     * Set the application context for this connection provider.
     * 
     * @param ctxName
     *            the name of the context
     */
    void setContextName(String ctxName);

    /**
     * Get the application context for this connection provider.
     * 
     * @return the context name
     */
    String getContextName();
} // End of Class
