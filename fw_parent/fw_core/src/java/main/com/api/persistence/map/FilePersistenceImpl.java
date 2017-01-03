package com.api.persistence.map;

import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;

import com.RMT2Base;
import com.api.persistence.CannotPersistException;
import com.api.persistence.CannotRemoveException;
import com.api.persistence.CannotRetrieveException;

/**
 * The class implements the MapPersistenceClient interface and provides users a
 * file based implementation when required. The state of this class is current
 * NOOP. Implementation will be in place once a better structure for file based
 * properties will be designed.
 * 
 * @author Roy Terrell
 */

class FilePersistenceImpl extends RMT2Base implements MapPersistenceClient {
    private final static Logger logger = Logger
            .getLogger(FilePersistenceImpl.class);

    private File file;

    private Properties properties;

    /**
     * Default Constructor
     */
    public FilePersistenceImpl() {
        return;
    }

    /**
     * Create FilePersistenceImpl object with the properties file used to
     * initialize its state.
     */
    public FilePersistenceImpl(String propertiesFilename) throws Exception {
        this.init(propertiesFilename);
    }

    /**
     * 
     */
    public void init(Object config) throws Exception {
        if (config != null
                && (config instanceof Properties || config instanceof String)) {
            // Configuration is valid...
        }
        else {
            this.msg = "Unable to initialize the MapPersistenceClient API using FilePersistenceImpl due to its input configuration is invalid or null.   The configuration must be a valid Properties instance or a String representing the file path or classpath to the API's configuration.";
            logger.error(this.msg);
            throw new Exception(this.msg);
        }

        if (config instanceof Properties) {
            // this isntance must have been setup using the default
            // configuration
            this.properties = (Properties) config;
        }
        else {
            // Load properties from the file path or classpath specified by the
            // user.
            this.properties = new Properties();
            this.properties.load(new FileInputStream(config.toString()));
        }

        // Create the data repository
        String path = this.properties
                .getProperty(PersistenceFactory.REPOSITORY);
        this.file = new File(path);
        this.file.createNewFile();
    }

    /**
     * Save new name value pair as serializable objects or if already exist;
     * store new state
     */
    public void save(Serializable key, Serializable val)
            throws CannotPersistException {
        try {
            Map map = retrieveAll();
            map.put(key, val);
            saveAll(map);
        } catch (CannotRetrieveException e) {
            throw new CannotPersistException(
                    "Unable to pre-load existing store.", e);
        }
    }

    /**
     * Remove existing name value from being persisted
     */
    public Serializable remove(Serializable key) throws CannotRemoveException {
        Object o;
        try {
            Map map = retrieveAll();
            o = map.remove(key);
            saveAll(map);
        } catch (CannotRetrieveException e) {
            throw new CannotRemoveException(
                    "Unable to pre-load existing store.", e);
        } catch (CannotPersistException e) {
            throw new CannotRemoveException(
                    "Unable to pre-load existing store.", e);
        }
        return (Serializable) o;
    }

    /**
     * Use to store a complete map into persistent state
     * 
     * @exception CannotPersistException
     *                ;
     */
    public void saveAll(Map map) throws CannotPersistException {
        try {
            OutputStream fos = new FileOutputStream(file);
            Properties prop = new Properties();
            // NB: For some reason Properties.putAll(map) doesn't seem to work -
            // dimc@users.sourceforge.net
            for (Iterator iterator = map.entrySet().iterator(); iterator
                    .hasNext();) {
                Map.Entry entry = (Map.Entry) iterator.next();
                prop.setProperty(entry.getKey().toString(), entry.getValue()
                        .toString());
            }
            prop.store(fos, null);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            throw new CannotPersistException("Cannot save to: "
                    + file.getAbsolutePath(), e);
        }
    }

    /**
     * Gives back the Map in last known state
     * 
     * @return Map;
     * @exception CannotRetrieveException
     *                ;
     */
    public Map retrieveAll() throws CannotRetrieveException {
        try {
            Properties prop = new Properties();
            FileInputStream fis = new FileInputStream(file);
            prop.load(fis);
            fis.close();
            return filterLoadedValues(prop);
        } catch (IOException e) {
            throw new CannotRetrieveException("Unable to load from file: "
                    + file.getAbsolutePath(), e);
        }
    }

    /**
     * Turns the values into Floats to enable
     * {@link org.jgroups.demos.DistributedHashtableDemo} to work. Subclasses
     * should override this method to convert the incoming map of string/string
     * key/value pairs into the types they want.
     * 
     * @param in
     * @return Map
     */
    protected Map filterLoadedValues(Map in) {
        // Map out = new HashMap();
        // for (Iterator iterator = in.entrySet().iterator();
        // iterator.hasNext();) {
        // Map.Entry entry = (Map.Entry) iterator.next();
        // out.put(entry.getKey().toString(),
        // Float.valueOf(entry.getValue().toString()));
        // }
        // return out;
        return in;
    }

    /**
     * Clears the complete name value state from the DB
     * 
     * @exception CannotRemoveException
     *                ;
     */
    public void clear() throws CannotRemoveException {
        try {
            saveAll(Collections.EMPTY_MAP);
        } catch (CannotPersistException e) {
            throw new CannotRemoveException("Unable to clear map.", e);
        }
    }

    /**
     * Used to handle shutdown call the MapPersistenceClient implementation.
     * Persistent engines can leave this implementation empty.
     */
    public void shutDown() {
        // TODO: Think about deleting repository file
        return;
    }
}// end of class
