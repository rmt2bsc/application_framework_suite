package com.api.jsp.taglib.security;

import javax.servlet.jsp.JspException;

import javax.servlet.jsp.tagext.IterationTag;

/**
 * Custom body tag that determines if the user is not logged on to the system.
 * The current session is interogated for an instance of an RMT2SessionBean.
 * When the session bean is null, the user is considered not to be logged on the
 * application.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2CheckUserNotLoggedInTag extends AbstractSecurityTag {
    private static final long serialVersionUID = -6415603640826495077L;

    /**
     * Determines if the user is not logged onto the system by determining if an
     * RMT2SessionBean instance exist on the session context. The purpose is to
     * decide if the body contents of the custom tag are to be evaulated or
     * skipped by testing the validity of the session. The body contents are
     * evaluated only if the user is found to assoicated with the current
     * session.
     * 
     * @return IterationTag.EVAL_BODY_AGAIN if the user is not logged on to
     *         application Otherwise, IterationTag.SKIP_BODY is returned.
     * @throws JspException
     *             When the current session is unobtainable.
     */
    public int doStartTag() throws JspException {
        super.doStartTag();

        // If session bean could not be obtained from the current session
        // context, the user is considered not to be logged into the system.
        // The session bean must exist in order to evaluate body.
        if (this.getSessionBean() == null) {
            return IterationTag.EVAL_BODY_AGAIN;
        }

        // If the session bean exist and the current session id differs from
        // that of
        // the session bean, the user is considered to not be logged in and body
        // evaluation must be skipped.
        if (this.getSessionBean().getSessionId()
                .equalsIgnoreCase(this.currentSession.getId())) {
            return IterationTag.SKIP_BODY;
        }

        // At this point, the user is considered not to be logged in despite an
        // instance of RMT2SessionBean begin found on the current session
        // context.
        return IterationTag.EVAL_BODY_AGAIN;
    }

}