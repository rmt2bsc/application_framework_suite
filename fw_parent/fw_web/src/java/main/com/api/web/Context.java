package com.api.web;

import java.util.Enumeration;

/**
 * Defines a set of methods that a servlet uses to communicate with its
 * surronding environment.
 * <p>
 * There is one context per "application" per Java Virtual Machine.
 * 
 * @author RTerrell
 * 
 */
public interface Context extends SystemAttributeMapping {

    /**
     * Returns the major version of the application that this JVM supports.
     * 
     * @return int
     */
    int getMajorVersion();

    /**
     * Returns the minor version of the application that this JVM supports.
     * 
     * @return int
     */
    int getMinorVersion();

    /**
     * Returns the name and version of the application running within this JVM.
     * The form of the returned string is <i>servername/versionnumber</i>.
     * 
     * @return a String containing at least the application name and version
     *         number
     */
    String getServerInfo();

    /**
     * Returns the actual object in which the implementation is built around.
     * 
     * @return An arbirary object.
     */
    Object getNativeInstance();

    /**
     * Returns a String containing the real path for a given application's
     * virtual path which is appropriate to the computer and operating system on
     * which the servlet container is running.
     * 
     * @param path
     *            a String specifying a virtual path
     * @return a String specifying the real path, or null if the translation
     *         cannot be performed
     */
    String getRealPath(String path);

    /**
     * Returns the value of the named attribute as an Object from the Context,
     * or null if no attribute of the given name exists.
     * 
     * @param name
     *            a String specifying the name of the attribute
     * @return an Object containing the value of the attribute, or null if the
     *         attribute does not exist or null if the context is invalid.
     */
    Object getAttribute(String name);

    /**
     * Returns an Enumeration containing the names of the attributes available
     * to this Context. This method returns an empty Enumeration if the context
     * has no attributes available to it.
     * 
     * @return an Enumeration of strings containing the names of the Context's
     *         attributes or null if the context is invalid.
     */
    Enumeration getAttributeNames();

    /**
     * Removes an attribute from this Context. This method is not generally
     * needed as attributes only persist as long as the context is being
     * handled.
     * 
     * @param name
     *            a String specifying the name of the attribute to remove.
     */
    void removeAttribute(String name);

    /**
     * Returns the context path of the application.
     * 
     * @return The context path of the web application, or "" for the default
     *         (root) context
     */
    String getContextPath();
}
