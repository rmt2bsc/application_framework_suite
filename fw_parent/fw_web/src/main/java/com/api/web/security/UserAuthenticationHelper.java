package com.api.web.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.RMT2Base;
import com.SystemException;
import com.api.config.AppPropertyPool;
import com.api.config.ConfigConstants;
import com.api.constants.RMT2ServletConst;
import com.api.security.authentication.web.AuthenticationConst;
import com.api.security.authentication.web.AuthenticationException;
import com.api.security.authentication.web.AuthorizationException;
import com.api.security.authentication.web.LogoutException;
import com.api.security.authentication.web.UserCredentials;
import com.api.util.RMT2String;
import com.api.web.Request;
import com.api.web.Session;

/**
 * Helper class for authenticating clients.
 * <p>
 * Provides basic functionality to obtain user input data such as user id,
 * password, application code, and current session id, create user session bean,
 * user authorization, and user role management to name a few.
 * 
 * @author RTerrell
 */
public class UserAuthenticationHelper extends RMT2Base {

    private static Logger logger = Logger
            .getLogger(UserAuthenticationHelper.class);

    /**
     * Constructs a ClientAuthenticationHelper object containing an initialized
     * logger.
     * 
     */
    public UserAuthenticationHelper() {
        super();
        logger.info("ClientAuthenticationHelper initialized");
    }

    /**
     * 
     * @param request
     *            A valid instance of {@link com.controller.Request Request}
     *            related to the user.
     * @return {@link com.api.security.authentication.web.UserCredentials
     *         UserCredentials} Holds the user's credentials tha came from the
     *         request parameter.
     */
    public UserCredentials getInputCredentiials(Request request) {
        String loginId;
        String pw;
        String appName;
        RMT2SessionBean sessionBean = (RMT2SessionBean) request.getSession().getAttribute(RMT2ServletConst.SESSION_BEAN);
        // Set the client action which actually is the service id.\
        String servId = request.getParameter("clientAction");
        String loginServId = AppPropertyPool.getProperty(ConfigConstants.PROPNAME_LOGINSRC);

        // Nullify sessionBean when logging into an application
        if (servId != null && servId.equalsIgnoreCase(loginServId)) {
            sessionBean = null;
        }

        // Look for the login id and application id from the request
        // as either a parameter or an attribute in the stated order.
        if (sessionBean == null) {
            loginId = request.getParameter(AuthenticationConst.AUTH_PROP_USERID);
            if (loginId == null || loginId.equals("")) {
                loginId = (String) request.getAttribute(AuthenticationConst.AUTH_PROP_USERID);
            }
            pw = request.getParameter(AuthenticationConst.AUTH_PROP_PASSWORD);
            if (pw == null || pw.equals("")) {
                pw = (String) request.getAttribute(AuthenticationConst.AUTH_PROP_PASSWORD);
            }
            appName = request.getParameter(AuthenticationConst.AUTH_PROP_MAINAPP);
            if (appName == null || appName.equals("")) {
                appName = (String) request.getAttribute(AuthenticationConst.AUTH_PROP_MAINAPP);
                if (appName == null) {
                    try {
                        appName = AppPropertyPool.getProperty(AuthenticationConst.AUTH_PROP_MAINAPP);
                    } catch (Exception e) {
                        appName = null;
                    }
                }
            }
        }
        else {
            loginId = sessionBean.getLoginId();
            appName = sessionBean.getOrigAppId();
            pw = request.getParameter(AuthenticationConst.AUTH_PROP_PASSWORD);
            if (pw == null || pw.equals("")) {
                pw = (String) request.getAttribute(AuthenticationConst.AUTH_PROP_PASSWORD);
            }
        }

        // Setup the user credentials object
        UserCredentials creds = new UserCredentials();
        creds.setUserId(loginId);
        creds.setPassword(pw);
        creds.setAppCode(appName);
        creds.setCurrentSessionId(request.getSession().getId());

        return creds;
    }

    /**
     * Determines user authorization by matching one or more required roles with
     * the roles associated with the user.
     * <p>
     * if any of the required roles in <i>roles</i> match up with any of the
     * roles setup in the user's session bean instance.
     * 
     * @param userId
     *            The user id of the person that is to be authorized.
     * @param roles
     *            A String containing a list of required roles which each role
     *            is delimited by a comma.
     * @param request
     *            A valid instance of {@link com.controller.Request Request}
     *            related to the authenticated user which contains the Session
     *            and in turn contains the list of user roles.
     * @return true if one or more of the required roles matches the user's
     *         assigned roles. Othewise, false is returned.
     * @throws AuthorizationException
     *             <ul>
     *             <li>roles</i> is null</li> <li>Invalid requrest instance is
     *             found</li> <li>Session instance is unobtainable</li> <li>The
     *             user's session bean token does not exist on the user's
     *             session</li>
     *             </ul>
     */
    public boolean isAuthorized(String userId, String roles, Request request)
            throws AuthorizationException {
        if (request == null) {
            this.msg = "Unable to authorize user due the invalid request instance";
            logger.error(this.msg);
            throw new AuthorizationException(this.msg);
        }
        Session session = request.getSession(false);
        if (session == null) {
            this.msg = "Unable to authorize user due user's session is not established or unavailable";
            logger.error(this.msg);
            throw new AuthorizationException(this.msg);
        }

        RMT2SessionBean sessionBean = (RMT2SessionBean) session
                .getAttribute(RMT2ServletConst.SESSION_BEAN);
        return this.isAuthorized(userId, roles, sessionBean);

    }

    /**
     * Determines user authorization by matching one or more required roles with
     * the roles associated with the user.
     * <p>
     * 
     * @param userId
     *            The login id of the user targeted for authorization
     * @param roles
     *            A String containing a comma separated list of required user
     *            roles
     * @param sessionBean
     *            The user's session bean which contains a list of roles
     *            assinged to the user.
     * @return true when the user is found to be authorized and fale when the
     *         user is not authorized.
     * @throws AuthorizationException
     */
    public boolean isAuthorized(String userId, String roles,
            RMT2SessionBean sessionBean) throws AuthorizationException {
        if (sessionBean == null) {
            this.msg = "Unable to authorize user due the session bean token is not found on the user's session";
            logger.error(this.msg);
            throw new AuthorizationException(this.msg);
        }

        if (!sessionBean.getLoginId().equals(userId)) {
            this.msg = "The login id from the current session is not congruent with the user  id targeted for authorization, "
                    + userId;
            logger.error(this.msg);
            throw new AuthorizationException(this.msg);
        }

        if (roles == null) {
            this.msg = "Unable to authorize user due the comma-separated String of required roles cannot is invalid or null";
            logger.error(this.msg);
            throw new AuthorizationException(this.msg);
        }

        // If for some strange reason that the required roles does not exist at
        // this point,
        // flag the user as authorized. May change this logic in the future.
        List<String> requiredRoles = RMT2String.getTokens(roles, ",");
        if (requiredRoles.size() <= 0) {
            return true;
        }
        // At this point we know we have at least one required role.
        // Indicate that the user is no authorized if no roles exist on the
        // user's session.
        if (sessionBean.getRoles() == null
                || sessionBean.getRoles().size() == 0) {
            return false;
        }

        return this.isAuthorized(userId, requiredRoles, sessionBean.getRoles());
    }

    /**
     * Determines user authorization by matching one or more required roles with
     * the roles associated with the user.
     * 
     * @param userId
     *            The login id of the user that is to be authorized.
     * @param requiredRoles
     *            A List of Strings values representing the required roles in
     *            which one or more of the user's roles must match in order to
     *            be considered authorized.
     * @param userRoles
     *            A List of String values representing the roles assigned to the
     *            user targeted for authorization.
     * @return true when the user is found to be authorized and fale when the
     *         user is not authorized.
     * @throws AuthorizationException
     */
    public boolean isAuthorized(String userId, List<String> requiredRoles,
            List<String> userRoles) throws AuthorizationException {
        if (requiredRoles == null) {
            this.msg = "Unable to authorize user due the list of required roles are invalid or null";
            logger.error(this.msg);
            throw new AuthorizationException(this.msg);
        }

        // Match up the roles.
        List<String> matches = this.getMatchingRoles(userRoles, requiredRoles);
        return (matches.size() > 0);
    }

    /**
     * Determines if the user possesses one or more required roles and builds a
     * list every user role that matches with a required role.
     * 
     * @param userRoles
     *            The roles assigned to the user.
     * @param requiredRoles
     *            The roles the user must authorized for.
     * @return A List of roles in code name form that match one or more of the
     *         required roles.
     */
    private List<String> getMatchingRoles(List<String> userRoles,
            List<String> requiredRoles) {
        List<String> matchedRoles = new ArrayList<String>();
        for (int ndx = 0; ndx < requiredRoles.size(); ndx++) {
            String reqRole = (String) requiredRoles.get(ndx);
            for (int ndx2 = 0; ndx2 < userRoles.size(); ndx2++) {
                String userRole = (String) userRoles.get(ndx2);
                if (userRole.trim().equalsIgnoreCase(reqRole.trim())) {
                    matchedRoles.add(reqRole.trim());
                }
            }
        }
        return matchedRoles;
    }

    /**
     * Creates a session bean for the user and stores it on the session context.
     * 
     * @param request
     *            A valid instance of {@link com.controller.Request Request}
     *            related to the user.
     * @return {@link com.api.security.authentication.web.RMT2SessionBean
     *         RMT2SessionBean } The newly created user session bean
     * @throws SystemException
     */
    public RMT2SessionBean createSessionBean(Request request)
            throws SystemException {
        String appId = AppPropertyPool
                .getProperty(AuthenticationConst.AUTH_PROP_MAINAPP);
        String loginId = request
                .getParameter(AuthenticationConst.AUTH_PROP_USERID);

        RMT2SessionBean sessionBean;
        try {
            sessionBean = UserAuthenticationFactory.getSessionBeanInstance(
                    loginId, appId);
            sessionBean.setOrigAppId(appId);
            sessionBean.setLoginId(loginId);
            SessionBeanManager sbm = SessionBeanManager.getInstance(request);
            sbm.addSessionBean(sessionBean);
            logger.info("User, " + sessionBean.getLoginId()
                    + ", remotely signed on application, " + appId
                    + ", successfully!");
            return sessionBean;
        } catch (AuthenticationException e) {
            throw new SystemException(e);
        }
    }

    /**
     * Invalidates the user's session using the a valid Request instance.
     * 
     * @param request
     *            A valid instance of {@link com.controller.Request Request}
     *            related to the user.
     * @return {@link com.api.security.authentication.web.RMT2SessionBean
     *         RMT2SessionBean } The user session bean that was removed from the
     *         user's session.
     * @throws LogoutException
     */
    public RMT2SessionBean removeSession(Request request)
            throws LogoutException {
        if (request == null) {
            return null;
        }
        RMT2SessionBean bean = (RMT2SessionBean) request.getSession()
                .getAttribute(RMT2ServletConst.SESSION_BEAN);
        try {
            request.getSession().removeAttribute(RMT2ServletConst.SESSION_BEAN);
            request.getSession().invalidate();
            return bean;
        } catch (Exception e) {
            throw new LogoutException(e);
        }
    }

}
