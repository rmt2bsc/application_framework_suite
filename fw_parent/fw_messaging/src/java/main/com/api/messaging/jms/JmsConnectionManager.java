package com.api.messaging.jms;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.api.config.SystemConfigurator;
import com.api.config.jaxb.AppServerConfig;
import com.util.RMT2BeanUtility;

/**
 * Creates and manages one or more JMS connection objects as a collection.
 * <p>
 * The collection of JMS connections are managed as a singleton.
 * 
 * @author rterrell
 */
public class JmsConnectionManager {
    private static Logger LOGGER = Logger.getLogger(JmsConnectionManager.class);

    /**
     * Connection to the JMS provider is set up once for all class instances
     */
    private static Map<String, JmsConnectionInfo> CONNECTIONS;

    /**
     * JNDI related data, loaded once for all class instances
     */
    private static Context jndi = null;

    protected String errMsg;

    /**
     * Internally manages the details of each registered JMS connection object.
     * 
     * @author rterrell
     * 
     */
    private class JmsConnectionInfo {
        String providerUrl;

        String userName;

        String password;

        boolean useAuth;

        boolean useJndi;

        String initalContextFactory;

        String connectionFactory;

        Connection con;

        String system;

        int maxConnectAttempts;

        int connectAttemptInterval;

        private JmsConnectionInfo() {
            return;
        }
    }

    /**
     * Creates a JmsConnectionManager object by setting up the pool of JMS
     * connections.
     * <p>
     * The pool of JMS connections are governed as a singleton.
     */
    private JmsConnectionManager() {
        if (CONNECTIONS == null) {
            CONNECTIONS = new HashMap<String, JmsConnectionInfo>();
            this.createConnection();
        }
        return;
    }

    /**
     * 
     * @return
     */
    public static final JmsConnectionManager getJmsConnectionManger() {
        return new JmsConnectionManager();
    }

    /**
     * Creates a JMS configuration for system identified as <i>jmsSysId</i>.
     * <p>
     * The source of the configuration data is servlet initialization parameters
     * that are captured in {@link WSConfig} during applicatin startup.
     * 
     * @param jmsSysId
     *            The id of the JMS enabled system to build the configuration.
     * @return {@link Properties}
     * 
     */
    private Properties createJmsSystemConfig(
            AppServerConfig.MessagingSystemsInfo.System sys) {
        Properties prop = new Properties();
        prop.put("system", sys.getName());
        prop.put("useJndi", sys.getUseJndi());
        prop.put("authentication", sys.getUseAuthentication());
        prop.put("initialContext", sys.getInitialContextFactory());
        prop.put("connectionFactory", sys.getConnectionFactory());
        prop.put("connectionURL", sys.getProviderUrl());
        prop.put("username", sys.getUserId());
        prop.put("password", sys.getPassword());
        prop.put("maxConnectAttempts",
                String.valueOf(sys.getMaxConnectAttemps()));
        prop.put("connectAttemptInterval",
                String.valueOf(sys.getConnectAttemptInterval()));

        LOGGER.info("System: " + sys.getName());
        LOGGER.info("Use JNDI: " + sys.getUseJndi());
        LOGGER.info("Use Authentication: " + sys.getUseAuthentication() + "\n");

        LOGGER.debug("Conncetin Factory: " + sys.getConnectionFactory());
        LOGGER.debug("Intial Context : " + sys.getInitialContextFactory());
        LOGGER.debug("connection URL: " + sys.getProviderUrl());
        LOGGER.debug("username: " + sys.getUserId());
        LOGGER.debug("Password: " + sys.getPassword());
        LOGGER.debug("\n");

        return prop;
    }

    /**
     * Creates one or more JMS connections based on the number of JMS system
     * configurations specified in
     * {@link WSConstants#JMS_SOURCE_SYSTEM_CONFIGURATIONS} and adds them to the
     * pool of JMS connection pool.
     */
    protected void createConnection() {
        AppServerConfig config = SystemConfigurator.getConfig();
        if (config.getMessagingSystemsInfo() == null) {
            return;
        }

        List<AppServerConfig.MessagingSystemsInfo.System> systems = config
                .getMessagingSystemsInfo().getSystem();
        for (AppServerConfig.MessagingSystemsInfo.System sys : systems) {
            LOGGER.info("==================================================");
            LOGGER.info("Begin configuring JMS connection...");
            Properties sysConfig = this.createJmsSystemConfig(sys);
            try {
                this.createConnection(sysConfig);
            } catch (JmsConnectiontManagerException e) {
                errMsg = "Failed to initialize JMS connection(s)";
                LOGGER.fatal(errMsg);
                e.printStackTrace();
                throw new JmsClientManagerException(errMsg, e);
            }
            LOGGER.info("Configuring JMS connection for " + sys.getName()
                    + " completed.");
            LOGGER.info("==================================================\n\n");
        }
    }

    /**
     * Creates a JMS connection based on the input JMS configuration.
     * <p>
     * The initialization of the JMS connection depends on the details that are
     * contained in the configuration, <i>config</i>.
     * 
     * @param conifg
     *            the JMS configuration properties file containing all the
     *            necessary key/value pairs.
     * @throws JmsConnectiontManagerException
     *             Error creating the JMS connection.
     */
    public void createConnection(Properties config)
            throws JmsConnectiontManagerException {
        JmsConnectionInfo info = new JmsConnectionInfo();
        info.system = config.getProperty("system");

        info.useJndi = Boolean.parseBoolean(config.getProperty("useJndi"));
        if (info.useJndi) {
            info.connectionFactory = config.getProperty("connectionFactory");
            info.initalContextFactory = config.getProperty("initialContext");
        }
        info.providerUrl = config.getProperty("connectionURL");
        info.useAuth = Boolean.parseBoolean(config
                .getProperty("authentication"));
        if (info.useAuth) {
            info.userName = config.getProperty("username");
            info.password = config.getProperty("password");
        }
        info.maxConnectAttempts = Integer.parseInt(config
                .getProperty("maxConnectAttempts"));
        info.connectAttemptInterval = Integer.parseInt(config
                .getProperty("connectAttemptInterval"));

        this.createConnection(info);
        if (info.con != null) {
            JmsConnectionManager.CONNECTIONS.put(info.system, info);
        }

    }

    /**
     * Creates a JMS connection using the <i>JmsConnectionInfo</i> data and
     * starts the connection to accepting requests.
     * 
     * @param info
     *            an instance of {@link JmsConnectionInfo}
     * @throws JmsConnectiontManagerException
     *             Error establishing a JMS connection using the data contained
     *             in <i>config</i>.
     */
    private void createConnection(JmsConnectionInfo info)
            throws JmsConnectiontManagerException {
        try {
            Connection con;
            // Get a JMS Connection
            ConnectionFactory factory = this.getConnectionFactory(info);

            if (info.useAuth) {
                con = factory.createConnection(info.userName, info.password);
            }
            else {
                con = factory.createConnection();
            }

            // con.setExceptionListener(new ConnectionExceptionListener());
            con.start();

            LOGGER.info("JMS connection successfully setup for " + info.system);
            LOGGER.info("Provider URL: " + info.providerUrl);
            LOGGER.info("JNDI enabled: " + info.useJndi);
            if (info.useJndi) {
                LOGGER.info("Initial Context Factory Class: "
                        + info.connectionFactory);
                LOGGER.info("Connection Factory Name: "
                        + info.initalContextFactory);
            }
            LOGGER.info("Authentication enabled: " + info.useAuth);
            if (info.useAuth) {
                LOGGER.info("User: " + info.userName);
            }
            info.con = con;
        } catch (Exception e) {
            this.errMsg = "Error establishing a JMS connection for "
                    + info.system;
            LOGGER.error(this.errMsg);
            e.printStackTrace();
            throw new JmsConnectiontManagerException(this.errMsg, e);
        }
    }

    /**
     * Fetches a connection from the pool of JMS connections using the messaging
     * system id.
     * 
     * @param messagingSystem
     *            the id of messaging system to fetch a connection.
     * @return The JMS connection associated with <i>messagingSystem</i>
     */
    public Connection getConnection(String messagingSystem) {
        String msg = null;
        // Check to see if already connected to JMS
        JmsConnectionInfo info = this.getConnectionInfo(messagingSystem);
        if (info != null) {
            return info.con;
        }
        else {
            msg = messagingSystem
                    + " JMS connection does not exist and must be created";
            LOGGER.warn(msg);
            return null;
        }
    }

    /**
     * Fetches the connection information object that is associated with
     * messaging system id.
     * 
     * @param messagingSystem
     *            the id of messaging system to fetch a connection.
     * @return An instance of {@link JmsConnectionInfo}
     */
    private JmsConnectionInfo getConnectionInfo(String messagingSystem) {
        String msg = null;
        // Check to see if already connected to JMS
        JmsConnectionInfo info = JmsConnectionManager.CONNECTIONS
                .get(messagingSystem);
        if (info != null) {
            return info;
        }
        else {
            msg = "JMS connection information is unavailable for system, "
                    + messagingSystem;
            LOGGER.error(msg);
            return null;
        }
    }

    /**
     * Determines if host system has a healthy connection to a particular
     * messaging system.
     * <p>
     * Basically, if the connection is found to exist in the pool, then it is
     * considered to be connected.
     * 
     * @param messagingSystem
     *            The system to check
     * @return true when connection is established. Otherwise, false is
     *         returned.
     */
    public boolean isConnected(String messagingSystem) {
        return (this.getConnection(messagingSystem) == null ? false : true);
    }

    /**
     * Obtains the JMS connection factory via JNDI lookup or by direct
     * instantiation for a particular messaging system
     * 
     * @param info
     *            An instance of {@link JmsConnectionInfo}
     * @return a JMS ConnectionFactory instance or null if connection factory
     *         cannot be created.
     * @throws JmsConnectiontManagerException
     */
    private ConnectionFactory getConnectionFactory(JmsConnectionInfo info) {
        String errMsg = null;
        if (info.useJndi) {
            try {
                return (ConnectionFactory) lookupConnectionFactory(info);
            } catch (Exception e) {
                errMsg = "Error obtaining JMS Connection Factory via JNDI";
                LOGGER.error(errMsg);
                throw new JmsConnectiontManagerException(errMsg, e);
            }
        }
        else {
            if (info.initalContextFactory != null) {
                try {
                    RMT2BeanUtility util = new RMT2BeanUtility();
                    return (ConnectionFactory) util
                            .createBean(info.initalContextFactory);
                } catch (Exception e) {
                    errMsg = "Error obtaining JMS Connection Factory via direct instantiation";
                    LOGGER.error(errMsg);
                    throw new JmsConnectiontManagerException(errMsg, e);
                }
            }
        }
        return null;
    }

    /**
     * Performs a JNDI lookup of the JMS connection factory.
     * 
     * @param info
     *            An instance of {@link JmsConnectionInfo} containing the
     *            connection details.
     * @return An arbitrary object representing the search results.
     */
    private Object lookupConnectionFactory(JmsConnectionInfo info) {
        try {
            if (jndi == null) {
                LOGGER.info("Getting JNDI Intial Context for " + info.system
                        + "...");
                Hashtable<String, String> props = new Hashtable<String, String>();
                props.put(Context.PROVIDER_URL, info.providerUrl);
                props.put(Context.INITIAL_CONTEXT_FACTORY,
                        info.initalContextFactory);
                if (info.useAuth) {
                    props.put(Context.SECURITY_PRINCIPAL, info.userName);
                    props.put(Context.SECURITY_CREDENTIALS, info.password);
                }
                LOGGER.info("JNDI Provider URL: " + info.providerUrl);
                LOGGER.info("JNDI Intial Context Factory: "
                        + info.connectionFactory);
                LOGGER.info("JNDI User Name: " + info.userName);
                LOGGER.info("JNDI Password: " + info.password);
                jndi = new InitialContext(props);
            }
            Object factory = jndi.lookup(info.connectionFactory);
            LOGGER.info("JNDI Intial Context successfully obtained for "
                    + info.system + "\n");
            return factory;
        } catch (Exception e) {
            String errMsg = "Failed JNDI lookup of Connection Factory, "
                    + info.initalContextFactory + ", for system, "
                    + info.system;
            LOGGER.warn(errMsg);
            e.printStackTrace();
            throw new RuntimeException(errMsg, e);
        }
    }

    /**
     * Shutsdown and removes all JMS connections from the pool.
     * 
     * @throws JmsConnectiontManagerException
     */
    public void removeConnection() throws JmsConnectiontManagerException {
        if (JmsConnectionManager.CONNECTIONS.isEmpty()) {
            return;
        }

        Iterator<String> keyIter = JmsConnectionManager.CONNECTIONS.keySet()
                .iterator();
        while (keyIter.hasNext()) {
            String key = keyIter.next();
            this.removeConnection(key);
        }
        return;
    }

    /**
     * Shutsdown and removes a single JMS connection from the pool.
     * 
     * @param messagingSystem
     *            The target system for removing the JMS connection
     * @throws JmsConnectiontManagerException
     */
    public void removeConnection(String messagingSystem)
            throws JmsConnectiontManagerException {
        JmsConnectionInfo info = this.getConnectionInfo(messagingSystem);
        if (info == null) {
            return;
        }
        this.removeConnection(info);
        return;
    }

    /**
     * 
     * @param conInfo
     * @throws JmsConnectiontManagerException
     */
    private void removeConnection(JmsConnectionInfo conInfo)
            throws JmsConnectiontManagerException {
        try {
            LOGGER.info("Attempting to reset exception listener...");
            conInfo.con.setExceptionListener(null);
            LOGGER.info("Reseting of connection's exception listener successfull.");
        } catch (JMSException e) {
            LOGGER.info("Could not reset exception listener for connection", e);
        }
        try {
            LOGGER.info("Attempting to stop the delivery of incoming messages for JMS connection...");
            conInfo.con.stop();
            LOGGER.info("The stopage of JMS connection's incoming messages successful.");
        } catch (JMSException e) {
            LOGGER.warn(
                    "Could not stop the delivery of incoming messages for the JMS connection",
                    e);
        }
        try {
            LOGGER.info("Attempting to close JMS connection...");
            conInfo.con.close();
            LOGGER.info("JMS connection was closed successfully.");
        } catch (JMSException e) {
            LOGGER.warn("Could not close the JMS connection", e);
        }

        // Remove from connection pool
        JmsConnectionManager.CONNECTIONS.remove(conInfo.system);
        return;
    }

    /**
     * A listener class for automatically making an attempt to re-establish the
     * EIS_CONNECTION to the JMS server after the server has gone down for
     * whaterver reason.
     * <p>
     * If the EIS_CONNECTION to the JMS server cannot be reformed, then a
     * message is sent to the host application's log file indicating such.
     * 
     * @author rterrell
     * 
     */
    class ConnectionExceptionListener implements ExceptionListener {

        private String systeToRecover;;

        private JmsConnectionInfo conInfo;

        private ConnectionExceptionListener(JmsConnectionInfo conInfo) {
            this.conInfo = conInfo;
            this.systeToRecover = conInfo.system;
        }

        @Override
        public void onException(JMSException ex) {
            LOGGER.fatal("Connection to the " + conInfo.system
                    + " JMS Server has been lost.", ex);
            recoverConnection();
        }

        /**
         * Makes an attempt to recover the JMS EIS_CONNECTION once it has been
         * reset.
         * <p>
         * Hopefully this procedure will prevent one from having to restart the
         * HATS Server manually.
         */
        public void recoverConnection() {
            LOGGER.info(this.systeToRecover
                    + "Attempting to reestablish connection for "
                    + conInfo.system + "... ");
            try {
                removeConnection(this.conInfo);
            } catch (JmsConnectiontManagerException e) {

            }
            int recoverAttempts = 1;
            boolean connected = false;
            while (!connected && recoverAttempts <= conInfo.maxConnectAttempts) {
                LOGGER.info("Attempt # " + recoverAttempts + " in recovering "
                        + conInfo.system + " connection...");
                try {
                    createConnection(conInfo);
                    if (isConnected(conInfo.system)) {
                        connected = true;
                        LOGGER.info(conInfo.system
                                + " connection recovered successfully!");
                        break;
                    }
                } catch (JmsConnectiontManagerException e) {
                    LOGGER.warn(conInfo.system
                            + " system is still down...will try again in "
                            + (conInfo.connectAttemptInterval * 1000)
                            + " seconds.");
                    // Pause loop for a specified time interval
                    try {
                        Thread.sleep(conInfo.connectAttemptInterval);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                recoverAttempts++;
            }

            if (!connected && recoverAttempts > conInfo.maxConnectAttempts) {
                LOGGER.error("Failed to recover connection to "
                        + conInfo.system
                        + " JMS server.  Host server will have to be restarted manually!");
            }

        }
    }

    /**
     * @return the jndi
     */
    public Context getJndi() {
        return jndi;
    }
}
