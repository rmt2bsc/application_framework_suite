package com.api.jsp.taglib.general;

import javax.servlet.jsp.JspException;

import com.api.jsp.taglib.RMT2TagSupportBase;

/**
 * The purpose of this class is to determine the SOAP Host and make it available
 * for scripting within the JSP
 * <p>
 * The SOAP Host exists as the Java System variable, <i>soaphost</>.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2SoapHostTag extends RMT2TagSupportBase {
    private static final long serialVersionUID = 7268388371120529607L;
    private static final String KEY_NAME = "soaphost";
    private String value;

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Drives the process of identifying and exporting the System variable,
     * "soaphost".
     * 
     * @throws JspException
     *             if the message object is not of type String or Hashtable.
     */
    public int doStartTag() throws JspException {
        super.doStartTag();
        return SKIP_BODY;
    }

    /**
     * Identifies the SOAP Host value from the System property, "soaphost".
     * 
     * @return String.
     * @throws JspException
     */
    protected Object initObject() throws JspException {
        return System.getProperty(KEY_NAME);
    }

}