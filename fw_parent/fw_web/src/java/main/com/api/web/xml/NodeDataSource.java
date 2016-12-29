package com.api.web.xml;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.w3c.dom.NodeList;

import com.SystemException;
import com.api.persistence.DatabaseException;
import com.api.web.Request;
import com.api.xml.SingleXmlNode;

/**
 * Implementation of the CommonQueryApi interface that provides functionality
 * for extracting values from a list of nodes representing elements of an XML
 * document. In order to use this api, the user is required to supply
 * org.w3x.dom.NodeList object as the data source. This data source is
 * considered to be the main dependency of this api and is needed to render
 * information back to the client. This api is flexible enough to operate in or
 * out of HTTP Web envirionment.
 * <p>
 * <b>Example using an XML document:</b> <blockquote> String xmlSource;<br>
 * String query = "//book[@year > 1996]/title";<br>
 * HttpServletRequest userRequest;<br>
 * NodeDataSource obj = NodeDataSource.getInstance(xmlSource, userRequest);<br>
 * String value = obj.get(query, "title");<br>
 * </blockquote>
 * <p>
 * <b>Example using an NodeList collection:</b> <blockquote> NodeList nodes;<br>
 * HttpServletRequest userRequest;<br>
 * NodeDataSource obj = NodeDataSource.getInstance(nodes, userRequest);<br>
 * String value = obj.get("title");<br>
 * </blockquote>
 * 
 * @author RTerrell
 * 
 */
class NodeDataSource extends AbstractXmlDataSource implements SingleXmlNode {

    private Logger logger;

    public NodeDataSource() {
        super();
    }

    public NodeDataSource(Request request) {
        super(request);
    }

    /**
     * Sets up the logger. This method is triggered by either constructor.
     * 
     * @see com.api.AbstractJspApiImpl#initApi()
     */
    @Override
    protected void initApi() throws DatabaseException, SystemException {
        logger = Logger.getLogger("NodeDataSource");
    }

    /**
     * Determines whether the XML data source is valid NodeList object.
     * 
     * @return null since there is no need to to query the XML document.
     * @throws SystemException
     *             If source is null, or source is of an invalid data type.
     */
    protected Object verifySource() throws SystemException {
        Object data = this.getXmlSrc();
        this.setXmlSrc(data);
        if (data instanceof NodeList) {
            // The data source is an XML node object.
            this.setNodes((NodeList) data);
            return (NodeList) data;
        }
        else {
            this.msg = "The data source reference must be of type NodeList";
            logger.log(Level.ERROR, this.msg);
            throw new SystemException(this.msg);
        }
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
     * Searches a list of XML Node objects (NodeList) for a value that is mapped
     * to the element argument. This method cycles through each element of the
     * NodeList until a match is found. This method requires that the node list
     * is properly initialized before its invocation.
     * 
     * @param element
     *            The name of the target element. Should be cast as a String.
     * @return Object The value of element, or null if a match is not found.
     * @throws SystemException
     *             If element is null or if the node list is not properly
     *             initialized.
     */
    public Object get(Object element) throws SystemException {
        // Get node list
        Object nodes = this.processXmlSource();
        // Look for element in list of nodes.
        Object value = this.get(nodes, element);
        return value;
    }

    /**
     * Obtains the value of an XML element from an XML data source. The data
     * type of the XML data source is either a single Node object or a NodeList
     * collection.
     * 
     * @param nodeData
     *            The XML data source which is either of type Node or NodeList.
     * @param nodeElement
     *            The name of the element to obtain XML value.
     * @return Object The value of element, or null if a match is not found.
     * @throws SystemException
     *             When nodeData is null, or nodeElement is null, or when
     *             nodeData is not of type Node or NodeList.
     */
    public Object get(Object nodeData, Object nodeElement)
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
        String value = this.searchNodes(nodes, elementName);
        return value;
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
