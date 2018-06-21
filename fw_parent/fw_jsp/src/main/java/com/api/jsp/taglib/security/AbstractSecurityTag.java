package com.api.jsp.taglib.security;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.api.constants.RMT2ServletConst;
//import com.api.security.UserSecurity;
//import com.api.security.authentication.web.AuthenticationFactory;
import com.api.jsp.taglib.RMT2BodyTagSupportBase;
import com.api.security.authentication.web.LoginException;
import com.api.web.Request;
import com.api.web.Response;
import com.api.web.controller.scope.HttpVariableScopeFactory;
import com.api.web.security.RMT2SessionBean;
import com.api.web.security.UserAuthenticationFactory;
import com.api.web.security.UserAuthenticator;

/**
 * Abstract custom body tag implementation for maintaining common security
 * properties such as application name and user name.
 * 
 * @author roy.terrell
 * 
 */
public abstract class AbstractSecurityTag extends RMT2BodyTagSupportBase {
    private static final long serialVersionUID = -6415603640826495077L;

    // Tag attributes
    private String appName;

    private String userName;

    // Internal attributes.
    private RMT2SessionBean sessionBean;

    protected HttpSession currentSession;

    protected boolean loggedIn;

    protected boolean loggedInLocally;

    protected Request request;

    protected Response response;

    // protected UserSecurity authenticator;

    protected UserAuthenticator authenticator;

    /**
     * Initializes the session bean, if available.
     * 
     * @return int Always returns IterationTag.EVAL_BODY_AGAIN
     * @throws JspException
     *             When the current session is unobtainable.
     */
    public int doStartTag() throws JspException {
        super.doStartTag();
        this.loggedInLocally = false;
        this.loggedIn = false;
        this.currentSession = null;
        this.currentSession = pageContext.getSession();
        if (currentSession == null) {
            throw new JspException(
                    "Problem obtaining the current session for user");
        }
        this.sessionBean = (RMT2SessionBean) currentSession
                .getAttribute(RMT2ServletConst.SESSION_BEAN);

        // Setup Request and Response instances from Page Context
        HttpServletRequest httpReq = (HttpServletRequest) this.pageContext
                .getRequest();
        HttpServletResponse httpResp = (HttpServletResponse) this.pageContext
                .getResponse();
        this.request = HttpVariableScopeFactory.createHttpRequest(httpReq);
        this.response = HttpVariableScopeFactory.createHttpResponse(httpResp);

        // Create application specific Authentication Api
        try {
            // this.authenticator = AuthenticationFactory.getAuthenticator(null,
            // this.request, this.response);
            this.authenticator = UserAuthenticationFactory
                    .getAuthenticator(null);
            this.authenticator.setRequest(this.request);
        } catch (LoginException e) {
            throw new JspException(e);
        }
        return IterationTag.EVAL_BODY_AGAIN;
    }

    /**
     * Process the body content.
     */
    public int doAfterBody() throws JspException {
        // Write out body content if condition was met.
        try {
            this.getBodyContent().writeOut(this.getPreviousOut());
            this.getBodyContent().clear();
        } catch (IOException e) {
            throw new JspException(
                    "Error: IOExeption while writing body content to the user");
        }
        return IterationTag.SKIP_BODY;
    }

    /**
     * Get application name.
     * 
     * @return the appName
     */
    public String getAppName() {
        return appName;
    }

    /**
     * Set the application name.
     * 
     * @param appName
     *            the appName to set
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * Get user name
     * 
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set the user name.
     * 
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Get the user's session bean which is obtained from the current session.
     * 
     * @return the sessionBean
     */
    protected RMT2SessionBean getSessionBean() {
        return sessionBean;
    }

    /**
     * Gets the logged in status of the user.
     * 
     * @return true if user is logged inand false when user is not logged into
     *         the system.
     */
    protected boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Sets the log-in status of a user.
     * 
     * @param loggedIn
     *            true = logged in and false = not logged in.
     */
    protected void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    /**
     * @return the loggedInLocally
     */
    public boolean isLoggedInLocally() {
        return loggedInLocally;
    }
}