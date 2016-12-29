package com.api.jsp.taglib.security;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.IterationTag;

/**
 * Implements AbstractRolesTag class in order to determine if the user is
 * granted permission to process the body contents of this tag provided that at
 * least one of the user's roles matches any of the required roles specified by
 * an external source.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2RoleExistCheckTag extends AbstractRolesTag {
    private static final long serialVersionUID = -1853678242346369297L;

    /**
     * Verifies that the user is logged in and has authorization to process the
     * body contents of the tag.
     * 
     * @return IterationTag.EVAL_BODY_AGAIN if the user is possesses one or more
     *         roles that mathch any of the required roles.
     *         IterationTag.SKIP_BODY if the user is not logged onto system, or
     *         the session bean is null or zero roles are matched.
     * @throws JspException
     */
    public int doStartTag() throws JspException {
        super.doStartTag();

        if (!this.isLoggedIn()) {
            return IterationTag.SKIP_BODY;
        }

        if (!this.isUserAuthorized()) {
            return IterationTag.SKIP_BODY;
        }
        return IterationTag.EVAL_BODY_AGAIN;
    }
}