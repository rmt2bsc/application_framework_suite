package com.api.jsp.taglib.general;

import javax.servlet.jsp.JspException;

import com.api.jsp.taglib.RMT2TagSupportBase;
import com.api.web.util.RMT2WebUtility;

/**
 * The purpose of this class is to determine the root identification of the
 * application and to make it available for scripting within the JSP.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2ApplicationRootTag extends RMT2TagSupportBase {
    private static final long serialVersionUID = 7268388371120529607L;

    private String value;

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Drives the process of identifying and exporting the application root
     * variable.
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
     * <i>value</i> attribute or by dynamic means
     * 
     * @return The application root value or null if the request object is
     *         invalid.
     * @throws JspException
     */
    protected Object initObject() throws JspException {
        Object request = pageContext.getRequest();

        // Get default value from JSP, if available.
        if (this.value != null) {
            return this.value;
        }

        String appRootValue = null;
        if (request == null) {
            System.out.println("request is null");
            return null;
        }
        else {
            appRootValue = RMT2WebUtility.getWebAppContext(request);
        }
        return appRootValue;
    }

}