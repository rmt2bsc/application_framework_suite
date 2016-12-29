package com.api.web;

/**
 * Defines an object to provide information that identifies a user across
 * request/response or client/server visits via the user interface. The session
 * should persist for a specified time period, across more than one connection
 * or page request from the user. One session basically corresponds to one user.
 * 
 * @author appdev
 * 
 */
public interface Session extends SystemAttributeMapping {

    /**
     * Returns the actual object in which the implementation is built around.
     * 
     * @return An arbirary object.
     */
    Object getNativeInstance();

    /**
     * Returns the time when this session was created, measured in milliseconds
     * since midnight January 1, 1970 GMT.
     * 
     * 
     * @return long specifying when this session was created, expressed in
     *         milliseconds since 1/1/1970 GMT
     */
    long getCreationTime();

    /**
     * Returns a string containing the unique identifier assigned to this
     * session. The creation and assignment of the identifier is implementation
     * and platform dependent.
     * 
     * @return String
     */
    String getId();

    /**
     * Returns the last time the client sent a request associated with this
     * session, as the number of milliseconds since midnight January 1, 1970
     * GMT, and marked by the time the container received the request.
     * 
     * @return a long representing the last time the client sent a request
     *         associated with this session, expressed in milliseconds since
     *         1/1/1970 GMT.
     * @throws IllegalStateException
     *             if this method is called on an invalidated session
     */
    long getLastAccessedTime() throws IllegalStateException;

    /**
     * Returns the maximum time interval, in seconds, that the application will
     * keep this session open between client accesses. Once the time interval
     * has been reached, the application will invalidate the session. A negative
     * time interval means that the session will never time out.
     * 
     * @return an integer specifying the number of seconds this session remains
     *         open between client requests
     */
    int getMaxInactiveInterval();

    /**
     * Specifies the time, in seconds, between client requests before the
     * servlet container will invalidate this session.
     * 
     * @param interval
     *            An integer specifying the number of seconds
     */
    void setMaxInactiveInterval(int interval);

    /**
     * Invalidates this session then unbinds any objects bound to it.
     * 
     * @throws IllegalStateException
     *             if this method is called on an already invalidated session
     */
    void invalidate() throws IllegalStateException;

    /**
     * Returns true if the client does not yet know about the session or if the
     * client chooses not to join the session.
     * 
     * @return true if the application has created a session, but the client has
     *         not yet joined
     */
    boolean isNew();

    /**
     * Returns the Context to which this session belongs.
     * 
     * @return {@link com.controller.Context Context} which the session belongs.
     */
    Context getContext();

}
