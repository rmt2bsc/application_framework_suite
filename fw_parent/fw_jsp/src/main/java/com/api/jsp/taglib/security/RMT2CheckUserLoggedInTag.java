package com.api.jsp.taglib.security;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.IterationTag;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.api.constants.RMT2ServletConst;
import com.api.web.security.RMT2SecurityToken;
import com.api.web.security.RMT2SessionBean;

/**
 * Custom body tag that verifies the authentication status of a user attempting
 * to obtain a session, which the session can be a brand new or existing. When
 * an existing session is used, the user is considered to be already
 * authenticated via single sign on (SSO) means. Otherwise, a new session is
 * associated with user, and the user is required to authenticated manually.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2CheckUserLoggedInTag extends AbstractSecurityTag {

    private static final long serialVersionUID = -6415603640826495077L;

    /**
     * Determines whether or not the user is logged into the system.
     * <p>
     * This is performed by comparing the session id of the current HttpSession
     * context to the session id property of the RMT2SessionBean instance that
     * is stored as an existing attribute to the HttpSession, if available, or
     * by querying the Authentication system to see if the user is signed onto
     * some other application.
     * <p>
     * The purpose of all this is to decide if the body contents of the custom
     * tag are to be evaluated or skipped by testing the validity of the
     * session. The body contents are evaluated only if the user is found to be
     * associated with the current session.
     * 
     * @return int IterationTag.EVAL_BODY_AGAIN if the user is found to be
     *         associated with the current session. Otherwise,
     *         IterationTag.SKIP_BODY is returned.
     * @throws JspException
     *             When the current session is unobtainable.
     */
    public int doStartTag() throws JspException {
        Logger logger = Logger.getLogger(RMT2CheckUserLoggedInTag.class);
        String msg = null;
        super.doStartTag();

        String appCode = this.pageContext.getRequest().getServletContext().getServletContextName();
        logger.info("Web Application name obtained from servlet context ======> " + appCode);

        // If session bean could not be obtained from the current session
        // context, the user is considered not to be logged into the system.
        // The session bean must exist in order to evaluate body.
        if (this.getSessionBean() != null) {
            this.loggedInLocally = true;
        }
        // If the current session id differs from session id that is tied to the
        // RMT2SessionBean stored as an HttpSession attribute, the user must
        // be forced to login and the body evaluation must be skipped.
        if (this.loggedInLocally && this.getSessionBean().getSessionId().equalsIgnoreCase(this.currentSession.getId())) {
            this.loggedIn = true;

            // UI-4: Ensure that we capture the application code in
            // RMT2SessionBean instance
            this.getSessionBean().setOrigAppId(appCode);
            return IterationTag.EVAL_BODY_AGAIN;
        }

        // Perform service call to check if user has logged in from another
        // application.
        Object obj = null;
        try {
            String userName = this.getUserName();
            String appName = this.getAppName();
            String sessionId = this.currentSession.getId();

            // At this point, skip body if user name is not provided as input
            // variable to the tag
            if (this.getUserName() == null) {
                logger.warn("User is not logged in system locally");
                return IterationTag.SKIP_BODY;
            }
            obj = this.authenticator.checkAuthenticationStatus(userName, appName, sessionId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JspException(e);
        }

        if (obj == null) {
            // User could not be authenticated by input user name. More than
            // likely the user does not exist in the system
            msg = "\"checkapp\" Authenticator failed to locate user.";
            logger.log(Level.WARN, msg);
            if (this.getUserName() == null) {
                msg = "User is not logged in system locally";
            }
            else {
                msg = "User, " + this.getUserName()
                        + ", is not logged in system locally";
            }
            logger.log(Level.WARN, msg);
        }

        // Throw exception if remote service invocation returned an exception
        if (obj != null && obj instanceof Exception) {
            msg = ((Exception) obj).getMessage();
            logger.log(Level.ERROR, msg);
            throw new JspException(msg);
        }

        RMT2SessionBean bean = null;
        if (obj != null && obj instanceof RMT2SecurityToken) {
            RMT2SecurityToken token = (RMT2SecurityToken) obj;
            bean = token.getSession();
        }
        if (bean != null) {
            // UI-4: Ensure that we capture the application code in
            // RMT2SessionBean instance
            bean.setOrigAppId(appCode);
            currentSession.setAttribute(RMT2ServletConst.SESSION_BEAN, bean);
            this.loggedIn = true;
        }

        return (this.loggedIn ? IterationTag.EVAL_BODY_AGAIN : IterationTag.SKIP_BODY);
    }
}