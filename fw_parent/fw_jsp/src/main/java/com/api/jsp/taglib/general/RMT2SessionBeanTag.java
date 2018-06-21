package com.api.jsp.taglib.general;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.api.constants.RMT2ServletConst;
import com.api.jsp.taglib.RMT2TagSupportBase;

/**
 * The purpose of this class is to export the user's session bean object and to
 * make it available for scripting within the JSP.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2SessionBeanTag extends RMT2TagSupportBase {
    private static final long serialVersionUID = -5417901669249357298L;

    /**
     * Drives the process of identifying and exporting the user's session bean
     * variable for the purpose of becoming a script varible within the JSP.
     * 
     * @throws JspException
     *             if the message object is not of type String or Hashtable.
     */
    public int doStartTag() throws JspException {
        super.doStartTag();
        return SKIP_BODY;
    }

    /**
     * Identifies the application root either by the value assigned to the
     * <i>value</i> attribute or by dynamic means.
     * 
     * @return An instance of
     *         {@link com.api.security.authentication.web.RMT2SessionBean
     *         RMT2SessionBean} or null when the session is invalid or the user
     *         is not logged in.
     * @throws JspException
     */
    protected Object initObject() throws JspException {
        HttpSession session = pageContext.getSession();
        if (session == null) {
            return null;
        }
        return session.getAttribute(RMT2ServletConst.SESSION_BEAN);
    }

}