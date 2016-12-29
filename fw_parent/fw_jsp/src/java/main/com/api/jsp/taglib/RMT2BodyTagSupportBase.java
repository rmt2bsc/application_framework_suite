package com.api.jsp.taglib;

import java.io.IOException;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.*;

import com.SystemException;

/**
 * This class provides tag handler implemntations for the BodyTagSupport class.
 * Extend this class when the need arises to create a custom tag that requires
 * attention its body.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2BodyTagSupportBase extends BodyTagSupport {
    private static final long serialVersionUID = 1L;

    /** String representation of PageContext.Page variable scope, "page". */
    public static final String PAGE_ID = "page";

    /** String representation of PageContext.REQUEST variable scope, "request". */
    public static final String REQUEST_ID = "request";

    /** String representation of PageContext.SESSION variable scope, "session". */
    public static final String SESSION_ID = "session";

    /**
     * String representation of PageContext.APPLICATION variable scope,
     * "application".
     */
    public static final String APPLICATION_ID = "application";

    protected String className = null;

    protected String methodName = null;

    protected Object obj;

    protected String dataSource = null;

    protected String objScope = null;

    protected String property = null;

    /**
     * Sets the object variable.
     * 
     * @param value
     */
    public void setObject(Object value) {
        this.obj = value;
    }

    /**
     * Sets the data source. This value is usually obtained from the property of
     * a Custom JSP Tag.
     * 
     * @param value
     *            The name that is to be assoicated with the data source.
     */
    public void setDataSource(String value) {
        PageVariableHelper helper = new PageVariableHelper();
        try {
            this.dataSource = (String) helper.getValue(this.pageContext, value,
                    null, null);
        } catch (SystemException e) {
            this.dataSource = value;
        }
        this.id = value;
    }

    /**
     * Retrieves the data source name.
     * 
     * @return String
     */
    public String getDataSource() {
        return this.dataSource;
    }

    /**
     * Sets the variable scope of the protected member variable, object.
     * 
     * @param value
     */
    public void setScope(String value) {
        this.objScope = value;
    }

    /**
     * Retrieves the variable scope of the protected memeber variable, object.
     * 
     * @return String
     */
    public String getScope() {
        return this.objScope;
    }

    /**
     * Sets the name of a given property to manage.
     * 
     * @param value
     *            property name
     */
    public void setProperty(String value) {
        this.property = value;
    }

    /**
     * Gets the name of a given property.
     * 
     * @return String
     */
    public String getProperty() {
        return this.property;
    }

    /**
     * Invoked to process the first iteration of body content which the method,
     * exportData, is triggered.
     */
    public void doInitBody() throws JspException {
        this.exportData();
        return;
    }

    /**
     * This method is used to identify and obtain the list of objects that need
     * to be processed using _name and _scope. .
     * 
     * @param _name
     *            The name assigned to the List collection
     * @param _scope
     *            The servlet variable scope to locate list refernece as _name.
     * @return An Object disguised as an ArrayList.
     * @throws JspException
     *             If unable to obtain a reference for the list collection using
     *             _name.
     */
    protected Object getObject(String _name, String _scope) throws JspException {

        Object rc = null;
        if (_scope != null) {
            rc = pageContext.getAttribute(_name, this.translateScope(_scope));
        }
        else {
            rc = pageContext.findAttribute(_name);
        }
        if (rc == null) {
            rc = this.initObject();
        }
        if (rc == null) {
            throw new JspException("Unable to get a reference for object, "
                    + _name);
        }
        return rc;
    }

    /**
     * Outputs the contents of the JSP custom tag using an enclosing JSP Writer
     * object.
     */
    public int doAfterBody() throws JspException {
        try {
            BodyContent body = getBodyContent();
            JspWriter out = body.getEnclosingWriter();
            String bodyString = body.getString();
            if (bodyString != null) {
                this.outputHtml(out, bodyString);
            }
        } catch (IOException e) {
            throw new JspException(
                    "Error: IOExeption while writing body content to the user");
        }
        return SKIP_BODY;
    }

    /**
     * No Action
     * 
     * @return null
     * @throws JspException
     */
    protected Object initObject() throws JspException {
        return null;
    }

    /**
     * No Action.
     * 
     * @throws JspException
     */
    protected void parseObject() throws JspException {
        return;
    }

    /**
     * No Action
     * 
     * @throws JspException
     */
    protected void exportData() throws JspException {
        return;
    }

    /**
     * Outputs HTML formatted data to client using the impicit JSP PrintWriter
     * object.
     * 
     * @param html
     *            HTML data
     * @throws IOException
     */
    protected void outputHtml(String html) throws IOException {
        JspWriter out = pageContext.getOut();
        this.outputHtml(out, html);
    }

    /**
     * Determines the integer equivalent of a JSP variable scope using _scope.
     * 
     * @param _scope
     *            A String that represents the name of one of the JSP variable
     *            scopes. Valid values are:
     *            <ul>
     *            <li>"page" - Page context</li>
     *            <li>"request" - Request context</li>
     *            <li>"session" - Session context</li>
     *            <li>"application" - Application context</li>
     *            </ul>
     * 
     * @return int as one of the PageContext's JSP variable scope values:
     *         <ul>
     *         <li>PageContext.PAGE_SCOPE</li>
     *         <li>PageContext.REQUEST_SCOPE</li>
     *         <li>PageContext.SESSION_SCOPE</li>
     *         <li>PageContext.APPLICATION_SCOPE</li>
     *         </ul>
     * @throws JspException
     */
    protected int translateScope(String _scope) throws JspException {
        if (_scope.equalsIgnoreCase(RMT2BodyTagSupportBase.PAGE_ID)) {
            return PageContext.PAGE_SCOPE;
        }
        else if (_scope.equalsIgnoreCase(RMT2BodyTagSupportBase.REQUEST_ID)) {
            return PageContext.REQUEST_SCOPE;
        }
        else if (_scope.equalsIgnoreCase(RMT2BodyTagSupportBase.SESSION_ID)) {
            return PageContext.SESSION_SCOPE;
        }
        else if (_scope.equalsIgnoreCase(RMT2BodyTagSupportBase.APPLICATION_ID)) {
            return PageContext.APPLICATION_SCOPE;
        }
        throw new JspException("Scope, " + _scope + ", cannot be translated.");
    }

    /**
     * Sends the HTML formatted data to clients browser.
     * 
     * @param out
     *            This is a valid JSP Writer object that is implicitly
     *            associated with the PrintWriter object of the ServletResponse.
     * @param html
     *            Data that is to be ouptut to the ServletResponse as HTML.
     * @throws IOException
     */
    protected void outputHtml(JspWriter out, String html) throws IOException {
        if ((html.indexOf('<') == -1) && (html.indexOf('>') == -1)
                && (html.indexOf('&') == -1)) {
            out.print(html);
        }
        else {
            int len = html.length();
            for (int ndx = 0; ndx < len; ndx++) {
                char c = html.charAt(ndx);
                if (c == '<') {
                    out.print("&lt;");
                }
                else if (c == '>') {
                    out.print("&gt;");
                }
                else if (c == '&') {
                    out.print("&amp;");
                }
                else if (c == '\t' || c == '\n' || c == '\r') {
                    continue;
                }
                else {
                    out.print(c);
                }
            }
        }
    }

}