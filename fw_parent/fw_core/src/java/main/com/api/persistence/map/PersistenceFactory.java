package com.api.persistence.map;

import org.apache.log4j.Logger;

import com.SystemException;
import com.api.persistence.CannotCreateClientException;
import com.api.persistence.CannotReadManagerConfigurationException;
import com.util.RMT2BeanUtility;
import com.util.RMT2File;
import com.util.RMT2Utility;

import java.io.FileInputStream;
import java.io.IOException;

import java.lang.reflect.Constructor;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * This class is the factory to get access to any DB based or file based
 * implementation. None of the implementations should expose directly to user
 * for migration purposes
 * 
 * @author Roy Terrell
 */
public class PersistenceFactory {

    private final static Logger logger = Logger
            .getLogger(PersistenceFactory.class);

    private static volatile PersistenceFactory _factory = null;

    /*
     * Please set this to the name of the .properties file used as the
     * persistence manager configuration
     */
    private final static String PROP_PATH = "persist";

    /** The class that implements a file-based MapPersistenceClient */
    private final static String MGR_CLASSNAME = "PersistenceManagerClass";

    public final static String REPOSITORY = "repository";

    private MapPersistenceClient manager;

    /**
     * Default private constructor// does nothing
     */
    private PersistenceFactory() {
        return;
    }

    /**
     * Singular public method to get access to any of the Persistence Manager
     * implementations. It is important to known at this point that properties
     * determine the implementation of the Persistence Manager, there is no
     * direct interface which gives access to either DB implemented ot FILE
     * implemented storage API.
     * 
     * @return PersistenceFactory;
     */
    public static PersistenceFactory getSingularInstance() {
        logger.error(" getting factory instance as a Singleton");
        if (_factory == null)
            _factory = new PersistenceFactory();
        return _factory;
    }

    public static PersistenceFactory getInstance() {
        logger.error(" getting factory instance ");
        return new PersistenceFactory();
    }

    /**
     * Register a custom persistence manager as opposed to the
     * {@link FilePersistenceManager} or {@link DBPersistenceManager}.
     */
    public synchronized void registerManager(MapPersistenceClient manager) {
        this.manager = manager;
    }

    /**
     * Reads the default properties and creates a persistencemanager The default
     * properties are picked up from the $USER_HOME or from the classpath.
     * Default properties are represented by "persist.properties".
     * 
     * @return MapPersistenceClient
     * @exception CannotCreateClientException
     *                ;
     */
    public synchronized MapPersistenceClient createManager()
            throws CannotCreateClientException {
        // uses default properties which should be located in the root
        // persistence package.
        Package pkg = this.getClass().getPackage();
        String pkgName = pkg.getName();
        String configLoc = pkgName + "." + PROP_PATH;
        this.manager = this.createManager(configLoc);
        return this.manager;
    }

    /**
     * Reads a user-provided properties and creates the persistence manager.
     * This method allows the user to provide a custom property file from any
     * where in the classpath. Firstly, an attempt to create the manager is done
     * based on the availability of the class name specified in the properties
     * file. In the event the class name is not specified, then an attempt is
     * made to create the manager using one of the default implementations
     * (database or file) based on the value of the property, <i>persist</i>.
     * 
     * @param filePath
     *            complete pathname to get the properties
     * @return MapPersistenceClient;
     * @exception CannotCreateClientException
     *                ;
     */
    public synchronized MapPersistenceClient createManager(String configFilePath)
            throws CannotCreateClientException {
        // will return null if not initialized
        if (this.manager == null) {
            Properties config;
            try {
                config = getConfig(configFilePath);
                String classname = config.getProperty(MGR_CLASSNAME);
                if (classname != null) {
                    RMT2BeanUtility beanUtil = new RMT2BeanUtility();
                    this.manager = (MapPersistenceClient) beanUtil
                            .createBean(classname);
                    this.manager.init(config);
                }
                else {
                    if (this.isDbPersistMethod(config))
                        this.manager = createDefaultManagerDB(configFilePath);
                    else
                        this.manager = createDefaultManagerFile(configFilePath);
                }
            } catch (Exception e) {
                throw new CannotCreateClientException(
                        "Unable to create persistence manager using properties, "
                                + configFilePath, e);
            }
        }
        return this.manager;
    }

    /**
     * Internal creator of DB persistence manager, the outside user accesses
     * only the MapPersistenceClient interface API
     */
    private MapPersistenceClient createDefaultManagerDB(String configFilePath)
            throws Exception {

        if (logger.isInfoEnabled())
            logger.info("Calling db persist from factory: " + configFilePath);
        if (this.manager == null)
            this.manager = new DBPersistenceImpl(configFilePath);
        return this.manager;
    }// end of DB persistence

    /**
     * creates instance of file based persistency manager
     * 
     * @return MapPersistenceClient
     */
    private MapPersistenceClient createDefaultManagerFile(String configFilePath)
            throws Exception {

        if (logger.isInfoEnabled()) {
            logger.info("Creating file manager: " + configFilePath);
        }
        if (this.manager == null)
            try {
                this.manager = new FilePersistenceImpl(configFilePath);
            } catch (Exception e) {
                throw new CannotCreateClientException(e);
            }
        return this.manager;

        // Properties props;
        //
        // try {
        // if (_manager == null) {
        // props = readProps(filePath);
        // String classname = props.getProperty(MGR_CLASSNAME);
        // if (classname != null) {
        // Class cl = RMT2Utility.loadClass(classname, this.getClass());
        // Constructor ctor = cl.getConstructor(new Class[] { String.class });
        // _manager = (MapPersistenceClient) ctor.newInstance(new Object[] {
        // filePath });
        // }
        // else {
        // _manager = new FilePersistenceImpl(filePath);
        // }
        // }
        // return _manager;
        // }
        // catch (Throwable t) {
        // t.printStackTrace();
        // return null;
        // }
    }// end of file persistence

    /**
     * checks the default properties for DB/File flag
     * 
     * @return
     * @throws Exception
     */
    private boolean isDbPersistMethod() throws Exception {
        Properties props = getConfig(PROP_PATH);
        return this.isDbPersistMethod(props);
    }

    /**
     * checks the properties file provided by the client for DB/File flag
     * 
     * @param filePath
     * @return
     * @throws Exception
     */
    private boolean isDbPersistMethod(String filePath) throws Exception {
        Properties props = getConfig(filePath);
        return this.isDbPersistMethod(props);
    }

    /**
     * Checks if the persistence flag is set for DB or File.
     * 
     * @param props
     * @return
     * @throws Exception
     */
    private boolean isDbPersistMethod(Properties props) throws Exception {
        String persist = props.getProperty(REPOSITORY);
        return "DB".equals(persist);
    }

    private Properties getConfig(String fileName)
            throws CannotReadManagerConfigurationException {
        try {
            return RMT2File.loadPropertiesObject(fileName);
        } catch (Exception e) {
            logger.warn(e);
        }

        try {
            ResourceBundle rb = RMT2File.loadAppConfigProperties(fileName);
            return RMT2File.convertResourceBundleToProperties(rb);
        } catch (Exception e) {
            throw new CannotReadManagerConfigurationException(
                    "Problem accessing or reading persistence configuration", e);
        }

    }

}
