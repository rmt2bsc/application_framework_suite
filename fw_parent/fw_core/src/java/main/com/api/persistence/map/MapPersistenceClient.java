package com.api.persistence.map;

import java.io.Serializable;
import java.util.Map;

import com.api.persistence.CannotPersistException;
import com.api.persistence.CannotRemoveException;
import com.api.persistence.CannotRetrieveException;

/**
 * This interface defines whata needs to be implemented to persist any
 * Map(Serializable) object. Primary usage would be users who need to store the
 * state of a given NV for fault tolerance.
 * 
 * @author Roy Terrell
 */

public interface MapPersistenceClient {

    /**
     * Initializes the instance with the specified properties file,
     * <i>propertiesFilename</i>.
     * 
     * @param config
     *            The persistence API's configuration. This is implementation
     *            specific.
     * @throws Exception
     */
    void init(Object config) throws Exception;

    /**
     * Save new NV pair as serializable objects or if already exist; store new
     * state
     * 
     * @param key
     * @param val
     * @exception CannotPersistException
     *                ;
     */
    void save(Serializable key, Serializable val) throws CannotPersistException;

    /**
     * Remove existing NV from being persisted
     * 
     * @param key
     *            value
     * @return Serializable; gives back the value
     * @exception CannotRemoveException
     *                ;
     */
    Serializable remove(Serializable key) throws CannotRemoveException;

    /**
     * Use to store a complete map into persistent state
     * 
     * @param map
     * @exception CannotPersistException
     *                ;
     */
    void saveAll(Map map) throws CannotPersistException;

    /**
     * Gives back the Map in last known state
     * 
     * @return Map;
     * @exception CannotRetrieveException
     *                ;
     */
    Map retrieveAll() throws CannotRetrieveException;

    /**
     * Clears the complete NV state from the DB
     * 
     * @exception CannotRemoveException
     *                ;
     */
    void clear() throws CannotRemoveException;

    /**
     * Used to handle shutdown call the MapPersistenceClient implementation.
     * Persistent engines can leave this implementation empty.
     */
    void shutDown();

}
