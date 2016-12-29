package com.api.web.controller.scope;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.RMT2Base;
import com.api.web.Context;
import com.api.web.Request;
import com.api.web.Response;
import com.api.web.Session;

/**
 * Factory for creating various context varialbe objects relative to a
 * particular application platform.
 * 
 * @author RTerrell
 * 
 */
public class HttpVariableScopeFactory extends RMT2Base {

    /**
     * Creates a Context instance based on the HTTP protocol implementation.
     * 
     * @param context
     *            An instance of ServletContext
     * @return {@link com.controller.Context Context}
     */
    public static final Context createHttpContext(ServletContext context) {
        Context obj = new HttpContext(context);
        return obj;
    }

    /**
     * Creates a Request instance based on the HTTP protocol implementation.
     * 
     * @param obj
     *            An instance of ServletRequest
     * @return {@link com.controller.Request Request}
     */
    public static final Request createHttpRequest(ServletRequest request) {
        Request obj = new HttpRequest(request);
        return obj;
    }

    /**
     * Creates a Response instance based on the HTTP protocol implementation.
     * 
     * @param response
     *            An instance of ServletResponse
     * @return {@link com.controller.Response Response}
     */
    public static final Response createHttpResponse(ServletResponse response) {
        Response obj = new HttpResponse(response);
        return obj;
    }

    /**
     * Create a Session using the HTTP session implementation which the internal
     * session is intialized to HttpSession parameter, <i>session</i>.
     * 
     * @param session
     *            The HttpSession used by the facade implementation.
     * @return {@link com.controller.Session Session}
     */
    public static final Session createHttpSession(HttpSession session) {
        Session obj = new HttpUserSession(session);
        return obj;
    }

    /**
     * Create a Session using the HTTP session implementation which the internal
     * session is setup via the HttpServleterquest parameter, <i>request</i>.
     * 
     * @param request
     *            The HttpServletRequest that holds a references to the session.
     * @return {@link com.controller.Session Session}
     */
    public static final Session createHttpSession(ServletRequest request) {
        if (request == null) {
            return null;
        }
        if (request instanceof HttpServletRequest) {
            return HttpVariableScopeFactory
                    .createHttpSession(((HttpServletRequest) request)
                            .getSession());
        }
        else {
            return null;
        }
    }

    /**
     * Conditionally create a Session that is associated with <i>request</i>
     * based on <i>create</i> and the current assoication of the request and the
     * internal session.
     * 
     * @param request
     *            The HttpServletRequest that is assoicated with the session.
     * @param create
     *            Set to true so taht a session is created in the event that the
     *            request has no valid session. Set to false to not create a
     *            session if one does not exist.
     * @return {@link com.controller.Session Session} if the session exist. If
     *         there is no current session and <i>create</i> is true, returns a
     *         new session. If <i>create</i> is false and the request has no
     *         valid session, this method returns null.
     */
    public static final Session createHttpSession(ServletRequest request,
            boolean create) {
        if (request == null) {
            return null;
        }

        HttpSession session = null;
        if (request instanceof HttpServletRequest) {
            session = ((HttpServletRequest) request).getSession(create);
            if (session == null) {
                return null;
            }
        }
        return HttpVariableScopeFactory.createHttpSession(session);
    }
}
