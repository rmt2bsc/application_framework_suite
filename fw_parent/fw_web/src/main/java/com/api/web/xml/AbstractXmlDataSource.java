package com.api.web.xml;

import java.io.ByteArrayInputStream;

import javax.xml.xpath.XPathConstants;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.SystemException;
import com.api.persistence.DatabaseException;
import com.api.web.AbstractWebRequestManager;
import com.api.web.Request;
import com.api.web.util.RMT2WebXmlUtility;

/**
 * This class provides a skeletal implementation to manipulate an XML document
 * into NodeList collection. The developer will need to extend this class an
 * implement the verifySource() method. The querying method used to interrogate
 * the XML source is XPath.
 * 
 * @author RTerrell
 * 
 */
public abstract class AbstractXmlDataSource extends AbstractWebRequestManager {

    private Logger logger;

    private Object xmlSrc;

    private NodeList nodes;

    private Object xmlDoc;

    protected AbstractXmlDataSource() {
        super();
    }

    protected AbstractXmlDataSource(Request request) {
        super(request);
    }

    /**
     * Sets up the logger. This method is triggered by either constructor.
     * 
     * @see com.api.AbstractJspApiImpl#initApi()
     */
    @Override
    protected void initApi() throws DatabaseException, SystemException {
        logger = Logger.getLogger("AbstractXmlDataSource");
        this.nodes = null;
    }

    /**
     * Determines whether the XML data source is valid and of the correct data
     * type. The valid XML source must be capable of being converted to an
     * InputSource object
     * 
     * @return Arbitrary object as the XML document
     * @throws SystemException
     *             If source is null, or source is of an invalid data type.
     */
    protected abstract Object verifySource() throws SystemException;

    /**
     * Preprocesses the XML document by properly identifying the document and
     * converting it to a form that is required by the implementor.
     * 
     * @return Object The XML document in a format required by the implemntor
     * @throws SystemException
     *             XML doucment is null or invalid.
     */
    protected Object processXmlSource() throws SystemException {
        // Invoke custom implementation to validate and obtain a reference to
        // the XML doucment.
        this.xmlDoc = this.verifySource();

        // Verify that XML documnet is identified and processed as expected by
        // the implementor
        if (this.xmlDoc == null) {
            this.msg = "XML document verification failed.  XML document is null or invalid";
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }
        return this.xmlDoc;
    }

    /**
     * Applies an XPath query against the source XML document. Before the query
     * is performed, the XML data source is validated and verified.
     * 
     * @param xpathExp
     *            The XPath query. Enter null to bypass quering the XML
     *            Document.
     * @return Object The result of the XPath expression which could be a
     *         String, Boolean, Number, Node, or NodeList. If an instance of
     *         this class is created with a NodeList then that NodeList is
     *         returned.
     * @throws SystemException
     *             If xpathExp is null, or XML doucment is null, or if the
     *             expression could not be applied.
     */
    protected Object processXmlSource(String xpathExp) throws SystemException {
        Object result = null;

        // Obtain a reference to processed XML Document.
        this.xmlDoc = this.processXmlSource();

        // At this point, the xpath expression is required.
        if (xpathExp == null) {
            this.msg = "XPath expression is null or invalid";
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }
        // Ensure that the expression is prefixed with the double front slash
        if (!xpathExp.startsWith("//")) {
            xpathExp = "//" + xpathExp;
        }
        try {
            result = RMT2WebXmlUtility.executeXpathQuery(xpathExp,
                    (InputSource) this.xmlDoc, XPathConstants.NODESET);
            this.nodes = (NodeList) result;
            return result;
        } catch (SystemException e) {
            this.msg = e.getMessage();
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }
    }

    /**
     * Creates an InputSource object whcih will serve as a wrapper of the XML
     * document.
     * 
     * @param xmlDocRef
     *            A referenece name to the XML document in memory.
     * @return InputSOurce
     */
    protected InputSource createXmlDocument(String xmlData) {
        // Create an InputStream
        ByteArrayInputStream bais = new ByteArrayInputStream(xmlData.getBytes());
        InputSource inputSource = new InputSource(bais);
        return inputSource;
    }

    /**
     * Obtains the value of elementName from a list of XML nodes.
     * 
     * @param nodes
     *            A NodeList collection.
     * @param elementName
     *            The name of the node to extract the value.
     * @return The value of mapped to elementName.
     * @throws SystemException
     *             If one or more elements of nodes is invalid or elementName is
     *             null. .
     */
    protected String searchNodes(NodeList nodes, String elementName)
            throws SystemException {
        if (elementName == null) {
            this.msg = "The element name is null or invalid";
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }
        for (int ndx = 0; ndx < nodes.getLength(); ndx++) {
            Node node = (Node) nodes.item(ndx);
            // Only process element nodes
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String val = this.getElementValue(node, elementName);
            if (val == null) {
                // If available, search children of current node since target
                // element value was not found.
                val = this.searchNodes(node.getChildNodes(), elementName);
                if (val != null) {
                    return val;
                }
            }
            else {
                return val;
            }
        }
        return null;
    }

    /**
     * Obtains a Node from a list of nodes, <i>nodes</i>, using
     * <i>elementName</i>.
     * 
     * @param nodes
     *            NodeList object used to search for target node.
     * @param elementName
     *            The name of the node to locate
     * @return A generic object disguised as {@link Node} instance.
     * @throws SystemException
     *             <i>elementName</i> is null or invalid.
     */
    protected Object getNode(NodeList nodes, String elementName)
            throws SystemException {
        if (elementName == null) {
            this.msg = "The element name is null or invalid";
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }
        for (int ndx = 0; ndx < nodes.getLength(); ndx++) {
            Node node = (Node) nodes.item(ndx);
            // Only process element nodes
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String val = this.getElementValue(node, elementName);
            Object results = null;
            if (val == null) {
                // If available, search children of current node since target
                // element value was not found.
                results = this.getNode(node.getChildNodes(), elementName);
                if (results != null) {
                    return results;
                }
            }
            else {
                return results;
            }
        }
        return null;
    }

    /**
     * Obtains the value of an XML element using its Node object. If the element
     * is not equal to the parameter, node, then an attempt is made to match the
     * element with the attributes of node, if available.
     * 
     * @param node
     *            The object containing the data components of an XML element.
     *            This object cannot be null.
     * @param elementName
     *            The name of the tag element in which the value is to be
     *            extracted.
     * @return The value of the element, or null if the element's name does not
     *         exist in node or as any of the node's attributes, or when node is
     *         null.
     */
    protected String getElementValue(Node node, String elementName)
            throws SystemException {
        if (node == null) {
            this.msg = "Node value could not be determined.  Input parameter, node, cannot be null";
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }
        if (node.getNodeName().equals(elementName)) {
            return node.getTextContent();
        }

        // this.element did not equal node. Try to
        // locate element as an attribute of node.
        NamedNodeMap attrMap = node.getAttributes();
        String val = null;
        if (attrMap == null) {
            return val;
        }
        for (int ndx = 0; ndx < attrMap.getLength(); ndx++) {
            Node attr = (Node) attrMap.item(ndx);
            val = this.getElementValue(attr, elementName);
            if (val != null) {
                return val;
            }
        }
        return null;
    }

    /**
     * Retrives the source of XML Data
     * 
     * @return arbitrary object
     */
    public Object getXmlSrc() {
        return xmlSrc;
    }

    /**
     * Set the source of XML data.
     * 
     * @param xmlSrc
     *            an arbitrary object.
     */
    public void setXmlSrc(Object xmlSrc) {
        this.xmlSrc = xmlSrc;
    }

    /**
     * Retrieves the XML node collection.
     * 
     * @return NodeList
     */
    public NodeList getNodes() {
        return nodes;
    }

    /**
     * Set the XML node collection.
     * 
     * @param nodes
     *            the NodeList to set
     */
    public void setNodes(NodeList nodes) {
        this.nodes = nodes;
    }
}
