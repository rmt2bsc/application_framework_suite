package com.api.messaging.rmi;

/**
 * Constants to be used for remote object messageing API.
 * 
 * @author rterrell
 * 
 */
public class RmiConstants {

    /**
     * The name of the {@link java.util.Properties} file used as the main
     * configuration resource for obtaining RMI clients.
     */
    public static final String CONFIG_SOURCE_PROPERTY_FILE = "config.RMI-Config";

    /**
     * The name of the properties file containing the list of name/value pair
     * mappings representing the Remote object names and the implementing class
     * names.
     */
    public static final String SERVICES_CONFIG_FILE = "config.RMI-Services";

    /**
     * Create RmiConstants object
     */
    public RmiConstants() {
        return;
    }

}
