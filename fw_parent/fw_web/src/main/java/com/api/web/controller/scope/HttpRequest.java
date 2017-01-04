package com.api.web.controller.scope;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.RMT2Base;
import com.api.web.Request;
import com.api.web.Session;

/**
 * Provides a convenient wrapper implementation of the Request interface, hence
 * the ServletRequest.
 * 
 * @author RTerrell
 * 
 */
class HttpRequest extends RMT2Base implements Request {
    private ServletRequest request;

    private Session session;

    private HttpServletRequest httpRequest;

    /**
     * Default constructor
     */
    protected HttpRequest() {
        return;
    }

    /**
     * Construct a HttpRequest object and initialize it with a ServletRquest
     * instance.
     * 
     * @param request
     *            ServlerRequest.
     */
    public HttpRequest(ServletRequest request) {
        if (request == null) {
            return;
        }
        this.request = request;

        // Capture the user's session for this request as well as the
        // HttpServletRequest instance
        if (this.request instanceof HttpServletRequest) {
            this.httpRequest = (HttpServletRequest) this.request;
            this.session = HttpVariableScopeFactory.createHttpSession(request);
        }
        return;
    }

    /**
     * Returns the actual ServletRequest object which is wrapped by the
     * implementation.
     * 
     * @return {@link javax.servlet.ServletRequest ServletRequest}
     */
    public Object getNativeInstance() {
        return this.request;
    }

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
     *         if the parameter is not found or null if the request is invalid.
     */
    public String getParameter(String name) {
        if (this.request == null) {
            return null;
        }
        return this.request.getParameter(name);
    }

    /**
     * Returns a java.util.Map of the parameters of this request. Request
     * parameters are extra information sent with the request.
     * 
     * @return java.util.Map containing parameter names as keys and parameter
     *         values as map values. The keys in the parameter map are of type
     *         String. The values in the parameter map are of type String array
     *         or null if the request is invalid.
     */
    public Map getParameterMap() {
        if (this.request == null) {
            return null;
        }
        return this.request.getParameterMap();
    }

    /**
     * Returns an Enumeration of String objects containing the names of the
     * parameters contained in this request. If the request has no parameters,
     * the method returns an empty Enumeration.
     * 
     * @return an Enumeration of String objects, each String containing the name
     *         of a request parameter; or an empty Enumeration if the request
     *         has no parameters or null if the request is invalid.
     */
    public Enumeration getParameterNames() {
        if (this.request == null) {
            return null;
        }
        return this.request.getParameterNames();
    }

    /**
     * Returns an array of String objects containing all of the values the given
     * request parameter has, or null if the parameter does not exist. If the
     * parameter has a single value, the array has a length of 1.
     * 
     * @param name
     *            a String containing the name of the parameter whose value is
     *            requested.
     * @return an array of String objects containing the parameter's values or
     *         null if the request is invalid.
     */
    public String[] getParameterValues(String name) {
        if (this.request == null) {
            return null;
        }
        return this.request.getParameterValues(name);
    }

    /**
     * Returns the current HttpUserSession associated with this request, or if
     * the request does not have a session, creates one.
     * 
     * @return The {@link com.controller.Session Session} object that is
     *         assoicated with this request or null if the request is invalid.
     * @see com.controller.Request#getSession(boolean) getSession(boolean)
     */
    public Session getSession() {
        if (this.request == null) {
            return null;
        }
        return this.session;
    }

    /**
     * Returns the current HttpUserSession object associated with this request
     * or, if there is no current session and create is true, returns a new
     * session. If create is false and the request has no valid session, this
     * method returns null.
     * 
     * @param create
     *            true to create a new session for this request if necessary;
     *            false to return null if there's no current session.
     * @return The {@link com.controller.Session Session} object that is
     *         associated with this request or null if create is false and the
     *         request has no valid session.
     * @see com.controller.Request#getSession() getSession()
     */
    public Session getSession(boolean create) {
        return HttpVariableScopeFactory.createHttpSession(this.httpRequest,
                create);
    }

    /**
     * Returns the value of the named attribute as an Object from the
     * ServletRquest, or null if no attribute of the given name exists.
     * 
     * @param name
     *            a String specifying the name of the attribute
     * @return an Object containing the value of the attribute, or null if the
     *         attribute does not exist or null if the request is invalid.
     */
    public Object getAttribute(String name) {
        if (this.request == null) {
            return null;
        }
        return this.request.getAttribute(name);
    }

    /**
     * Returns an Enumeration containing the names of the attributes available
     * to this ServletRquest. This method returns an empty Enumeration if the
     * request has no attributes available to it.
     * 
     * @return an Enumeration of strings containing the names of the
     *         ServletRquest's attributes or null if the request is invalid.
     */
    public Enumeration getAttributeNames() {
        if (this.request == null) {
            return null;
        }
        return this.request.getAttributeNames();
    }

    /**
     * Removes an attribute from this ServletRquest. This method is not
     * generally needed as attributes only persist as long as the request is
     * being handled.
     * 
     * @param name
     *            a String specifying the name of the attribute to remove.
     */
    public void removeAttribute(String name) {
        if (this.request == null) {
            return;
        }
        this.request.removeAttribute(name);
    }

    /**
     * Stores an attribute in this ServletRquest. Attributes are reset between
     * requests. If the object passed in is null, the effect is the same as
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
        if (this.request == null) {
            return;
        }
        this.request.setAttribute(name, obj);
    }

    /**
     * Returns the port number to which the request was sent. It is the value of
     * the part after ":" in the Host header value, if any, or the server port
     * where the client connection was accepted on.
     * 
     * @return an integer specifying the port number
     */
    public int getServerPort() {
        return this.request.getServerPort();
    }

    /**
     * Returns the name of the scheme used to make this request, for example,
     * http, https, or ftp. Different schemes have different rules for
     * constructing URLs, as noted in RFC 1738.
     * 
     * @return A String containing the name of the scheme used to make this
     *         request.
     */
    public String getScheme() {
        return this.request.getScheme();
    }

    /**
     * Returns the host name of the server to which the request was sent. It is
     * the value of the part before ":" in the Host header value, if any, or the
     * resolved server name, or the server IP address.
     * 
     * @return A String containing the name of the server
     */
    public String getServerName() {
        return this.request.getServerName();
    }

    /**
     * Returns the name and version of the protocol the request uses in the form
     * protocol/majorVersion.minorVersion, for example, HTTP/1.1. For HTTP
     * servlets, the value returned is the same as the value of the CGI variable
     * SERVER_PROTOCOL.
     * 
     * @return a String containing the protocol name and version number
     */
    public String getProtocol() {
        return this.request.getProtocol();
    }

    /**
     * Returns the Internet Protocol (IP) address of the client or last proxy
     * that sent the request. For HTTP servlets, same as the value of the CGI
     * variable REMOTE_ADDR.
     * 
     * @return a String containing the IP address of the client that sent the
     *         request
     */
    public String getRemoteAddr() {
        return this.request.getRemoteAddr();
    }

    /**
     * Returns the fully qualified name of the client or the last proxy that
     * sent the request. If the engine cannot or chooses not to resolve the
     * hostname (to improve performance), this method returns the dotted-string
     * form of the IP address. For HTTP servlets, same as the value of the CGI
     * variable REMOTE_HOST.
     * 
     * @return a String containing the fully qualified name of the client
     */
    public String getRemoteHost() {
        return this.request.getRemoteHost();
    }

    /**
     * Returns the Internet Protocol (IP) source port of the client or last
     * proxy that sent the request.
     * 
     * @return an integer specifying the port number
     */
    public int getRemotePort() {
        return this.request.getRemotePort();
    }

    /**
     * Returns the part of this request's URL that calls the servlet. This path
     * starts with a "/" character and includes either the servlet name or a
     * path to the servlet, but does not include any extra path information or a
     * query string. Same as the value of the CGI variable SCRIPT_NAME.
     * 
     * @return a String containing the name or path of the servlet being called,
     *         as specified in the request URL, decoded, or an empty string if
     *         the servlet used to process the request is matched using the "/*"
     *         pattern. Returns null if the request object is not a descendent
     *         of HttpServletRequest.
     */
    public String getServletPath() {
        if (this.request instanceof HttpServletRequest) {
            return ((HttpServletRequest) this.request).getServletPath();
        }
        return null;
    }

    /**
     * Returns the portion of the request URI that indicates the context of the
     * request. The context path always comes first in a request URI. The path
     * starts with a "/" character but does not end with a "/" character. For
     * servlets in the default (root) context, this method returns "". The
     * container does not decode this string.
     * <p>
     * It is possible that a servlet container may match a context by more than
     * one context path. In such cases this method will return the actual
     * context path used by the request and it may differ from the path returned
     * by the ServletContext.getContextPath() method. The context path returned
     * by ServletContext.getContextPath() should be considered as the prime or
     * preferred context path of the application.
     * 
     * @return a String specifying the portion of the request URI that indicates
     *         the context of the request
     */
    public String getContextPath() {
        if (this.request instanceof HttpServletRequest) {
            return ((HttpServletRequest) this.request).getContextPath();
        }
        return null;
    }

    /**
     * Returns the value of the specified request header as a String
     * 
     * @param name
     *            a String specifying the header name
     * @return String a String containing the value of the requested header, or
     *         null if the request does not have a header of that name or if the
     *         request is not of type HttpServletRequest.
     */
    public String getHeader(String name) {
        if (this.request instanceof HttpServletRequest) {
            return ((HttpServletRequest) this.request).getHeader(name);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.controller.Request#getRequestDispatcher(java.lang.String)
     */
    public RequestDispatcher getRequestDispatcher(String path) {
        if (this.request instanceof HttpServletRequest) {
            return ((HttpServletRequest) this.request)
                    .getRequestDispatcher(path);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.controller.Request#getInputStream()
     */
    public InputStream getInputStream() throws IOException {
        return this.request.getInputStream();
    }

    /**
     * 
     */
    public Enumeration getHeaderNames() {
        return ((HttpServletRequest) this.request).getHeaderNames();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.controller.Request#getContentType()
     */
    public String getContentType() {
        return ((HttpServletRequest) this.request).getContentType();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.controller.Request#getContentLength()
     */
    public int getContentLength() {
        return ((HttpServletRequest) this.request).getContentLength();
    }

}
