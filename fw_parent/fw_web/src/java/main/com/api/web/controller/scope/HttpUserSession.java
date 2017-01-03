package com.api.web.controller.scope;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;

import com.RMT2Base;
import com.api.web.Context;
import com.api.web.Session;

/**
 * 
 * Implements the {@link com.controller.Session Session} interface to provide
 * functionality for an servlet container to identify a user across web page
 * requests.
 * 
 * @author appdev
 * 
 */
class HttpUserSession extends RMT2Base implements Session {
    private HttpSession session;

    private Context context;

    /**
     * Default constructor
     */
    private HttpUserSession() {
        return;
    }

    /**
     * Constructs a HttpUserSession using a Http session object to initialize
     * the instance with a valid HttpSession. The HttpSession instance is also
     * used to establish a ServletContext reference as well.
     * 
     * @param session
     *            HttpSession
     */
    public HttpUserSession(HttpSession session) {
        if (session == null) {
            return;
        }
        this.session = session;
        this.context = HttpVariableScopeFactory.createHttpContext(session
                .getServletContext());
        return;
    }

    /**
     * Returns the actual HttpSession object which is wrapped by this
     * implementation.
     * 
     * @return {@link javax.servlet.ServletRequest ServletRequest}
     */
    public Object getNativeInstance() {
        return this.session;
    }

    /**
     * Returns the time when this HTTP session was created, measured in
     * milliseconds since midnight January 1, 1970 GMT.
     * 
     * 
     * @return long specifying when this session was created, expressed in
     *         milliseconds since 1/1/1970 GMT
     */
    public long getCreationTime() {
        if (this.session == null) {
            return -1;
        }
        return this.session.getCreationTime();
    }

    /**
     * Returns a string containing the unique identifier assigned to this
     * session. The identifier is assigned by the servlet container and is
     * implementation dependent.
     * 
     * @return String
     */
    public String getId() {
        if (this.session == null) {
            return null;
        }
        return this.session.getId();
    }

    /**
     * Returns the last time the client sent a request associated with this HTTP
     * session, as the number of milliseconds since midnight January 1, 1970
     * GMT, and marked by the time the container received the request.
     * 
     * @return a long representing the last time the client sent a request
     *         associated with this session, expressed in milliseconds since
     *         1/1/1970 GMT.
     * @throws IllegalStateException
     *             if this method is called on an invalidated session
     */
    public long getLastAccessedTime() throws IllegalStateException {
        if (this.session == null) {
            return -1;
        }
        return this.session.getLastAccessedTime();
    }

    /**
     * Returns the maximum time interval, in seconds, that the servlet container
     * will keep this session open between client accesses. Once the time
     * interval has been reached, the application will invalidate the session. A
     * negative time interval means that the session will never time out.
     * 
     * @return an integer specifying the number of seconds this session remains
     *         open between client requests
     */
    public int getMaxInactiveInterval() {
        if (this.session == null) {
            return 0;
        }
        return this.session.getMaxInactiveInterval();
    }

    /**
     * Invalidates this session then unbinds any objects bound to it.
     * 
     * @throws IllegalStateException
     *             if this method is called on an already invalidated session
     */
    public void invalidate() throws IllegalStateException {
        if (this.session == null) {
            return;
        }
        this.session.invalidate();
    }

    /**
     * Returns true if the client does not yet know about the HTTP session or if
     * the client chooses not to join the session.
     * 
     * @return true if the application has created a session, but the client has
     *         not yet joined.
     */
    public boolean isNew() {
        if (this.session == null) {
            return false;
        }
        return this.session.isNew();
    }

    /**
     * Returns the ServletContext to which this session belongs.
     * 
     * @return {@link com.controller.Context Context} which the session belongs.
     */
    public Context getContext() {
        return this.context;
    }

    /**
     * Specifies the time, in seconds, between client requests before the
     * servlet container will invalidate this session.
     * 
     * @param interval
     *            An integer specifying the number of seconds
     */
    public void setMaxInactiveInterval(int interval) {
        if (this.session == null) {
            return;
        }
        this.session.setMaxInactiveInterval(interval);
    }

    /**
     * Returns the value of the named attribute as an Object from the
     * HttpSession, or null if no attribute of the given name exists.
     * 
     * @param name
     *            a String specifying the name of the attribute
     * @return an Object containing the value of the attribute, or null if the
     *         attribute does not exist or null if the session is invalid.
     */
    public Object getAttribute(String name) {
        if (this.session == null) {
            return null;
        }
        return this.session.getAttribute(name);
    }

    /**
     * Returns an Enumeration containing the names of the attributes available
     * to this HttpSession. This method returns an empty Enumeration if the
     * session has no attributes available to it.
     * 
     * @return an Enumeration of strings containing the names of the
     *         HttpSession's attributes or null if the session is invalid.
     */
    public Enumeration getAttributeNames() {
        if (this.session == null) {
            return null;
        }
        return this.session.getAttributeNames();
    }

    /**
     * Removes an attribute from this HttpSession. This method is not generally
     * needed as attributes only persist as long as the session is being
     * handled.
     * 
     * @param name
     *            a String specifying the name of the attribute to remove.
     */
    public void removeAttribute(String name) {
        if (this.session == null) {
            return;
        }
        this.session.removeAttribute(name);
    }

    /**
     * Stores an attribute in this HttpSession. Attributes are reset between
     * sessions. If the object passed in is null, the effect is the same as
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
        if (this.session == null) {
            return;
        }
        this.session.setAttribute(name, obj);
    }

}
