package com.api.jsp.taglib.xml;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.IterationTag;

import com.api.DaoApi;
import com.api.jsp.taglib.RMT2BodyTagSupportBase;
import com.api.xml.XmlApiFactory;

/**
 * Extracts the value of a particular tag element from a XML document for the
 * total number of times the tag element occurs in the XML document. This JSP
 * custom body tag implementation* manipulates an XML document using XPath as
 * its querying method to export the results as a org.w3c.dom.NodeList object to
 * one of the JSP variable scopes: application, session, request or pageContext.
 * <p>
 * The data source is expected to be a String formatted as an XML document. For
 * each body iteration, the design exports an org.w3c.dom.Node object that is
 * accessible by other custom JSP tags that are capable of managing the current
 * node.
 * <p>
 * For example, To get all values of tag, title, from anywhere in the document
 * (xmlDocRef) do:<br>
 * 
 * <pre>
 *  &lt;xml:LoopNodes dataSource="xmlDocRef" nodeRef="nodeIter" query="title"&gt; 
 *     &lt;xml:ElementValue dataSource="nodeIter" element="title"/&gt;
 *  &lt;/xml:LoopNodes&gt;
 * </pre>
 * <p>
 * <u>Description of the importain tag elements</u><br>
 * <ul>
 * <li><i>xmlDocRef</i> is the attribute name that points to the XML document in
 * String format.</li>
 * <li><i>nodeIter</i> is the node retreived from each iteration.</li>
 * <li><i>title</i> is the name of the tag element to obtain the value for each
 * iteration</li>
 * </ul>
 * 
 * {@literal <xml:LoopNodes dataSource="xmlDocRef" nodeRef="test" query="title">}
 * <br>
 * {@literal <xml:ElementValue dataSource="test" element="title"/>}<br>
 * {@literal </xml:LoopNodes>}
 * 
 * 
 * @author roy.terrell
 * 
 */
public class RMT2ForEachXmlDataValueTag extends RMT2BodyTagSupportBase {
    private static final long serialVersionUID = -2387423412142421708L;

    private String msg;

    private int row = 0;

    /** The XPath expression used to query the XML document */
    protected String query;

    /** The reference name used to export each node of the NodeList collection. */
    protected String nodeRef;

    private DaoApi api;

    /**
     * Sets the XPath expression that is used to find an element in the XML
     * Document.
     * 
     * @param value
     */
    public void setQuery(String value) {
        this.query = value;
    }

    /**
     * Sets the attribute name that serves as a mapping to the current node from
     * looping through the result set of a XPath expression.
     * 
     * @param value
     */
    public void setNodeRef(String value) {
        this.nodeRef = value;
    }

    /**
     * Entry point into the RMT2ForEachXmlDataValueTag. Applies the XPath
     * expression to the input XML document, locates the first node from the
     * result set, if available, and exports the node reference to JSP.
     * 
     * @return IterationTag.EVAL_BODY_AGAIN if there is at least one item to
     *         process. IterationTag.SKIP_BODY when there is no more content to
     *         process.
     * @throws JspException
     *             If document reference does not exist, or if XPath expression
     *             is invalid.
     */
    public int doStartTag() throws JspException {
        Object results[];
        try {
            this.obj = null;
            this.obj = this.getObject();
            // Setup xml document data as an InputSource
            this.api = XmlApiFactory.createXmlDao(this.obj.toString());
            results = api.retrieve(this.query);
            if (results == null) {
                return IterationTag.SKIP_BODY;
            }
            if (results[0] == null) {
                return IterationTag.SKIP_BODY;
            }
            int count = ((Integer) results[0]).intValue();
            if (count <= 0) {
                return IterationTag.SKIP_BODY;
            }
            if (this.api.nextRow()) {
                return IterationTag.EVAL_BODY_AGAIN;
            }
            else {
                return IterationTag.SKIP_BODY;
            }
        } catch (Exception e) {
            this.msg = "Error: Property, dataSource, must evaluate as type org.w3c.dom.Document for tag <ForEachXmlValue>.   Class: RMT2ForEachXmlDataValueTag";
            throw new JspException(msg);
        }
    }

    /**
     * This method is used to identify and obtain the input XML document via the
     * dataSource property. The dataSource property could contain the name of an
     * attribute that is mapped to the XML document stored on one of the
     * variable scopes of the servlet API or the property could contain the
     * actual XML document itself.
     * 
     * @return An org.w3c.dom.Document object
     * @throws JspException
     *             If the reference to dataSource is invalid or null.
     */
    private Object getObject() throws JspException {
        Object ds = null;
        try {
            // Obtain datasource from one of the variable scopes of the web
            // server
            ds = (this.obj == null ? this.getObject(this.dataSource,
                    this.objScope) : this.obj);
        } catch (Exception e) {
            if (this.dataSource != null && this.dataSource.contains("<")
                    && this.dataSource.contains("/>")) {
                // Obtains datasource as is, which shoul be an XML document is
                // String form
                ds = this.dataSource;
            }
        }
        return ds;
    }

    /**
     * This process is triggered once the remaining content of the current
     * iteration has been evaluated. The content is written to the JSP via the
     * JSP writer and the next iteration of body content is set to begin which
     * includes the exporting of data until all iterations have been exhausted.
     * 
     * @return IterationTag.EVAL_BODY_AGAIN to process the next iteration of the
     *         body. IterationTag.SKIP_BODY when there is no more content to
     *         process.
     * @throws JspException
     *             unable to write body content to JSP.
     */
    public int doAfterBody() throws JspException {
        try {
            this.getBodyContent().writeOut(this.getPreviousOut());
            this.getBodyContent().clear();

            // Export next node provided index is not out of bounds of the
            // NodeList object.
            if (this.api.nextRow()) {
                this.row++;
                this.exportData();
                return IterationTag.EVAL_BODY_AGAIN;
            }
            else {
                this.api.close();
                this.api = null;
                return IterationTag.SKIP_BODY;
            }
        } catch (Exception e) {
            throw new JspException(
                    "Error: IOExeption while writing body content to the user");
        }
    }

    /**
     * Exports the node from the current iteration as a result of applying the
     * XPath expression to the XML document. The node will be mapped to the
     * reference name specified by the property, nodeRef. PageContext which will
     * be accessible from the JSP. Also, the current indexed row is made
     * available to the JSP via the PageContext.
     * 
     * @throws JspException
     */
    protected void exportData() throws JspException {
        // Publish the xml api which is positioned on the row that is to be
        // processed.
        pageContext
                .setAttribute(this.nodeRef, this.api, PageContext.PAGE_SCOPE);

        // Publish the current iteration count as "ROW" at page scope
        pageContext.setAttribute("ROW", String.valueOf(this.row),
                PageContext.PAGE_SCOPE);
    }

}