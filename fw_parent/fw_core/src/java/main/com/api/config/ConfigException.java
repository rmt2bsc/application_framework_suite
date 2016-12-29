package com.api.config;

import com.RMT2RuntimeException;

/**
 * @author RTerrell
 * 
 */
public class ConfigException extends RMT2RuntimeException {
    private static final long serialVersionUID = 749903225419625851L;

    /**
     * 
     */
    public ConfigException() {
        super();
    }

    /**
     * @param e
     */
    public ConfigException(Exception e) {
        super(e);
    }

    /**
     * @param msg
     */
    public ConfigException(String msg) {
        super(msg);
    }

    /**
     * 
     * @param msg
     * @param e
     */
    public ConfigException(String msg, Throwable e) {
        super(msg, e);
    }

}
