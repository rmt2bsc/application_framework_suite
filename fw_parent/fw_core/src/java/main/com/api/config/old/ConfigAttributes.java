package com.api.config.old;

/**
 * An interface for accessing properties.
 * 
 * @author RTerrell
 * @deprecated No longer needed
 */
public interface ConfigAttributes {

    /**
     * Return the value of the property named as, <i>key</i>
     * 
     * @param key
     *            The name of the property to access.
     * @return The property value.
     */
    String getProperty(String key);

}
