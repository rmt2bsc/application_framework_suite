package com.api.messaging.rmi.engine;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.RMT2Base;
import com.SystemException;
import com.api.config.old.ProviderConfig;
import com.api.messaging.rmi.RmiConstants;
import com.api.messaging.rmi.service.RmiServiceCommand;
import com.util.RMT2BeanUtility;
import com.util.RMT2File;

/**
 * RMI engine server that functions as a setup procedure to create the RMI
 * runtime.
 * <p>
 * It creates the intial remote objects and exports them to the RMI runtime,
 * which makes them available to receive incoming remote invocations. This
 * engine relies on the Properites file, <i>RMIServices.properties</i>,
 * containing a list of name/value pairs identifying each remote object that is
 * to be created. The <i>name</i> represents the lookup name of the remote
 * object, and its value is the name of the class that is to be instantitaed as
 * the remote object. All remote objects must implement the
 * {@link RmiServiceCommand} interface.
 * <p>
 * The setup procedure described above will do the following:
 * <ol>
 * <li>Setup and install the Security Manager, if needed.</li>
 * <li>
 * Initialize the Map that will contain all remote object implementations as
 * strong references for the purpose of preventing the premature garbage
 * collection of the stubs after being bound to the registry.</li>
 * <li>Create the RMI Registry listening on port 1099</li>
 * <li>Load the list of remote object definitions.</li>
 * <li>Create and export one or more remote objects based on the number loaded
 * in the previous step.</li>
 * <li>Register each remote object with the RMI registry.</li>
 * </ol>
 * <p>
 * There are a few other configuration steps that are required, but not
 * documented readily in most RMI tutorials, in order to establish proper
 * communication between a RMI server and client.
 * <ol>
 * <li>Be sure to create server.policy and client.policy files that will grant
 * all security permissions. For example<br>
 * 
 * <pre>
 *             grant {
 *               permission java.security.AllPermission;
 *             };
 * </pre>
 * 
 * </li>
 * <li>Include the server.policy file and the RMI server codebase as a VM
 * options for running within TOMCAT as such:
 * <ul>
 * <li>-Djava.security.policy=&lt;Path to policy file&gt;/server.policy</li>
 * <li>-Djava.rmi.server.codebase=&lt;Path to source code package or .jar
 * file&gt;</li>
 * </ul>
 * </li>
 * <li>Be sure to start the RMI registry on the server machine at the desired
 * port.</li>
 * <li>In the client app, assign the java.security.policy property to the
 * client.policy file via the setProperty method of the System class. The
 * client.policy file will more than likely live on the client's machine. For
 * example:
 * 
 * <pre>
 * if (System.getSecurityManager() == null) {
 *     System.setProperty(&quot;java.security.policy&quot;,
 *             &quot;&lt;Path to client policy file&gt;/client.policy&quot;);
 *     System.setSecurityManager(new RMISecurityManager());
 * }
 * </pre>
 * 
 * </li>
 * </ol>
 * 
 * @author rterrell
 * 
 */
public class RmiEngine extends RMT2Base implements Runnable {

    private static Logger logger;

    /**
     * The default port for RMI runtime to listen for incoming requests.
     */
    public static final int DEFAULT_RMI_PORT = 1099;

    private static String POLICY_FILE;

    /**
     * A Map collection of strong reference {@link RmiServiceCommand} instances
     * for the purpose of preventing the premature garbage collection of the
     * stubs after being bound to the registry.
     */
    protected Map<String, RmiServiceCommand> stubs;

    private int port;

    /**
     * Create a RmiEngine object that will listen for incoming request at port
     * 1099 but without establishing the RMI runtime.
     */
    public RmiEngine() {
        super();
        this.port = RmiEngine.DEFAULT_RMI_PORT;
        return;
    }

    /**
     * Create a RmiEngine object that will listen for incoming request at the
     * port specified as <i>port</i> but without establishing the RMI runtime.
     * <p>
     * Also, it initializes the security manager (if needed).
     * 
     * @param config
     *            An instance of {@link ProviderConfig} containing the required
     *            RMI engine configuration properties
     */
    public RmiEngine(ProviderConfig config) {
        this();
        String portStr = config.getPort();
        try {
            this.port = Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            this.port = RmiEngine.DEFAULT_RMI_PORT;
        }

        // Install security manager if not available.
        POLICY_FILE = config.getPolicyFile();
        if (System.getSecurityManager() == null) {
            System.setProperty("java.security.policy", POLICY_FILE);
            System.setSecurityManager(new RMISecurityManager());
        }
        return;
    }

    /**
     * Performs any pre RMI runtime setup Initialization.
     * <p>
     * This method initializes the logger and the Map that will contain the
     * refereences of all the remote objects created.
     * 
     */
    @Override
    public void init() {
        super.init();

        // Initialize logger
        RmiEngine.logger = Logger.getLogger(RmiEngine.class);

        // Initialize Map to hold strong references RmiServiceCommand
        // implementations.
        this.stubs = new HashMap<String, RmiServiceCommand>();
    }

    /**
     * Creates the RMI registry and creates and exports one or more remote
     * objects to the RMI runtime.
     * <p>
     * The source of the remote object listing should exist as
     * <i>RMI-Services.properties</i> in the root of the user application's
     * classpath. The key/value pairs represent the name of the remote object
     * and the name of the implementing class of the remote object. The class
     * name is required to be fully qualified by its package name.
     */
    @Override
    public void run() {
        RmiEngine.logger.info("RMI Engine thread initializing...");
        // Start the RMI Registry
        Registry registry = null;

        // This approach starts the registry programmatically and assigns it to
        // a local variable.
        try {
            registry = LocateRegistry.createRegistry(this.port);
        } catch (RemoteException e) {
            this.msg = "An error occurred creating the RMI registry @ port "
                    + this.port;
            logger.fatal(this.msg, e);
            throw new SystemException(this.msg, e);
        }

        // Get list of remote objects from
        int count = 0;
        boolean error = false;
        try {
            ResourceBundle rb = RMT2File
                    .loadAppConfigProperties(RmiConstants.SERVICES_CONFIG_FILE);
            Enumeration<String> keys = rb.getKeys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                String implClass = rb.getString(key);
                this.registerRemoteObject(registry, key, implClass);
                count++;
            }
            this.msg = "There were " + count + "RMI objects registered";
        } catch (Exception e) {
            this.msg = "An error occurred registering remote objects";
            logger.fatal(this.msg, e);
            this.msg = "There were "
                    + count
                    + "RMI objects registered prior to the initialization error";
            error = true;
            throw new SystemException(this.msg, e);
        } finally {
            if (error) {
                logger.fatal(this.msg);
                logger.fatal("RMI Engine thread completed with errors");
            }
            else {
                logger.info(this.msg);
                logger.info("RMI Engine thread completed successfully");
            }
        }
    }

    /**
     * Creates amd registers a remote object for the RMI runtime.
     * 
     * @param registry
     *            an instance of the RMI registry
     * @param remoteObjName
     *            The name that the remote object will be identified as.
     * @param remoteObjClass
     *            The class name of the remote object. All remote objects must
     *            implement the {@link RmiServiceCommand} interface
     * @throws RmiEngineInitializationException
     *             Failure to initialize the RMI engine due to permissions
     *             errors, communication erros, or the inability to instantiate
     *             the remote object class.
     */
    private void registerRemoteObject(Registry registry, String remoteObjName,
            String remoteObjClass) throws RmiEngineInitializationException {
        StringBuffer buf = new StringBuffer();
        try {
            // Create bean utility so that we can dynamically instantiate the
            // stub implementation.
            RMT2BeanUtility util = new RMT2BeanUtility();
            RmiServiceCommand remoteObj = (RmiServiceCommand) util
                    .createBean(remoteObjClass);

            // Export the object
            RmiServiceCommand stub = (RmiServiceCommand) UnicastRemoteObject
                    .exportObject(remoteObj, 0);
            // Bind the remote object's stub in the registry
            registry.bind(remoteObjName, stub);
            // Add to the list of strong reference Map
            this.stubs.put(remoteObjName, remoteObj);

            buf.append("RMI object registered successfully: name=");
            buf.append(remoteObjName);
            buf.append(", class=");
            buf.append(remoteObjClass);
            logger.info(buf.toString());
        } catch (AccessException e) {
            this.msg = "Failure initiializing RMI server due to one or more user permissions problems";
            throw new RmiEngineInitializationException(this.msg, e);
        } catch (RemoteException e) {
            this.msg = "Failure initiializing RMI server due remote communication related problems";
            throw new RmiEngineInitializationException(this.msg, e);
        } catch (AlreadyBoundException e) {
            buf.append("Failure initiializing RMI server due the remote object is alread bound to the registry [name=");
            buf.append(remoteObjName);
            buf.append(", class=");
            buf.append(remoteObjClass);
            buf.append("]");
            throw new RmiEngineInitializationException(buf.toString(), e);
        } catch (SystemException e) {
            this.msg = "Failure initiializing RMI server due class instantiation error regarding, "
                    + remoteObjClass;
            throw new RmiEngineInitializationException(this.msg, e);
        }
    }

    /**
     * Removes every remote object from the registry.
     * <p>
     * Since a strong reference is maintained in a Map collection for each
     * remote object, this method is necessary to mimic the Cntrl-C shutdown
     * process of the RMI server. The following steps are carried out for this
     * process:
     * <ol>
     * <li>Get the registry created earlier (assuming it is local).</li>
     * <li>Unbind the remote object</li>
     * <li>Unexport the remote object by using the remote object instance
     * obtained from this.stubs Map.</li>
     * <li>Repeat steps 2 and 3 until all remote objects are unbound and
     * unexported.</li>
     * </ol>
     */
    public void clearRegistry() {
        RmiEngine.logger.info("Start removing objects from RMI registry...");
        // Start the RMI Registry
        Registry registry = null;

        // This approach starts the registry programmatically and assigns it to
        // a local variable.
        try {
            registry = LocateRegistry.getRegistry();
        } catch (RemoteException e) {
            this.msg = "Unable to remove objects from the registry due to an error occurred obtaining the RMI registry @ port "
                    + this.port;
            logger.fatal(this.msg, e);
            throw new SystemException(this.msg, e);
        }

        // Get list of remote objects from
        int count = 0;
        boolean error = false;
        try {
            ResourceBundle rb = RMT2File
                    .loadAppConfigProperties(RmiConstants.SERVICES_CONFIG_FILE);
            Enumeration<String> keys = rb.getKeys();
            boolean rc = false;
            while (keys.hasMoreElements()) {
                String objName = keys.nextElement();
                count++;
                registry.unbind(objName);
                Remote obj = (Remote) this.stubs.get(objName);
                rc = UnicastRemoteObject.unexportObject(obj, true);
                if (rc) {
                    this.msg = "Remote object, "
                            + objName
                            + ", was unbound and unexported from the registry successfully";
                    logger.info(this.msg);
                }
                else {
                    this.msg = "Remote object, "
                            + objName
                            + ", failed to be unbound and unexported from the registry";
                    logger.error(this.msg);
                }
            }
            this.msg = "There were " + count
                    + "RMI objects removed from the registry";
        } catch (Exception e) {
            this.msg = "An error occurred removing objects from the registry";
            logger.fatal(this.msg, e);
            this.msg = "There were "
                    + count
                    + "RMI objects removed from the registry prior to the initialization error";
            error = true;
            throw new SystemException(this.msg, e);
        } finally {
            if (error) {
                logger.fatal(this.msg);
                logger.fatal("RMI Engine object removal process completed with errors");
            }
            else {
                logger.info(this.msg);
                logger.info("RMI Engine object removal process completed successfully");
            }
        }
    }
}
