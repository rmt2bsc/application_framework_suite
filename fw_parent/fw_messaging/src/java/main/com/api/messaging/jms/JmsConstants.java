package com.api.messaging.jms;

/**
 * Constants to be used for the JMS messageing API.
 * 
 * @author rterrell
 * 
 */
public class JmsConstants {

    public static final String DEFAUL_MSG_SYS = "RMT2";

    public static final String PROVIDER_OPENJMS = "OpenJMS";

    public static final String PROVIDER_ACTIVEMQ = "ActiveMQ";

    public static final String HANDLER_MAPPING_CONFIG = "HandlerMappings";

    public static final String HEADER_BASE_XPATH_QUERY = "//header";

    public static final String HEADER_NODE_APPLICATION = "application";

    public static final String HEADER_NODE_MODULE = "module";

    public static final String HEADER_NODE_TRANSACTION = "transaction";

    /**
     * The application or System hash key used to identify the java package
     * where the message handler registry Properties files live.
     */
    public static final String MSG_HANDLER_MAPPINGS_KEY = "msg_handler_reg_pkg";

    /**
     * The name of the {@link java.util.Properties} file used as the main
     * configuration resource for initializing the JMS environment.
     */
    public static final String CONFIG_SOURCE_PROPERTY_FILE = "config.JMS-Config";

    public static final String JAXB_DEFAULT_PKG = System
            .getProperty("jms.jaxb.defaultpackage");

    public static final String JNDI_SRC_LOCAL = "local";

    public static final String JNDI_SRC_LDAP = "ldap";

    public static final String JNDI_SRC_DNS = "dns";

    public static final String JNDI_SRC_DIRECT = "direct";

    public static final String CONNECTION_FACTORY = "jms/DatabaseConnectionFactory";

    /**
     * The String property name for the request command that is assoicated with
     * a {@link javax.jms.Message} instance.
     */
    public static final String CONSUMER_COMMAND_NAME = "command";

    /**
     * The String property name for the consumer handler class that is
     * assoicated with a {@link javax.jms.Message} instance.
     */
    public static final String CONSUMER_HANDLER_CLASS = "ConsumerHandlerClassName";

    /**
     * The mode value for a consumer listening for messages synchronously.
     */
    public static final int LISTEN_MODE_SYNC = 100;

    /**
     * The mode value for a consumer listening for messages asynchronously.
     */
    public static final int LISTEN_MODE_ASYNC = 101;

    /**
     * Create RmiConstants object
     */
    public JmsConstants() {
        return;
    }

}
