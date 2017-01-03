package com.api.persistence.db;

/**
 * Constants to be used with the db package within the Persistence API.
 * 
 * @author rterrell
 * 
 */
public class DatabaseConnectionConstants {

    /**
     * This is the name of the properties file containing the directives needed
     * to locate and intantiate a {@link ConnectionProvider} object.
     * <p>
     * The value of this variable should be the filname minus the file extension
     * since it is loaded in to memory via the ResourceBundle class. This
     * properties file is expected to be found in the root of the application's
     * classpath.
     */
    public static final String CONNECTION_CONFIG = "config.DB-ORM-Config";

    public static final String API_CONNECTION_PLACEHOLDER = "${ApiName}";

    public static final String API_CONNECTION_CONFIG = "config.${ApiName}-DB-ORM-Config";

    /**
     * Create DatabaseConnectionConstants object
     */
    public DatabaseConnectionConstants() {
        return;
    }

}
