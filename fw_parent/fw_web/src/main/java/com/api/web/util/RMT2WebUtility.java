package com.api.web.util;

import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.SystemException;
import com.api.security.authentication.web.AuthenticationConst;
import com.api.web.Request;
import com.api.web.Session;
import com.api.web.security.RMT2SessionBean;
import com.constants.RMT2ServletConst;
import com.util.RMT2Date;
import com.util.UserTimestamp;

/**
 * Class contains a collection general purpose web utilities.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2WebUtility {

    /**
     * Builds a timestamp of message of the chronology of the Session when the
     * session has been terminated.
     * 
     * @param _sessionBean
     *            The RMT2SessionBean object.
     * @return A string indicating the results of the logout process which
     *         useful in outputing to the console.
     */
    public static final String getLogoutMessage(RMT2SessionBean _sessionBean) {

        long create = 0;
        long current = 0;
        double elapsed = 0;
        String msg = null;
        boolean sessionTimedout = false;

        try {
            if (_sessionBean == null) {
                throw new IllegalStateException("");
            }
            create = _sessionBean.getSessionCreateTime();
            Date createDate = new Date(create);
            String createDateStr = RMT2Date.formatDate(createDate,
                    "yyyy/MM/dd HH:mm:ss:SSS");
            msg = "Session Create Date: " + createDateStr + "\n";
        } catch (SystemException e) {
            msg = "";
        } catch (IllegalStateException e) {
            sessionTimedout = true;
            msg = "Session timed out!\n";
        }

        current = System.currentTimeMillis();
        elapsed = (current - create) / 1000.0; // Compute time in seconds

        Date currentDate = new Date(current);
        String currentDateStr = null;

        try {
            currentDateStr = RMT2Date.formatDate(currentDate,
                    "yyyy/MM/dd HH:mm:ss:SSS");
        } catch (SystemException e) {
            msg = "";
        }

        msg += "Session Logout Date: " + currentDateStr + "\n";
        if (!sessionTimedout) {
            msg += "Session Elapsed Time: " + elapsed + " seconds";
        }

        return msg;

    }

    /**
     * Gets the current user's session.
     * 
     * @param _request
     * @return User's HttpSession object
     */
    public static final HttpSession getSession(HttpServletRequest _request) {
        // Get new session
        HttpSession session = _request.getSession();
        return session;
    }

    /**
     * Copies the request's parameter and attribute data into a Properties
     * collection.
     * 
     * @return Properties instance or null if <i>request</i> is null.
     */
    public static Properties getRequestData(Request request) {
        return RMT2WebUtility.copyRequestData(request, null);
    }

    /**
     * Copies the parameters and attributes key/value pairs in <i>request</i> to
     * <i>prop</i>.
     * 
     * @param request
     *            The source HttpServletRequest
     * @param prop
     *            A Properties instance which serves as the destination. If
     *            null, a new Properties instance is created.
     * @return Properties instance or null if <i>request</i> is null.
     */
    public static Properties getRequestData(Request request, Properties prop) {
        return RMT2WebUtility.copyRequestData(request, prop);
    }

    /**
     * Copies the parameters and attributes key/value pairs in <i>request</i> to
     * <i>prop</i>. The combined keys between the request's parameters and
     * attributes must be unique in the Properties object which the parameters
     * will take precedence over the attributes. All parameter values are
     * expected to be of type String.
     * 
     * @param request
     *            The source HttpServletRequest
     * @param prop
     *            A Properties instance which serves as the destination. If
     *            null, a new Properties instance is created.
     * @return Properties instance or null if <i>request</i> is null.
     */
    private static Properties copyRequestData(Request request, Properties prop) {
        if (request == null) {
            return null;
        }
        if (prop == null) {
            prop = new Properties();
        }

        // Add available request attributes to the Properties object.
        Enumeration iter = request.getParameterNames();
        String key = null;
        String value = null;
        while (iter.hasMoreElements()) {
            key = (String) iter.nextElement();
            if (!prop.containsKey(key)) {
                value = request.getParameter(key);
                prop.setProperty(key, value);
            }
        }

        // Add available request attributes to the Properties object.
        iter = request.getAttributeNames();
        while (iter.hasMoreElements()) {
            key = (String) iter.nextElement();
            // Do not add if parm name already exist in target properties
            if (!prop.containsKey(key)) {
                Object obj = request.getAttribute(key);
                if (obj instanceof String || obj instanceof Integer
                        || obj instanceof Double || obj instanceof Boolean
                        || obj instanceof Date || obj instanceof Character) {
                    value = obj.toString();
                    prop.setProperty(key, value);
                }
            }
        }
        return prop;
    }

    /**
     * Obtains the web appliation context.
     * 
     * @param req
     *            Servlet request object.
     * @return String as the web application context.
     */
    public static String getWebAppContext(Object req) {
        if (req == null) {
            return null;
        }

        if (req instanceof ServletRequest) {
            return ((HttpServletRequest) req).getContextPath();
        }
        if (req instanceof Request) {
            return ((Request) req).getContextPath();
        }

        return null;
    }

    /**
     * Obtains the entire URI up to the servlet context. This includes the
     * scheme, server name, the server port number, and the servlet context
     * name.
     * 
     * @param obj
     *            An instance of ServletRequest, HttpServletRequest, or
     *            {@link com.controller.Request Request}
     * @return The URI from the scheme to the servlet context.
     */
    public static String getWebAppAbsoluteContext(Object obj) {
        if (obj == null) {
            return null;
        }

        String uriPrefix = null;
        if (obj instanceof ServletRequest) {
            ServletRequest req = (ServletRequest) obj;
            uriPrefix = req.getScheme() + "://" + req.getServerName() + ":"
                    + req.getServerPort();
            if (obj instanceof HttpServletRequest) {
                HttpServletRequest req2 = (HttpServletRequest) obj;
                uriPrefix += req2.getContextPath();
            }
            return uriPrefix;
        }
        if (obj instanceof Request) {
            Request req = (Request) obj;
            uriPrefix = req.getScheme() + "://" + req.getServerName() + ":"
                    + req.getServerPort() + req.getContextPath();
            return uriPrefix;
        }

        return null;

    }

    /**
     * Creates a UserTimestamp object using the user's request. The request
     * should contain the user's {@link SessionBean}.
     * 
     * @param request
     *            The HttpServletRequest object linked to the user's session.
     * @return {@link UserTimestamp} conatining the data needed to satisfy a
     *         user timestamp or null if <i>request</i> is null or invalid.
     * @throws SystemException
     *             If the session is invalid or if the user has not logged on to
     *             the system.
     */
    public static final UserTimestamp getUserTimeStamp(Request request)
            throws SystemException {
        Session session = null;
        RMT2SessionBean sessionBean = null;
        String userId;
        String message = null;

        if (request == null) {
            return null;
        }
        session = request.getSession();
        if (session == null) {
            message = "Session object is not availble for current user";
            throw new SystemException(message);
        }
        sessionBean = (RMT2SessionBean) session
                .getAttribute(RMT2ServletConst.SESSION_BEAN);
        if (sessionBean == null) {
            // If originating from a service call, the login id may exist as a
            // request parameter.
            userId = request.getParameter(AuthenticationConst.AUTH_PROP_USERID);
            if (userId == null) {
                userId = (String) request
                        .getAttribute(AuthenticationConst.AUTH_PROP_USERID);
                if (userId == null) {
                    message = "Current user's Session Bean object has not been setup...User may not be logged into the system";
                    throw new SystemException(message);
                }
            }
        }
        else {
            userId = sessionBean.getLoginId();
        }
        UserTimestamp ts = RMT2Date.getUserTimeStamp(userId);
        ts.setIpAddr(request.getRemoteAddr());
        return ts;
    }

} // end class

