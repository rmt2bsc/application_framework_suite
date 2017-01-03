package com.api.jsp.taglib.security;

import org.apache.log4j.Logger;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import javax.servlet.jsp.JspException;

import javax.servlet.jsp.tagext.IterationTag;

/**
 * Custom body tag that is designed to route the user to a specific web resource
 * based on the user's login state. The following properties are involved in
 * this implementation:
 * <ul>
 * <li><b>state</b> - accepts the value {@link GeneralConst.USER_LOGGEDIN
 * USER_LOGGEDIN} or {@link GeneralConst.USER_LOGGEDIN USER_LOGGEDOUT} which is
 * an indicator to determine whether the user is logged in or logged out,
 * respectively. {@link GeneralConst.USER_LOGGEDIN USER_LOGGEDIN} is the default
 * value when staet is blank.</li>
 * <li><b>roles</b> - a list of roles that requires the user to staisfy before
 * accessing resource. Not required.</li>
 * <li><b>success</b> - The URL to invoke when the user satisfies security
 * requirements. Required.</li>
 * <li><b>failure</b> - The URL to invoke when the user fails to satisfy
 * security requirements. Required.</li>
 * </ul>
 * <p>
 * 
 * 
 * @author roy.terrell
 * 
 */
public class RMT2AppPageRouterTag extends RMT2RoleExistCheckTag {
    private static final long serialVersionUID = -7805131001657688838L;

    private static Logger logger = Logger.getLogger("RMT2AppPageRouterTag");

    private String success;

    private String failure;

    /**
     * Sets the URL value for failure state.
     * 
     * @param failure
     *            the failure URL
     */
    public void setFailure(String failure) {
        this.failure = failure;
    }

    /**
     * Sets the URL value for success state.
     * 
     * @param success
     *            the success URL
     */
    public void setSuccess(String success) {
        this.success = success;
    }

    /**
     * Routes the user to a specified resource based on the login state of the
     * user. The user is redirected to the "success" URL when found to be logged
     * in either locally or remotely. Otherwise, the user is directed to the URL
     * assoicated with failure attribute.
     * 
     * @return Always return IterationTag.SKIP_BODY.
     * @throws JspException
     *             When the forwarding URL is invalid or a problem exist when
     *             attempting to forward user to another resource.
     */
    public int doStartTag() throws JspException {
        String pagePath = null;
        String msg = null;

        // Check if user is logged into system and, if applicable, meet role
        // requirements.
        int rc = super.doStartTag();

        // Determine which URL we should target based on user state and validate
        // the URL.
        if (rc == IterationTag.EVAL_BODY_AGAIN) {
            pagePath = this.success;
        }
        else {
            // User is not signed on and authenticated
            pagePath = this.failure;
            logger.warn("User is not logged in system remotely");
        }

        // Forward user to the selected URL.
        try {
            RequestDispatcher rd = this.request.getRequestDispatcher(pagePath);
            rd.forward(this.pageContext.getRequest(),
                    this.pageContext.getResponse());
        } catch (IOException e) {
            msg = e.getMessage();
            msg = (msg == null ? "Unknown" : msg);
            msg = "An general IO Exception occurred when attempting to forward user to URL, "
                    + pagePath + ".  Message: " + msg;
            throw new JspException(msg);
        } catch (ServletException e) {
            msg = e.getMessage();
            msg = (msg == null ? "Unknown" : msg);
            msg = "An Servlet Exception occurred when attempting to forward user to URL, "
                    + pagePath + ".  Message: " + msg;
            throw new JspException(msg);
        }
        return IterationTag.SKIP_BODY;
    }

}