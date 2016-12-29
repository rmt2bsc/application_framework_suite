package com.api.jsp.taglib.general;

import javax.servlet.jsp.JspException;

import com.api.jsp.taglib.RMT2TagSupportBase;

/**
 * Combines the process of throwing a JspException from a JSP file. The user has
 * the option of specifying the message to be assoicated with the exception.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2ExceptionTag extends RMT2TagSupportBase {

    private static final long serialVersionUID = 3883265984663916402L;

    private String msg;

    /**
     * Set the message of the exception.
     * 
     * @param msg
     *            the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * Throw the intended JspException.
     */
    public int doStartTag() throws JspException {
        if (this.msg == null) {
            msg = "Message Unknown";
        }
        throw new JspException(msg);
    }

}