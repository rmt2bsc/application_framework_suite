package com.api.web;

import java.io.Serializable;
import java.util.Enumeration;

/**
 * Provides a common interface for mapping attributes of a particular
 * application context object.
 * 
 * @author RTerrell
 * 
 */
public interface SystemAttributeMapping extends Serializable {

    /**
     * Returns the value of the named attribute as an Object, or null if no
     * attribute of the given name exists.
     * 
     * @param name
     *            a String specifying the name of the attribute
     * @return an Object containing the value of the attribute, or null if the
     *         attribute does not exist
     */
    Object getAttribute(String name);

    /**
     * Returns an Enumeration containing the names of the attributes available
     * to this request. This method returns an empty Enumeration if the request
     * has no attributes available to it.
     * 
     * @return an Enumeration of strings containing the names of the request's
     *         attributes
     */
    Enumeration getAttributeNames();

    /**
     * Stores an attribute in this request. Attributes are reset between
     * requests.
     * <p>
     * If the object passed in is null, the effect is the same as calling
     * {@link com.controller.SystemAttributeMapping#removeAttribute(String)
     * removeAttribute(String)}
     * 
     * @param name
     *            a String specifying the name of the attribute
     * @param obj
     *            the Object to be stored
     */
    void setAttribute(String name, Object obj);

    /**
     * Removes an attribute from this request. This method is not generally
     * needed as attributes only persist as long as the request is being
     * handled.
     * 
     * @param name
     *            a String specifying the name of the attribute to remove.
     */
    void removeAttribute(String name);

}
