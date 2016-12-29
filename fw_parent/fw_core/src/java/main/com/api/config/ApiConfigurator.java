package com.api.config;

/**
 * Initializes an API application by loading the api's configuration.
 * 
 * @author roy terrell
 * 
 */
public interface ApiConfigurator {
    /**
     * Driver for loading the api's configuration and initializing the api's
     * environment.
     */
    void start() throws ConfigException;
}