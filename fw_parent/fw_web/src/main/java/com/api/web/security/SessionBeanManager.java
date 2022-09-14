package com.api.web.security;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.RMT2Base;
import com.SystemException;
import com.api.config.AppPropertyPool;
import com.api.config.ConfigConstants;
import com.api.constants.RMT2ServletConst;
import com.api.security.RMT2TagQueryBean;
import com.api.security.authentication.web.AuthenticationConst;
import com.api.security.authentication.web.AuthenticationException;
import com.api.web.Request;
import com.api.web.Session;
import com.api.xml.XmlApiFactory;
import com.api.xml.XmlDao;

/**
 * Configures an existing user's session bean object with data specific to the
 * local of the application context.
 * 
 * @author RTerrell
 * 
 */
public class SessionBeanManager extends RMT2Base {
    private Request request;

    private SessionBeanManager(Request request) {
        this.request = request;
    }

    /**
     * Creates an instance of SessionBeanManager using a HttpServletRequest.
     * 
     * @param request
     *            THe user's request.
     * @return SessionBeanManager.
     */
    public static SessionBeanManager getInstance(Request request) {
        SessionBeanManager manager = new SessionBeanManager(request);
        return manager;
    }

    /**
     * Populates the usre's Session Bean object with information that is
     * domicile to the user's application context and adds the session bean to
     * the user's web session. A HttpSession object is obtained and associated
     * with the session bean. It is required that the session bean is valid and
     * contains a valid user's login id. A temporary work area is created in the
     * file system for the user to store and manage state data pertaining to the
     * application.
     * 
     * @return RMT2SessionBean A valid user session bean.
     * @throws SystemException
     *             When the session bean is invalid or does not contain a valid
     *             login id and source application identifier.
     */
    public void addSessionBean(RMT2SessionBean bean) throws SystemException {
        // At this point a session should already be assigned. If not, throw an
        // exception.
        Session session = request.getSession(false);
        String msg;
        if (session == null) {
            msg = "SessionBean setup failed:  The user's session is invalid.";
            Logger.getLogger("SessionBeanManager").log(Level.ERROR, msg);
            throw new SystemException(msg);
        }

        this.validateSessionBean(bean);
        bean.setSessionId(session.getId());

        // Initialize User's Session Bean
        bean.initSessionBean(request);

        // Obtain the timeout interval
        this.configureSessionTimeOut(bean);

        // Create user's work area in the file system where the web server
        // resides
        bean.createUserWorkArea();

        // Begin to configure HttpSession object with data from the user's
        // session bean.
        session.setMaxInactiveInterval(bean.getSessionMaxInactSecs());
        // Store the Session Bean on user's session
        session.setAttribute(RMT2ServletConst.SESSION_BEAN, bean);
        // Store a RMT2TagQueryBean on user's session
        session.setAttribute(RMT2ServletConst.QUERY_BEAN, new RMT2TagQueryBean());

        HttpServletRequest nativeReq = null;
        if (request.getNativeInstance() != null) {
            nativeReq = (HttpServletRequest) request.getNativeInstance();
            bean.setLocale(nativeReq.getLocale().getDisplayName());
        }
        bean.setRemoteAddress(request.getRemoteAddr());
        bean.setRemoteHost(request.getRemoteHost());
        bean.setRemotePort(request.getRemotePort());
        bean.setScheme(request.getScheme());
        bean.setServerName(request.getServerName());
        bean.setServerPort(request.getServerPort());
        bean.setServerProtocol(request.getProtocol());
        bean.setServletContext(request.getContextPath());
        return;
    }

    /**
     * validates the user's session bean.
     * 
     * @param bean
     *            The session bean to be validated.
     * @throws SystemException
     *             When the session bean is invalid or does not contain a valid
     *             login id and source application identifier.
     */
    protected void validateSessionBean(RMT2SessionBean bean) throws SystemException {
        if (bean == null) {
            msg = "The user session bean object is invalid";
            Logger.getLogger("SessionBeanManager").log(Level.ERROR, msg);
            throw new SystemException(msg);
        }
        if (bean.getLoginId() == null) {
            msg = "The user session object must have login id";
            Logger.getLogger("SessionBeanManager").log(Level.ERROR, msg);
            throw new SystemException(msg);
        }
        if (bean.getOrigAppId() == null) {
            msg = "The user session object must have the name of the source application";
            Logger.getLogger("SessionBeanManager").log(Level.ERROR, msg);
            throw new SystemException(msg);
        }
    }

    /**
     * Initializes the session bean with default session timeout values obtained
     * from the application's configuration properties file.
     * 
     * @param bean
     *            The session bean to be initialized.
     * @throws SystemException
     *             If the timeout value is not obtainable.
     */
    protected void configureSessionTimeOut(RMT2SessionBean bean) throws SystemException {
        int timeoutInterval = 0;
        String temp = AppPropertyPool.getProperty(ConfigConstants.PROPNAME_SESSION_TIMEOUT);
        try {
            timeoutInterval = new Integer(temp).intValue();
            if (timeoutInterval < AuthenticationConst.MIN_TIMEOUT) {
                timeoutInterval = AuthenticationConst.MIN_TIMEOUT;
            }
        } catch (NumberFormatException e) {
            // If interval does not exist or is invalid, default to 1 hour
            timeoutInterval = AuthenticationConst.DEFAULT_TIMEOUT;
        } finally {
            bean.setSessionMaxInactSecs(timeoutInterval);
        }
    }

    /**
     * Creates a SessionBean instance using user authentication data formatted
     * as a XML String.
     * 
     * @param xml
     * @param rootQuery
     * @param roleQuery
     * @return {@link com.api.security.authentication.web.RMT2SessionBean
     *         RMT2SessionBean}
     */
    public RMT2SessionBean buildSessionBean(String xml, String rootQuery, String roleQuery) {
        RMT2SessionBean session;
        try {
            if (this.request != null) {
                session = UserAuthenticationFactory.getSessionBeanInstance(this.request, this.request.getSession(false));
            }
            else {
                // this is normally used in environments outside a web context.
                // For example, JUnit Testing.
                session = UserAuthenticationFactory.getSessionBeanInstance("dummy", "dummy");
            }
        } catch (AuthenticationException e) {
            throw new SystemException("Unable to create Session Bean instance for authenticated user", e);
        }

        XmlDao dao = XmlApiFactory.createXmlDao(xml);
        Object rs = dao.retrieve(rootQuery);
        if (rs == null) {
            return null;
        }

        String val = null;
        while (dao.nextRow()) {
            session.setLoginId(dao.getColumnValue("login_id"));

            session.setAuthSessionId(dao.getColumnValue("auth_session_id"));
            session.setFirstName(dao.getColumnValue("fname"));
            session.setLastName(dao.getColumnValue("lname"));

            val = dao.getColumnValue("access_level");
            session.setAccessLevel(Integer.parseInt(val));

            val = dao.getColumnValue("server_port");
            session.setServerPort(Integer.parseInt(val));

            session.setOrigAppId(dao.getColumnValue("orig_app_id"));

            val = dao.getColumnValue("session_create");
            session.setSessionCreateTime(Long.parseLong(val));

            val = dao.getColumnValue("session_last_accessed");
            session.setSessionLastAccessedTime(Long.parseLong(val));

            val = dao.getColumnValue("session_max");
            session.setSessionMaxInactSecs(Integer.parseInt(val));

            val = dao.getColumnValue("group_id");
            session.setGroupId(val == null ? 0 : Integer.parseInt(val));
        }
        dao.close();

        // Get user roles
        List<String> roles = new ArrayList<String>();
        dao = XmlApiFactory.createXmlDao(xml);
        dao.retrieve(roleQuery);
        while (dao.nextRow()) {
            val = dao.getColumnValue("app_role_code");
            roles.add(val);
        }
        session.setRoles(roles);
        dao.close();

        // Return session instance
        return session;
    }
}
