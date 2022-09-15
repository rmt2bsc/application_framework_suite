package com.api.web.security;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.RMT2Base;
import com.SystemException;
import com.api.config.AppPropertyPool;
import com.api.config.ConfigConstants;
import com.api.security.authentication.web.AuthenticationConst;
import com.api.util.RMT2File;
import com.api.web.Request;
import com.api.web.Session;

/**
 * Manages detail information about the user and his session.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2SessionBean extends RMT2Base implements Serializable, HttpSessionBindingListener {
    private static final long serialVersionUID = -2656457027971715300L;

    private String loginId;

    private String firstName;

    private String lastName;

    private int groupId;

    private List roles;

    private int accessLevel;

    private String sessionId;

    private String gatewayInterface;

    private String remoteHost;

    private String remoteAddress;

    private int remotePort;

    private String remoteAppName;

    private String serverProtocol;

    private String serverSoftware;

    private String serverName;

    private int serverPort;

    private String userAgent;

    private String locale;

    private String accept;

    private String acceptLanguage;

    private String acceptEncoding;

    private String scheme;

    private String serverInfo;

    private String servletContext;

    private String contextPath;

    private long sessionCreateTime;

    private long sessionLastAccessedTime;

    private int sessionMaxInactSecs;

    private String origAppId;

    private String authSessionId;

    /**
     * Constructs a RMT2SessionBean object which does not initialize all
     * properties.
     * 
     * @throws SystemException
     */
    protected RMT2SessionBean() throws SystemException {
        super();
    }

    /**
     * Constructs a RMT2SessionBean object using the request and session objects
     * to initialized all properties.
     * 
     * @param _request
     *            The user's request
     * @param _session
     *            The user's session.
     * @throws SystemException
     */
    protected RMT2SessionBean(Request request, Session session) throws SystemException {
        this();
        Logger logger = Logger.getLogger("RMT2SessionBean");

        // setup bean with http request data, if applicable
        this.initSessionBean(request);

        logger.log(Level.INFO, "Servlet scheme: " + this.scheme);
        logger.log(Level.INFO, "Servlet context: " + this.servletContext);
        logger.log(Level.INFO, "Context Path: " + this.contextPath);
        logger.log(Level.INFO, "Server name: " + this.serverName);
    }

    /**
     * Performs additional object initialization.
     * 
     */
    public void initBean() {

    }

    /**
     * Initialize the session bean with values from the Request.
     * 
     * @param req
     */
    public void initSessionBean(Request req) {
        this.serverProtocol = req.getProtocol();
        this.scheme = req.getScheme();
        this.serverName = req.getServerName();
        this.serverPort = req.getServerPort();
        this.remoteAddress = req.getRemoteAddr();
        this.remoteHost = req.getRemoteHost();
        // this.locale = req.getLocale();
        this.userAgent = req.getHeader("User-Agent");
        this.accept = req.getHeader("Accept");
        this.acceptLanguage = req.getHeader("Accept-Language");
        this.acceptEncoding = req.getHeader("Accept-Encoding");
        this.servletContext = req.getServletPath();
        this.contextPath = req.getContextPath();

        // Get properties pertaining to the session. BTW, create session if it
        // does not exist.
        Session session = req.getSession(true);
        this.sessionId = session.getId();
        this.setServerInfo(session.getContext().getServerInfo());
        this.sessionCreateTime = session.getCreationTime();
        this.sessionLastAccessedTime = session.getLastAccessedTime();
        this.sessionMaxInactSecs = session.getMaxInactiveInterval();
    }

    /**
     * Creates the user's session work area in the file system.
     * <p>
     * A user profile output directory is created at some specified location
     * during user login. The path of the directory is determined fro the
     * SystemParms properties file via the user_out_path hasah value. The name
     * of the sub-directory representing the user's profile is base on the user
     * web session id. The process of creating the user profile output
     * sub-directory verifies the existence of the profile. If found to exist
     * then directory creation is bypassed. Otherwise, the directory is created
     * which its name is derived from the user web session id.
     * 
     * @return The full path of session work area created.
     * @throws SystemException
     */
    public String createUserWorkArea() throws SystemException {
        Logger logger = Logger.getLogger("RMT2SessionBean");
        String outPath = null;
        File userProfileDir = null;
        if (this.sessionId == null) {
            return null;
        }
        outPath = AppPropertyPool.getProperty("user_out_path");
        outPath += "/" + this.sessionId;
        userProfileDir = new File(outPath);
        if (userProfileDir.exists()) {
            logger.log(Level.INFO, "Profile area for user, " + this.loginId + ": " + outPath
                    + " was not created since it already exist.");
            return this.sessionId;
        }
        if (userProfileDir.mkdir()) {
            logger.log(Level.INFO, "Profile area create for user, " + this.loginId + ": " + outPath);
        }
        else {
            logger.log(Level.ERROR, "Profile area for user, " + this.loginId + ", was not created at login: " + outPath);
        }
        return outPath;
    }

    /**
     * Removes the user's session work area from the file system.
     * 
     * @return The full path of session work area removed.
     * @throws SystemException
     */
    public String removeUserWorkArea() throws SystemException {
        Logger logger = Logger.getLogger("RMT2SessionBean");
        String outPath = null;
        File userProfileDir = null;
        if (this.sessionId == null) {
            return null;
        }
        outPath = AppPropertyPool.getProperty("user_out_path");
        outPath += "/" + this.sessionId;
        userProfileDir = new File(outPath);
        if (!userProfileDir.exists()) {
            logger.log(Level.INFO, "Unable to delete session work area for user, " + this.loginId + ": " + outPath
                            + " was not does not exist.");
            return this.sessionId;
        }

        int deleteCount = RMT2File.deleteFile(userProfileDir);
        logger.log(Level.INFO, deleteCount + " files and directories were removed from the session profile area for user, "
                        + this.loginId);
        return this.sessionId;
    }

    /**
     * Obtains the path of the user's work area.
     * 
     * @return String The full path of session work area or null if the work
     *         area does not exist.
     * @throws SystemException
     *             If work ara is inaccessible.
     */
    public String getWorkAreaPath() throws SystemException {
        String outPath = null;
        File userProfileDir = null;
        if (this.sessionId == null) {
            return null;
        }
        outPath = AppPropertyPool.getProperty("user_out_path");
        outPath += "/" + this.sessionId;
        userProfileDir = new File(outPath);
        if (userProfileDir.exists()) {
            return outPath;
        }
        else {
            return null;
        }
    }

    /**
     * Calculates and returns a URL formatted String containing the following
     * security parms, login id and application code. The login id will eqaul
     * <i>"!@#$%^&(*)"</i> if it has not been properly initialized.
     * 
     * @return String formatted as URL parameters.
     */
    public String getAuthUrlParms() {
        StringBuffer parms = new StringBuffer(20);
        String login = (this.loginId == null ? "!@#$%^&(*)" : this.loginId);
        String app = AppPropertyPool.getProperty(ConfigConstants.PROPNAME_APP_NAME);

        parms.append(AuthenticationConst.AUTH_PROP_USERID);
        parms.append("=");
        parms.append(login);
        parms.append("&");
        parms.append(AuthenticationConst.AUTH_PROP_MAINAPP);
        parms.append("=");
        parms.append(app);
        return parms.toString();
    }

    /**
     * Receives the notification to log the user out of the system when the
     * RMT2SessionBean instance is removed from the user's Session context. The
     * logout process involves clearing out the user's work area and accessing
     * the application's authenticator to perform the actual sign off.
     * 
     * @param event
     *            the binding event, HttpSessionBindingEvent, that identifies
     *            the session, hence containing the RMT2SessionBean instance.
     */
    public void valueUnbound(HttpSessionBindingEvent event) {
        Logger logger = Logger.getLogger("RMT2SessionBean");

        // Remove user's session work area
        this.removeUserWorkArea();

        // Get Needed data
        RMT2SessionBean sessionBean = (RMT2SessionBean) event.getValue();
        if (sessionBean == null) {
            String userMsg = "Unable to perform removal of user's session bean due to the session bean does not exist";
            logger.log(Level.INFO, userMsg);
            return;
        }
        String appName = sessionBean.getOrigAppId();
        String userName = sessionBean.getLoginId();
        String sessionId = sessionBean.getSessionId();

        String userMsg = "User, " + userName
                + ", was logged out of application, " + appName
                + " for session, " + sessionId;
        logger.log(Level.INFO, userMsg);
    }

    /**
     * Adds a user role to the list of roles for this session bean
     * 
     * @param role
     */
    public void addUserRole(String role) {
        if (role == null || role.equals("")) {
            return;
        }
        if (this.roles == null) {
            this.roles = new ArrayList();
        }
        this.roles.add(role);
    }

    /**
     * Gets the first name.
     * 
     * @return String
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Sets the first name
     * 
     * @param value
     *            String
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the last name
     * 
     * @return String
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Sets the last name
     * 
     * @param value
     *            String
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the login id
     * 
     * @return String
     */
    public String getLoginId() {
        return this.loginId;
    }

    /**
     * Sets the login id.
     * 
     * @param value
     *            String
     */
    public void setLoginId(String value) {
        this.loginId = value;
    }

    /**
     * Gest the access level
     * 
     * @return int
     */
    public int getAccessLevel() {
        return this.accessLevel;
    }

    /**
     * Sets the access level
     * 
     * @param value
     *            int
     */
    public void setAccessLevel(int value) {
        this.accessLevel = value;
    }

    /**
     * Gest the session id
     * 
     * @return String
     */
    public String getSessionId() {
        return this.sessionId;
    }

    /**
     * Sets the session id
     * 
     * @param value
     *            String
     */
    public void setSessionId(String value) {
        this.sessionId = value;
    }

    /**
     * Gest the gateway interface
     * 
     * @return String
     */
    public String getGatewayInterface() {
        return this.gatewayInterface;
    }

    /**
     * Sets the gateway interface
     * 
     * @param value
     *            String
     */
    public void setGatewayInterface(String value) {
        this.gatewayInterface = value;
    }

    /**
     * Gets the remote host name
     * 
     * @return String
     */
    public String getRemoteHost() {
        return this.remoteHost;
    }

    /**
     * Sets the remote host name
     * 
     * @param value
     *            String
     */
    public void setRemoteHost(String value) {
        this.remoteHost = value;
    }

    /**
     * Gets the remote address
     * 
     * @return String
     */
    public String getRemoteAddress() {
        return this.remoteAddress;
    }

    /**
     * sets the remot address
     * 
     * @param value
     *            String
     */
    public void setRemoteAddress(String value) {
        this.remoteAddress = value;
    }

    /**
     * Gets the remote applicatio name
     * 
     * @return String
     */
    public String getRemoteAppName() {
        return this.remoteAppName;
    }

    /**
     * Sets the remote application name
     * 
     * @param value
     *            String
     */
    public void setRemoteAppName(String value) {
        this.remoteAppName = value;
    }

    /**
     * Gets the server protocol
     * 
     * @return String
     */
    public String getServerProtocol() {
        return this.serverProtocol;
    }

    /**
     * Sets the server protocol
     * 
     * @param value
     *            String
     */
    public void setServerProtocol(String value) {
        this.serverProtocol = value;
    }

    /**
     * Gest the server software
     * 
     * @return String
     */
    public String getServerSoftware() {
        return this.serverSoftware;
    }

    /**
     * Sets teh Server Software
     * 
     * @param value
     *            String
     */
    public void setServerSoftware(String value) {
        this.serverSoftware = value;
    }

    /**
     * Gets the server name
     * 
     * @return String
     */
    public String getServerName() {
        return this.serverName;
    }

    /**
     * Sets the server name
     * 
     * @param value
     *            String
     */
    public void setServerName(String value) {
        this.serverName = value;
    }

    /**
     * Gets the server port
     * 
     * @return int
     */
    public int getServerPort() {
        return this.serverPort;
    }

    /**
     * Sets the server port
     * 
     * @param value
     *            int
     */
    public void setServerPort(int value) {
        this.serverPort = value;
    }

    /**
     * Gets the user agent
     * 
     * @return String
     */
    public String getUserAgent() {
        return this.userAgent;
    }

    /**
     * Sets the u ser agent
     * 
     * @param value
     *            String
     */
    public void setUserAgent(String value) {
        this.userAgent = value;
    }

    /**
     * Gets the locale
     * 
     * @return String
     */
    public String getLocale() {
        return this.locale;
    }

    /**
     * Sets the locale
     * 
     * @param value
     *            String
     */
    public void setLocale(String value) {
        this.locale = value;
    }

    /**
     * Gets the accept value
     * 
     * @return String
     */
    public String getAccept() {
        return this.accept;
    }

    /**
     * Sets the accept value
     * 
     * @param value
     *            String
     */
    public void setAccept(String value) {
        this.accept = value;
    }

    /**
     * Gets the accept language
     * 
     * @return String
     */
    public String getAcceptLanguage() {
        return this.acceptLanguage;
    }

    /**
     * Sets the accept language
     * 
     * @param value
     *            String
     */
    public void setAcceptLanguage(String value) {
        this.acceptLanguage = value;
    }

    /**
     * Gets the accept encoding
     * 
     * @return String
     */
    public String getAcceptEncoding() {
        return this.acceptEncoding;
    }

    /**
     * Sets the accept encoding
     * 
     * @param value
     *            String
     */
    public void setAcceptEncoding(String value) {
        this.acceptEncoding = value;
    }

    /**
     * Gets the scheme
     * 
     * @return String
     */
    public String getScheme() {
        return this.scheme;
    }

    /**
     * Sets the scheme
     * 
     * @param value
     *            String
     */
    public void setScheme(String value) {
        this.scheme = value;
    }

    /**
     * Gest the server Info value
     * 
     * @return String
     */
    public String getServerInfo() {
        return this.serverInfo;
    }

    /**
     * Sets the server info value
     * 
     * @param value
     *            String
     */
    public void setServerInfo(String value) {
        this.serverInfo = value;
    }

    /**
     * Gets the session create time
     * 
     * @return long
     */
    public long getSessionCreateTime() {
        return this.sessionCreateTime;
    }

    /**
     * Sets the session create time
     * 
     * @param value
     *            long
     */
    public void setSessionCreateTime(long value) {
        this.sessionCreateTime = value;
    }

    /**
     * Gets the session last accessed time
     * 
     * @return long
     */
    public long getSessionLastAccessedTime() {
        return sessionLastAccessedTime;
    }

    /**
     * Sets the session last accessed time
     * 
     * @param value
     *            long
     */
    public void setSessionLastAccessedTime(long value) {
        this.sessionLastAccessedTime = value;
    }

    /**
     * Gets the session maximum inactive seconds
     * 
     * @return int
     */
    public int getSessionMaxInactSecs() {
        return this.sessionMaxInactSecs;
    }

    /**
     * Sets the session maximum intactive seconds
     * 
     * @param value
     *            int
     */
    public void setSessionMaxInactSecs(int value) {
        this.sessionMaxInactSecs = value;
    }

    /**
     * Gets the servlet context value
     * 
     * @return String
     */
    public String getServletContext() {
        return this.servletContext;
    }

    /**
     * Gets the servlet context value
     * 
     * @return String
     */
    public String getContextPath() {
        return this.contextPath;
    }

    /**
     * Get the id of the application that serves as the entry point into the
     * system for the user.
     * 
     * @return the the application code
     */
    public String getOrigAppId() {
        return origAppId;
    }

    /**
     * Set the id of the application that serves as the entry point into the
     * system for the user.
     * 
     * @param origAppId
     *            the application code
     */
    public void setOrigAppId(String origAppId) {
        this.origAppId = origAppId;
    }

    /**
     * Get user roles.
     * 
     * @return the list of roles
     */
    public List getRoles() {
        return roles;
    }

    /**
     * Set user roles.
     * 
     * @param roles
     *            the list of user roles
     */
    public void setRoles(List roles) {
        this.roles = roles;
    }

    /**
     * Get the user's group id.
     * 
     * @return the groupId
     */
    public int getGroupId() {
        return groupId;
    }

    /**
     * Set the user's group id.
     * 
     * @param groupId
     *            the groupId to set
     */
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    /**
     * @param contextPath
     *            the contextPath to set
     */
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpSessionBindingListener#valueBound(javax.servlet
     * .http.HttpSessionBindingEvent)
     */
    public void valueBound(HttpSessionBindingEvent event) {
        // TODO Auto-generated method stub

    }

    /**
     * @param servletContext
     *            the servletContext to set
     */
    public void setServletContext(String servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * @return the authSessionId
     */
    public String getAuthSessionId() {
        return authSessionId;
    }

    /**
     * @param authSessionId
     *            the authSessionId to set
     */
    public void setAuthSessionId(String authSessionId) {
        this.authSessionId = authSessionId;
    }

    /**
     * @return the remotePort
     */
    public int getRemotePort() {
        return remotePort;
    }

    /**
     * @param remotePort
     *            the remotePort to set
     */
    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

}
