package com.api.jsp.action;

import com.api.persistence.DatabaseException;
import com.api.web.ActionCommandException;

/**
 * This interface provides common functionality that an Action Handler is
 * required to perform. Generally most action handlers should possess the common
 * ability to respond to add, edit, save, and delete client requests. Also, the
 * ability to receive data from and send data to the client is common is
 * essential for most action handlers.
 * 
 * @author roy.terrell
 * 
 */
public interface ICommonAction {

    /**
     * This method must be implemented in order to provide customized
     * functionality for adding items.
     * 
     * @throws ActionHandlerException
     */
    void add() throws ActionCommandException;

    /**
     * This method must be implemented in order to provide customized
     * functionality for editing items.
     * 
     * @throws ActionHandlerException
     */
    void edit() throws ActionCommandException;

    /**
     * This method must be implemented in order to provide customized
     * functionality for updating items.
     * 
     * @throws ActionHandlerException
     * @throws DatabaseException
     *             when a database access error occurs.
     */
    void save() throws ActionCommandException, DatabaseException;

    /**
     * This method must be implemented in order to provide customized
     * functionality for deleting items.
     * 
     * @throws ActionHandlerException
     * @throws DatabaseException
     *             when a database access error occurs.
     */
    void delete() throws ActionCommandException, DatabaseException;
}
