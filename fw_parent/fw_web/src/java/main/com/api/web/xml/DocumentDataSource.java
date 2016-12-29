package com.api.web.xml;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.SystemException;
import com.api.persistence.DatabaseException;
import com.api.web.Request;
import com.api.xml.RMT2XmlUtility;
import com.api.xml.SingleXmlNode;

/**
 * Implementation of the CommonQueryApi interface that provides functionality
 * for querying an XML document for specific data. In order to use this api, the
 * user is required to supply the XML document in the format of a String. This
 * api is flexible enough to operate in or out of HTTP Web envirionment.
 * <p>
 * <b>Example using an XML document:</b> <blockquote> String xmlSource;<br>
 * String query = "//book[@year > 1996]/title";<br>
 * HttpServletRequest userRequest;<br>
 * DocumentDataSource obj = DocumentDataSource.getInstance(xmlSource,
 * userRequest);<br>
 * String value = obj.get(query, "title");<br>
 * </blockquote>
 * <p>
 * <b>Example using an NodeList collection:</b> <blockquote> NodeList nodes;<br>
 * HttpServletRequest userRequest;<br>
 * DocumentDataSource obj = DocumentDataSource.getInstance(nodes, userRequest);<br>
 * String value = obj.get("title");<br>
 * </blockquote>
 * 
 * @author RTerrell
 * 
 */
class DocumentDataSource extends AbstractXmlDataSource implements SingleXmlNode {

    private Logger logger;

    public DocumentDataSource() {
        super();
    }

    public DocumentDataSource(Request request) {
        super(request);
    }

    /**
     * Sets up the logger. This method is triggered by either constructor.
     * 
     * @see com.api.AbstractJspApiImpl#initApi()
     */
    @Override
    protected void initApi() throws DatabaseException, SystemException {
        logger = Logger.getLogger("DocumentDataSource");
    }

    /**
     * Determines whether the XML data source is valid and is either of type
     * String or InputSource.
     * 
     * @return XML document as an InputSource.
     * @throws SystemException
     *             If source is null, or source is of an invalid data type.
     */
    protected Object verifySource() throws SystemException {
        Object data = this.getXmlSrc();
        InputSource doc = null;
        if (data == null) {
            this.msg = "The XML data source is invalid or null";
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }
        // Determine the appropriate data type of data source.
        if (data instanceof String) {
            // The data source is an XML document
            String xmlDocStr = (String) data;
            if (xmlDocStr.length() <= 0) {
                this.msg = "The XML document input source does not contain any content";
                logger.log(Level.ERROR, this.msg);
                throw new SystemException(this.msg);
            }
            doc = RMT2XmlUtility.createXmlDocument(xmlDocStr);
        }
        else if (data instanceof InputSource) {
            // The XML document is an INputSource
            doc = (InputSource) data;
        }
        else {
            this.msg = "The data source reference (XML document) must be of type String";
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }
        return doc;
    }

    /**
     * There is no implementation for this method.
     * 
     * @return null
     */
    public Object get(int uid) throws SystemException {
        return null;
    }

    /**
     * Searches an a list of XML Node objects for the value that is mapped to
     * elementName. This method cycles through each element of the NodeList
     * until a match is found.
     * 
     * @param elementName
     *            The name of the target element. Should be cast as a String.
     * @return Object The value of element or null if a match is not found.
     * @throws SystemException
     *             If element is null or if the node list is not properly
     *             initialized.
     */
    public Object get(Object elementName) throws SystemException {
        String xpath = null;
        if (elementName == null) {
            // Element name is required. Let the overloaded method handle the
            // error.
        }
        else {
            // Lets look for all occurences of elementName in the document.
            xpath = "//" + elementName.toString();
        }
        Object value = this.get(xpath, elementName);
        return value;
    }

    /**
     * Searches an XML document for the value mapped to elementName. This method
     * applies the XPath expression (xpathExp) to the XML document in order to
     * target elemtnName's value.
     * 
     * @param xpathExp
     *            The XPath query
     * @param elementName
     *            The name of the target element.
     * @return Object The value of element, or null if elementName does not
     *         exist as an element in the XML document.
     * @throws SystemException
     *             If xpathExp is null, or the element name is null, or XML
     *             doucment is null, or if the expression could not be applied.
     */
    public Object get(Object xpathExp, Object element) throws SystemException {
        Object result = null;
        String xpathStr = null;
        String elementName = null;

        if (xpathExp instanceof String) {
            // Valid String
            xpathStr = xpathExp.toString();
        }
        else {
            this.msg = "Xpath Expression must be of type String";
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }

        if (element == null) {
            this.msg = "Target element name is invalid or null";
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }
        if (element instanceof String) {
            elementName = element.toString();
        }
        else {
            this.msg = "Target element name must be of type String";
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }

        try {
            result = this.processXmlSource(xpathStr);
            if (result == null) {
                return null;
            }
            NodeList nodes = (NodeList) result;
            String value = this.searchNodes(nodes, elementName);
            return value;
        } catch (SystemException e) {
            this.msg = e.getMessage();
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }
    }

    /**
     * Locates and returns an XML node based on the its node name,
     * <i>element</i>.
     * 
     * @param element
     *            The name of the XML tag to retrieve node.
     * @return org.w3c.dom.Node
     * @throws SystemException
     */
    public Object getNode(Object element) throws SystemException {
        // Get node list
        Object nodes = this.processXmlSource();
        // Look for element in list of nodes.
        Object value = this.getNode(nodes, element);
        return value;
    }

    /**
     * Obtains a Node from a list of nodes, <i>nodes</i>, using
     * <i>elementName</i>.
     * 
     * @param nodeData
     *            NodeList object used to search for target node.
     * @param nodeElement
     *            The name of the node to locate
     * @return A generic object disguised as {@link Node} instance.
     * @throws SystemException
     *             <i>elementName</i> is null or invalid.
     */
    protected Object getNode(Object nodeData, Object nodeElement)
            throws SystemException {
        if (nodeData == null) {
            this.msg = "XML data source cannot be null";
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }
        if (nodeElement == null) {
            this.msg = "Element name cannot be null";
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }
        String elementName = nodeElement.toString();

        // Since NodeList and Node interfaces are implemented by the same
        // class, treat search medium as a NodeList.
        NodeList nodes = (NodeList) nodeData;
        Object value = super.getNode(nodes, elementName);
        return value;
    }
}
