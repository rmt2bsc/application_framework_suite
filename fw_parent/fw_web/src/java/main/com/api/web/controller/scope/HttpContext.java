package com.api.web.controller.scope;

import javax.servlet.ServletContext;

import java.util.Enumeration;

import com.RMT2Base;
import com.api.web.Context;

/**
 * Provides a convenient wrapper implementation of the Context interface, hence
 * the ServletContext.
 * 
 * @author RTerrell
 * 
 */
class HttpContext extends RMT2Base implements Context {
    private ServletContext context;

    /**
     * Default constructor
     */
    private HttpContext() {
        return;
    }

    /**
     * Construct a HttpContext object and initialize it with a ServletContext
     * instance.
     * 
     * @param context
     *            ServletContext
     */
    public HttpContext(ServletContext context) {
        this.context = context;
        return;
    }

    /**
     * Returns the major version of the application that this JVM supports.
     * 
     * @return int or -1 if the context is invalid.
     */
    public int getMajorVersion() {
        if (this.context == null) {
            return -1;
        }
        return this.context.getMajorVersion();
    }

    /**
     * Returns the minor version of the application that this JVM supports.
     * 
     * @return int or -1 if the context is invalid.
     */
    public int getMinorVersion() {
        if (this.context == null) {
            return -1;
        }
        return this.context.getMinorVersion();
    }

    /**
     * Returns the actual ServletContext object which is wrapped by this
     * implementation.
     * 
     * @return {@link javax.servlet.ServletContext ServletContext}
     */
    public Object getNativeInstance() {
        return this.context;
    }

    /**
     * Returns the name and version of the application running within this JVM.
     * The form of the returned string is <i>servername/versionnumber</i>.
     * 
     * @return a String containing at least the application name and version
     *         number or null if the context is invalid.
     */
    public String getServerInfo() {
        if (this.context == null) {
            return null;
        }
        return this.context.getServerInfo();
    }

    /**
     * Returns a String containing the real path for a given virtual path. For
     * example, the path "/index.html" returns the absolute file path on the
     * server's filesystem would be served by a request for
     * "http://host/contextPath/index.html", where contextPath is the context
     * path of this ServletContext.
     * <p>
     * The real path returned will be in a form appropriate to the computer and
     * operating system on which the servlet container is running, including the
     * proper path separators. This method returns null if the servlet container
     * cannot translate the virtual path to a real path for any reason (such as
     * when the content is being made available from a .war archive).
     * 
     * @param path
     *            a String specifying a virtual path
     * @return a String specifying the real path, or null if the translation
     *         cannot be performed
     */
    public String getRealPath(String path) {
        if (this.context == null) {
            return null;
        }
        return this.context.getRealPath(path);
    }

    /**
     * Returns the value of the named attribute as an Object from the
     * ServletContext, or null if no attribute of the given name exists.
     * 
     * @param name
     *            a String specifying the name of the attribute
     * @return an Object containing the value of the attribute, or null if the
     *         attribute does not exist or null if the context is invalid.
     */
    public Object getAttribute(String name) {
        if (this.context == null) {
            return null;
        }
        return this.context.getAttribute(name);
    }

    /**
     * Returns an Enumeration containing the names of the attributes available
     * to this ServletContext. This method returns an empty Enumeration if the
     * context has no attributes available to it.
     * 
     * @return an Enumeration of strings containing the names of the
     *         ServletContext's attributes or null if the context is invalid.
     */
    public Enumeration getAttributeNames() {
        if (this.context == null) {
            return null;
        }
        return this.context.getAttributeNames();
    }

    /**
     * Removes an attribute from this ServletContext. This method is not
     * generally needed as attributes only persist as long as the context is
     * being handled.
     * 
     * @param name
     *            a String specifying the name of the attribute to remove.
     */
    public void removeAttribute(String name) {
        if (this.context == null) {
            return;
        }
        this.context.removeAttribute(name);
    }

    /**
     * Stores an attribute in this ServletContext. Attributes are reset between
     * contexts. If the object passed in is null, the effect is the same as
     * calling
     * {@link com.controller.SystemAttributeMapping#removeAttribute(String)
     * removeAttribute(String)}
     * 
     * @param name
     *            a String specifying the name of the attribute
     * @param obj
     *            the Object to be stored
     */
    public void setAttribute(String name, Object obj) {
        if (this.context == null) {
            return;
        }
        this.context.setAttribute(name, obj);
    }

    /**
     * Returns the context path of the application.
     * <p>
     * The context path is the portion of the request URI that is used to select
     * the context of the request. The context path always comes first in a
     * request URI. The path starts with a "/" character but does not end with a
     * "/" character. For servlets in the default (root) context, this method
     * returns "".
     * <p>
     * It is possible that a servlet container may match a context by more than
     * one context path. In such cases the HttpServletRequest.getContextPath()
     * will return the actual context path used by the request and it may differ
     * from the path returned by this method. The context path returned by this
     * method should be considered as the prime or preferred context path of the
     * application.
     * 
     * @return The context path of the web application, or "" for the default
     *         (root) context
     */
    public String getContextPath() {
        return this.getContextPath();
    }
}
