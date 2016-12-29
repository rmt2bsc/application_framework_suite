package com.api.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.RequestDispatcher;

/**
 * Defines an object to provide information pertaining the the client's request.
 * 
 * @author RTerrell
 * 
 */
public interface Request extends SystemAttributeMapping {

    /**
     * Returns the value of a request parameter as a String, or null if the
     * parameter does not exist. Request parameters are extra information sent
     * with the request.
     * <p>
     * You should only use this method when you are sure the parameter has only
     * one value. If the parameter might have more than one value, use
     * {@link com.controller.Request#getParameterValues(String)
     * getParameterValues}.
     * 
     * @param name
     *            a String specifying the name of the parameter
     * @return a String representing the single value of the parameter or null
     *         if the parameter is not found.
     */
    String getParameter(String name);

    /**
     * Returns an Enumeration of String objects containing the names of the
     * parameters contained in this request. If the request has no parameters,
     * the method returns an empty Enumeration.
     * 
     * @return an Enumeration of String objects, each String containing the name
     *         of a request parameter; or an empty Enumeration if the request
     *         has no parameters.
     */
    Enumeration getParameterNames();

    /**
     * Returns an array of String objects containing all of the values the given
     * request parameter has, or null if the parameter does not exist. If the
     * parameter has a single value, the array has a length of 1.
     * 
     * @param name
     *            a String containing the name of the parameter whose value is
     *            requested.
     * @return an array of String objects containing the parameter's values.
     */
    String[] getParameterValues(String name);

    /**
     * Returns a java.util.Map of the parameters of this request. Request
     * parameters are extra information sent with the request.
     * 
     * @return java.util.Map containing parameter names as keys and parameter
     *         values as map values. The keys in the parameter map are of type
     *         String. The values in the parameter map are of type String array.
     */
    Map getParameterMap();

    /**
     * Returns the actual object in which the implementation is built around.
     * 
     * @return An arbirary object.
     */
    Object getNativeInstance();

    /**
     * Returns the current session associated with this request, or if the
     * request does not have a session, creates one.
     * 
     * @return The {@link com.controller.Session Session} that is assoicated
     *         with this request.
     * @see com.controller.Request#getSession(boolean) getSession(boolean)
     */
    Session getSession();

    /**
     * Returns the current arbitrary session object associated with this request
     * or, if there is no current session and create is true, returns a new
     * session. If create is false and the request has no valid session, this
     * method returns null.
     * 
     * @param create
     *            true to create a new session for this request if necessary;
     *            false to return null if there's no current session.
     * @return The {@link com.controller.Session Session} that is associated
     *         with this request or null if create is false and the request has
     *         no valid session.
     * @see com.controller.Request#getSession() getSession()
     */
    Session getSession(boolean create);

    /**
     * Returns the port number to which the request was sent.
     * 
     * @return an integer specifying the port number
     */
    int getServerPort();

    /**
     * Returns the name of the scheme used to make this request.
     * 
     * @return A String containing the name of the scheme.
     */
    String getScheme();

    /**
     * Returns the host name of the server to which the request was sent.
     * 
     * @return A String containing the name of the server
     */
    String getServerName();

    /**
     * Returns the name and version of the protocol the request uses in the form
     * protocol/majorVersion.minorVersion.
     * 
     * @return a String containing the protocol name and version number
     */
    String getProtocol();

    /**
     * Returns the Internet Protocol (IP) address of the client or last proxy
     * that sent the request.
     * 
     * @return a String containing the IP address of the client that sent the
     *         request
     */
    String getRemoteAddr();

    /**
     * Returns the fully qualified name of the client or the last proxy that
     * sent the request.
     * 
     * @return a String containing the fully qualified name of the client
     */
    String getRemoteHost();

    /**
     * Returns the port of the client making the request.
     * 
     * @return an integer specifying the port number
     */
    int getRemotePort();

    /**
     * Returns the part of this request's URL that calls the servlet.
     * 
     * @return a String containing the name or path of the servlet being called
     */
    String getServletPath();

    /**
     * Returns the portion of the request URI that indicates the context of the
     * request.
     * 
     * @return a String specifying the portion of the request URI that indicates
     *         the context of the request
     */
    String getContextPath();

    /**
     * Returns the value of the specified request header as a String
     * 
     * @param name
     *            a String specifying the header name
     * @return String a String containing the value of the requested header, or
     *         null if the request does not have a header of that name
     */
    String getHeader(String name);

    /**
     * Returns an enumeration of all the header names this request contains. If
     * the request has no headers, this method returns an empty enumeration.
     * 
     * @return an enumeration of all the header names sent with this request; if
     *         the request has no headers, an empty enumeration; if the servlet
     *         container does not allow servlets to use this method, null
     */
    Enumeration getHeaderNames();

    /**
     * Retrieves the body of the request as binary data.
     * 
     * @return An arbitrary object representing the binary stream.
     * @throws IOException
     */
    InputStream getInputStream() throws IOException;

    /**
     * Returns the MIME type of the body of the request, or null if the type is
     * not known.
     * 
     * @return a String containing the name of the MIME type of the request, or
     *         null if the type is not known
     */
    String getContentType();

    /**
     * Returns the length, in bytes, of the request body and made available by
     * the input stream, or -1 if the length is not known.
     * 
     * @return an integer containing the length of the request body or -1 if the
     *         length is not known
     */
    int getContentLength();

    /**
     * Returns a RequestDispatcher object that acts as a wrapper for the
     * resource located at the given path. A RequestDispatcher object can be
     * used to forward a request to the resource or to include the resource in a
     * response. The resource can be dynamic or static.
     * <p>
     * The pathname specified may be relative, although it cannot extend outside
     * the current servlet context. If the path begins with a "/" it is
     * interpreted as relative to the current context root. This method returns
     * null if the servlet container cannot return a <i>RequestDispatcher</i>.
     * <p>
     * The difference between this method and
     * ServletContext.getRequestDispatcher(java.lang.String) is that this method
     * can take a relative path.
     * 
     * @param path
     *            a String specifying the pathname to the resource. If it is
     *            relative, it must be relative against the current servlet.
     * @return a <i>RequestDispatcher</i> object that acts as a wrapper for the
     *         resource at the specified path, or null if the servlet container
     *         cannot return a <i>RequestDispatcher</i>.
     */
    RequestDispatcher getRequestDispatcher(String path);
}
