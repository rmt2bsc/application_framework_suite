package com.api.jsp.taglib.xml;

import javax.servlet.jsp.JspException;

import com.SystemException;
import com.api.jsp.taglib.RMT2TagSupportBase;
import com.api.web.util.RMT2WebXmlUtility;

/**
 * This class functions as a JSP custom tag handler to transform a JSP related
 * XSL stylesheet and sends the output to the user's browser. References to the
 * system file paths of the XML and XSL documents are exected to be found in one
 * of four JSP context scopes and can be obtain using <JSP
 * scope>.getAttribute("ref") method calls.
 * 
 * @author roy.terrell
 * 
 */
public class RMT2TransformTag extends RMT2TagSupportBase {
    /** The name of the property targeted for this tag */
    protected String xml = null;

    /** The datatype the property */
    protected String xsl = null;

    /**
     * The entry point to transforming JSP related XSL stylesheets.
     * <ul>
     * <li>Obtains the file path of the XML document.</li>
     * <li>Obtains the file path of the XSL document.</li>
     * <li>Transforms the XSL document at JSP translation time which the output
     * is channeled via the JspWriter object.</li>
     * </ul>
     * 
     * @throws JspException
     *             XSLT Transformation errors.
     */
    public int doStartTag() throws JspException {
        try {
            super.doStartTag();
        } catch (JspException e) {
            // Expect to JspException to occur since we are not processing a
            // DataSource
        }

        String xmlPath;
        String xslPath;

        try {
            xmlPath = (String) this.getObject(this.xml, null);
            xslPath = (String) this.getObject(this.xsl, null);
            RMT2WebXmlUtility xslt = RMT2WebXmlUtility.getInstance();
            xslt.transformXslt(xmlPath, xslPath, this.pageContext.getOut());
            // / this.outputHtml(buildInputControl());
            return SKIP_BODY;
        } catch (SystemException e) {
            throw new JspException("Class: " + this.className + "  Method: "
                    + this.methodName + "  Message (SystemException): "
                    + e.getMessage());
        }
    }

    /**
     * No Action
     */
    protected void exportData() throws JspException {
        return;
    }

} // end class
