package com.api.jsp.taglib.security;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.IterationTag;

import com.api.security.authentication.web.AuthenticationException;
import com.api.web.security.RMT2SecurityToken;
import com.api.web.security.RMT2SessionBean;
import com.constants.RMT2ServletConst;

/**
 * Custom body tag that determines if the user is logged on to the system. The
 * current session is interogated for an instance of an RMT2SessionBean, which
 * indicates that the use is signed on.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2CheckUserLoggedInTag extends AbstractSecurityTag {

    private static final long serialVersionUID = -6415603640826495077L;

    /**
     * Determines whether or not the user is logged into the system. This is
     * performed by comparing the current session context to an existing session
     * bean, if available, or by qeurying the Authentication system to see if
     * the user is signed onto some other application.
     * <p>
     * The purpose of all this is to decide if the body contents of the custom
     * tag are to be evaulated or skipped by testing the validity of the
     * session. The body contents are evaluated only if the user is found to be
     * assoicated with the current session.
     * 
     * @return int IterationTag.EVAL_BODY_AGAIN if the user is found to be
     *         assoicated with the current session. Otherwise,
     *         IterationTag.SKIP_BODY is returned.
     * @throws JspException
     *             When the current session is unobtainable.
     */
    public int doStartTag() throws JspException {
        Logger logger = Logger.getLogger(RMT2CheckUserLoggedInTag.class);
        String msg = null;
        super.doStartTag();

        // If session bean could not be obtained from the current session
        // context, the user is considered not to be logged into the system.
        // The session bean must exist in order to evaluate body.
        if (this.getSessionBean() != null) {
            this.loggedInLocally = true;
        }
        // If the current session id differs from that of the session bean, the
        // user must be forced to login and body evaluation must be skipped.
        if (this.loggedInLocally
                && this.getSessionBean().getSessionId()
                        .equalsIgnoreCase(this.currentSession.getId())) {
            this.loggedIn = true;
            return IterationTag.EVAL_BODY_AGAIN;
        }

        // Perform service call to check if user has logged in from another
        // applicaton.
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
            // obj = this.authenticator.getAuthentication(userName, appName,
            // sessionId);
            obj = this.authenticator.checkAuthenticationStatus(userName,
                    appName, sessionId);
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
            currentSession.setAttribute(RMT2ServletConst.SESSION_BEAN, bean);
            this.loggedIn = true;
        }

        return (this.loggedIn ? IterationTag.EVAL_BODY_AGAIN
                : IterationTag.SKIP_BODY);
    }
}