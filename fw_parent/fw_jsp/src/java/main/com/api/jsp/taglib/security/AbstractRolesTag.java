package com.api.jsp.taglib.security;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.IterationTag;

import com.api.web.security.UserAuthenticationHelper;

/**
 * An abstract user role verification implementation which basically determines
 * if the logged-in user is configured with the mandatory roles to access an
 * external resource. The required roles are supplied by the user of this
 * implementation.
 * 
 * @author roy.terrell
 * 
 */
public abstract class AbstractRolesTag extends RMT2CheckUserLoggedInTag {
    private static final long serialVersionUID = -1853678242346369297L;

    private String roles;

    private boolean userAuthorized;

    /**
     * Indicates whether or not the user is authorized based on roles.
     * 
     * @return the userAuthorized
     */
    protected boolean isUserAuthorized() {
        return userAuthorized;
    }

    /**
     * Set the comma separated list of user roles.
     * 
     * @param roles
     *            the roles to set
     */
    public void setRoles(String roles) {
        this.roles = roles;
    }

    /**
     * Determines the user's authorization based on a list of required roles.
     * The user is required to be logged into the system in order to evaluate
     * the body contents of the tag.
     * 
     * @return IterationTag.EVAL_BODY_AGAIN if the user is possesses one or more
     *         roles that mathch any of the required roles.
     *         IterationTag.SKIP_BODY if the user is not logged onto system, or
     *         the user does not have authorization to process the body
     *         contentes
     * @throws JspException
     */
    public int doStartTag() throws JspException {
        // Check if user is logged into system
        super.doStartTag();

        // abort if user is not logged in.
        if (!this.isLoggedIn()) {
            return IterationTag.SKIP_BODY;
        }

        // Verify that user possesses at least one of the rerquire permissions.
        this.userAuthorized = this.getUserAuthorization();
        if (!this.userAuthorized) {
            return IterationTag.SKIP_BODY;
        }
        return IterationTag.EVAL_BODY_AGAIN;
    }

    /**
     * Uses the session bean to determine if the user is authorized to process
     * the tag's body based on assigned roles.
     * 
     * @return true if user is authorized or roles were not speciied. Returns
     *         false when user is not authorized.
     * @throws JspException
     */
    private boolean getUserAuthorization() throws JspException {
        if (this.roles == null) {
            return true;
        }
        try {
            UserAuthenticationHelper helper = new UserAuthenticationHelper();
            return helper.isAuthorized(this.getSessionBean().getLoginId(),
                    this.roles, this.request);
            // return helper.isAuthorized(this.getUserName(), this.roles,
            // this.request);
            // return this.authenticator. isAuthorized(this.roles);
        } catch (Exception e) {
            throw new JspException(e);
        }
    }

}