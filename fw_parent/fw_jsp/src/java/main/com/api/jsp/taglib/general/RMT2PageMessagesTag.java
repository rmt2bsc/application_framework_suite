package com.api.jsp.taglib.general;

import java.io.IOException;

import java.util.Hashtable;
import java.util.Enumeration;

import javax.servlet.jsp.JspException;

import com.RMT2Exception;
import com.api.jsp.taglib.RMT2TagSupportBase;
import com.util.RMT2Utility;

/**
 * The purpose of this class is to locate and process a message object found on
 * either the page, request, session, or application object and display the
 * content of the message object onto the client's JSP page. Single or multiple
 * messages can be processed via one tag. A single message is required to exsit
 * as an object of type String. Multiple messages are required to exist as
 * Hashtable.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2PageMessagesTag extends RMT2TagSupportBase {
    private static final long serialVersionUID = 1L;

    /**
     * This method retreives the message object and determines if it needs to
     * process a single message or multiple messages for this tag. The
     * message(s) are located using the dataSource property.
     * 
     * @throws JspException
     *             if the message object is not of type String or Hashtable.
     */
    public int doStartTag() throws JspException {
        StringBuffer msgText = null;
        Hashtable msgHash;
        String msgString;
        Enumeration msgEnum;
        String method = "doStartTag";
        this.className = "RMT2PageMessagesTag";
        String msg;

        try {
            this.obj = null;
            this.obj = this.getObject();
            if (this.obj == null) {
                this.outputHtml("&nbsp;");
                return SKIP_BODY;
            }
            // Get messages from Hashtable
            if (this.obj instanceof Hashtable) {
                msgHash = (Hashtable) this.obj;
                msgEnum = msgHash.elements();
                while (msgEnum.hasMoreElements()) {
                    if (msgText == null) {
                        msgText = new StringBuffer(100);
                        msgText.append((String) msgEnum.nextElement());
                    }
                    else {
                        msgText.append("<br>");
                        msgText.append((String) msgEnum.nextElement());
                    }
                }
                msgString = msgText.toString();
            }
            // Get message from String object.
            else if (this.obj instanceof String) {
                msgString = this.obj.toString();
            }
            else if (this.obj instanceof RMT2Exception) {
                msgString = ((RMT2Exception) this.obj).getHtmlMessage();
            }
            else {
                msg = "Message (JspException): Datasource must be of type Hashtable, String, or RMT2Exception";
                throw new JspException(msg);
            }
            this.outputHtml(msgString);
            return SKIP_BODY;
        } catch (IOException e) {
            msg = "Class: " + this.className + "  Method: " + method
                    + "  Message (IOException): " + e.getMessage();
            throw new JspException(msg);
        }
    }

    /**
     * Attempts to locate the message object via the dataSource property.
     */
    protected Object getObject() throws JspException {
        return (this.obj == null ? this.getObject(this.dataSource,
                this.objScope) : this.obj);
    }

    /**
     * Overide method to provide special logic that will ignore throwing a JSP
     * exception in the event this.obj turns out to be null... which is okay.
     * 
     * @param _name
     *            The name of the object to search for.
     * @param _scope
     *            The JSP variable scope where object can be found.
     * @return The object.
     * @throws JspException
     */
    protected Object getObject(String _name, String _scope) throws JspException {
        Object rc = null;
        if (_scope != null) {
            rc = pageContext.getAttribute(_name, this.translateScope(_scope));
        }
        else {
            if (_name != null) {
                rc = pageContext.findAttribute(_name);
            }
        }

        // rc will be null if the creation of the object is initiated from the
        // JSP which is totally valid. When this is true, be sure to
        // include necessary logic to create and export object to JSP if needed
        if (rc == null) {
            rc = this.initObject();
        }
        return rc;
    }
}