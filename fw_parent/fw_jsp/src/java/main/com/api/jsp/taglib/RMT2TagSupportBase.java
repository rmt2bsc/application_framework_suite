package com.api.jsp.taglib;

import java.io.IOException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.SystemException;

/**
 * This base class provides tag handler implemntations for the TagSupport class.
 * Extend this class when the need arises to create a custom tag that does not
 * want to manipulate its body.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2TagSupportBase extends TagSupport {
    private static final long serialVersionUID = 1675978903693629828L;

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

    protected String classId = null;

    protected String dataSource = null;

    protected String objScope = null;

    /**
     * Sets the Object property.
     * 
     * @param value
     */
    public void setObject(Object value) {
        this.obj = value;
    }

    /**
     * Sets the class id property
     * 
     * @param value
     */
    public void setClassId(String value) {
        this.classId = value;
    }

    /**
     * Gets the class id.
     * 
     * @return
     */
    public String getClassId() {
        return this.classId;
    }

    /**
     * Sets the datasource property.
     * 
     * @param value
     */
    public void setDataSource(String value) {
        PageVariableHelper helper = new PageVariableHelper();
        try {
            this.dataSource = (String) helper.getValue(this.pageContext, value,
                    null, null);
        } catch (SystemException e) {
            this.dataSource = value;
        }
    }

    /**
     * Gets the datasource property name.
     * 
     * @return
     */
    public String getDataSource() {
        return this.dataSource;
    }

    /**
     * Sets the JSP variable scope property.
     * 
     * @param value
     */
    public void setScope(String value) {
        this.objScope = value;
    }

    /**
     * Gets the JSP variable scope property.
     * 
     * @return
     */
    public String getScope() {
        return this.objScope;
    }

    /**
     * Entry point into custom tag. Invokes processes responsible for
     * determining the data source and exporting data to the client JSP. Lastly,
     * the body of the custom tag is skipped by default.
     */
    public int doStartTag() throws JspException {
        this.processObject(this.getObject());
        if (this.obj != null) {
            this.exportData();
        }
        return SKIP_BODY;
    }

    /**
     * Determines the data source that is assoicated with this custom tag.
     * 
     * @return Object. The user is responsible for properly casting return value
     *         for usage.
     * @throws JspException
     *             If datasource is not found
     */
    protected Object getObject() throws JspException {
        return (this.obj == null ? this.getObject(this.dataSource,
                this.objScope) : this.obj);
    }

    /**
     * Locates the data source object that is assoicated with this custom tag
     * using _name and _scope.
     * 
     * @param _name
     *            The name of the datasource object.
     * @param _scope
     *            The JSP scope the datasource object can be found. When null,
     *            all scopes are probed.
     * @return Object
     * @throws JspException
     *             If datasource is not found
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
        // JSP. When this is true, be sure to
        // include necessary logic to create and export object to JSP
        if (rc == null) {
            rc = this.initObject();
        }

        // Do not reject null object if JSP intends not to use it (Setting the
        // object == "")
        // of if the custom tag is trying to verify the existence of an object
        // withou
        // providing the name of the data source.
        if (rc == null && _name != null && !_name.equalsIgnoreCase("")) {
            throw new JspException(
                    "Problem obtaining a reference for property, " + _name);
        }
        return rc;
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
        if (_scope.equalsIgnoreCase(RMT2TagSupportBase.PAGE_ID)) {
            return PageContext.PAGE_SCOPE;
        }
        else if (_scope.equalsIgnoreCase(RMT2TagSupportBase.REQUEST_ID)) {
            return PageContext.REQUEST_SCOPE;
        }
        else if (_scope.equalsIgnoreCase(RMT2TagSupportBase.SESSION_ID)) {
            return PageContext.SESSION_SCOPE;
        }
        else if (_scope.equalsIgnoreCase(RMT2TagSupportBase.APPLICATION_ID)) {
            return PageContext.APPLICATION_SCOPE;
        }
        throw new JspException("Scope, " + _scope + ", cannot be translated.");
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
        out.print(html);
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

    /**
     * This objec is invoked from the getObject method, which is designed for
     * implementing initialization routines regarding the target data source
     * object..
     * 
     * @return null;
     * @throws JspException
     */
    protected Object initObject() throws JspException {
        return null;
    }

    /**
     * This method is called from the doStartTag method which initializes the
     * obj member variable with the data source object.
     * 
     * @param _obj
     *            The data source object that was located via one of the JSP
     *            varialbe scopes.
     * @throws JspException
     */
    protected void processObject(Object _obj) throws JspException {
        this.obj = _obj;
    }

    /**
     * This method exposes or exports the target data object of this tag to the
     * JSP client page as "id".
     * 
     * @throws JspException
     */
    protected void exportData() throws JspException {
        if (this.obj == null) {
            return;
        }
        pageContext.setAttribute(id, this.obj, PageContext.PAGE_SCOPE);
        return;
    }

}